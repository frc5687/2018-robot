package org.frc5687.powerup.robot.utils;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PIDSource;
import org.frc5687.powerup.robot.Constants;

public abstract class IRSensor extends AnalogInput implements PIDSource {

    public IRSensor(int channel) {
        super(channel);
    }

    public double getRaw() {
        return super.getVoltage();
    }

    abstract public double getDistance();

    @Override
    public double pidGet() {
        return getDistance();
    }
}
