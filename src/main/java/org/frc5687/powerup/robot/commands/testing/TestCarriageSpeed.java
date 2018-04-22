package org.frc5687.powerup.robot.commands.testing;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.subsystems.Carriage;

public class TestCarriageSpeed extends Command {

    private Carriage _carriage;
    private int _ticks;
    private int _tick;

    public TestCarriageSpeed(Carriage carriage, int ticks) {
        requires(carriage);
        _carriage = carriage;
        _ticks = ticks;
    }

    @Override
    protected boolean isFinished() {
        if (_tick >= _ticks) {
            _carriage.drive(0.1);
            return true;
        }
        return false;
    }

    @Override
    protected void initialize() {
        super.initialize();
        DriverStation.reportError("TestCarriageSpeed starting", false);
        _carriage.drive(1);
    }

    @Override
    protected void execute() {
        _tick++;
    }
}
