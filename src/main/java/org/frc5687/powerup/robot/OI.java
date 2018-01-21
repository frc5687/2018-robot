package org.frc5687.powerup.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.frc5687.powerup.robot.utils.Gamepad;

/**
 * Created by Baxter on 3/22/2017.
 */
public class OI {
    public class ButtonNumbers {
        public static final int LEFT_AXIS = 1;
        public static final int RIGHT_AXIS = 5;

    }

    private Joystick driveGamepad;
    private Gamepad intakeGamepad;

    private JoystickButton carriageUpButton;
    private JoystickButton carriageDownButton;

    private JoystickButton armUpButton;
    private JoystickButton armDownButton;

    public OI() {
        driveGamepad = new Joystick(0);
        intakeGamepad = new Gamepad(1);

        carriageUpButton = new JoystickButton(intakeGamepad, Gamepad.Buttons.Y.getNumber());
        carriageDownButton = new JoystickButton(intakeGamepad, Gamepad.Buttons.A.getNumber());

        armUpButton = new JoystickButton(intakeGamepad, Gamepad.Buttons.X.getNumber());
        armDownButton = new JoystickButton(intakeGamepad, Gamepad.Buttons.B.getNumber());
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
        if (carriageUpButton.get()) {
            return -1.0;
        } else if (carriageDownButton.get()) {
            return 0.5;
        }
        return -.1;
    }

    public double getArmSpeed() {
        if (armUpButton.get()) {
            return 1;
        } else if (armDownButton.get()) {
            return -.5;
        }
        return .1;
    }

    private double getSpeedFromAxis(Joystick gamepad, int axisNumber) {
        double raw = gamepad.getRawAxis(axisNumber);
        return applyDeadband(raw, Constants.DriveTrain.DEADBAND);
    }

    private double applyDeadband(double value, double deadband) {
        return Math.abs(value) >= deadband ? value : 0;
    }

}
