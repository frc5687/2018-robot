package org.frc5687.powerup.robot.subsystems;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.DriveArm;
import org.frc5687.powerup.robot.utils.AnglePotentiometer;
import org.frc5687.powerup.robot.utils.PDP;

public class Arm extends PIDSubsystem {
    private PDP _pdp;
    private Encoder encoder;
    private VictorSP _motor;
    private OI _oi;
    private Intake _intake;
    private DigitalInput hallEffect;
    private DigitalOutput led;
    private AnglePotentiometer _pot;
    private double TOP;
    private double BOTTOM;
    private boolean _isCompetitionBot;
    private int motorInversionMultiplier;

    public static final double kP = 0.03;
    public static final double kI = 0.002;
    public static final double kD = 0.0002;
    public static final double kF = 0;


    public Arm (OI oi, PDP pdp, Intake intake, boolean isCompetitionBot) {
        super("Arm", kP, kI, kD, kF, 0.02);
        setAbsoluteTolerance(5);
        _isCompetitionBot = isCompetitionBot;
        TOP = isCompetitionBot ? Constants.Arm.Pot.TOP_COMP : Constants.Arm.Pot.TOP_PROTO;
        BOTTOM = isCompetitionBot ? Constants.Arm.Pot.BOTTOM_COMP : Constants.Arm.Pot.BOTTOM_PROTO;
        setInputRange(BOTTOM, TOP);
        setOutputRange(Constants.Arm.PID_MIN_SPEED, Constants.Arm.PID_MAX_SPEED);
        _oi=oi;
        _intake = intake;
        _pdp = pdp;
        _motor=new VictorSP(RobotMap.Arm.MOTOR);
        motorInversionMultiplier = (isCompetitionBot ? Constants.Arm.MOTOR_INVERTED_COMP : Constants.Arm.MOTOR_INVERTED_PROTO) ? -1 : 1;
        encoder = new Encoder(RobotMap.Arm.ENCODER_A, RobotMap.Arm.ENCODER_B);
        hallEffect = new DigitalInput(RobotMap.Arm.HALL_EFFECT_STARTING_POSITION);
        led = new DigitalOutput(RobotMap.Arm.STARTING_POSITION_LED);
        _pot = isCompetitionBot ?
                new AnglePotentiometer(RobotMap.Arm.POTENTIOMETER, 33.0, 0.604, 166.0,  0.205)
                : new AnglePotentiometer(RobotMap.Arm.POTENTIOMETER, 38.0,  0.574, 163.0, 0.20);
    }

    public double calculateHoldSpeed() {
        return calculateHoldSpeed(false);
    }

    public double calculateHoldSpeed(boolean cubeDetected) {
        double ang = getPot();
        if (ang > 160 && cubeDetected) {
            return _isCompetitionBot ? Constants.Arm.HoldSpeeds.PAST_160_NO_CUBE_GRETA : Constants.Arm.HoldSpeeds.PAST_160_NO_CUBE_PROTO;
        } else if (ang > 160) {
            return _isCompetitionBot ? Constants.Arm.HoldSpeeds.PAST_160_CUBE_GRETA : Constants.Arm.HoldSpeeds.PAST_160_CUBE_PROTO;
        } else if (ang > 90 && cubeDetected) {
            return _isCompetitionBot ? Constants.Arm.HoldSpeeds.PAST_90_CUBE_GRETA : Constants.Arm.HoldSpeeds.PAST_90_CUBE_PROTO;
        } else if (ang > 90) {
            return _isCompetitionBot ? Constants.Arm.HoldSpeeds.PAST_90_NO_CUBE_GRETA : Constants.Arm.HoldSpeeds.PAST_90_NO_CUBE_PROTO;
        } else if (ang > 55 && cubeDetected) {
            return _isCompetitionBot ? Constants.Arm.HoldSpeeds.PAST_55_CUBE_GRETA : Constants.Arm.HoldSpeeds.PAST_55_CUBE_PROTO;
        } else if (ang > 55) {
            return _isCompetitionBot ? Constants.Arm.HoldSpeeds.PAST_55_NO_CUBE_GRETA : Constants.Arm.HoldSpeeds.PAST_55_NO_CUBE_PROTO;
        } else if (cubeDetected) {
            return _isCompetitionBot ? Constants.Arm.HoldSpeeds.PAST_BOTTOM_CUBE_GRETA : Constants.Arm.HoldSpeeds.PAST_BOTTOM_CUBE_PROTO;
        }
        return _isCompetitionBot ? Constants.Arm.HoldSpeeds.PAST_BOTTOM_NO_CUBE_GRETA : Constants.Arm.HoldSpeeds.PAST_BOTTOM_NO_CUBE_PROTO;
    }

    public void drive(double speed) {
        if(atTop() && speed > 0) {
            SmartDashboard.putString("Arm/Capped)", "Top");
            speed = 0.0;
        } else if (atBottom() && speed < 0) {
            SmartDashboard.putString("Arm/Capped)", "Bottom");
            speed = 0.0;
        }
        if (_pdp.excessiveCurrent(RobotMap.PDP.ARM_SP, Constants.Arm.PDP_EXCESSIVE_CURRENT)) {
            speed = 0.0;
        }
        speed = Math.max(speed, Constants.Arm.MIN_SPEED);
        speed = Math.min(speed, Constants.Arm.MAX_SPEED);
        SmartDashboard.putNumber("Arm/speedPreInversion", speed); // TODO: "EXCESSIVE" REAL TIME LOGGING
        speed *= motorInversionMultiplier;
        _motor.setSpeed(speed);
    }


    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveArm(this, _oi, _intake));
    }

    public boolean inStartingPosition () {
        return !hallEffect.get();
    }

    public boolean atTop() {
        double diff = getPot() - TOP;
        return Math.abs(diff) <= Constants.Arm.Pot.TOLERANCE;
    }

    public boolean atBottom() {
        double diff = getPot() - BOTTOM;
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
        return getAngle();
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

    public void updateDashboard() {
        SmartDashboard.putNumber("Arm/encoder.get()", encoder.get());
        SmartDashboard.putNumber("Arm/setpoint", getSetpoint());
        SmartDashboard.putNumber("Arm/position", getPosition());
        SmartDashboard.putBoolean("Arm/inStartingPosition", inStartingPosition());
        SmartDashboard.putBoolean("Arm/atTop()", atTop());
        SmartDashboard.putBoolean("Arm/atBottom()", atBottom());
        SmartDashboard.putNumber("Arm/potAngle", getPot());
        SmartDashboard.putNumber("Arm/potRaw", _pot.getRaw());
    }

    @Override
    public void periodic() {
        led.set(inStartingPosition());
    }

    public boolean isCompetitionBot() {
        return _isCompetitionBot;
    }

    public boolean isHealthy() {
        return true;
    }

    public double estimateHeight() {
        double armAngleRadians = Math.toRadians(getAngle() - 90);
        return Math.sin(armAngleRadians) * Constants.Arm.LENGTH;
    }
}