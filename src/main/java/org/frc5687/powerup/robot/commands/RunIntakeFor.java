package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.subsystems.Intake;

public class RunIntakeFor extends Command {
    private Intake _intake;
    private long _startTime;
    private long _runForMillis;
    private double _leftSpeed;
    private double _rightSpeed;

    public RunIntakeFor(Intake intake, long runForMillis, double leftSpeed, double rightSpeed) {
        _intake = intake;
        _runForMillis = runForMillis;
        _leftSpeed = leftSpeed;
        _rightSpeed = rightSpeed;
    }

    @Override
    protected void initialize() {
        _startTime = System.currentTimeMillis();
        _intake.drive(_leftSpeed, _rightSpeed);
    }

    @Override
    protected void end() {
        _intake.drive(0, 0);
    }

    @Override
    protected boolean isFinished() {
        return System.currentTimeMillis() >= _startTime + _runForMillis;
    }
}
