package org.frc5687.powerup.robot.commands.testing;

import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.subsystems.DriveTrain;

public class SpinAtConstantSpeed extends Command {
    private DriveTrain _driveTrain;
    private double _speed;

    public SpinAtConstantSpeed(DriveTrain driveTrain, double speed) {
        _driveTrain = driveTrain;
        _speed = speed;
        requires(_driveTrain);
    }

    @Override
    protected void initialize() {
        _driveTrain.setPower(_speed, -_speed, true);
    }

    @Override
    protected void end() {
        _driveTrain.setPower(0, 0, true);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
