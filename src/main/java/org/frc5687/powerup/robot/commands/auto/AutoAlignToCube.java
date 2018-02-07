package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.subsystems.DriveTrain;

public class AutoAlignToCube extends Command implements PIDOutput {
    private CubeSource _cubeSource;
    private PIDController controller;
    private double speed;
    private double pidOut;
    private int i;

    private DriveTrain driveTrain;

    public AutoAlignToCube(Robot robot, double speed) {
        driveTrain = robot.getDriveTrain();
        _cubeSource = new CubeSource(robot);
        this.speed = speed;
    }

    @Override
    protected void initialize() {
        double kP = Constants.Auto.Align.kP; // Double.parseDouble(SmartDashboard.getString("DB/String 0", ".04"));
        double kI = Constants.Auto.Align.kI; // Double.parseDouble(SmartDashboard.getString("DB/String 1", ".006"));
        double kD = Constants.Auto.Align.kD; //Double.parseDouble(SmartDashboard.getString("DB/String 2", ".09"));

        controller = new PIDController(kP, kI, kD, _cubeSource, this);
        controller.setInputRange(-30, 30);
        controller.setOutputRange(-speed, speed);
        controller.setAbsoluteTolerance(1);
        controller.setContinuous();
        controller.setSetpoint(0);
        controller.enable();
        DriverStation.reportError("kP="+kP+" , kI="+kI+", kD="+kD + ",T="+ Constants.Auto.Align.TOLERANCE, false);
    }

    @Override
    protected void execute() {
        driveTrain.tankDrive(pidOut, -pidOut); // positive output is clockwise
        SmartDashboard.putBoolean("AutoAlignToCube/onTarget", controller.onTarget());
        SmartDashboard.putData("AutoAlignToCube/pid", controller);
        SmartDashboard.putNumber("AutoAlignToCube/i", i);
    }

    @Override
    public void pidWrite(double output) {
        synchronized (this) {
            i++;
            pidOut = output;
            SmartDashboard.putNumber("AutoAlignToCube/pidOut", pidOut);
        }
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
}
