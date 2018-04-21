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
import org.frc5687.powerup.robot.utils.*;

public class OI {
    public class ButtonNumbers {
        public static final int LEFT_AXIS = 1;
        public static final int LEFT_TRIGGER_AXIS = 2;
        public static final int RIGHT_TRIGGER_AXIS = 3;
        public static final int RIGHT_AXIS = 5;
    }

    private Joystick driverGamepad;
    private Gamepad operatorGamepad;
    private OperatorConsole console;

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


    private JoystickLight leftDriveMotorsLED;
    private JoystickLight rightDriveMotorsLED;

    private JoystickLight leftIntakeMotorLED;
    private JoystickLight rightIntakeMotorLED;

    private JoystickLight carriageMotorLED;

    private JoystickLight armMotorLED;

    private JoystickLight leftDriveEncoderLED;
    private JoystickLight rightDriveEncoderLED;

    private JoystickLight intakeBackIRLED;

    private JoystickLight carriageEncoderLED;
    private JoystickLight armPotLED;

    private AxisButton climberHoldUp;
    private AxisButton climberHoldDown;
    private AxisButton intakeHoldUp;
    private AxisButton intakeHoldDown;
    private AxisButton carriageHoldUp;
    private AxisButton carriageHoldDown;
    private AxisButton armHoldUp;
    private AxisButton armHoldDown;

    private JoystickButton driveCapsOverride;
    private JoystickButton driveLimitsOverride;

    private JoystickButton climberKill;

    private JoystickButton intakeIROverride;
    private JoystickButton intakeKill;

    private JoystickButton carriageZero;
    private JoystickButton carriageCapsOverride;
    private JoystickButton carriageLimitsOverride;
    private JoystickButton carriageKill;

    private JoystickButton armZeroTop;
    private JoystickButton armZeroBottom;
    private JoystickButton armCapsOverride;
    private JoystickButton armLimitsOverride;
    private JoystickButton armKill;




    private POV driverPOV;

    private Robot _robot;

    public OI(Robot robot) {
        _robot = robot;
        driverGamepad = new Gamepad(0);
        operatorGamepad = new Gamepad(1);
        console = new OperatorConsole(2);

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

        initializeDriverGamepad(robot);
        initializeOperatorGamepad(robot);
        initializeConsole(robot);

        DriverStation.reportError("driverArmToIntakeButton " + (driverArmToIntakeButton ==null), false);
        DriverStation.reportError("driverArmToDriveButton " + (driverArmToDriveButton ==null), false);
        DriverStation.reportError("driverArmToSwitchButton " + (driverArmToSwitchButton ==null), false);
        DriverStation.reportError("driverArmToScaleButton " + (driverArmToScaleButton ==null), false);

        DriverStation.reportError("robot " + (robot==null), false);
        DriverStation.reportError("carriage " + (robot.getCarriage()==null), false);
        DriverStation.reportError("arm " + (robot.getArm()==null), false);
    }

    private void initializeDriverGamepad(Robot robot) {
        driverArmToIntakeButton.whenPressed(new IntakeToFloor(robot.getCarriage(), robot.getArm()));
        driverArmToDriveButton.whenPressed(new IntakeToDrive(robot.getCarriage(), robot.getArm()));
        driverArmToSwitchButton.whenPressed(new IntakeToSwitch(robot.getCarriage(), robot.getArm()));
        driverArmToScaleButton.whenPressed(new IntakeToScale(robot.getCarriage(), robot.getArm()));

    }

    private void initializeOperatorGamepad(Robot robot) {
        operatorArmToIntakeButton.whenPressed(new IntakeToFloor(robot.getCarriage(), robot.getArm()));
        operatorArmToDriveButton.whenPressed(new IntakeToDrive(robot.getCarriage(), robot.getArm()));
        operatorArmToSwitchButton.whenPressed(new IntakeToSwitch(robot.getCarriage(), robot.getArm()));
        operatorArmToScaleButton.whenPressed(new IntakeToScale(robot.getCarriage(), robot.getArm()));

        selfTest.whenPressed(new SelfTestBootstrapper(robot));
    }

    private void initializeConsole(Robot robot) {
        // LEDs
        leftDriveMotorsLED = new JoystickLight(console, OperatorConsole.LEDs.K.getNumber());
        rightDriveMotorsLED = new JoystickLight(console, OperatorConsole.LEDs.F.getNumber());

        leftIntakeMotorLED = new JoystickLight(console, OperatorConsole.LEDs.E.getNumber());
        rightIntakeMotorLED = new JoystickLight(console, OperatorConsole.LEDs.A.getNumber());

        carriageMotorLED = new JoystickLight(console, OperatorConsole.LEDs.J.getNumber());

        armMotorLED = new JoystickLight(console, OperatorConsole.LEDs.H.getNumber());


        leftDriveEncoderLED = new JoystickLight(console, OperatorConsole.LEDs.A.getNumber());
        rightDriveEncoderLED = new JoystickLight(console, OperatorConsole.LEDs.B.getNumber());

        intakeBackIRLED = new JoystickLight(console, OperatorConsole.LEDs.C.getNumber());

        carriageEncoderLED = new JoystickLight(console, OperatorConsole.LEDs.I.getNumber());

        armPotLED = new JoystickLight(console, OperatorConsole.LEDs.G.getNumber());


        climberHoldUp = new AxisButton(console, OperatorConsole.Axes.AX.getNumber(), 0.75, 1.0);
        climberHoldDown = new AxisButton(console, OperatorConsole.Axes.AX.getNumber(), 0.25, 0.5);

        intakeHoldUp = new AxisButton(console, OperatorConsole.Axes.AY.getNumber(), 0.75, 1.0);
        intakeHoldDown = new AxisButton(console, OperatorConsole.Axes.AY.getNumber(), 0.25, 0.5);

        carriageHoldUp = new AxisButton(console, OperatorConsole.Axes.AZ.getNumber(), 0.75, 1.0);
        carriageHoldDown = new AxisButton(console, OperatorConsole.Axes.AZ.getNumber(), 0.25, 0.5);

        armHoldUp = new AxisButton(console, OperatorConsole.Axes.AXR.getNumber(), 0.75, 1.0);
        armHoldDown = new AxisButton(console, OperatorConsole.Axes.AXR.getNumber(), 0.25, 0.5);

        driveCapsOverride = new JoystickButton(console, OperatorConsole.Buttons.G.getNumber());
        driveLimitsOverride= new JoystickButton(console, OperatorConsole.Buttons.K.getNumber());;

        climberKill = new JoystickButton(console, OperatorConsole.Buttons.C.getNumber());;

        // intakeIROverride = new JoystickButton(console, OperatorConsole.Buttons.?.getNumber());;

        intakeKill = new JoystickButton(console, OperatorConsole.Buttons.B.getNumber());;

        carriageZero = new JoystickButton(console, OperatorConsole.Buttons.A.getNumber());
        carriageZero.whenPressed(new AutoZeroCarriage(_robot.getCarriage()));

        // carriageCapsOverride = new JoystickButton(console, OperatorConsole.Buttons.?.getNumber());;
        carriageLimitsOverride = new JoystickButton(console, OperatorConsole.Buttons.I.getNumber());;
        carriageKill = new JoystickButton(console, OperatorConsole.Buttons.F.getNumber());;

        armZeroTop = new JoystickButton(console, OperatorConsole.Buttons.H.getNumber());;
        armZeroBottom = new JoystickButton(console, OperatorConsole.Buttons.E.getNumber());;

        // armCapsOverride = new JoystickButton(console, OperatorConsole.Buttons.?.getNumber());;
        armLimitsOverride = new JoystickButton(console, OperatorConsole.Buttons.C.getNumber());;
        armKill = new JoystickButton(console, OperatorConsole.Buttons.J.getNumber());;
    }


    public void updateConsole() {

        leftDriveMotorsLED.set(!_robot.getDriveTrain().isLeftMotorHealthy());
        rightDriveMotorsLED.set(!_robot.getDriveTrain().isRightMotorHealthy());

        leftIntakeMotorLED.set(!_robot.getIntake().isLeftMotorHealthy());
        rightIntakeMotorLED.set(!_robot.getIntake().isRightMotorHealthy());

        carriageMotorLED.set(!_robot.getCarriage().isMotorHealthy());

        armMotorLED.set(!_robot.getArm().isMotorHealthy());

        leftDriveEncoderLED.set(!_robot.getDriveTrain().isLeftEncoderHealthy());
        rightDriveEncoderLED.set(!_robot.getDriveTrain().isRightEncoderHealthy());

        intakeBackIRLED.set(!_robot.getIntake().isIRHealthy());

        carriageEncoderLED.set(!_robot.getCarriage().isEncoderHealthy());

        armPotLED.set(!_robot.getArm().isPotHealthy());

        /*  Code used to identify LEDs for testing.  s
        int pos = (int)SmartDashboard.getNumber("DB/Slider 3", 0);

        if (pos!=0) {
            console.setOutput(Math.abs(pos), pos>0);
        }
        */
    }

    public void pollConsole() {

        // Poll kill switches
        boolean climberDisabled = false;
        try {
            climberDisabled = climberKill.get();
        } catch (Exception e) { }
        _robot.getClimber().setDisabled(climberDisabled);

        boolean intakeDisabled = false;
        try {
            intakeDisabled = intakeKill.get();
        } catch (Exception e) { }
        _robot.getIntake().setDisabled(intakeDisabled);


        boolean carriageDisabled = false;
        try {
            carriageDisabled = carriageKill.get();
        } catch (Exception e) { }
        _robot.getCarriage().setDisabled(carriageDisabled);

        boolean armDisabled = false;
        try {
            armDisabled = armKill.get();
        } catch (Exception e) { }
        _robot.getArm().setDisabled(armDisabled);


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
