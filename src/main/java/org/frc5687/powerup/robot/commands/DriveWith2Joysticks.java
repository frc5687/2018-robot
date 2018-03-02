package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.frc5687.powerup.robot.subsystems.DriveTrain;
import org.frc5687.powerup.robot.OI;

/**
 * Command for controlling each side of the drive train with a joystick
 */
public class DriveWith2Joysticks extends Command {
    private DriveTrain driveTrain;
    private OI oi;

    public DriveWith2Joysticks(DriveTrain driveTrain, OI oi) {
        requires(driveTrain);
        this.driveTrain = driveTrain;
        this.oi = oi;
    }

    @Override
    protected void execute() {
        driveTrain.setPower(oi.getLeftSpeed(), oi.getRightSpeed());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
