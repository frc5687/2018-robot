package org.frc5687.powerup.robot.utils;

import org.frc5687.powerup.robot.Constants;

public class IRSensorLong extends IRSensor {

    public IRSensorLong(int channel) { super(channel); }

    public double getDistance() {
        return (Constants.IRPID.CUBIC_COEFFICIENT_LONG * Math.pow(getRaw(), 3) +
                Constants.IRPID.QUADRATIC_COEFFICIENT_LONG * Math.pow(getRaw(),2) +
                Constants.IRPID.LINEAR_COEFFICIENT_LONG * getRaw() +
                Constants.IRPID.CONSTANT_COEFFICIENT_LONG )/ 2.54;    }
}
