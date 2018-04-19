package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.commands.auto.AutoWaitForDistance;
import org.frc5687.powerup.robot.commands.auto.AutoWaitForMillis;
import org.frc5687.powerup.robot.subsystems.Carriage;
import org.frc5687.powerup.robot.subsystems.DriveTrain;

public class MoveCarriageToSetpointPIDButWaitForNInchesFirst extends CommandGroup {
    public MoveCarriageToSetpointPIDButWaitForNInchesFirst(DriveTrain drivetrain, Carriage carriage, int target, double inches) {
        addSequential(new AutoWaitForDistance(drivetrain, inches, 5000));
        addSequential(new MoveCarriageToSetpointPIDButFirstZeroIt(carriage, target));
    }
}
