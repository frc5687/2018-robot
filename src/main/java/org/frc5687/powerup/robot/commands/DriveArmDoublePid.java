package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.subsystems.Arm;
import org.frc5687.powerup.robot.utils.TrajectoryController;

public class DriveArmDoublePid extends Command {
    private Arm _arm;
    private TrajectoryController first;

    public DriveArmDoublePid(Robot robot) {
        _arm = robot.getArm();
        requires(_arm);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void end() {

    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
