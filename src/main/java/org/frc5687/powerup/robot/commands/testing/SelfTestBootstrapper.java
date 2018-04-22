package org.frc5687.powerup.robot.commands.testing;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.DriverStation;

import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.subsystems.Intake;

public class SelfTestBootstrapper extends Command {
    private Robot _robot;
    private boolean done = false;

    public SelfTestBootstrapper(Robot robot) {
    _robot = robot;
        }

    @Override
    protected void execute(){

        if(!done && !DriverStation.getInstance().isFMSAttached()){
            Scheduler.getInstance().add(new FullSelfTest(_robot));
            done = true;
        }

    }

    @Override
    protected boolean isFinished() {
        return done;
    }
}