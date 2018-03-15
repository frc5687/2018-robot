package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.Command;

public class AutoWaitForMillis extends Command {
    private long _millis;
    private long _endTime;

    public AutoWaitForMillis(long millis) {
        _millis = millis;
    }

    @Override
    protected void initialize() {
        _endTime = System.currentTimeMillis() + _millis;
    }

    @Override
    protected boolean isFinished() {
        return System.currentTimeMillis() >= _endTime;
    }

}
