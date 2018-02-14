package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.Robot;

/**
 * Created by Ben Bernard on 2/12/2018.
 */
public class PrepIntakeForSwitch extends CommandGroup {
    public PrepIntakeForSwitch(Robot robot, double inches, long millis) {
        addSequential(new AutoWaitForDistance(robot.getArm(), robot.getDriveTrain(), inches, millis));
        addSequential(new IntakeToSwitch(robot.getCarriage(), robot.getArm()) );
    }
}
