package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.OI;

public class RumbleControllersForNMillis extends Command {
    private long _duration;
    private long _endTime;
    private double _operatorLeftIntensity;
    private double _operatorRightIntensity;
    private double _driverLeftIntensity;
    private double _driverRightIntensity;
    private OI _oi;

    public RumbleControllersForNMillis(OI oi, long duration, double intensity) {
        _oi = oi;
        _duration = duration;
        _operatorLeftIntensity = intensity;
        _operatorRightIntensity = intensity;
        _driverLeftIntensity = intensity;
        _driverRightIntensity = intensity;
    }

    @Override
    protected void initialize() {
        _endTime = System.currentTimeMillis() + _duration;
        _oi.setDriverGamepadRumble(_driverLeftIntensity, _driverRightIntensity);
        _oi.setOperatorGamepadRumble(_operatorLeftIntensity, _operatorRightIntensity);
        DriverStation.reportError("RumbleControllersForNMillis initializing. Going to rumble for " + Long.toString(_duration) + "ms", false)
    }

    @Override
    protected void end() {
        _oi.setDriverGamepadRumble(0);
        _oi.setOperatorGamepadRumble(0);
        DriverStation.reportError("RumbleControllersForNMillis ended.", false);
    }

    @Override
    protected boolean isFinished() {
        return System.currentTimeMillis() > _endTime;
    }
}
