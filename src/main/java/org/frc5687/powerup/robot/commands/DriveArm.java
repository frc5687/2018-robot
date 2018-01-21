package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.subsystems.Arm;

public class DriveArm extends Command {
    private Arm _arm;
    private OI _oi;
    public DriveArm(Arm arm, OI oi){
        _arm=arm;
        _oi=oi;
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void execute() {
        _arm.drive(_oi.getArmSpeed());
    }
}
