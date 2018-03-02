package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
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
    public PrepIntakeForScale(Robot robot, double inches, long millis) {
        addSequential(new AutoWaitForDistance(robot.getArm(), robot.getDriveTrain(), inches, millis));
        if (robot.getCarriage().isHealthy()) {
            addParallel(new MoveCarriageToSetpointPID(robot.getCarriage(), -5, 10000, false));
        }
        if (robot.getArm().isHealthy()) {
            addSequential(new MoveArmToSetpointPID(robot.getArm(), 164));
        }
    }

    public PrepIntakeForScale(Robot robot, double inches, long millis, boolean distinctiveAutoZero) {
        addParallel(new AutoZeroCarriageThenLower(robot));
        addSequential(new AutoWaitForDistance(robot.getArm(), robot.getDriveTrain(), inches, millis));
        if (robot.getArm().isHealthy()) {
            addSequential(new MoveArmToSetpointPID(robot.getArm(), 150));
        }
        if (robot.getCarriage().isHealthy()) {
            addSequential(new MoveCarriageToSetpointPID(robot.getCarriage(), -5, 30000, false));
        }
    }
}
