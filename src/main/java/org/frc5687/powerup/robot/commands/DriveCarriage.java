package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
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
        double oiSpeed = DriverStation.getInstance().isAutonomous() ? 0 : oi.getCarriageSpeed();
        if (oiSpeed != 0.0) {
            carriage.disable();
            //DriverStation.reportError("DriveCarriage requesting oiSpeed: " + Double.toString(oiSpeed), false);
            carriage.drive(oiSpeed);
        } else if (!carriage.getPIDController().isEnabled()) {
            double holdSpeed = carriage.calculateHoldSpeed();
            //DriverStation.reportError("DriveCarriage requested Hold Speed: " + Double.toString(holdSpeed), false);
            carriage.drive(holdSpeed);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}