package org.frc5687.powerup.robot.subsystems;

import org.frc5687.powerup.robot.commands.DriveArm;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.commands.DriveClimber;



public class Climber extends Subsystem {


    public Climber() {
        }

    protected void initDefaultCommand() {
        setDefaultCommand(new DriveClimber(this));

    }
}