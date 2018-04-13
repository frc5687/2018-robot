package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.frc5687.powerup.robot.subsystems.Intake;

public class AbortIfNoCubeDetected extends Command {
    private Intake _intake;
    private boolean _isFinished;

    public AbortIfNoCubeDetected(Intake intake) {
        _intake = intake;
    }

    @Override
    protected void initialize() {
        DriverStation.reportError("AbortIfNoCubeDetected initialized", false);
        _isFinished = true;
        if (!_intake.cubeIsSecured()) {
            DriverStation.reportError("AbortIfNoCubeDetected no cube detected.. running Scheduler.getInstance().removeAll()", false);
            Scheduler.getInstance().removeAll();
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
