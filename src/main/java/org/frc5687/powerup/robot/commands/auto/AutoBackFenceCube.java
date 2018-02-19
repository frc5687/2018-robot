package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.Robot;

/**
 * Created by Ben Bernard on 2/18/2018.
 */
public class AutoBackFenceCube extends CommandGroup {
    public AutoBackFenceCube(int switchSide, int scaleSide, Robot robot) {
        if (scaleSide==switchSide) {
            addSequential(new AutoFetchCube(robot));
            addSequential(new AutoDrive(robot.getDriveTrain(), -6.0, 0.7, true, true, 1000, "retreat"));
            addSequential(new IntakeToScale(robot.getCarriage(), robot.getArm()));
            addSequential(new AutoEject(robot.getIntake()));
        }
    }


}
