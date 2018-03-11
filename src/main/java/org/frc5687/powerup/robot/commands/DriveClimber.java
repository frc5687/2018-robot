package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.subsystems.Climber;

public class DriveClimber extends Command {
    private Climber climber;
    private OI oi;

    private boolean _holdOn = false;

    public DriveClimber(Climber climber, OI oi){
        requires(climber);
        this.climber = climber;
        this.oi = oi;
    }

    @Override
    protected void execute() {
        double direction = oi.getClimberSpeed();
        double speed = 0;
        if (direction > 0) {
            _holdOn = true;
            speed = Constants.Climber.WIND_SPEED;
        } else if (direction < 0) {
            _holdOn = false;
            speed = Constants.Climber.UNWIND_SPEED;
        } else if (_holdOn) {
            speed = Constants.Climber.HOLD_SPEED;
        }

        climber.drive(speed);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}
