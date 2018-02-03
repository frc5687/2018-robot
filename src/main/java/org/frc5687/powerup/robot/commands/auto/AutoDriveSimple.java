package org.frc5687.powerup.robot.commands.auto;

import com.sun.prism.shader.Solid_TextureSecondPassLCD_AlphaTest_Loader;
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
            _drivetrain.tankDrive(0,0);
            return true;
        }
        return false;
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        _drivetrain.tankDrive(_speed, _speed);
    }

}
