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


    /**
     * Applies a transform to the input to provide better sensitivity at low speeds.
     * @param input the raw input value from a joystick
     * @param factor the raw sensitivity factor to apply
     * @return the adjusted control value
     */
    public static double applySensitivityFactor(double input, double factor){
        // See http://www.chiefdelphi.com/forums/showthread.php?p=921992

        // The transform can only work on values between -1 and 1.
        if (input>1) { return 1; }
        if (input <-1) { return -1; }

        // The sensitivity factor MUST be between 0 and 1!
        double capped = Math.max(Math.min(factor, 1),0);

        return factor*input*input*input + (1-factor)*input;
    }

    public static double applyDeadband(double value, double deadband) {
        return Math.abs(value) >= deadband ? value : 0;
    }

    public static double applyDeadband(double value, double deadband, double _default) {
        return Math.abs(value) >= deadband ? value : _default;
    }


}

