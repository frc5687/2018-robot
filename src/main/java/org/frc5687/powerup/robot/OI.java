package org.frc5687.powerup.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.commands.*;
import org.frc5687.powerup.robot.commands.testing.*;
import org.frc5687.powerup.robot.commands.actions.IntakeToDrive;
import org.frc5687.powerup.robot.commands.actions.IntakeToFloor;
import org.frc5687.powerup.robot.commands.actions.IntakeToScale;
import org.frc5687.powerup.robot.commands.actions.IntakeToSwitch;
import org.frc5687.powerup.robot.commands.auto.*;
import org.frc5687.powerup.robot.utils.Gamepad;
import org.frc5687.powerup.robot.utils.Helpers;
import org.frc5687.powerup.robot.utils.POV;

public class OI {
    public class ButtonNumbers {
        public static final int LEFT_AXIS = 1;
        public static final int LEFT_TRIGGER_AXIS = 2;
        public static final int RIGHT_TRIGGER_AXIS = 3;
        public static final int RIGHT_AXIS = 5;
    }

    private Joystick driverGamepad;
    private Gamepad operatorGamepad;

    private JoystickButton intakeLeftIn;
    private JoystickButton intakeRightIn;

    private JoystickButton climberWind;
    private JoystickButton climberUnwind;

    private JoystickButton driverArmToScaleButton;
    private JoystickButton driverArmToIntakeButton;
    private JoystickButton driverArmToSwitchButton;
    private JoystickButton driverArmToDriveButton;

    private JoystickButton operatorArmToScaleButton;
    private JoystickButton operatorArmToIntakeButton;
    private JoystickButton operatorArmToSwitchButton;
    private JoystickButton operatorArmToDriveButton;

    private JoystickButton carriageZeroEncoder;

    private JoystickButton selfTest;

    private JoystickButton driverArmUp;
    private JoystickButton driverArmDown;
    private JoystickButton driverCarriageUp;
    private JoystickButton driverCarriageDown;

    private POV driverPOV;

    private Robot _robot;

    public OI(Robot robot) {
        _robot = robot;
        driverGamepad = new Joystick(0);
        operatorGamepad = new Gamepad(1);

        driverArmToScaleButton = new JoystickButton(driverGamepad, Gamepad.Buttons.Y.getNumber());
        driverArmToSwitchButton = new JoystickButton(driverGamepad, Gamepad.Buttons.B.getNumber());
        driverArmToDriveButton = new JoystickButton(driverGamepad, Gamepad.Buttons.X.getNumber());
        driverArmToIntakeButton = new JoystickButton(driverGamepad, Gamepad.Buttons.A.getNumber());

        operatorArmToScaleButton = new JoystickButton(operatorGamepad, Gamepad.Buttons.Y.getNumber());
        operatorArmToSwitchButton = new JoystickButton(operatorGamepad, Gamepad.Buttons.B.getNumber());
        operatorArmToDriveButton = new JoystickButton(operatorGamepad, Gamepad.Buttons.X.getNumber());
        operatorArmToIntakeButton = new JoystickButton(operatorGamepad, Gamepad.Buttons.A.getNumber());


        climberUnwind = new JoystickButton(driverGamepad, Gamepad.Buttons.BACK.getNumber());
        climberWind = new JoystickButton(driverGamepad, Gamepad.Buttons.START.getNumber());

        intakeLeftIn = new JoystickButton(operatorGamepad, Gamepad.Buttons.LEFT_BUMPER.getNumber());
        intakeRightIn = new JoystickButton(operatorGamepad, Gamepad.Buttons.RIGHT_BUMPER.getNumber());
        carriageZeroEncoder = new JoystickButton(operatorGamepad, Gamepad.Buttons.BACK.getNumber());
        selfTest = new JoystickButton(operatorGamepad, Gamepad.Buttons.START.getNumber());
        driverArmUp = new JoystickButton(driverGamepad, Gamepad.Buttons.RIGHT_BUMPER.getNumber());
        driverArmDown = new JoystickButton(driverGamepad, Gamepad.Buttons.RIGHT_STICK.getNumber());

        driverCarriageUp = new JoystickButton(driverGamepad, Gamepad.Buttons.LEFT_BUMPER.getNumber());
        driverCarriageDown = new JoystickButton(driverGamepad, Gamepad.Buttons.LEFT_STICK.getNumber());
    }

    public double getLeftSpeed() {
        double speed = -getSpeedFromAxis(driverGamepad, ButtonNumbers.LEFT_AXIS);
        speed = Helpers.applyDeadband(speed, Constants.DriveTrain.DEADBAND);
        return Helpers.applySensitivityFactor(speed, Constants.DriveTrain.SENSITIVITY);
    }


    public boolean isStartPressed(){
        return operatorGamepad.getRawButton(Gamepad.Buttons.START);
    }

    public double getRightSpeed() {
        double speed = -getSpeedFromAxis(driverGamepad, ButtonNumbers.RIGHT_AXIS);
        speed = Helpers.applyDeadband(speed, Constants.DriveTrain.DEADBAND);
        return Helpers.applySensitivityFactor(speed, Constants.DriveTrain.SENSITIVITY);
    }

    public double getLeftIntakeSpeed() {
        double driver = Helpers.absMax(
                getSpeedFromAxis(driverGamepad, ButtonNumbers.LEFT_TRIGGER_AXIS),
                -getSpeedFromAxis(driverGamepad, ButtonNumbers.RIGHT_TRIGGER_AXIS)
        );
        if (intakeLeftIn.get()){
            return Constants.Intake.INTAKE_SPEED;
        }
        SmartDashboard.putNumber("Intake/left/driver", driver);
        double driverTrigger = driver;
        driverTrigger = Helpers.applySensitivityFactor(driverTrigger, Constants.Intake.SENSITIVITY);

        if (Math.abs(getSpeedFromAxis(operatorGamepad, ButtonNumbers.LEFT_TRIGGER_AXIS)) > Constants.Intake.DEADBAND) {
            return Helpers.applySensitivityFactor(-getSpeedFromAxis(operatorGamepad, ButtonNumbers.LEFT_TRIGGER_AXIS), Constants.Intake.SENSITIVITY);
        }
        if (Math.abs(driverTrigger) > Constants.Intake.DEADBAND) {
            return driverTrigger;
        }

        return 0;
    }

    public double getRightIntakeSpeed() {
        double driver = Helpers.absMax(
                getSpeedFromAxis(driverGamepad, ButtonNumbers.LEFT_TRIGGER_AXIS),
                -getSpeedFromAxis(driverGamepad, ButtonNumbers.RIGHT_TRIGGER_AXIS)
        );
        if(intakeRightIn.get()){
            return Constants.Intake.INTAKE_SPEED;
        }
        SmartDashboard.putNumber("Intake/right/driver", driver);
        double trigger = driver;
        trigger = Helpers.applySensitivityFactor(trigger, Constants.Intake.SENSITIVITY);

        if (Math.abs(getSpeedFromAxis(operatorGamepad, ButtonNumbers.RIGHT_TRIGGER_AXIS)) > Constants.Intake.DEADBAND) {
            return Helpers.applySensitivityFactor(-getSpeedFromAxis(operatorGamepad, ButtonNumbers.RIGHT_TRIGGER_AXIS), Constants.Intake.SENSITIVITY);
        }

        if (getSpeedFromAxis(operatorGamepad, ButtonNumbers.RIGHT_TRIGGER_AXIS) > Constants.Intake.DEADBAND) {
            return getSpeedFromAxis(operatorGamepad, ButtonNumbers.RIGHT_TRIGGER_AXIS);
        } else if (Math.abs(trigger) > Constants.Intake.DEADBAND) {
            return trigger;
        }
        return 0;
    }

    public double getCarriageSpeed() {
        double operator = -getSpeedFromAxis(operatorGamepad, ButtonNumbers.LEFT_AXIS);
        double driver = driverCarriageUp.get() ? 1 : (driverCarriageDown.get() ? -0.99 : 0);
        double speed = Helpers.absMax(operator, driver);
        speed = Helpers.applySensitivityFactor(speed, Constants.Carriage.SENSITIVITY);
        return Helpers.applyDeadband(speed, Constants.Carriage.DEADBAND);
    }

    public double getArmSpeed() {
        double driver = driverArmUp.get() ? -0.75 : (driverArmDown.get() ? 1.0 : 0);
        double operator = getSpeedFromAxis(operatorGamepad, ButtonNumbers.RIGHT_AXIS);
        double speed = Helpers.absMax(operator, driver);
        speed = Helpers.applySensitivityFactor(speed,Constants.Arm.SENSITIVITY);
        return Helpers.applyDeadband(-speed, 0.05);
    }

    public double getClimberSpeed() {
        if (climberWind.get()) {
            return 1;
        } else if (climberUnwind.get()) {
            return -1;
        }
        return 0;
    }

    public int getDriverPOV() {
        return POV.fromWPILIbAngle(0, driverGamepad.getPOV()).getDirectionValue();
    }

    public int getOperatorPOV() {
        return POV.fromWPILIbAngle(0, operatorGamepad.getPOV()).getDirectionValue();
    }

    private double getSpeedFromAxis(Joystick gamepad, int axisNumber) {
        return gamepad.getRawAxis(axisNumber);
    }

    public void initializeButtons(Robot robot) {
        carriageZeroEncoder.whenPressed(new AutoZeroCarriage(robot.getCarriage()));

        driverArmToIntakeButton.whenPressed(new IntakeToFloor(robot.getCarriage(), robot.getArm()));
        driverArmToDriveButton.whenPressed(new IntakeToDrive(robot.getCarriage(), robot.getArm()));
        driverArmToSwitchButton.whenPressed(new IntakeToSwitch(robot.getCarriage(), robot.getArm()));
        driverArmToScaleButton.whenPressed(new IntakeToScale(robot.getCarriage(), robot.getArm()));

        operatorArmToIntakeButton.whenPressed(new IntakeToFloor(robot.getCarriage(), robot.getArm()));
        operatorArmToDriveButton.whenPressed(new IntakeToDrive(robot.getCarriage(), robot.getArm()));
        operatorArmToSwitchButton.whenPressed(new IntakeToSwitch(robot.getCarriage(), robot.getArm()));
        operatorArmToScaleButton.whenPressed(new IntakeToScale(robot.getCarriage(), robot.getArm()));

        selfTest.whenPressed(new SelfTestBootstrapper(robot));

        DriverStation.reportError("driverArmToIntakeButton " + (driverArmToIntakeButton ==null), false);
        DriverStation.reportError("driverArmToDriveButton " + (driverArmToDriveButton ==null), false);
        DriverStation.reportError("driverArmToSwitchButton " + (driverArmToSwitchButton ==null), false);
        DriverStation.reportError("driverArmToScaleButton " + (driverArmToScaleButton ==null), false);

        DriverStation.reportError("robot " + (robot==null), false);
        DriverStation.reportError("carriage " + (robot.getCarriage()==null), false);
        DriverStation.reportError("arm " + (robot.getArm()==null), false);
    }

    public void setDriverGamepadRumble(double leftIntensity, double rightIntensity) {
        driverGamepad.setRumble(RumbleType.kLeftRumble, leftIntensity);
        driverGamepad.setRumble(RumbleType.kRightRumble, rightIntensity);
    }

    public void setDriverGamepadRumble(double intensity) {
        driverGamepad.setRumble(RumbleType.kLeftRumble, intensity);
        driverGamepad.setRumble(RumbleType.kRightRumble, intensity);
    }

    public void setOperatorGamepadRumble(double leftIntensity, double rightIntensity) {
        operatorGamepad.setRumble(RumbleType.kLeftRumble, leftIntensity);
        operatorGamepad.setRumble(RumbleType.kRightRumble, rightIntensity);
    }

    public void setOperatorGamepadRumble(double intensity) {
        operatorGamepad.setRumble(RumbleType.kLeftRumble, intensity);
        operatorGamepad.setRumble(RumbleType.kRightRumble, intensity);
    }
}
