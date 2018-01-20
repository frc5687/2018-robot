package org.frc5687.powerup.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.DriveCarriage;

public class Carriage extends Subsystem {
    private VictorSP motor;
    private OI oi;
    private Encoder encoder;

    public Carriage(OI oi) {
        motor = new VictorSP(RobotMap.Carriage.MOTOR);
        encoder = new Encoder(RobotMap.Carriage.ENCODER_A, RobotMap.Carriage.ENCODER_B);
        this.oi = oi;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveCarriage(this, oi));
    }

    public void drive(double speed) {
        motor.setSpeed(speed);
    }
}
