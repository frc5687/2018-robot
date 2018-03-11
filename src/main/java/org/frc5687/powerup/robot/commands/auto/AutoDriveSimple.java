package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.subsystems.DriveTrain;

/**
 * Created by Ben Bernard on 2/3/2018.
 */
public class AutoDriveSimple extends Command {
    private double _speed=0;
    private double _distance=0;
    private DriveTrain _drivetrain;

    public AutoDriveSimple(DriveTrain driveTrain, double distance, double speed) {
        requires(driveTrain);
        _distance = distance;
        _speed = speed;
        _drivetrain = driveTrain;
    }

    @Override
    protected boolean isFinished() {
        if (_drivetrain.getDistance() >= _distance) {
            _drivetrain.setPower(0,0, true);
            return true;
        }
        return false;
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        _drivetrain.setPower(_speed, _speed);
    }

}
