package org.frc5687.powerup.robot.utils;

import edu.wpi.first.wpilibj.AnalogInput;
import org.frc5687.powerup.robot.Constants;

public class IRSensorShort extends IR {
    public IRSensorShort(int channel) {
        super(channel);
    }

    public double getDistance() {
        return Constants.IRPID.TRANSFORM_COEFFICIENT_SHORT * Math.pow(getRaw(), Constants.IRPID.TRANSFORM_POWER_SHORT) / 2.54;

    }
}