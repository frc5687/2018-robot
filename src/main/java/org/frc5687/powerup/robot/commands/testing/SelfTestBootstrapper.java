package org.frc5687.powerup.robot.commands.testing;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.DriverStation;

import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.subsystems.Intake;

public class SelfTestBootstrapper extends Command {
    private Intake intake;
    private boolean done = false;

    public SelfTestBootstrapper(Intake intake) {
    this.intake = intake;
        }

    @Override
    protected void execute(){

        if(!done && DriverStation.getInstance().isFMSAttached()){
            Scheduler.getInstance().add(new FullSelfTest(intake));
            done = true;
        }

    }

    @Override
    protected boolean isFinished() {
        return done;
    }
}