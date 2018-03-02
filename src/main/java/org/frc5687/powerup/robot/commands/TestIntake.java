package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.commands.auto.AutoIntake;
import org.frc5687.powerup.robot.commands.auto.AutoZeroCarriage;
import org.frc5687.powerup.robot.subsystems.Carriage;
import org.frc5687.powerup.robot.subsystems.Intake;
import org.frc5687.powerup.robot.utils.PDP;

public class TestIntake extends CommandGroup {
    public TestIntake(Intake intake, Carriage carriage, PDP pdp) {
        DriverStation.reportError("Running right intake inwards for one second", false);
        addSequential(new RunIntakeFor(intake, 1000, 0, 1));
        DriverStation.reportError("Running right intake outwards for one second", false);
        addSequential(new RunIntakeFor(intake, 1000, 0, -1));
        DriverStation.reportError("Running left intake inwards for one second", false);
        addSequential(new RunIntakeFor(intake, 1000, 1, 0));
        DriverStation.reportError("Running left intake outwards for one second", false);
        addSequential(new RunIntakeFor(intake, 1000, -1, 0));
        if (intake.cubeIsDetected()) {
            DriverStation.reportError("There is a cube detected. There should not be a cube detected", false);
        }
        addSequential(new AutoIntake(intake));
        if (!intake.cubeIsDetected()) {
            DriverStation.reportError("There is not a cube detected. There should be a cube detected", false);
        }

        if (!intake.switchDetected()) {
            DriverStation.reportError("Switch / Floor is not detected. There should be a switch / floor detected", false);
        }
        addSequential(new AutoZeroCarriage(carriage));
        if (intake.switchDetected()) {
            DriverStation.reportError("Switch / Floor detected. There should not be a switch / floor detected!", false);
        }
    }
}
