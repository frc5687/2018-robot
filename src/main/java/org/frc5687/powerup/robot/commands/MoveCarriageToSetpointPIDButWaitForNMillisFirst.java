package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.commands.auto.AutoWaitForMillis;
import org.frc5687.powerup.robot.subsystems.Carriage;

public class MoveCarriageToSetpointPIDButWaitForNMillisFirst extends CommandGroup {
    public MoveCarriageToSetpointPIDButWaitForNMillisFirst(Carriage carriage, double target, long millis) {
        addSequential(new AutoWaitForMillis(millis));
        addSequential(new MoveCarriageToSetpointPID(carriage, target));
    }
}
