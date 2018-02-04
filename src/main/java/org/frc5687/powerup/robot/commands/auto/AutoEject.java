package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.subsystems.Intake;

/**
 * Created by Ben Bernard on 2/3/2018.
 */
public class AutoEject extends Command {

    private Intake _intake;
    private long _endMillis;

    public AutoEject(Intake intake) {
        requires(intake);
        _intake = intake;
    }

    @Override
    protected void initialize() {
        super.initialize();
        _endMillis = System.currentTimeMillis() + Constants.Intake.EJECT_TIME;
    }

    @Override
    protected void execute() {
        _intake.drive(Constants.Intake.DROP_SPEED, Constants.Intake.DROP_SPEED);
    }

    @Override
    protected boolean isFinished() {
        return System.currentTimeMillis() > _endMillis;

    }

    @Override
    protected void end() {
        _intake.drive(0, 0);
    }

    @Override
    protected void interrupted() {
        end();
    }
}
