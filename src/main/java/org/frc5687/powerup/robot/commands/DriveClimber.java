package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.subsystems.Climber;

public class DriveClimber extends Command {
    private Climber _climber;
    public DriveClimber(Climber climber){
        requires(climber);
        _climber=climber;
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}
