package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.subsystems.Arm;

public class DriveArm extends Command {
    private Arm _arm;
    private OI _oi;
    public DriveArm(Arm arm, OI oi){
        requires(arm);
        _arm=arm;
        _oi=oi;
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void execute() {
        double speed = _oi.getArmSpeed();
        if (speed > 0 && _arm.atTop()) {
            speed = 0;
        } else if (speed < 0 && _arm.atBottom()) {
            speed = 0;
        }
        SmartDashboard.putNumber("Arm/Speed", speed);
        _arm.drive(speed);
    }
}
