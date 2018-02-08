package org.frc5687.powerup.robot.commands.auto;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.subsystems.DriveTrain;

public class AutoAlignToCube extends Command {
    private CubeSource _cubeSource;
    private PIDController controller;
    private double speed;
    private PIDListener listener;
    private AHRS imu;
    private double initialAngle;

    private DriveTrain driveTrain;

    public AutoAlignToCube(Robot robot, double speed) {
        driveTrain = robot.getDriveTrain();
        imu = robot.getIMU();
        _cubeSource = new CubeSource(robot);
        this.speed = speed;
    }

    @Override
    protected void initialize() {
        initialAngle = imu.getYaw();
        double kP = 0.4; // Double.parseDouble(SmartDashboard.getString("DB/String 0", ".04"));
        double kI = Constants.Auto.Align.kI; // Double.parseDouble(SmartDashboard.getString("DB/String 1", ".006"));
        double kD = Constants.Auto.Align.kD; //Double.parseDouble(SmartDashboard.getString("DB/String 2", ".09"));
        listener = new PIDListener();

        controller = new PIDController(kP, kI, kD, imu, listener);
        controller.setInputRange(-30, 30);
        controller.setOutputRange(-speed, speed);
        controller.setAbsoluteTolerance(1);
        controller.setContinuous();
        controller.setSetpoint(initialAngle);
        controller.enable();
        DriverStation.reportError("kP="+kP+" , kI="+kI+", kD="+kD + ",T="+ Constants.Auto.Align.TOLERANCE, false);
    }

    @Override
    protected void execute() {
        double targetAngle = _cubeSource.pidGet() + initialAngle;
        controller.setSetpoint(targetAngle);
        double turn = listener.get();
        driveTrain.tankDrive(turn, -turn); // positive output is clockwise
        SmartDashboard.putBoolean("AutoAlignToCube/onTarget", controller.onTarget());
        SmartDashboard.putData("AutoAlignToCube/pid", controller);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        controller.disable();
        driveTrain.tankDrive(0,0);
    }


    private class CubeSource implements PIDSource {
        Robot _robot;
        PIDSourceType _pidSourceType;

        public CubeSource(Robot robot) {
            _robot = robot;
        }

        public void setPIDSourceType(PIDSourceType pidSource) {
            _pidSourceType = pidSource;
        }

        public PIDSourceType getPIDSourceType() {
            return _pidSourceType;
        }

        public double pidGet() {
            return _robot.jeVoisProxy.GetX();
        }
    }

    private class PIDListener implements PIDOutput {

        private double value;

        public double get() {
            return value;
        }

        @Override
        public void pidWrite(double output) {
            synchronized (this) {
                value = output;
            }
        }

    }

}
