package org.frc5687.powerup.robot.utils;

import org.frc5687.powerup.robot.Constants;

public class IRSensorShort extends IRSensor {
    public IRSensorShort(int channel) {
        super(channel);
    }

    public double getDistance() {
        return (Constants.IRPID.CUBIC_COEFFICIENT_SHORT * Math.pow(getRaw(), 3) +
                Constants.IRPID.QUADRATIC_COEFFICIENT_SHORT * Math.pow(getRaw(),2) +
                Constants.IRPID.LINEAR_COEFFICIENT_SHORT * getRaw() +
                Constants.IRPID.CONSTANT_COEFFICIENT_SHORT )/ 2.54;    }
}