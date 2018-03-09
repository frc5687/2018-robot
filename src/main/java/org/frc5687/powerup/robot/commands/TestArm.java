package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.subsystems.Arm;
import org.frc5687.powerup.robot.subsystems.Carriage;

public class TestArm extends CommandGroup{
    public TestArm(Robot robot){
        addSequential(new ArmMotorTest(robot.getArm(), robot.getCarriage()));
        addSequential(new MoveArmToSetpointPID(robot.getArm(), Constants.Arm.Encoder.ENCODER_START));
        addSequential(new MoveArmToSetpointPID(robot.getArm(), Constants.Arm.Encoder.ENCODER_TOP));

    }


}
