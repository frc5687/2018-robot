package org.frc5687.powerup.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.DriveIntake;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.commands.DriveWith2Joysticks;

public class Intake extends Subsystem {

    private VictorSP leftMotor;
    private VictorSP rightMotor;
    private Servo servo;
    private AnalogInput irBack;
    private AnalogInput irSide;

    private OI oi;

    public Intake(OI oi) {
        leftMotor = new VictorSP(RobotMap.Intake.LEFT_MOTOR);
        rightMotor = new VictorSP(RobotMap.Intake.RIGHT_MOTOR);

        leftMotor.setName("Intake", "Left Victor");
        rightMotor.setName("Intake", "Right Victor");

        servo = new Servo(RobotMap.Intake.SERVO);

        this.oi = oi;

        irBack = new AnalogInput(RobotMap.Intake.IR_BACK);
        irSide = new AnalogInput(RobotMap.Intake.IR_SIDE);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveIntake(this, oi));
    }

    public void drive(double leftSpeed, double rightSpeed) {
        leftMotor.set(leftSpeed * (Constants.Intake.LEFT_MOTORS_INVERTED ? -1 : 1));
        rightMotor.set(rightSpeed * (Constants.Intake.RIGHT_MOTORS_INVERTED ? -1 : 1));
    }

    public void driveServo(double val) {
        SmartDashboard.putNumber("Intake/Servo", val);
        servo.set(val);
    }

    /**
     * Checks if cube is detected
     * @return Whether or not the infrared sensor sees anything
     */
    public boolean cubeIsDetected() {
        return irBack.getValue() < Constants.IR.DETECTION_THRESHOLD;
    }

    public void updateDashboard(){
        SmartDashboard.putNumber("Intake/IR Back raw", irBack.getValue());
        SmartDashboard.putNumber("Intake/IR Side raw", irSide.getValue());
        SmartDashboard.putBoolean("Intake/cubeIsDetected()", cubeIsDetected());
    }

}
