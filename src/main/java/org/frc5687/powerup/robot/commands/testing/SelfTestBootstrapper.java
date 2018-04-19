package org.frc5687.powerup.robot.command.testing;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.DriverStation;

import org.frc5687.powerup.robot.OI;

public class SelfTestBootstrapper extends Command {
        private OI oi;
        boolean done = false;

    public SelfTestBootstrapper(OI oi) {
    this.oi = oi;
    }

    @Override
    protected void execute(){

        if(!done &&DriverStation.getInstance().isFMSAttached() && oi.isYesPressed() && oi.isNoPressed()){
            Scheduler.getInstance().add(new FullSelfTest());
            done = true;
        }

    }

    @Override
    protected boolean isFinished() {
        return done;
    }
}