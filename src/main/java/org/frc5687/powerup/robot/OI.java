package org.frc5687.powerup.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.commands.*;
import org.frc5687.powerup.robot.commands.auto.*;
import org.frc5687.powerup.robot.utils.Gamepad;
import org.frc5687.powerup.robot.utils.Helpers;

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

    private JoystickButton driverArmToScaleButton;
    private JoystickButton driverArmToIntakeButton;
    private JoystickButton driverArmToSwitchButton;
    private JoystickButton driverArmToDriveButton;

    private JoystickButton operatorArmToScaleButton;
    private JoystickButton operatorArmToIntakeButton;
    private JoystickButton operatorArmToSwitchButton;
    private JoystickButton operatorArmToDriveButton;

    private JoystickButton carriagePID;

    private JoystickButton carriageZeroEncoder;

    private JoystickButton servoToggle;

    private JoystickButton driverArmUp;
    private JoystickButton driverArmDown;
    private JoystickButton driverCarriageUp;
    private JoystickButton driverCarriageDown;

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

        intakeLeftOut = new JoystickButton(operatorGamepad, Gamepad.Buttons.LEFT_BUMPER.getNumber());
        intakeRightOut = new JoystickButton(operatorGamepad, Gamepad.Buttons.RIGHT_BUMPER.getNumber());
        carriageZeroEncoder = new JoystickButton(operatorGamepad, Gamepad.Buttons.BACK.getNumber());
        carriagePID = new JoystickButton(operatorGamepad, Gamepad.Buttons.X.getNumber());
        servoToggle = new JoystickButton(operatorGamepad, Gamepad.Buttons.START.getNumber());

        driverArmUp = new JoystickButton(driverGamepad, Gamepad.Buttons.RIGHT_BUMPER.getNumber());
        driverArmDown = new JoystickButton(driverGamepad, Gamepad.Buttons.RIGHT_STICK.getNumber());

        driverCarriageUp = new JoystickButton(driverGamepad, Gamepad.Buttons.LEFT_BUMPER.getNumber());
        driverCarriageDown = new JoystickButton(driverGamepad, Gamepad.Buttons.LEFT_STICK.getNumber());
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
        double driver = Helpers.absMax(
                getSpeedFromAxis(driverGamepad, ButtonNumbers.LEFT_TRIGGER_AXIS),
                -getSpeedFromAxis(driverGamepad, ButtonNumbers.RIGHT_TRIGGER_AXIS)
        );
        double operator = getSpeedFromAxis(operatorGamepad, ButtonNumbers.LEFT_TRIGGER_AXIS);
        SmartDashboard.putNumber("Intake/left/driver", driver);
        SmartDashboard.putNumber("Intake/left/operator", operator);
        double trigger = Helpers.absMax(driver, operator);

        if (intakeLeftOut.get()) {
            return Constants.Intake.OUTTAKE_SPEED;
        } else if (Math.abs(trigger) > Constants.Intake.DEADBAND) {
            return trigger;
        }
        return 0;
    }

    public double getRightIntakeSpeed() {
        double driver = Helpers.absMax(
                getSpeedFromAxis(driverGamepad, ButtonNumbers.LEFT_TRIGGER_AXIS),
                -getSpeedFromAxis(driverGamepad, ButtonNumbers.RIGHT_TRIGGER_AXIS)
        );
        double operator = getSpeedFromAxis(operatorGamepad, ButtonNumbers.RIGHT_TRIGGER_AXIS);
        SmartDashboard.putNumber("Intake/right/driver", driver);
        SmartDashboard.putNumber("Intake/right/operator", operator);
        double trigger = Helpers.absMax(driver, operator);

        if (intakeRightOut.get()) {
            return Constants.Intake.OUTTAKE_SPEED;
        } else if (Math.abs(trigger) > Constants.Intake.DEADBAND) {
            return trigger;
        }
        return 0;
    }

    public double getCarriageSpeed() {
        double operator = getSpeedFromAxis(operatorGamepad, ButtonNumbers.LEFT_AXIS);
        double driver = driverCarriageUp.get() ? -1 : (driverCarriageDown.get() ? 0.3 : 0);
        double speed = Helpers.absMax(operator, driver);
        SmartDashboard.putNumber("OI/LEFT AXIS", speed);
        return applyDeadband(-speed, Constants.Carriage.DEADBAND, .1);
    }

    public double getArmSpeed() {
        double driver = driverArmUp.get() ? -0.75 : (driverArmDown.get() ? 0.3 : 0);
        double operator = getSpeedFromAxis(operatorGamepad, ButtonNumbers.RIGHT_AXIS);
        double speed = Helpers.absMax(operator, driver);
        double holdSpeed = _robot.pickConstant(Constants.Arm.HOLD_SPEED_COMP, Constants.Arm.HOLD_SPEED_PROTO);
        double holdSpeedWithCube = _robot.pickConstant(Constants.Arm.HOLD_SPEED_WITH_CUBE_COMP, Constants.Arm.HOLD_SPEED_WITH_CUBE_PROTO);
        double final_speed = _robot.getIntake().cubeIsDetected() ? holdSpeedWithCube : holdSpeed;
        return applyDeadband(-speed, 0.05, final_speed);
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
        carriageZeroEncoder.whenPressed(new AutoZeroCarriage(robot.getCarriage()));
        carriagePID.whenPressed(new MoveCarriageToSetpointPID(robot.getCarriage(), 500));

        driverArmToIntakeButton.whenPressed(new IntakeToFloor(robot.getCarriage(), robot.getArm()));
        driverArmToDriveButton.whenPressed(new IntakeToDrive(robot.getCarriage(), robot.getArm()));
        driverArmToSwitchButton.whenPressed(new IntakeToSwitch(robot.getCarriage(), robot.getArm()));
        driverArmToScaleButton.whenPressed(new IntakeToScale(robot.getCarriage(), robot.getArm()));

        operatorArmToIntakeButton.whenPressed(new IntakeToFloor(robot.getCarriage(), robot.getArm()));
        operatorArmToDriveButton.whenPressed(new IntakeToDrive(robot.getCarriage(), robot.getArm()));
        operatorArmToSwitchButton.whenPressed(new IntakeToSwitch(robot.getCarriage(), robot.getArm()));
        operatorArmToScaleButton.whenPressed(new IntakeToScale(robot.getCarriage(), robot.getArm()));

        servoToggle.whenPressed(new ServoToggle(robot.getIntake()));

        DriverStation.reportError("driverArmToIntakeButton " + (driverArmToIntakeButton ==null), false);
        DriverStation.reportError("driverArmToDriveButton " + (driverArmToDriveButton ==null), false);
        DriverStation.reportError("driverArmToSwitchButton " + (driverArmToSwitchButton ==null), false);
        DriverStation.reportError("driverArmToScaleButton " + (driverArmToScaleButton ==null), false);

        DriverStation.reportError("robot " + (robot==null), false);
        DriverStation.reportError("carriage " + (robot.getCarriage()==null), false);
        DriverStation.reportError("arm " + (robot.getArm()==null), false);
    }

}
