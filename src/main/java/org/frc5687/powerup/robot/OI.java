package org.frc5687.powerup.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.frc5687.powerup.robot.utils.Gamepad;

public class OI {
    public class ButtonNumbers {
        public static final int LEFT_AXIS = 1;
        public static final int LEFT_TRIGGER_AXIS = 2;
        public static final int RIGHT_TRIGGER_AXIS = 3;
        public static final int RIGHT_AXIS = 5;

    }

    private Joystick driveGamepad;
    private Gamepad intakeGamepad;

    private JoystickButton intakeLeftOut;
    private JoystickButton intakeRightOut;

    private JoystickButton servoUp;
    private JoystickButton servoDown;

    public OI() {
        driveGamepad = new Joystick(0);
        intakeGamepad = new Gamepad(1);

        intakeLeftOut = new JoystickButton(intakeGamepad, Gamepad.Buttons.LEFT_BUMPER.getNumber());
        intakeRightOut = new JoystickButton(intakeGamepad, Gamepad.Buttons.RIGHT_BUMPER.getNumber());

        servoUp = new JoystickButton(intakeGamepad, Gamepad.Buttons.X.getNumber());
        servoDown = new JoystickButton(intakeGamepad, Gamepad.Buttons.B.getNumber());
    }

    public double getLeftSpeed() {
        return getSpeedFromAxis(driveGamepad, ButtonNumbers.LEFT_AXIS);
    }

    public double getRightSpeed() {
        return getSpeedFromAxis(driveGamepad, ButtonNumbers.RIGHT_AXIS);
    }

    public double getLeftIntakeSpeed() {
        double trigger = getSpeedFromAxis(intakeGamepad, ButtonNumbers.LEFT_TRIGGER_AXIS);

        if (intakeLeftOut.get()) {
            return -0.7;
        } else if (trigger > Constants.Intake.DEADBAND) {
            return trigger;
        }
        return 0;
    }

    public double getRightIntakeSpeed() {
        double trigger = getSpeedFromAxis(intakeGamepad, ButtonNumbers.RIGHT_TRIGGER_AXIS);
        if (intakeRightOut.get()) {
            return -0.7;
        } else if (trigger > Constants.Intake.DEADBAND) {
            return trigger;
        }
        return 0;
    }

    public double getCarriageSpeed() {
        double speed = getSpeedFromAxis(intakeGamepad, ButtonNumbers.LEFT_AXIS);
        if (Math.abs(speed) > Constants.Carriage.DEADBAND) {
            return speed;
        }
        return -.1;
    }

    public double getArmSpeed() {
        double speed = getSpeedFromAxis(intakeGamepad, ButtonNumbers.RIGHT_AXIS);
        if (Math.abs(speed) > 0.05) {
            return -speed;
        }
        return .1;
    }

    public double getServoSpeed() {
        if (servoUp.get()) {
            return 0.0;
        } else if (servoDown.get()) {
            return 1.0;
        }
        return 0.1;
    }

    private double getSpeedFromAxis(Joystick gamepad, int axisNumber) {
        double raw = gamepad.getRawAxis(axisNumber);
        return applyDeadband(raw, Constants.DriveTrain.DEADBAND);
    }

    private double applyDeadband(double value, double deadband) {
        return Math.abs(value) >= deadband ? value : 0;
    }

}
