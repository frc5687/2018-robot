package org.frc5687.powerup.robot.utils;

import org.frc5687.powerup.robot.Constants;

public class IRSensorMedium extends IRSensor {

    public IRSensorMedium(int channel) { super(channel); }

    public double getDistance() {
        return Constants.IRPID.TRANSFORM_COEFFICIENT_MEDIUM * Math.pow(getRaw(), Constants.IRPID.TRANSFORM_POWER_MEDIUM) / 2.54;
    }
}
