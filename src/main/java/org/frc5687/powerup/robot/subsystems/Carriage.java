package org.frc5687.powerup.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.DriveCarriage;

public class Carriage extends Subsystem {
    private VictorSP motor;
    private OI oi;
    private Encoder encoder;
    private DigitalInput hallEffectTop;
    private DigitalInput hallEffectBottom;

    public Carriage(OI oi) {
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

    public void updateDashboard () {
        SmartDashboard.putBoolean("Carriage/At top", isAtTop());
        SmartDashboard.putBoolean("Carriage/At bottom", isAtBottom());
    }
}
