package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.subsystems.Intake;

/**
 * Created by Ben Bernard on 2/3/2018.
 */
public class AutoEject extends Command {

    private Intake _intake;
    private long _endMillis;
    private double DROP_SPEED;

    public AutoEject(Intake intake) {
        requires(intake);
        _intake = intake;
        DROP_SPEED = Constants.Intake.DROP_SPEED;
    }

    public AutoEject(Intake intake, double dropSpeed) {
        requires(intake);
        _intake = intake;
        DROP_SPEED = dropSpeed;
    }

    @Override
    protected void initialize() {
        super.initialize();
        _endMillis = System.currentTimeMillis() + Constants.Intake.EJECT_TIME;
        DriverStation.reportError("AutoEject initializing", false);
    }

    @Override
    protected void execute() {
        _intake.drive(DROP_SPEED, DROP_SPEED);
    }

    @Override
    protected boolean isFinished() {
        return System.currentTimeMillis() >= _endMillis;

    }

    @Override
    protected void end() {
        _intake.drive(0, 0);
        DriverStation.reportError("AutoEject ending", false);
    }

    @Override
    protected void interrupted() {
        end();
    }
}
