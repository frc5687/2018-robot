package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
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
        carriage.drive(oi.getCarriageSpeed());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}