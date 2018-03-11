package org.frc5687.powerup.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.DriveIntake;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.utils.LidarProxy;


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
    private LidarProxy lidar;
    private boolean _isCompetitionBot;

    public Intake(Robot robot, boolean isCompetitionBot) {
        leftMotor = new VictorSP(RobotMap.Intake.LEFT_MOTOR);
        rightMotor = new VictorSP(RobotMap.Intake.RIGHT_MOTOR);
        servo = new Servo(RobotMap.Intake.SERVO);

        leftMotor.setName("Intake", "Left Victor");
        rightMotor.setName("Intake", "Right Victor");

        oi = robot.getOI();
        lidar = robot.getLidarProxy();
        _isCompetitionBot = isCompetitionBot;

        irBack = new AnalogInput(RobotMap.Intake.IR_BACK);
        irDown = new AnalogInput(RobotMap.Intake.IR_SIDE);

    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveIntake(this, oi));
    }

    public void drive(double leftSpeed, double rightSpeed) {
        if (cubeIsDetected()) {
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

        return irBack.getValue() > Constants.Intake.BACK_IR.SECURED_THRESHOLD;
    }

    /**
     * Checks if cube is detected
     * @return Whether or not the infrared sensor sees anything
     */
    public boolean cubeIsDetected() {
        if (!Constants.Intake.BACK_IR.ENABLED) {
            return false;
        }
        return irBack.getValue() > Constants.Intake.BACK_IR.DETECTED_THRESHOLD;
    }

    public boolean isPlateDetected() {
        if (!Constants.Intake.DOWN_IR.ENABLED) {
            return false;
        }
        return irDown.getValue() > Constants.Intake.DOWN_IR.DETECTION_THRESHOLD;
    }

    public int isScaleDetected() {
        int lidar_val = 0;
        int ir_val = 0;
        if (Constants.Intake.DOWN_IR.ENABLED && irDown.getValue() >= Constants.Intake.DOWN_IR.DETECTION_THRESHOLD) {
            ir_val = 1;
        }
        if (lidar.isInitializedProperly() && Constants.Intake.Lidar.ENABLED && lidar.get() >= Constants.Intake.Lidar.DETECTION_THRESHOLD) {
            lidar_val = 2;
        }
        return lidar_val + ir_val;
    }

    public void updateDashboard() {
        SmartDashboard.putNumber("Intake/IR Back raw", irBack.getValue());
        SmartDashboard.putNumber("Intake/IR Side raw", irDown.getValue());
        SmartDashboard.putBoolean("Intake/cubeIsDetected()", cubeIsDetected());
        SmartDashboard.putBoolean("Intake/cubeIsSecured()", cubeIsSecured());
        SmartDashboard.putNumber("Intake/isScaleDetected()", isScaleDetected());
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
