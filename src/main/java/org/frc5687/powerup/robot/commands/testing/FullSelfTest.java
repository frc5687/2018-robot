package org.frc5687.powerup.robot.commands.testing;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.subsystems.Intake;


public class FullSelfTest extends CommandGroup {

    public FullSelfTest(Intake intake) {
        requires(intake);
        SmartDashboard.putBoolean("IsTesting", true);
        intake.drive(0.5, -0.5);

    }
}