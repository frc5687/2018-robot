package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.subsystems.Carriage;

public class MoveCarriageToSetpointPID extends Command {

    private double _target;
    private Carriage _carriage;



    public MoveCarriageToSetpointPID(Carriage carriage, double target) {
        requires(carriage);
        _carriage = carriage;
        _target = target;
    }

    @Override
    protected boolean isFinished() {
        if (_carriage.onTarget()) {
            return true;
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
