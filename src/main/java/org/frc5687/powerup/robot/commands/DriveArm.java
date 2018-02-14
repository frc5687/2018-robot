package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.subsystems.Arm;
import org.frc5687.powerup.robot.Constants;

public class DriveArm extends Command {
    private Arm arm;
    private OI oi;
    private double target;
    public DriveArm(Arm arm, OI oi){
        requires(arm);
        this.arm = arm;
        this.oi = oi;
    }

    protected void initialize(){
        target = arm.getPosition();
        super.initialize();
        arm.enable();

    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void interrupted() {
        arm.disable();
    }

    @Override
    protected void end() {
        arm.disable();
    }


    @Override
    protected void execute() {

        if(oi.getArmSpeed() != 0) {
            target = target + oi.getArmSpeed();
            if (target < Constants.Arm.Pot.BOTTOM) { target = Constants.Arm.Pot.BOTTOM; }
            if (target > Constants.Arm.Pot.TOP) { target = Constants.Arm.Pot.TOP; }

            if (Math.abs(target - arm.getSetpoint()) > Constants.Arm.Pot.TOLERANCE / 5) {
                arm.setSetpoint(target);
            }
        }

    }
}
