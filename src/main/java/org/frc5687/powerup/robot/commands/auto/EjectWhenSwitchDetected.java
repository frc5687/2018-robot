package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.Robot;

public class EjectWhenSwitchDetected extends CommandGroup {
    public EjectWhenSwitchDetected(Robot robot) {
        addSequential(new AutoWaitForDistance(robot.getDriveTrain(), 30, 5000));
        addSequential(new WaitUntilSwitchDetected(robot));
        addSequential(new AutoEject(robot.getIntake(), -0.62));
    }
}
