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


public class Intake extends Subsystem {

    private VictorSP leftMotor;
    private VictorSP rightMotor;
    private AnalogInput irBack;
    private AnalogInput irDown;
    private Servo servo;
    private double _lastServoPos;

    private double _lastLeftSpeed;
    private double _lastRightSpeed;

    private OI oi;
    private boolean _isCompetitionBot;

    public Intake(OI oi, boolean isCompetitionBot) {
        leftMotor = new VictorSP(RobotMap.Intake.LEFT_MOTOR);
        rightMotor = new VictorSP(RobotMap.Intake.RIGHT_MOTOR);
        servo = new Servo(RobotMap.Intake.SERVO);

        leftMotor.setName("Intake", "Left Victor");
        rightMotor.setName("Intake", "Right Victor");

        this.oi = oi;
        _isCompetitionBot = isCompetitionBot;

        irBack = new AnalogInput(RobotMap.Intake.IR_BACK);
        irDown = new AnalogInput(RobotMap.Intake.IR_SIDE);

    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveIntake(this, oi));
    }

    public void drive(double leftSpeed, double rightSpeed) {
        if (cubeIsSecured()) {
            if (leftSpeed==0) {leftSpeed = Constants.Intake.HOLD_SPEED; }
            if (rightSpeed==0) {rightSpeed = Constants.Intake.HOLD_SPEED; }
        }
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

        return irBack.getValue() < Constants.Intake.BACK_IR.NOTHING_THRESHOLD && irBack.getValue() > Constants.Intake.BACK_IR.SECURED_THRESHOLD;
    }

    /**
     * Checks if cube is detected
     * @return Whether or not the infrared sensor sees anything
     */
    public boolean cubeIsDetected() {
        if (!Constants.Intake.BACK_IR.ENABLED) {
            return false;
        }
        return irBack.getValue() < Constants.Intake.BACK_IR.NOTHING_THRESHOLD & irBack.getValue() > Constants.Intake.BACK_IR.DETECTED_THRESHOLD;
    }

    public boolean isPlateDetected() {
        if (!Constants.Intake.DOWN_IR.ENABLED) {
            return false;
        }
        return irDown.getValue() > Constants.Intake.DOWN_IR.DETECTION_THRESHOLD;
    }

    public void updateDashboard() {
        SmartDashboard.putNumber("Intake/IR Back raw", irBack.getValue());
        SmartDashboard.putNumber("Intake/IR Side raw", irDown.getValue());
        SmartDashboard.putBoolean("Intake/cubeIsDetected()", cubeIsDetected());
        SmartDashboard.putBoolean("Intake/cubeIsSecured()", cubeIsSecured());
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

}
