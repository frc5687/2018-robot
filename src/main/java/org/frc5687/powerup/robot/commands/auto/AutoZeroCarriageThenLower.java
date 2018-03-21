package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.commands.MoveCarriageToSetpointPID;
import org.frc5687.powerup.robot.commands.actions.IntakeToDrive;

public class AutoZeroCarriageThenLower extends CommandGroup {
    public AutoZeroCarriageThenLower(Robot robot) {
        addSequential(new AutoZeroCarriage(robot.getCarriage()));
        addSequential(new IntakeToDrive(robot.getCarriage(), robot.getArm()));
    }
}
