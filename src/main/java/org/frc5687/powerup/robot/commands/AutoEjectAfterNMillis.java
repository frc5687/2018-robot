package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.commands.auto.AutoEject;
import org.frc5687.powerup.robot.commands.auto.AutoWaitForMillis;
import org.frc5687.powerup.robot.subsystems.Intake;

public class AutoEjectAfterNMillis extends CommandGroup {
    public AutoEjectAfterNMillis(Intake intake, long n) {
        addSequential(new AutoWaitForMillis(n));
        addSequential(new AutoEject(intake));
    }

    public AutoEjectAfterNMillis(Intake intake, double ejectSpeed, long n) {
        addSequential(new AutoWaitForMillis(n));
        addSequential(new AutoEject(intake, ejectSpeed));
    }
}
