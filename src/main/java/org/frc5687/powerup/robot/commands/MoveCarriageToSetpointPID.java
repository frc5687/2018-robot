package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.subsystems.Carriage;

public class MoveCarriageToSetpointPID extends Command {

    private double _target;
    private Carriage _carriage;
    private long _startMillis;
    private double _timeoutMS = -1;
    private double _holdForMS = -1;

    public MoveCarriageToSetpointPID(Carriage carriage, double target) {
        requires(carriage);
        _carriage = carriage;
        _target = target;
        _startMillis = System.currentTimeMillis();
    }

    public MoveCarriageToSetpointPID(Carriage carriage, double target, double timeoutMS) {
        requires(carriage);
        _carriage = carriage;
        _target = target;
        _timeoutMS = timeoutMS;
        _startMillis = System.currentTimeMillis();
    }

    public MoveCarriageToSetpointPID(Carriage carriage, double target, double timeoutMS, double holdForMS) {
        requires(carriage);
        _carriage = carriage;
        _target = target;
        _timeoutMS = timeoutMS;
        _startMillis = _startMillis;
        _holdForMS = holdForMS;
    }

    public MoveCarriageToSetpointPID(Carriage carriage, double target, double holdForMS, boolean placeHolder) {
        requires(carriage);
        _carriage = carriage;
        _target = target;
        _startMillis = _startMillis;
        _holdForMS = holdForMS;
    }

    protected boolean _isTimedOut() {
        if (_timeoutMS != -1) {
            return System.currentTimeMillis() >= _timeoutMS + _startMillis;
        } else {
            return false;
        }
    }

    protected boolean isDoneHolding() {
        if (_holdForMS != -1) {
            return System.currentTimeMillis() >= _holdForMS + _startMillis;
        } else {
            return true;
        }
    }

    @Override
    protected boolean isFinished() {
        if (_isTimedOut()) {
            return true;
        } else if (_carriage.onTarget()) {
            if (isDoneHolding()) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected void initialize() {
        super.initialize();

        DriverStation.reportError("Starting MoveCarriageToSetpointPID", false);
        _carriage.setSetpoint(_target);
        _carriage.enable();
    }

    @Override
    protected void end() {
        _carriage.disable();
        DriverStation.reportError("Ending MoveCarriageToSetpointPID", false);
    }

    @Override
    protected void execute() {
        // Add logging here
        DriverStation.reportError("MoveCarriageToSetpointPID at " + _carriage.getPosition(), false);
    }
}
