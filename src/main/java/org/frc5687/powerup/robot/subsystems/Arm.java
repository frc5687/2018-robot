package org.frc5687.powerup.robot.subsystems;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.DriveArm;

public class Arm extends PIDSubsystem {
    private Encoder encoder;
    private VictorSP _motor;
    private OI _oi;
    private DigitalInput hallEffect;
    private DigitalOutput led;

    public static final double kP = 0.5;
    public static final double kI = 0.1;
    public static final double kD = 0.1;
    public static final double kF = 0.5;


    public Arm (OI oi) {
        super("Arm", kP, kI, kD, kF);
        setAbsoluteTolerance(5);
        setInputRange(0, 340);
        setOutputRange(-.75, 0.75);
        _oi=oi;
        _motor=new VictorSP(RobotMap.Arm.MOTOR);
        encoder = new Encoder(RobotMap.Arm.ENCODER_A, RobotMap.Arm.ENCODER_B);
        hallEffect = new DigitalInput(RobotMap.Arm.HALL_EFFECT_STARTING_POSITION);
        led = new DigitalOutput(RobotMap.Arm.STARTING_POSITION_LED);
        _oi.initializeArmButtons(this);
    }

    public void drive(double speed) {
        if(atTop() && speed > 0) {
            SmartDashboard.putString("Arm/Capped)", "Top");
            speed = Constants.Arm.HOLD_SPEED;
        } else if (atBottom() && speed < 0) {
            SmartDashboard.putString("Arm/Capped)", "Bottom");
            speed = Constants.Arm.HOLD_SPEED;
        }
        SmartDashboard.putNumber("Arm/speed", _motor.get());
        _motor.setSpeed(speed);
    }


    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveArm(this, _oi));
    }

    public boolean inStartingPosition () {
        return !hallEffect.get();
    }

    public boolean atTop() {
        return encoder.get() > Constants.Arm.ENCODER_TOP;
    }

    public boolean atBottom() {
        return encoder.get() < Constants.Arm.ENCODER_START;
    }

    public void zeroEncoder() {
        encoder.reset();
    }

    public int getAngle() {
        return encoder.get();
    }

    /**
     * Get the position of the arm in the range of 0 to 1.
     * @return the position of the arm in the range of 0 to 1. 0 is the bottom and 1 is the top.
     */
    public double getPosition() {
        return (double) encoder.get() / (double) Constants.Arm.ENCODER_TOP;
    }

    @Override
    protected double returnPIDInput() {
        return encoder.get();
    }

    @Override
    protected void usePIDOutput(double output) {
        SmartDashboard.putNumber("Arm PID output at ", output);
        drive(output);
    }

    public void updateDashboard () {
        SmartDashboard.putNumber("Arm/encoder.get()", encoder.get());
        SmartDashboard.putNumber("Arm/position", getPosition());
        SmartDashboard.putBoolean("Arm/inStartingPosition", inStartingPosition());
        led.set(inStartingPosition());
        SmartDashboard.putBoolean("Arm/atTop()", atTop());
        SmartDashboard.putBoolean("Arm/atBottom()", atBottom());

    }
}