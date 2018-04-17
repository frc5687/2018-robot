package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.subsystems.Carriage;


public class TestCarriageMotor extends Command {
    private Carriage carriage;
    private long upMillis;
    public long downMillis;
    public TestCarriageMotor(Carriage carriage) {
        requires(carriage);
    }

    @Override
    protected void initialize() {
        upMillis = System.currentTimeMillis() + 250;
        downMillis = System.currentTimeMillis() + 500;
    }

    @Override
    protected void execute() {
        if(System.currentTimeMillis() < upMillis){
            carriage.drive(1);
        }
        else {
            carriage.drive(-1);
        }
    }

    @Override
    protected boolean isFinished() {
        return System.currentTimeMillis()> downMillis;
    }
}

