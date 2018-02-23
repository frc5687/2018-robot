package org.frc5687.powerup.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.DriveCarriage;
import org.frc5687.powerup.robot.utils.PDP;

public class Carriage extends PIDSubsystem {
    private PDP _pdp;
    private Encoder encoder;
    private VictorSP _motor;
    private OI _oi;
    private DigitalInput hallEffectTop;
    private DigitalInput hallEffectBottom;
    private boolean _isCompetitionBot;

    public static final double kP = 0.5;
    public static final double kI = 0.1;
    public static final double kD = 0.1;
    public static final double kF = 0.5;

    public Carriage(OI oi, PDP pdp, boolean isCompetitionBot) {
        super("Carriage", kP, kI, kD, kF);
        setAbsoluteTolerance(15);
        setInputRange(
                isCompetitionBot ? Constants.Carriage.ENCODER_BOTTOM_COMP : Constants.Carriage.ENCODER_BOTTOM_PROTO,
                isCompetitionBot ? Constants.Carriage.ENCODER_TOP_COMP : Constants.Carriage.ENCODER_TOP_PROTO
        );
        setOutputRange(-.50, 0.75);
        _oi = oi;
        _pdp = pdp;
        _motor = new VictorSP(RobotMap.Carriage.MOTOR);
        encoder = new Encoder(RobotMap.Carriage.ENCODER_A, RobotMap.Carriage.ENCODER_B);
        hallEffectTop = new DigitalInput(RobotMap.Carriage.HALL_EFFECT_TOP);
        hallEffectBottom = new DigitalInput(RobotMap.Carriage.HALL_EFFECT_BOTTOM);
        _isCompetitionBot = isCompetitionBot;
    }

    public void drive(double desiredSpeed) {
        double speed = desiredSpeed;
        if (speed > 0 && isAtTop()) {
            speed = Constants.Carriage.HOLD_SPEED;
        } else if (speed < 0 && isAtBottom()) {
            speed = -Constants.Carriage.HOLD_SPEED;
        } else if (speed > 0 && isInTopZone()) {
            speed *= Constants.Carriage.ZONE_SPEED_LIMIT;
        } else if (speed < 0 && isInBottomZone()) {
            speed *= Constants.Carriage.ZONE_SPEED_LIMIT;
        }
        if (_pdp.excessiveCurrent(RobotMap.PDP.CARRIAGE_SP, Constants.Carriage.PDP_EXCESSIVE_CURRENT)) {
            speed = 0.0;
        }

        speed *= (Constants.Carriage.MOTOR_INVERTED ? -1 : 1);
        SmartDashboard.putNumber("Carriage/rawSpeed", speed);
        SmartDashboard.putNumber("Carriage/speed", -speed);
        _motor.setSpeed(speed);
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

    public void updateDashboard() {
        SmartDashboard.putNumber("Carriage/position", getPos());
        SmartDashboard.putBoolean("Carriage/At top", isAtTop());
        SmartDashboard.putBoolean("Carriage/At bottom", isAtBottom());
    }

    public boolean isCompetitionBot() {
        return _isCompetitionBot;
    }

    public boolean isHealthy() {
        return false;
    }

    public boolean isInTopZone() {
        return getPos() > (_isCompetitionBot ? Constants.Carriage.START_TOP_ZONE_COMP : Constants.Carriage.START_TOP_ZONE_PROTO);
    }

    public boolean isInBottomZone() {
        return getPos() < (_isCompetitionBot ? Constants.Carriage.START_BOTTOM_ZONE_COMP : Constants.Carriage.START_BOTTOM_ZONE_PROTO);
    }

}
