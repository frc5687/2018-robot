package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.Robot;

public class KillAll extends Command {
    public KillAll(Robot robot) {
        requires(robot.getArm());
        requires(robot.getCarriage());
        requires(robot.getClimber());
        requires(robot.getDriveTrain());
        requires(robot.getIntake());
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
