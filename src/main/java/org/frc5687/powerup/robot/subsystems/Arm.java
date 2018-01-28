package org.frc5687.powerup.robot.subsystems;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.DriveArm;

public class Arm extends Subsystem {
    private Encoder encoder;
    private VictorSP _motor;
    private OI _oi;
    private DigitalInput hallEffect;
    private DigitalOutput led;

    public Arm (OI oi){
        _oi=oi;
        _motor=new VictorSP(RobotMap.Arm.MOTOR);
        encoder = new Encoder(RobotMap.Arm.ENCODER_A, RobotMap.Arm.ENCODER_B);
        hallEffect = new DigitalInput(RobotMap.Arm.HALL_EFFECT_STARTING_POSITION);
        led = new DigitalOutput(RobotMap.Arm.STARTING_POSITION_LED);
    }

    public void drive(double speed) {
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
        return encoder.get() < Constants.Arm.ENCODER_BOTTOM;
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

    public void updateDashboard () {
        SmartDashboard.putNumber("Arm/encoder.get()", encoder.get());
        SmartDashboard.putNumber("Arm/position", getPosition());
        SmartDashboard.putBoolean("Arm/inStartingPosition", inStartingPosition());
        led.set(inStartingPosition());
        SmartDashboard.putBoolean("Arm/atTop()", atTop());
        SmartDashboard.putBoolean("Arm/atBottom()", atBottom());
    }
}