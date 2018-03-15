package org.frc5687.powerup.robot.commands.actions;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.commands.MoveArmToSetpointPID;
import org.frc5687.powerup.robot.commands.MoveCarriageToSetpointPID;
import org.frc5687.powerup.robot.subsystems.Arm;
import org.frc5687.powerup.robot.subsystems.Carriage;

public class IntakeToApproach extends CommandGroup {

    public IntakeToApproach(Carriage carriage, Arm arm) {
        int carriageBottom = carriage.isCompetitionBot() ? Constants.Carriage.ENCODER_BOTTOM_COMP : Constants.Carriage.ENCODER_BOTTOM_PROTO;
        double armTop = arm.isCompetitionBot() ? Constants.Arm.Pot.TOP_COMP : Constants.Arm.Pot.TOP_PROTO;
        addParallel(new MoveArmToSetpointPID(arm, armTop));
        addSequential(new MoveCarriageToSetpointPID(carriage, carriageBottom));
    }
}
