package org.frc5687.powerup.robot.subsystems;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.DriveArm;

public class Arm extends Subsystem {
    private VictorSP _motor;
    private OI _oi;

    public Arm (OI oi){
        _oi=oi;
        _motor=new VictorSP(RobotMap.Arm.MOTOR);

    }
    public void drive(double speed){
        _motor.setSpeed(speed);
    }


    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveArm(this, _oi));
    }
}
