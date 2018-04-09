package org.frc5687.powerup.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.DriveIntake;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.utils.MotorHealthChecker;
import org.frc5687.powerup.robot.utils.PDP;


public class Intake extends Subsystem {

    private VictorSP leftMotor;
    private VictorSP rightMotor;
    private AnalogInput irBack;
    private AnalogInput irDown;
    private AnalogInput irUp;
    private Servo servo;
    private Arm _arm;
    private double _lastServoPos;

    private double _lastLeftSpeed;
    private double _lastRightSpeed;

    private OI oi;
    private boolean _isCompetitionBot;

    private MotorHealthChecker _leftHC;
    private MotorHealthChecker _rightHC;

    private boolean _disabled = false;

    private PDP _pdp;

    public Intake(OI oi, PDP pdp, boolean isCompetitionBot) {
        leftMotor = new VictorSP(RobotMap.Intake.LEFT_MOTOR);
        rightMotor = new VictorSP(RobotMap.Intake.RIGHT_MOTOR);
        servo = new Servo(RobotMap.Intake.SERVO);

        leftMotor.setName("Intake", "Left Victor");
        rightMotor.setName("Intake", "Right Victor");

        this.oi = oi;
        _isCompetitionBot = isCompetitionBot;

        irBack = new AnalogInput(RobotMap.Intake.IR_BACK);
        irDown = new AnalogInput(RobotMap.Intake.IR_SIDE);
        irUp = new AnalogInput(RobotMap.Intake.IR_UP);

        _pdp = pdp;

        _leftHC = new MotorHealthChecker(Constants.Intake.HC_MIN_SPEED, Constants.Intake.HC_MIN_CURRENT, Constants.HEALTH_CHECK_CYCLES, _pdp, RobotMap.PDP.INTAKE_LEFT_SP);
        _rightHC = new MotorHealthChecker(Constants.Intake.HC_MIN_SPEED, Constants.Intake.HC_MIN_CURRENT, Constants.HEALTH_CHECK_CYCLES, _pdp, _isCompetitionBot ? RobotMap.PDP.INTAKE_RIGHT_SP_COMP : RobotMap.PDP.INTAKE_RIGHT_SP_PROTO);

    }

    public void setArm(Arm arm) {
        _arm = arm;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveIntake(this, oi));
    }

    public void drive(double leftSpeed, double rightSpeed) {
        leftSpeed = leftSpeed == 0 ? Constants.Intake.HOLD_SPEED : leftSpeed;
        rightSpeed = rightSpeed == 0 ? Constants.Intake.HOLD_SPEED : rightSpeed;

        _lastLeftSpeed = leftSpeed;
        leftMotor.set(leftSpeed * (Constants.Intake.LEFT_MOTORS_INVERTED ? -1 : 1));

        _lastRightSpeed = rightSpeed;
        rightMotor.set(
                rightSpeed * (
                    (
                            _isCompetitionBot ? Constants.Intake.RIGHT_MOTORS_INVERTED_COMP : Constants.Intake.RIGHT_MOTORS_INVERTED_PROTO
                    ) ? -1 : 1
                )
        );

        _leftHC.checkHealth(leftSpeed);
        _rightHC.checkHealth(rightSpeed);
    }

    public void driveServo(double val) {
        _lastServoPos = val;
        SmartDashboard.putNumber("Intake/Servo", val);
        servo.set(val);
    }

    public double getServoPosition() {
        return _lastServoPos;
    }

    /**
     * Checks if cube is fully in the intake.
     * @return
     */
    public boolean cubeIsSecured() {
        if (!Constants.Intake.BACK_IR.ENABLED) {
            return false;
        }
        int dist = irBack.getValue();
        return Constants.Intake.BACK_IR.SECURED_HIGH_END > dist && dist > Constants.Intake.BACK_IR.SECURED_LOW_END;
    }

    /**
     * Checks if cube is detected (Should not be used due to inaccuracy in IR sensor used
     * @return Whether or not the infrared sensor sees anything
     */
    @Deprecated
    public boolean cubeIsDetected() {
        if (!Constants.Intake.BACK_IR.ENABLED) {
            return false;
        }
        int dist = irBack.getValue();
        return Constants.Intake.BACK_IR.DETECTED_HIGH_END > dist && dist > Constants.Intake.BACK_IR.DETECTED_LOW_END;
    }

    public boolean isPlateDetected() {
        if (!Constants.Intake.UP_IR.ENABLED || _arm == null) {
            return false;
        }
        return _arm.getPot() > Constants.Intake.UP_IR.MIN_ARM_ANGLE && irUp.getValue() > Constants.Intake.UP_IR.PLATE_DETECTION_THRESHOLD;
    }

    public boolean isIRHealthy() {
        return true;
    }

    public void updateDashboard() {
        SmartDashboard.putNumber("Intake/IR Back raw", irBack.getValue());
        SmartDashboard.putNumber("Intake/IR Side raw", irDown.getValue());
        SmartDashboard.putNumber("Intake/IR Up raw", irUp.getValue());
        SmartDashboard.putBoolean("Intake/cubeIsDetected()", cubeIsDetected());
        SmartDashboard.putBoolean("Intake/cubeIsSecured()", cubeIsSecured());
        SmartDashboard.putBoolean("Intake/isPlateDetected()", isPlateDetected());
        SmartDashboard.putBoolean("Intake/is healthy", isHealthy());
        SmartDashboard.putBoolean("Intake/is left healthy", isLeftMotorHealthy());
        SmartDashboard.putBoolean("Intake/is right healthy", isRightMotorHealthy());
    }

    @Override
    public void periodic(){

    }

    public boolean isIntaking() {
        return (_lastRightSpeed > Constants.Intake.HOLD_SPEED || _lastLeftSpeed > Constants.Intake.HOLD_SPEED);
    }

    public boolean isLeftIntaking() {
        return (_lastLeftSpeed > Constants.Intake.HOLD_SPEED);
    }

    public boolean isRightIntaking() {
        return (_lastRightSpeed > Constants.Intake.HOLD_SPEED);
    }


    public boolean isEjecting() {
        return (_lastRightSpeed < 0 || _lastLeftSpeed < 0);
    }

    public boolean isLeftEjecting() {
        return (_lastLeftSpeed < 0);
    }
    public boolean isRightEjecting() {
        return (_lastRightSpeed < 0);
    }

    public boolean isLeftMotorHealthy() {
        return _leftHC.IsHealthy();
    }

    public boolean isRightMotorHealthy() {
        return _rightHC.IsHealthy();
    }

    public boolean isHealthy() {
        return isLeftMotorHealthy() && isRightMotorHealthy();
    }

    public boolean isEnabled() {
        return !_disabled;
    }

    public void setDisabled(boolean value) {
        _disabled = value;
    }
}
