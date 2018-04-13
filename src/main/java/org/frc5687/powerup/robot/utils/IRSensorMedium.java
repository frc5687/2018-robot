package org.frc5687.powerup.robot.utils;

import org.frc5687.powerup.robot.Constants;

public class IRSensorMedium extends IRSensor {

    public IRSensorMedium(int channel) { super(channel); }

    public double getDistance() {
        return (Constants.IRPID.CUBIC_COEFFICIENT_MEDIUM * Math.pow(getRaw(), 3) +
                Constants.IRPID.QUADRATIC_COEFFICIENT_MEDIUM * Math.pow(getRaw(),2) +
                Constants.IRPID.LINEAR_COEFFICIENT_MEDIUM * getRaw() +
                Constants.IRPID.CONSTANT_COEFFICIENT_MEDIUM )/ 2.54;    }
}
