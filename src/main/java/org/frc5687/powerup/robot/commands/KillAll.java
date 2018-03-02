package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.Robot;

public class KillAll extends Command {
    private boolean _finished;
    private Robot _robot;

    public KillAll(Robot robot) {
        requires(robot.getArm());
        requires(robot.getCarriage());
        requires(robot.getClimber());
        requires(robot.getDriveTrain());
        requires(robot.getIntake());
        requires(robot.getLights());
        _robot = robot;
    }

    @Override
    protected void initialize() {
        _robot.getArm().disable();
        _robot.getCarriage().disable();
        _finished = true;
        DriverStation.reportError("Initialize KillAll Command", false);
    }

    @Override
    protected void end() {
        DriverStation.reportError("Ending KillAll Command", false);
    }

    @Override
    protected boolean isFinished() {
        return _finished;
    }
}
