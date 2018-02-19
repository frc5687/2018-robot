package org.frc5687.powerup.robot.commands.auto;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.Constants.Auto.Approach;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.subsystems.DriveTrain;
import org.frc5687.powerup.robot.subsystems.Intake;
import org.frc5687.powerup.robot.utils.JeVoisProxy;

/**
 * Autonomous command to turn to specified angle
 */
public class AutoApproachCube extends Command implements PIDOutput {

    private PIDController controller;
    private long _endMillis;
    private double _endInches;
    private long _maxMillis;
    private double _maxInches;
    private double _angle;
    private double _speed;

    private double _pidOut;

    private long onTargetMillis;
    private long startTimeMillis;

    private DriveTrain _driveTrain;
    private AHRS _imu;
    private JeVoisProxy _jevois;
    private Intake _intake;

    public AutoApproachCube(Robot robot, double speed, double maxInches, long maxMillis) {
        this(robot, -999, speed, maxInches, maxMillis);
    }
    public AutoApproachCube(Robot robot, double angle, double speed, double maxInches, long maxMillis) {
        _driveTrain = robot.getDriveTrain();
        _imu = robot.getIMU();
        _jevois = robot.getJevois();
        _intake = robot.getIntake();
        requires(_driveTrain);
        _angle = angle;
        _speed = speed;
        _maxInches = maxInches;
        _maxMillis = maxMillis;
    }

    @Override
    protected void initialize() {
        _endMillis = System.currentTimeMillis() + _maxMillis;
        _endInches = _driveTrain.getDistance() + _maxInches;
        if (_angle==-999) { _angle = _imu.getYaw(); }

        double kP = Approach.kP;
        double kI = Approach.kI; // Double.parseDouble(SmartDashboard.getString("DB/String 1", ".006"));
        double kD = Approach.kD; //Double.parseDouble(SmartDashboard.getString("DB/String 2", ".09"));

        controller = new PIDController(kP, kI, kD, _imu, this);
        controller.setInputRange(Constants.Auto.MIN_IMU_ANGLE, Constants.Auto.MAX_IMU_ANGLE);
        controller.setOutputRange(Approach.TWIST, -Approach.TWIST);
        controller.setAbsoluteTolerance(Approach.TOLERANCE);
        controller.setContinuous();
        controller.setSetpoint(_angle);
        controller.enable();
        DriverStation.reportError("AutoApproachCube initialized to " + _angle + " at " + _speed, false);
        DriverStation.reportError("kP="+kP+" , kI="+kI+", kD="+kD + ",T="+ Approach.TOLERANCE, false);
        startTimeMillis = System.currentTimeMillis();
    }

    @Override
    protected void execute() {
        double cubeAngle = _jevois.GetX();
        double robotAngle = _imu.getYaw();
        double targetAngle = robotAngle + cubeAngle;
        double pidOut;
        synchronized(this) { pidOut = _pidOut;}
        if (Math.abs(targetAngle - controller.getSetpoint()) > Approach.TOLERANCE) {

            controller.setSetpoint(targetAngle);
        }
        _driveTrain.tankDrive(_speed + pidOut, _speed - pidOut); // positive output is clockwise
        SmartDashboard.putBoolean("AutoApproachCube/onTarget", controller.onTarget());
        SmartDashboard.putNumber("AutoApproachCube/robotAngle", robotAngle);
        SmartDashboard.putNumber("AutoApproachCube/cubeAngle", cubeAngle);
        SmartDashboard.putNumber("AutoApproachCube/targetAngle", targetAngle);
        // SmartDashboard.putData("AutoApproachCube/pid", controller);
    }

    @Override
    protected boolean isFinished() {
        if (_driveTrain.getDistance() > _endInches) {
            DriverStation.reportError("AutoApproachCube exceeded max distance of " + _maxInches + " inches.", false);
            return true;
        }
        if (System.currentTimeMillis() > _endMillis) {
            DriverStation.reportError("AutoApproachCube exceeded max time of " + _maxMillis + "ms.", false);
            return true;
        }
        if (_intake.cubeIsDetected()) {
            DriverStation.reportError("AutoApproachCube detected a cube in the intake!", false);
            return true;
        }
        return false;
    }

    @Override
    protected void end() {
        controller.disable();
        _driveTrain.tankDrive(0,0);
    }

    @Override
    public void pidWrite(double output) {
        synchronized (this) {
            _pidOut = output;
            SmartDashboard.putNumber("AutoApproachCube/pidOut", _pidOut);
        }
    }
}