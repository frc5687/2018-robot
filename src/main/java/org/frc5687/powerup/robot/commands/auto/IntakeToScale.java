package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.commands.MoveArmToSetpointPID;
import org.frc5687.powerup.robot.commands.MoveCarriageToSetpointPID;
import org.frc5687.powerup.robot.subsystems.Arm;
import org.frc5687.powerup.robot.subsystems.Carriage;

public class IntakeToScale extends CommandGroup {

    public IntakeToScale(Carriage carriage, Arm arm) {
        int ENCODER_TOP = carriage.isCompetitionBot() ? Constants.Carriage.ENCODER_TOP_COMP : Constants.Carriage.ENCODER_TOP_PROTO;
        addParallel(new MoveCarriageToSetpointPID(carriage, ENCODER_TOP));
        addParallel(new MoveArmToSetpointPID(arm, Constants.Arm.ENCODER_TOP));
    }
}

