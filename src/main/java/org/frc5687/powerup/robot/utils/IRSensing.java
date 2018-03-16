package org.frc5687.powerup.robot.utils;

import edu.wpi.first.wpilibj.AnalogInput;
import org.frc5687.powerup.robot.Constants;

public class IRSensing extends AnalogInput {

    public IRSensing(int channel) {
        super(channel);
    }


    public double getRaw() {
        return super.getVoltage();
    }


    public class sensorShort {
        public double getDistance() {
            return Constants.IRPID.TRANSFORM_COEFFICIENT_SHORT * Math.pow(getRaw(), Constants.IRPID.TRANSFORM_POWER_SHORT) / 2.54;
        }
    }


    public class sensorMedium {
        public double getDistance() {
            return Constants.IRPID.TRANSFORM_COEFFICIENT_MEDIUM * Math.pow(getRaw(), Constants.IRPID.TRANSFORM_POWER_MEDIUM) / 2.54;
        }
    }


    public class sensorLong {
        public double getDistance() {
            return Constants.IRPID.TRANSFORM_COEFFICIENT_LONG * Math.pow(getRaw(), Constants.IRPID.TRANSFORM_POWER_LONG) / 2.54;

        }
    }
}
