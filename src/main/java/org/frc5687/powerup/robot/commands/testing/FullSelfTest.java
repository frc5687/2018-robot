package org.frc5687.powerup.robot.command.testing;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FullSelfTest extends CommandGroup {
    public FullSelfTest() {
        SmartDashboard.putBoolean("IsTesting", true);
    }
}