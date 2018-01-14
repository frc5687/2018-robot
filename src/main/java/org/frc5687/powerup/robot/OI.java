package org.frc5687.powerup.robot;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Created by Baxter on 3/22/2017.
 */
public class OI {

    public class ButtonNumbers {
        public static final int LEFT_AXIS = 1;
        public static final int RIGHT_AXIS = 5;
    }

    private Joystick gamepad;

    public OI() {
        gamepad = new Joystick(0);
    }

    public double getLeftSpeed() {
        return getSpeedFromAxis(ButtonNumbers.LEFT_AXIS);
    }

    public double getRightSpeed() {
        return getSpeedFromAxis(ButtonNumbers.RIGHT_AXIS);
    }

    private double getSpeedFromAxis(int axisNumber) {
        double raw = gamepad.getRawAxis(axisNumber);
        return applyDeadband(raw, Constants.DriveTrain.DEADBAND);
    }

    private double applyDeadband(double value, double deadband) {
        return Math.abs(value) >= deadband ? value : 0;
    }

}
