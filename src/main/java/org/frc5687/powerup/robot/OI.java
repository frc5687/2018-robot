package org.frc5687.powerup.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.commands.CarriageZeroEncoder;
import org.frc5687.powerup.robot.commands.MoveArmToSetpointPID;
import org.frc5687.powerup.robot.commands.MoveCarriageToSetpointPID;
import org.frc5687.powerup.robot.subsystems.Arm;
import org.frc5687.powerup.robot.subsystems.Carriage;
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

    private JoystickButton resetArmEncoder;

    private JoystickButton armToScaleButton;
    private JoystickButton armToIntakeButton;

    private JoystickButton carriagePID;

    private JoystickButton carriageZeroEncoder;


    public OI() {
        driveGamepad = new Joystick(0);
        intakeGamepad = new Gamepad(1);

        intakeLeftOut = new JoystickButton(intakeGamepad, Gamepad.Buttons.LEFT_BUMPER.getNumber());
        intakeRightOut = new JoystickButton(intakeGamepad, Gamepad.Buttons.RIGHT_BUMPER.getNumber());

        resetArmEncoder = new JoystickButton(intakeGamepad, Gamepad.Buttons.START.getNumber());
        carriageZeroEncoder = new JoystickButton(intakeGamepad, Gamepad.Buttons.BACK.getNumber());

        armToScaleButton = new JoystickButton(driveGamepad, Gamepad.Buttons.Y.getNumber());
        armToIntakeButton = new JoystickButton(driveGamepad, Gamepad.Buttons.A.getNumber());

        carriagePID = new JoystickButton(intakeGamepad, Gamepad.Buttons.A.getNumber());
    }

    public double getLeftSpeed() {
        double speed = getSpeedFromAxis(driveGamepad, ButtonNumbers.LEFT_AXIS);
        return applyDeadband(speed, Constants.DriveTrain.DEADBAND);
    }

    public double getRightSpeed() {
        double speed = getSpeedFromAxis(driveGamepad, ButtonNumbers.RIGHT_AXIS);
        return applyDeadband(speed, Constants.DriveTrain.DEADBAND);
    }

    public double getLeftIntakeSpeed() {
        double trigger = getSpeedFromAxis(intakeGamepad, ButtonNumbers.LEFT_TRIGGER_AXIS);

        if (intakeLeftOut.get()) {
            return Constants.Intake.OUTTAKE_SPEED;
        } else if (trigger > Constants.Intake.DEADBAND) {
            return trigger;
        }
        return 0;
    }

    public double getRightIntakeSpeed() {
        double trigger = getSpeedFromAxis(intakeGamepad, ButtonNumbers.RIGHT_TRIGGER_AXIS);
        if (intakeRightOut.get()) {
            return Constants.Intake.OUTTAKE_SPEED;
        } else if (trigger > Constants.Intake.DEADBAND) {
            return trigger;
        }
        return 0;
    }

    public double getCarriageSpeed() {
        double speed = -getSpeedFromAxis(intakeGamepad, ButtonNumbers.LEFT_AXIS);
        SmartDashboard.putNumber("OI/LEFT AXIS", speed);
        return applyDeadband(speed, Constants.Carriage.DEADBAND, .1);
    }

    public double getArmSpeed() {
        double speed = getSpeedFromAxis(intakeGamepad, ButtonNumbers.RIGHT_AXIS);
        return applyDeadband(-speed, 0.05, .1);
    }

    public boolean zeroArmEncoderRequested() {
        return resetArmEncoder.get();
    }

    private double getSpeedFromAxis(Joystick gamepad, int axisNumber) {
        return gamepad.getRawAxis(axisNumber);
    }

    private double applyDeadband(double value, double deadband) {
        return Math.abs(value) >= deadband ? value : 0;
    }

    private double applyDeadband(double value, double deadband, double _default) {
        return Math.abs(value) >= deadband ? value : _default;
    }

    public void initializeCarriageButtons(Carriage carriage) {
        carriageZeroEncoder.whenPressed(new CarriageZeroEncoder(carriage));
        carriagePID.whenPressed(new MoveCarriageToSetpointPID(carriage, 500));
    }

    public void initializeArmButtons(Arm arm) {
        armToIntakeButton.whenPressed(new MoveArmToSetpointPID(arm, Constants.Arm.ENCODER_MIDDLE));
    }
}
