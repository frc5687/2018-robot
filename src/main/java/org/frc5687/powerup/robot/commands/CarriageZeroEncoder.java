package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.subsystems.Carriage;

public class CarriageZeroEncoder extends Command{
    private Carriage _carriage;

    public CarriageZeroEncoder(Carriage carriage) {
        requires(carriage);
        _carriage = carriage;
    }

    @Override
    protected void initialize() {
        _carriage.zeroEncoder();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
