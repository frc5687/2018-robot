package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.commands.MoveCarriageToSetpointPID;

public class AutoZeroCarriageThenLower extends CommandGroup {
    public AutoZeroCarriageThenLower(Robot robot) {
        addSequential(new AutoZeroCarriage(robot.getCarriage()));
        addSequential(new MoveCarriageToSetpointPID(robot.getCarriage(), -600));
    }
}
