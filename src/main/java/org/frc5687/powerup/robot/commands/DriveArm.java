package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.subsystems.Arm;
import org.frc5687.powerup.robot.subsystems.Intake;

public class DriveArm extends Command {
    private Arm arm;
    private OI oi;
    private Intake intake;

    public DriveArm(Arm arm, OI oi, Intake intake){
        requires(arm);
        this.arm = arm;
        this.intake = intake;
        this.oi = oi;
    }

    @Override
    protected void initialize() {
        DriverStation.reportError("DriveArm initialized", false);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void execute() {
        double oiSpeed = DriverStation.getInstance().isAutonomous() ? 0 : oi.getArmSpeed();
        if (oiSpeed != 0.0) {
            arm.disable();
            arm.drive(oiSpeed);
            DriverStation.reportError("oiSpeed not zero, presumably because we're not in auto", false); // TODO: "EXCESSIVE" LOGGING
        } else if (!arm.getPIDController().isEnabled()) {
            double holdSpeed = arm.calculateHoldSpeed(intake.cubeIsSecured());
            DriverStation.reportError("DriveArm: driving arm at holdspeed: " + holdSpeed, false); // TODO: "EXCESSIVE" LOGGING
            arm.drive(holdSpeed);
        }
    }
}
