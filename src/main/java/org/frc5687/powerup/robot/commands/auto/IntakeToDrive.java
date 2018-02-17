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
public class IntakeToDrive extends CommandGroup {
    private int ENCODER_CLEAR_BUMPERS;
    private int ENCODER_DRIVE;
    private Carriage _carriage;
    private Arm _arm;

    public IntakeToDrive(Carriage carriage, Arm arm) {
        _carriage = carriage;
        _arm = arm;
        ENCODER_DRIVE = carriage.isCompetitionBot() ? Constants.Carriage.ENCODER_DRIVE_COMP : Constants.Carriage.ENCODER_DRIVE_PROTO;
        // If the carriage is low enough to restrict arm movement due to bumpers, wait until the carriage is up
        /*
        ENCODER_CLEAR_BUMPERS = carriage.isCompetitionBot() ? Constants.Carriage.ENCODER_CLEAR_BUMPERS_COMP : Constants.Carriage.ENCODER_CLEAR_BUMPERS_PROTO;
        if (_carriage.getPos() < ENCODER_CLEAR_BUMPERS) {
            addSequential(new MoveCarriageToSetpointPID(_carriage, ENCODER_CLEAR_BUMPERS));
        }
        */

        addSequential(new ClearBumpersIfNeeded(_carriage));
        addParallel(new MoveArmToSetpointPID(_arm, Constants.Arm.Pot.BOTTOM));
        addSequential(new MoveCarriageToSetpointPID(_carriage, ENCODER_DRIVE));
    }
}

