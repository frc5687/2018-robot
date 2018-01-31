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
    private VictorSP motor;
    private OI oi;
    private Encoder encoder;
    private DigitalInput hallEffectTop;
    private DigitalInput hallEffectBottom;

    public static final double kP = 0.0;
    public static final double kI = 0.0;
    public static final double kD = 0.0;
    public static final double kF = 0.0;

    public Carriage(OI oi) {
        super("Carriage", kP, kI, kD, kF);
        setAbsoluteTolerance(5);
        setInputRange(0, Constants.Carriage.ENCODER_TOP);
        setOutputRange(-.75, 0.75);
        motor = new VictorSP(RobotMap.Carriage.MOTOR);
        encoder = new Encoder(RobotMap.Carriage.ENCODER_A, RobotMap.Carriage.ENCODER_B);
        hallEffectTop = new DigitalInput(RobotMap.Carriage.HALL_EFFECT_TOP);
        hallEffectBottom = new DigitalInput(RobotMap.Carriage.HALL_EFFECT_BOTTOM);
        this.oi = oi;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveCarriage(this, oi));
    }

    public void drive(double speed) {
        motor.setSpeed(speed);
    }

    public boolean isAtTop() {
        return !hallEffectTop.get();
    }

    public boolean isAtBottom() {
        return !hallEffectBottom.get();
    }

    public int position() {
        return encoder.get();
    }

    public void updateDashboard () {
        SmartDashboard.putNumber("Carriage/position", position());
        SmartDashboard.putBoolean("Carriage/At top", isAtTop());
        SmartDashboard.putBoolean("Carriage/At bottom", isAtBottom());
    }

    @Override
    protected double returnPIDInput() {
        return encoder.get();
    }

    @Override
    protected void usePIDOutput(double output) {
        DriverStation.reportError("Arm PID output at " + output, false);
        drive(output);
    }
}
