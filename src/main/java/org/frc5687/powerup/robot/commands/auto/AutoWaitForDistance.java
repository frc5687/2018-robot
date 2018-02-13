package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.subsystems.Arm;
import org.frc5687.powerup.robot.subsystems.DriveTrain;

public class AutoWaitForDistance extends Command {
    private Arm _arm;
    private DriveTrain _driveTrain;
    private double _inches;
    private long _millis;
    private double _endInches;
    private long _endMillis;


    public AutoWaitForDistance(Arm arm, DriveTrain driveTrain, double inches, long millis) {
        requires(arm);
        _arm = arm;
        _driveTrain = driveTrain;
        _inches = inches;
        _millis = millis;
    }

    @Override
    protected void initialize() {
        _endInches = _driveTrain.getDistance() + _inches;
        _endMillis = System.currentTimeMillis() + _millis;
    }

    @Override
    protected boolean isFinished() {
        if (_driveTrain.getDistance()>= _endInches ) {
            DriverStation.reportError("AutoWaitForDistance finished at " + _endInches + " inches.", false);
            return true;
        }
        if (System.currentTimeMillis() >= _endMillis) {
            DriverStation.reportError("AutoWaitForDistance timed out after " + _millis + " milliseconds.", false);
            return true;
        }
        return false;
    }
}
