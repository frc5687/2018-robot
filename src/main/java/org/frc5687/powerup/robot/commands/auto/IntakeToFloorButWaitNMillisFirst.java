package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.commands.actions.IntakeToFloor;
import org.frc5687.powerup.robot.subsystems.Arm;
import org.frc5687.powerup.robot.subsystems.Carriage;

public class IntakeToFloorButWaitNMillisFirst extends CommandGroup {
    public IntakeToFloorButWaitNMillisFirst(Carriage carriage, Arm arm, long millis) {
        addSequential(new AutoWaitForMillis(millis));
        addSequential(new IntakeToFloor(carriage, arm));
    }
}
