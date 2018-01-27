package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.subsystems.Climber;

public class DriveClimber extends Command {
    private Climber climber;
    private OI oi;

    public DriveClimber(Climber climber, OI oi){
        requires(climber);
        this.climber = climber;
        this.oi = oi;
    }

    @Override
    protected void execute() {
        SmartDashboard.putNumber("Climber/speed", oi.getClimberSpeed());
        climber.drive(oi.getClimberSpeed());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}
