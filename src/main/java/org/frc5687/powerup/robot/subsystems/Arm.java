package org.frc5687.powerup.robot.subsystems;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
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
    private Potentiometer _pot;

    public static final double kP = 0.04;
    public static final double kI = 0.002;
    public static final double kD = 0.0002;
    public static final double kF = 0;


    public Arm (OI oi) {
        super("Arm", kP, kI, kD, kF, 0.01);
        setAbsoluteTolerance(5);
        setInputRange(Constants.Arm.Pot.BOTTOM, Constants.Arm.Pot.TOP);
        setOutputRange(-.25, 0.75);
        _oi=oi;
        _motor=new VictorSP(RobotMap.Arm.MOTOR);
        encoder = new Encoder(RobotMap.Arm.ENCODER_A, RobotMap.Arm.ENCODER_B);
        hallEffect = new DigitalInput(RobotMap.Arm.HALL_EFFECT_STARTING_POSITION);
        led = new DigitalOutput(RobotMap.Arm.STARTING_POSITION_LED);
        _pot = new AnalogPotentiometer(RobotMap.Arm.POTENTIOMETER, 360, -2);
    }

    public void drive(double speed) {
        if(atTop() && speed > 0) {
            SmartDashboard.putString("Arm/Capped)", "Top");
            speed = Constants.Arm.HOLD_SPEED;
        } else if (atBottom() && speed < 0) {
            SmartDashboard.putString("Arm/Capped)", "Bottom");
            speed = Constants.Arm.HOLD_SPEED;
        }
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
        double diff = getPot() - Constants.Arm.Pot.TOP;
        return Math.abs(diff) <= Constants.Arm.Pot.TOLERANCE;
    }

    public boolean atBottom() {
        double diff = getPot() - Constants.Arm.Pot.BOTTOM;
        return Math.abs(diff) <= Constants.Arm.Pot.TOLERANCE;
    }

    public void zeroEncoder() {
        encoder.reset();
    }

    public double getPot() { return _pot.get(); }

    public double getAngle() {
        return getPot();
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
        return getPot();
    }

    @Override
    protected void usePIDOutput(double output) {
        SmartDashboard.putNumber("Arm/PID output", output);
        drive(output);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Arm/encoder.get()", encoder.get());
        SmartDashboard.putNumber("Arm/setpoint", getSetpoint());
        SmartDashboard.putNumber("Arm/position", getPosition());
        SmartDashboard.putBoolean("Arm/inStartingPosition", inStartingPosition());
        led.set(inStartingPosition());
        SmartDashboard.putBoolean("Arm/atTop()", atTop());
        SmartDashboard.putBoolean("Arm/atBottom()", atBottom());
        SmartDashboard.putNumber("Arm/getPot()", getPot());
    }
}