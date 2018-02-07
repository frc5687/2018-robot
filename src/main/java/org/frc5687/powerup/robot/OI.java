package org.frc5687.powerup.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.commands.*;
import org.frc5687.powerup.robot.commands.auto.IntakeToDrive;
import org.frc5687.powerup.robot.commands.auto.IntakeToFloor;
import org.frc5687.powerup.robot.commands.auto.IntakeToScale;
import org.frc5687.powerup.robot.commands.auto.IntakeToSwitch;
import org.frc5687.powerup.robot.utils.Gamepad;

public class OI {
    public class ButtonNumbers {
        public static final int LEFT_AXIS = 1;
        public static final int LEFT_TRIGGER_AXIS = 2;
        public static final int RIGHT_TRIGGER_AXIS = 3;
        public static final int RIGHT_AXIS = 5;
    }

    private Joystick driverGamepad;
    private Gamepad operatorGamepad;

    private JoystickButton intakeLeftOut;
    private JoystickButton intakeRightOut;

    private JoystickButton climberWind;
    private JoystickButton climberUnwind;

    private JoystickButton resetArmEncoder;

    private JoystickButton armToScaleButton;
    private JoystickButton armToIntakeButton;
    private JoystickButton armToSwitchButton;
    private JoystickButton armToDriveButton;

    private JoystickButton carriagePID;

    private JoystickButton carriageZeroEncoder;

    private JoystickButton servoHoldCube;
    private JoystickButton servoReleaseCube;

    public OI() {
        driverGamepad = new Joystick(0);
        operatorGamepad = new Gamepad(1);

        armToScaleButton = new JoystickButton(operatorGamepad, Gamepad.Buttons.Y.getNumber());
        armToSwitchButton = new JoystickButton(operatorGamepad, Gamepad.Buttons.B.getNumber());
        armToDriveButton = new JoystickButton(operatorGamepad, Gamepad.Buttons.X.getNumber());
        armToIntakeButton = new JoystickButton(operatorGamepad, Gamepad.Buttons.A.getNumber());

        climberUnwind = new JoystickButton(driverGamepad, Gamepad.Buttons.LEFT_BUMPER.getNumber());
        climberWind = new JoystickButton(driverGamepad, Gamepad.Buttons.RIGHT_BUMPER.getNumber());

        intakeLeftOut = new JoystickButton(operatorGamepad, Gamepad.Buttons.LEFT_BUMPER.getNumber());
        intakeRightOut = new JoystickButton(operatorGamepad, Gamepad.Buttons.RIGHT_BUMPER.getNumber());
        resetArmEncoder = new JoystickButton(operatorGamepad, Gamepad.Buttons.START.getNumber());
        carriageZeroEncoder = new JoystickButton(operatorGamepad, Gamepad.Buttons.BACK.getNumber());
        carriagePID = new JoystickButton(operatorGamepad, Gamepad.Buttons.X.getNumber());
        servoHoldCube = new JoystickButton(operatorGamepad, Gamepad.Buttons.B.getNumber());
        servoReleaseCube = new JoystickButton(operatorGamepad, Gamepad.Buttons.A.getNumber());
    }

    public double getLeftSpeed() {
        double speed = -getSpeedFromAxis(driverGamepad, ButtonNumbers.LEFT_AXIS);
        return applyDeadband(speed, Constants.DriveTrain.DEADBAND);
    }

    public double getRightSpeed() {
        double speed = -getSpeedFromAxis(driverGamepad, ButtonNumbers.RIGHT_AXIS);
        return applyDeadband(speed, Constants.DriveTrain.DEADBAND);
    }

    public double getLeftIntakeSpeed() {
        double trigger = getSpeedFromAxis(operatorGamepad, ButtonNumbers.LEFT_TRIGGER_AXIS);

        if (intakeLeftOut.get()) {
            return Constants.Intake.OUTTAKE_SPEED;
        } else if (trigger > Constants.Intake.DEADBAND) {
            return trigger;
        }
        return Constants.Intake.HOLD_SPEED;
    }

    public double getRightIntakeSpeed() {
        double trigger = getSpeedFromAxis(operatorGamepad, ButtonNumbers.RIGHT_TRIGGER_AXIS);
        if (intakeRightOut.get()) {
            return Constants.Intake.OUTTAKE_SPEED;
        } else if (trigger > Constants.Intake.DEADBAND) {
            return trigger;
        }
        return Constants.Intake.HOLD_SPEED;
    }

    public double getCarriageSpeed() {
        double speed = -getSpeedFromAxis(operatorGamepad, ButtonNumbers.LEFT_AXIS);
        SmartDashboard.putNumber("OI/LEFT AXIS", speed);
        return applyDeadband(speed, Constants.Carriage.DEADBAND, .1);
    }

    public double getArmSpeed() {
        double speed = getSpeedFromAxis(operatorGamepad, ButtonNumbers.RIGHT_AXIS);
        return applyDeadband(-speed, 0.05, 0)*Constants.Arm.SPEED_SCALAR;
    }

    public boolean zeroArmEncoderRequested() {
        return resetArmEncoder.get();
    }

    public double getClimberSpeed() {
        double speed = 0;
        if (climberWind.get()) {
            speed = Constants.Climber.WIND_SPEED;
        } else if (climberUnwind.get()) {
            speed = Constants.Climber.UNWIND_SPEED;
        }
        return speed;
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

    public void initializeButtons(Robot robot) {
        carriageZeroEncoder.whenPressed(new CarriageZeroEncoder(robot.getCarriage()));
        carriagePID.whenPressed(new MoveCarriageToSetpointPID(robot.getCarriage(), 500));

        DriverStation.reportError("armToIntakeButton " + (armToIntakeButton==null), false);
        DriverStation.reportError("armToDriveButton " + (armToDriveButton==null), false);
        DriverStation.reportError("armToSwitchButton " + (armToSwitchButton==null), false);
        DriverStation.reportError("armToScaleButton " + (armToScaleButton==null), false);

        DriverStation.reportError("robot " + (robot==null), false);
        DriverStation.reportError("carriage " + (robot.getCarriage()==null), false);
        DriverStation.reportError("arm " + (robot.getArm()==null), false);

            armToIntakeButton.whenPressed(new IntakeToFloor(robot.getCarriage(), robot.getArm()));
        armToDriveButton.whenPressed(new IntakeToDrive(robot.getCarriage(), robot.getArm()));
        armToSwitchButton.whenPressed(new IntakeToSwitch(robot.getCarriage(), robot.getArm()));
        armToScaleButton.whenPressed(new IntakeToScale(robot.getCarriage(), robot.getArm()));

        servoHoldCube.whenPressed(new ServoHoldCube(robot.getIntake()));
        servoReleaseCube.whenPressed(new ServoReleaseCube(robot.getIntake()));
    }

}
