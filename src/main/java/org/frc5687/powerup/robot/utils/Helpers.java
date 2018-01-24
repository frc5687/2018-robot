package org.frc5687.powerup.robot.utils;

public class Helpers {
    /**
     * Checks if a value is within tolerance of another value
     * @param value the value to check
     * @param target the target or desired value
     * @param tolerance the acceptable tolerance around the target
     * @return true if the value is within tolerance of the target value
     */
    public static boolean IsValueWithinTolerance(double value, double target, double tolerance) {
        return Math.abs(value - target) <= tolerance;
    }
}
