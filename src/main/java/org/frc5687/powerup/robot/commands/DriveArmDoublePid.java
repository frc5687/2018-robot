package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.subsystems.Arm;

public class DriveArmDoublePid extends Command {
    private Arm _arm;

    public DriveArmDoublePid(Robot robot) {
        _arm = robot.getArm();
        requires(_arm);
    }
    @Override
    public boolean isFinished() {
        return false;
    }
}
