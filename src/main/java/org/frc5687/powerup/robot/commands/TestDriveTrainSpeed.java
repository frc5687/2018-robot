package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.subsystems.DriveTrain;

public class TestDriveTrainSpeed extends Command {
    private DriveTrain _driveTrain;
    private int _i;

    public TestDriveTrainSpeed(DriveTrain driveTrain) {
        requires(driveTrain);
        _driveTrain = driveTrain;
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void initialize() {
        super.initialize();
        _driveTrain.tankDrive(1.0, 1.0);
    }

    @Override
    protected void execute() {
        _i++;
        SmartDashboard.putNumber("TestDriveTrainSpeed/_i", _i);
    }
}
