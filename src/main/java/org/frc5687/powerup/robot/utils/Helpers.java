package org.frc5687.powerup.robot.utils;

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

}
