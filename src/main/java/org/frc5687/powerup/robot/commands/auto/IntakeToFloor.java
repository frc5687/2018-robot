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
        int ENCODER_BOTTOM = carriage.isCompetitionBot() ? Constants.Carriage.ENCODER_BOTTOM_COMP : Constants.Carriage.ENCODER_BOTTOM_PROTO;
        double INTAKE = arm.isCompetitionBot() ? Constants.Arm.Pot.INTAKE_COMP : Constants.Arm.Pot.INTAKE_PROTO;
        addSequential(new ClearBumpersIfNeeded(carriage));
        addParallel(new MoveArmToSetpointPID(arm, INTAKE));
        addSequential(new MoveCarriageToSetpointPID(carriage, ENCODER_BOTTOM));
    }
}
