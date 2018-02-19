package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.Robot;

/**
 * Created by Ben Bernard on 2/18/2018.
 */
public class AutoFetchCube extends CommandGroup {
    public AutoFetchCube(Robot robot) {
        addParallel(new AutoIntake(robot));
        addParallel(new AutoApproachCube(robot,0.7, 120, 5000));
    }
}
