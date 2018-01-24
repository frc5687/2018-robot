package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.subsystems.Carriage;

public class DriveCarriage extends Command {
    private Carriage carriage;
    private OI oi;

    public DriveCarriage(Carriage carriage, OI oi) {
        requires(carriage);

        this.oi = oi;
        this.carriage = carriage;
    }

    @Override
    protected void execute() {
        double speed = oi.getCarriageSpeed();
        if (speed < 0 && carriage.isAtTop()) {
            speed = 0;
        } else if (speed > 0 && carriage.isAtBottom()) {
            speed = 0;
        }
        SmartDashboard.putNumber("CarriageSpeed", speed);
        // If speed is positive, then we're going to go down
        // If speed is negative, then we're going to go up
        carriage.drive(speed);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}