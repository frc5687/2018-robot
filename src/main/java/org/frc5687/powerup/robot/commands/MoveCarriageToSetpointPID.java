package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.subsystems.Carriage;

public class MoveCarriageToSetpointPID extends Command {

    private double _target;
    private Carriage _carriage;
    private long _endMillis;
    private long _timeoutMS = 4000;

    public MoveCarriageToSetpointPID(Carriage carriage, double target) {
        requires(carriage);
        _carriage = carriage;
        _target = target;
    }

    public MoveCarriageToSetpointPID(Carriage carriage, double target, long timeoutMS) {
        requires(carriage);
        _carriage = carriage;
        _target = target;
        _timeoutMS = timeoutMS;
    }


    @Override
    protected boolean isFinished() {
        if (System.currentTimeMillis() >= _endMillis) {
            DriverStation.reportError("MoveCarriageToSetpointPID timed out after " + _timeoutMS + "ms", false);
            return true;
        }
        if (_carriage.onTarget()) {
            DriverStation.reportError("MoveCarriageToSetpointPID completed at " + _carriage.getPosition(), false);
            return true;
        }
        return false;
    }


    @Override
    protected void initialize() {
        super.initialize();
        _endMillis = System.currentTimeMillis() + _timeoutMS;
        DriverStation.reportError("Starting MoveCarriageToSetpointPID to " + _target + " for max " + _timeoutMS + "ms", false);
        _carriage.setSetpoint(_target);
        _carriage.enable();
    }

    @Override
    protected void end() {
        _carriage.disable();
        DriverStation.reportError("Ending MoveCarriageToSetpointPID", false);
        _carriage.drive(_carriage.calculateHoldSpeed());
    }

    @Override
    protected void execute() {
        // Add logging here
        // DriverStation.reportError("MoveCarriageToSetpointPID at " + _carriage.getPosition(), false);
    }
}
