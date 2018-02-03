package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.subsystems.Intake;

public class ServoReleaseCube extends Command {
    private Intake _intake;
    private boolean _isFinished;

    public ServoReleaseCube(Intake intake) {
        _intake = intake;
    }

    @Override
    protected boolean isFinished() {
        return _isFinished;
    }

    @Override
    protected void initialize() {
        SmartDashboard.putNumber("Intake/Servo", Constants.Intake.SERVO_BOTTOM);
        _intake.driveServo(Constants.Intake.SERVO_BOTTOM);
        _isFinished = true;
        DriverStation.reportError("Release Cube init", false);
    }

}
