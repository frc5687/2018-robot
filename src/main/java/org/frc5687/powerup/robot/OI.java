package org.frc5687.powerup.robot;

import edu.wpi.first.wpilibj.Joystick;

import static org.frc5687.powerup.robot.OI.ButtonNumbers.BUTTON_A;

/**
 * Created by Baxter on 3/22/2017.
 */
public class OI {

    public class ButtonNumbers {
        public static final int LEFT_AXIS = 1;
        public static final int RIGHT_AXIS = 5;

        public static final int BUTTON_A = 0;
        public static final int BUTTON_Y = 3;
    }

    private Joystick driveGamepad;
    private Joystick intakeGamepad;

    public OI() {
        driveGamepad = new Joystick(0);
        intakeGamepad = new Joystick(1);
    }

    public double getLeftSpeed() {
        return getSpeedFromAxis(driveGamepad, ButtonNumbers.LEFT_AXIS);
    }

    public double getRightSpeed() {
        return getSpeedFromAxis(driveGamepad, ButtonNumbers.RIGHT_AXIS);
    }

    public double getLeftIntakeSpeed() {
        return getSpeedFromAxis(intakeGamepad, ButtonNumbers.LEFT_AXIS);
    }

    public double getRightIntakeSpeed() {
        return getSpeedFromAxis(intakeGamepad, ButtonNumbers.RIGHT_AXIS);
    }

    public double getCarriageSpeed() {
        if (intakeGamepad.getRawButton(ButtonNumbers.BUTTON_Y)) {
            return 0.1;
        } else if (intakeGamepad.getRawButton(ButtonNumbers.BUTTON_A)) {
            return -0.1;
        }
        return 0;
    }

    private double getSpeedFromAxis(Joystick gamepad, int axisNumber) {
        double raw = gamepad.getRawAxis(axisNumber);
        return applyDeadband(raw, Constants.DriveTrain.DEADBAND);
    }

    private double applyDeadband(double value, double deadband) {
        return Math.abs(value) >= deadband ? value : 0;
    }

}
