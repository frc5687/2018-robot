package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.Robot;

public class WaitUntilSwitchDetected extends Command {
    private Robot _robot;

    public WaitUntilSwitchDetected(Robot robot) {
        _robot = robot;
        requires(robot.getIntake());
    }

    @Override
    protected boolean isFinished() {
        return _robot.getIntake().switchDetected();
    }
}
