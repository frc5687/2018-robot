package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.subsystems.Arm;

public class DriveArm extends Command implements PIDOutput {
    private Arm arm;
    private OI oi;
    private PIDController controller;
    private double pidOut;
    private double target;

    public DriveArm(Arm arm, OI oi) {
        requires(arm);
        this.arm = arm;
        this.oi = oi;
    }

    @Override
    protected void initialize() {
        double kP = 0.5;
        double kI = 0.1;
        double kD = 0.1;
        double target = 0;
        controller = new PIDController(kP, kI, kD, arm.getAngle(), this);
        controller.setAbsoluteTolerance(5);
        controller.setInputRange(0, 340);
        controller.setOutputRange(0, 1020);
        controller.setAbsoluteTolerance(5);
        controller.setContinuous();
        controller.setSetPoint(target);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void execute() {
        if (oi.getArmSpeed() != 0) {
            target = oi.getArmSpeed() * Constants.Arm.SPEED_SCALAR + target;
        }
        SmartDashboard.putNumber("Arm/Speed", speed);
    }
}