package org.frc5687.powerup.robot.commands.auto;

/**
 * Created by Ben Bernard on 1/14/2018.
 */

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.Constants.Auto.Align;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.subsystems.DriveTrain;

/**
 * Autonomous command to turn to specified angle
 */
public class AutoAlignExperimental extends Command implements PIDOutput {

    private PIDController controller;
    private double endTime;
    private double angle;
    private double speed;
    private long _timeout = 2000;

    private double pidOut;

    private long _onTargetSince;
    private long startTimeMillis;
    private long _endTimeMillis;

    private DriveTrain driveTrain;
    private AHRS imu;
    private IMUSource imuSource;
    private Constants.typeOfTurn _typeOfTurn;

    private double _tolerance;

    public AutoAlignExperimental(Robot robot, double angle) {
        this(robot, angle, Align.SPEED);
    }

    public AutoAlignExperimental(Robot robot, double angle, double speed) {
        this(robot.getDriveTrain(), robot.getIMU(), angle, speed);
    }

    public AutoAlignExperimental(DriveTrain driveTrain, AHRS imu, double angle, double speed) {
        this(driveTrain, imu, angle, speed, 2000);
    }

    public AutoAlignExperimental(DriveTrain driveTrain, AHRS imu, double angle, double speed, long timeout) {
        this(driveTrain, imu, angle, speed, timeout, Align.TOLERANCE);
    }

    public AutoAlignExperimental(Robot robot, double angle, long timeout, double tolerance) {
        this(robot.getDriveTrain(), robot.getIMU(), angle, Align.SPEED, timeout, tolerance);
    }

    public AutoAlignExperimental(Robot robot, double angle, double speed, long timeout, double tolerance) {
        this(robot.getDriveTrain(), robot.getIMU(), angle, speed, timeout, tolerance);
    }

    public AutoAlignExperimental(DriveTrain driveTrain, AHRS imu, double angle, double speed, long timeout, double tolerance) {
        this(driveTrain, imu, angle, speed, timeout, tolerance, Constants.typeOfTurn.shortest);
    }

    public AutoAlignExperimental(DriveTrain driveTrain, AHRS imu, double angle, double speed, long timeout, double tolerance, Constants.typeOfTurn typeOfTurn) {
        requires(driveTrain);
        this.angle = angle;
        this.speed = speed;
        this.driveTrain = driveTrain;
        this.imu = imu;
        _timeout = timeout;
        _tolerance = tolerance;
        _typeOfTurn = typeOfTurn;
        imuSource = new IMUSource(this.imu, _typeOfTurn);
    }

    private class IMUSource implements PIDSource {
        private AHRS _imu;
        private int _angleOffset;
        private PIDSourceType _pidSourcetype;
        private double minIMU;
        private double maxIMU;
        private boolean shouldSetContinuous;
        private double _setpoint;

        public void setPIDSourceType(PIDSourceType pidSource) {
            _pidSourcetype = pidSource;
        }

        public PIDSourceType getPIDSourceType() {
            return _pidSourcetype;
        }

        public IMUSource(AHRS imu, Constants.typeOfTurn typeOfTurn) {
            _imu = imu;
            _pidSourcetype = PIDSourceType.kDisplacement;
            if (typeOfTurn == Constants.typeOfTurn.rightOnly) {
                _angleOffset = 90;
                shouldSetContinuous = false;
            } else if (typeOfTurn == Constants.typeOfTurn.leftOnly) {
                _angleOffset = -90;
                shouldSetContinuous = false;
            } else {
                _angleOffset = 0;
                shouldSetContinuous = true;
            }
            minIMU = Constants.Auto.MIN_IMU_ANGLE + _angleOffset;
            maxIMU = Constants.Auto.MAX_IMU_ANGLE + _angleOffset;
        }

        public double pidGet() {
            return _imu.getYaw() + _angleOffset;
        }

        public double getMinIMU() {
            return minIMU;
        }

        public double getMaxIMU() {
            return maxIMU;
        }

        public boolean shouldSetContinuous() {
            return shouldSetContinuous;
        }

        public void setSetpoint(double setpoint) {
            _setpoint = setpoint;
        }

        public double getSetpoint() {
            return _setpoint + _angleOffset;
        }
    }

    @Override
    protected void initialize() {
        double kP = Align.kP; // Double.parseDouble(SmartDashboard.getString("DB/String 0", ".04"));
        double kI = Align.kI; // Double.parseDouble(SmartDashboard.getString("DB/String 1", ".006"));
        double kD = Align.kD; //Double.parseDouble(SmartDashboard.getString("DB/String 2", ".09"));

        controller = new PIDController(kP, kI, kD, imu, this, 0.01);
        controller.setInputRange(imuSource.getMinIMU(), imuSource.getMaxIMU());
        controller.setOutputRange(-speed, speed);
        controller.setAbsoluteTolerance(_tolerance);
        if (imuSource.shouldSetContinuous()) {
            controller.setContinuous();
        }
        imuSource.setSetpoint(angle);
        controller.setSetpoint(imuSource.getSetpoint());
        controller.enable();
        DriverStation.reportError("AutoAlign initialized to " + angle + " at " + speed, false);
        DriverStation.reportError("kP="+kP+" , kI="+kI+", kD="+kD + ",T="+ Align.TOLERANCE, false);
        startTimeMillis = System.currentTimeMillis();
        _endTimeMillis = startTimeMillis + _timeout;
    }

    @Override
    protected void execute() {
        //actOnPidOut();
        SmartDashboard.putBoolean("AutoAlign/onTarget", controller.onTarget());
        SmartDashboard.putNumber("AutoAlign/imu", imu.getYaw());
        SmartDashboard.putData("AutoAlign/pid", controller);
    }

    private void actOnPidOut() {
        if (pidOut > 0 && pidOut < Align.MINIMUM_SPEED) {
            pidOut = Align.MINIMUM_SPEED;
        }
        if (pidOut < 0 && pidOut > -Align.MINIMUM_SPEED) {
            pidOut = -Align.MINIMUM_SPEED;
        }
        driveTrain.setPower(pidOut, -pidOut, true); // positive output is clockwise
    }

    @Override
    protected boolean isFinished() {
        if (!controller.onTarget()) {
            _onTargetSince = 0;
        }

        if(System.currentTimeMillis() >= _endTimeMillis){
            DriverStation.reportError("AutoAlign timed out after " + _timeout + "ms at " + imu.getYaw(), false);
            return true;
        }

        if (controller.onTarget()) {
            if (_onTargetSince == 0) {
                DriverStation.reportError("AutoAlign reached target " + imu.getYaw(), false);
                _onTargetSince = System.currentTimeMillis();
            }

            if (System.currentTimeMillis() > _onTargetSince + Align.STEADY_TIME) {
                DriverStation.reportError("AutoAlign complete after " + Align.STEADY_TIME + " at " + imu.getYaw(), false);
                return  true;
            }
        }

        return false;
    }

    @Override
    protected void end() {
        driveTrain.setPower(0,0, true);
        DriverStation.reportError("AutoAlign finished: angle = " + imu.getYaw() + ", time = " + (System.currentTimeMillis() - startTimeMillis), false);
        controller.disable();
        DriverStation.reportError("AutoAlign.end() controller disabled", false);
    }

    @Override
    public void pidWrite(double output) {
        pidOut = output;
        actOnPidOut();
        //SmartDashboard.putNumber("AutoAlign/pidOut", pidOut);
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}