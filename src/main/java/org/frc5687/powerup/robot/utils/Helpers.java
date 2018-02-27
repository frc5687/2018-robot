package org.frc5687.powerup.robot.utils;

import org.frc5687.powerup.robot.Constants;

public class Helpers {

    /**
     * Converts feed & inches to meters. src: Martyr2 from http://www.dreamincode.net/forums/topic/31467-converting-inches-to-meters/
     * @param feet
     * @param inches
     * @return
     */
    public static double i2m(double feet, double inches) {
        //1 foot is .3048 meters and 1 inch is .0254 meters (The formula to convert inches to meters))
        return (feet * .3048) + (inches * .0254);
    }

    public static double applyMinSpeed(double speed, double min) {
        if (speed < 0) {
            return Math.max(speed, min);
        } else {
            return Math.min(speed, -min);
        }
    }

    public static double absMax(double a, double b) {
        double absA = Math.abs(a);
        double absB = Math.abs(b);

        if (absA > absB) {
            return a;
        } else {
            return b;
        }
    }

    /**
     * Credit: 862
     * @param inches
     * @return
     */
    public static double inches2ticks(double inches) {
        return inches / Constants.Encoders.Defaults.DistancePerRotation.INCHES * Constants.Encoders.Defaults.PULSES_PER_ROTATION;
    }

    public static double ips2talon(double ips) {
        double ip100ms = ips / 10;
        return inches2ticks(ip100ms);
    }

}
