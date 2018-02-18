package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.subsystems.Carriage;

/**
 * Created by Ben Bernard on 2/3/2018.
 */
public class AutoZeroCarriage extends Command {

    private Carriage _carriage;

    public AutoZeroCarriage(Carriage carriage) {
        requires(carriage);
        _carriage = carriage;
    }

    @Override
    protected void initialize() {
        super.initialize();
        DriverStation.reportError("Starting AutoZeroCarriage", false);
    }

    @Override
    protected void execute() {
        _carriage.drive(Constants.Carriage.ZERO_SPEED);
        super.execute();
    }

    @Override
    protected void end() {
        _carriage.zeroEncoder();
        DriverStation.reportError("Ending AutoZeroCarriage", false);
        super.end();
    }

    @Override
    protected boolean isFinished() {
        return _carriage.isAtTop();
    }
}
