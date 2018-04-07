package org.frc5687.powerup.robot.commands.actions;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.subsystems.Intake;

public class ServoUp extends Command {
    private Intake _intake;
    private boolean _isFinished;

    public ServoUp(Intake intake) {
        _intake = intake;
    }

    @Override
    protected void initialize() {
        DriverStation.reportError("ServoUp initialized", false);
        _intake.driveServo(Constants.Intake.SERVO_UP);
        SmartDashboard.putNumber("Intake/Servo", Constants.Intake.SERVO_UP);
        _isFinished = true;
    }

    @Override
    protected void end() {
        DriverStation.reportError("ServoUp.end(): ServoUp finished", false);
    }

    @Override
    protected boolean isFinished() {
        return _isFinished;
    }
}
