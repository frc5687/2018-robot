package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.subsystems.Intake;

public class AbortIfNoCubeDetected extends Command {
    private Robot _robot;
    private Intake _intake;
    private boolean _isFinished;

    public AbortIfNoCubeDetected(Robot robot) {
        _robot = robot;
        _intake = robot.getIntake();
    }

    @Override
    protected void initialize() {
        DriverStation.reportError("AbortIfNoCubeDetected initialized", false);
        _isFinished = true;
        if (!_intake.cubeIsSecured()) {
            DriverStation.reportError("AbortIfNoCubeDetected no cube detected.. calling robot.requestAbortAuton()", false);
            _robot.requestAbortAuton();
        }
    }

    @Override
    protected boolean isFinished() {
        return _isFinished;
    }

    @Override
    protected void end() {
        DriverStation.reportError("AbortIfNoCubeDetected ending", false);
    }
}
