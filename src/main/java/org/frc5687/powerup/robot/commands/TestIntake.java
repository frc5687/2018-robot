package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.commands.auto.AutoIntake;
import org.frc5687.powerup.robot.commands.auto.AutoZeroCarriage;
import org.frc5687.powerup.robot.commands.tests.TestLeftIntakeMotorIn;
import org.frc5687.powerup.robot.commands.tests.TestLeftIntakeMotorOut;
import org.frc5687.powerup.robot.commands.tests.TestRightIntakeMotorIn;
import org.frc5687.powerup.robot.commands.tests.TestRightIntakeMotorOut;
import org.frc5687.powerup.robot.subsystems.Carriage;
import org.frc5687.powerup.robot.subsystems.Intake;
import org.frc5687.powerup.robot.utils.PDP;

public class TestIntake extends CommandGroup {
    public TestIntake(Intake intake, Carriage carriage, PDP pdp) {

        addSequential(new TestLeftIntakeMotorIn(intake, pdp));
        addSequential(new TestLeftIntakeMotorOut(intake, pdp));
        addSequential(new TestRightIntakeMotorIn(intake, pdp));
        addSequential(new TestRightIntakeMotorOut(intake, pdp));

        if (intake.cubeIsDetected()) {
            DriverStation.reportError("There is a cube detected. There should not be a cube detected", false);
        }
        addSequential(new AutoIntake(intake));
        if (!intake.cubeIsDetected()) {
            DriverStation.reportError("There is not a cube detected. There should be a cube detected", false);
        }

        if (!intake.isPlateDetected()) {
            DriverStation.reportError("Plate is not detected. There should be a switch / floor detected", false);
        }
        addSequential(new AutoZeroCarriage(carriage));
        if (intake.isPlateDetected()) {
            DriverStation.reportError("Plate detected. There should not be a switch / floor detected!", false);
        }
    }
}
