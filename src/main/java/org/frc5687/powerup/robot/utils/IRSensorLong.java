package org.frc5687.powerup.robot.utils;

import org.frc5687.powerup.robot.Constants;

public class IRSensorLong extends IRSensor {

    public IRSensorLong(int channel) { super(channel); }

    public double getDistance() {
        return Constants.IRPID.TRANSFORM_COEFFICIENT_LONG * Math.pow(getRaw(), Constants.IRPID.TRANSFORM_POWER_LONG) / 2.54;
    }
}
