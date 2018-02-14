package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.commands.MoveArmToSetpointPID;
import org.frc5687.powerup.robot.commands.MoveCarriageToSetpointPID;
import org.frc5687.powerup.robot.subsystems.Arm;
import org.frc5687.powerup.robot.subsystems.Carriage;

/**
 * Created by Ben Bernard on 2/4/2018.
 */
public class IntakeToFloor extends CommandGroup {
    public IntakeToFloor(Carriage carriage, Arm arm) {
        // If the carriag is below bumper heights, raise it!
        if (carriage.getPos() > Constants.Carriage.ENCODER_CLEAR_BUMPERS) {
            addParallel(new MoveCarriageToSetpointPID(carriage, Constants.Carriage.ENCODER_CLEAR_BUMPERS));
        }
        addParallel(new MoveArmToSetpointPID(arm, Constants.Arm.Angles.INTAKE, true));

        addSequential(new MoveCarriageToSetpointPID(carriage, Constants.Carriage.ENCODER_BOTTOM));
    }
}

