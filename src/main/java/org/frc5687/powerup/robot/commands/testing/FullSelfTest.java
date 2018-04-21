package org.frc5687.powerup.robot.commands.testing;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.subsystems.Intake;


public class FullSelfTest extends CommandGroup {

    public FullSelfTest(Robot robot) {


        addSequential(new ConfirmTest(robot.getOI(), "Please ensure the robot is drydocked and press Start to continue.", "Test started.", "Test aborted."));
        addSequential(new TestDriveTrain(robot.getDriveTrain(), robot.getPDP(), robot.getLights()));

        addSequential(new ConfirmTest(robot.getOI(), "Please be sure that the carriage is clear and press Start to continue.", "Test started.", "Test aborted."));
        addSequential(new TestCarriage(robot.getCarriage(), robot.getPDP(), robot.getLights()));

        addSequential(new ConfirmTest(robot.getOI(), "Please be sure that the arm is clear and press Start to continue.", "Test started.", "Test aborted."));

        addSequential(new ConfirmTest(robot.getOI(), "Please be sure that the intake is clear and press Start to continue.", "Test started.", "Test aborted."));

        addSequential(new ConfirmTest(robot.getOI(), "Tests complete.", "Test started.", "Test aborted."));


    }
}