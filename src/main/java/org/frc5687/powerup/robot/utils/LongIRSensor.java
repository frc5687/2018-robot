package org.frc5687.powerup.robot.utils;

import org.frc5687.powerup.robot.Constants;

/**
 * Created by Ben Bernard on 3/17/2018.
 */
public class LongIRSensor extends IRSensor {

    public LongIRSensor(int channel) { super(channel); }

    public double getDistance() {
        return Constants.IRPID.TRANSFORM_COEFFICIENT_LONG * Math.pow(getRaw(), Constants.IRPID.TRANSFORM_POWER_LONG) / 2.54;
    }
}
