package org.frc5687.powerup.robot.subsystems;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.DriveArm;

public class Arm extends Subsystem {
    private Encoder encoder;
    private VictorSP _motor;
    private OI _oi;

    public Arm(OI oi) {
        _oi = oi;
        _motor = new VictorSP(RobotMap.Arm.MOTOR);
        encoder = new Encoder(RobotMap.Arm.ENCODER_A, RobotMap.Arm.ENCODER_B, false);

    }

    public void drive(double speed) {
        _motor.setSpeed(speed);
    }


    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveArm(this, _oi));
    }

    public void updateDashboard() {
        SmartDashboard.putNumber("encoder value", encoder.getRaw());
    }
}