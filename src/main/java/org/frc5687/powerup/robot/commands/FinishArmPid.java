package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

public class FinishArmPid extends Command {
    private MoveArmToSetpointPID armPid;
    private boolean permitFinish;

    public FinishArmPid(MoveArmToSetpointPID armPid) {
        this.armPid = armPid;
    }

    @Override
    protected void initialize() {
        permitFinish = false;
        armPid.stopWaiting();
        permitFinish = true;
    }

    @Override
    protected boolean isFinished() {
        return permitFinish;
    }
}
