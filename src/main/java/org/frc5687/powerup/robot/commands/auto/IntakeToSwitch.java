package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.commands.MoveArmToSetpointPID;
import org.frc5687.powerup.robot.commands.MoveCarriageToSetpointPID;
import org.frc5687.powerup.robot.subsystems.Arm;
import org.frc5687.powerup.robot.subsystems.Carriage;

public class IntakeToSwitch extends CommandGroup {

    public IntakeToSwitch(Carriage carriage, Arm arm) {
        int ENCODER_MIDDLE = carriage.isCompetitionBot() ? Constants.Carriage.ENCODER_MIDDLE_COMP : Constants.Carriage.ENCODER_MIDDLE_PROTO;
        addParallel(new MoveCarriageToSetpointPID(carriage, ENCODER_MIDDLE));
        addParallel(new MoveArmToSetpointPID(arm, arm.isCompetitionBot() ? Constants.Arm.Pot.SWITCH_HEIGHT_COMP : Constants.Arm.Pot.SWITCH_HEIGHT_PROTO ));
    }
}

