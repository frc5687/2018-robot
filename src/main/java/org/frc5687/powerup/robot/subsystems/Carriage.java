package org.frc5687.powerup.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.DriveCarriage;

public class Carriage extends PIDSubsystem {
    private Encoder encoder;
    private VictorSP _motor;
    private OI _oi;
    private DigitalInput hallEffectTop;
    private DigitalInput hallEffectBottom;

    public static final double kP = 0.5;
    public static final double kI = 0.1;
    public static final double kD = 0.1;
    public static final double kF = 0.5;

    public Carriage(OI oi) {
        super("Carriage", kP, kI, kD, kF);
        setAbsoluteTolerance(15);
        setInputRange(0, Constants.Carriage.ENCODER_TOP);
        setOutputRange(-.50, 0.75);
        _oi = oi;
        _motor = new VictorSP(RobotMap.Carriage.MOTOR);
        encoder = new Encoder(RobotMap.Carriage.ENCODER_A, RobotMap.Carriage.ENCODER_B);
        hallEffectTop = new DigitalInput(RobotMap.Carriage.HALL_EFFECT_TOP);
        hallEffectBottom = new DigitalInput(RobotMap.Carriage.HALL_EFFECT_BOTTOM);
    }

    public void drive(double speed) {
        double _speed = speed;
        if (_speed > 0 && isAtTop()) {
            _speed = 0;//Constants.Carriage.HOLD_SPEED;
        } else if (_speed < 0 && isAtBottom()) {
            _speed = 0;//-Constants.Carriage.HOLD_SPEED;
        }
        _speed *= (Constants.Carriage.MOTOR_INVERTED ? -1 : 1);
        SmartDashboard.putNumber("Carriage/rawSpeed", _speed);
        SmartDashboard.putNumber("Carriage/speed", -_speed);
        _motor.setSpeed(_speed);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveCarriage(this, _oi));
    }

    public boolean isAtTop() {
        return !hallEffectTop.get();
    }

    public boolean isAtBottom() {
        return !hallEffectBottom.get();
    }

    public int getPos() {
        return encoder.get();
    }

    public void zeroEncoder() {
        encoder.reset();
    }

    @Override
    protected double returnPIDInput() {
        return encoder.get();
    }

    @Override
    protected void usePIDOutput(double output) {
        SmartDashboard.putNumber("Carriage/PID output", output);
        drive(output);
    }
    @Override
    public void periodic () {
        SmartDashboard.putNumber("Carriage/position", getPos());
        SmartDashboard.putBoolean("Carriage/At top", isAtTop());
        SmartDashboard.putBoolean("Carriage/At bottom", isAtBottom());
    }
}
