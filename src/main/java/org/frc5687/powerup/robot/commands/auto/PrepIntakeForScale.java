package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.MoveArmToSetpointPID;
import org.frc5687.powerup.robot.commands.MoveCarriageToSetpointPID;
import org.frc5687.powerup.robot.subsystems.Arm;
import org.frc5687.powerup.robot.subsystems.Carriage;
import org.frc5687.powerup.robot.subsystems.DriveTrain;

/**
 * Created by Ben Bernard on 2/12/2018.
 */
public class PrepIntakeForScale extends CommandGroup {

    public PrepIntakeForScale(Robot robot, double inches, long millis, boolean zeroCarriageFirst, boolean lowerCarriageAfterZero) {
        if (zeroCarriageFirst && lowerCarriageAfterZero) {
            addParallel(new AutoZeroCarriageThenLower(robot));
        } else if (zeroCarriageFirst) {
            addParallel(new AutoZeroCarriage(robot.getCarriage()));
        }
        addSequential(new AutoWaitForDistance(robot.getDriveTrain(), inches, millis));
        addSequential(new MoveArmToSetpointPID(robot.getArm(), Constants.Arm.Pot.SCALE));
        int carriageHeight = robot.isCompetitionBot() ? Constants.Carriage.ENCODER_TOP_COMP : Constants.Carriage.ENCODER_TOP_PROTO;
        addSequential(new MoveCarriageToSetpointPID(robot.getCarriage(), carriageHeight, 5000));
    }

    public PrepIntakeForScale(Robot robot, double inches, long millis, boolean zeroCarriageFirst) {
        if (zeroCarriageFirst) {
            addParallel(new AutoZeroCarriageThenLower(robot));
        }
        addSequential(new AutoWaitForDistance(robot.getDriveTrain(), inches, millis));
        if (robot.getArm().isHealthy()) {
            addSequential(new MoveArmToSetpointPID(robot.getArm(), Constants.Arm.Pot.SCALE));
        }
        int carriageHeight = robot.isCompetitionBot() ? Constants.Carriage.ENCODER_TOP_COMP : Constants.Carriage.ENCODER_TOP_PROTO;
        if (robot.getCarriage().isHealthy()) {
            addSequential(new MoveCarriageToSetpointPID(robot.getCarriage(), carriageHeight, 5000));
        }
    }

    public PrepIntakeForScale(Robot robot, long millis, boolean zeroCarriageFirst) {
        if (zeroCarriageFirst) {
            addParallel(new AutoZeroCarriageThenLower(robot));
        }
        addSequential(new AutoWaitForMillis(millis));
        if (robot.getArm().isHealthy()) {
            addSequential(new MoveArmToSetpointPID(robot.getArm(), Constants.Arm.Pot.SCALE));
        }
        if (robot.getCarriage().isHealthy()) {
            int carriageHeight = robot.isCompetitionBot() ? Constants.Carriage.ENCODER_TOP_COMP : Constants.Carriage.ENCODER_TOP_PROTO;
            addSequential(new MoveCarriageToSetpointPID(robot.getCarriage(), carriageHeight, 5000));
        }
    }
}
