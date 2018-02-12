package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.subsystems.Arm;
import org.frc5687.powerup.robot.subsystems.DriveTrain;

public class AutoWaitForDistance extends Command {

    private Arm _arm;
    private DriveTrain driveTrain;
    private double _distance;
    public AutoAlignToSwitch(Arm arm {
        requires(Arm arm);
            }

    @Override
    protected boolean isFinished() {
        return driveTrain.getDistance()< _distance;
    }
}
