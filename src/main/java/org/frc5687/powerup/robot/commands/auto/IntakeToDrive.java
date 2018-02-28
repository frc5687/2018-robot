package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.commands.MoveArmToSetpointPID;
import org.frc5687.powerup.robot.commands.MoveCarriageToSetpointPID;
import org.frc5687.powerup.robot.subsystems.Arm;
import org.frc5687.powerup.robot.subsystems.Carriage;

public class IntakeToDrive extends CommandGroup {

    public IntakeToDrive(Carriage carriage, Arm arm) {
        int ENCODER_DRIVE = carriage.isCompetitionBot() ? Constants.Carriage.ENCODER_DRIVE_COMP : Constants.Carriage.ENCODER_DRIVE_PROTO;
        double DRIVE = arm.isCompetitionBot() ? Constants.Arm.Pot.DRIVE_COMP : Constants.Arm.Pot.DRIVE_PROTO;
        //addSequential(new ClearBumpersIfNeeded(carriage));
        addParallel(new MoveArmToSetpointPID(arm, DRIVE));
        addSequential(new MoveCarriageToSetpointPID(carriage, ENCODER_DRIVE));
    }
}
