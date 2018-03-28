package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.subsystems.Intake;

import java.nio.ReadOnlyBufferException;

/**
 * Created by Ben Bernard on 2/8/2018.
 */
public class AutoIntake extends Command {

    private Intake _intake;
    private long _endMillis;
    private State _state = State.INTAKE;

    public AutoIntake(Intake intake) {
        _intake = intake;
        requires(intake);
    }

    @Override
    protected void initialize() {
        super.initialize();
        _state = State.INTAKE;
        DriverStation.reportError("AutoIntake initializing", false);
    }

    @Override
    protected void execute() {
        super.execute();
        switch (_state) {
            case INTAKE:
                _intake.drive(Constants.Intake.INTAKE_SPEED, Constants.Intake.INTAKE_SPEED);
                if (_intake.cubeIsDetected()) {
                    _state = State.SETTLE;
                    _endMillis = System.currentTimeMillis() + Constants.Intake.SETTLE_TIME;
                }
                break;
            case SETTLE:
                if (System.currentTimeMillis() > _endMillis) {
                    _intake.drive(Constants.Intake.INTAKE_SPEED, Constants.Intake.INTAKE_SPEED);
                    _state = State.HOLD;
                }
        }
    }

    @Override
    protected void end() {
        DriverStation.reportError("AutoIntake ending", false);
    }

    @Override
    protected boolean isFinished() {
        return _state==State.HOLD;
    }


    public enum State {
        INTAKE,
        SETTLE,
        HOLD
    }


}
