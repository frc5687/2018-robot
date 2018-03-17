package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.commands.actions.IntakeToApproach;

/**
 * Created by Ben Bernard on 3/10/2018.
 */
public class PrepIntakeForTraverse extends CommandGroup {

    public PrepIntakeForTraverse(Robot robot, double inches, long millis) {
        addSequential(new AutoWaitForDistance(robot.getDriveTrain(), inches, millis));
        addSequential(new IntakeToApproach(robot.getCarriage(), robot.getArm()));
    }
}
