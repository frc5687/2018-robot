package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.subsystems.Intake;

public class ServoToggle extends Command {
    private Intake _intake;
    private boolean _isFinished;

    public ServoToggle(Intake intake) {
        _intake = intake;
    }

    @Override
    protected boolean isFinished() {
        return _isFinished;
    }

    @Override
    protected void initialize() {
        double servoPos = _intake.getServoPosition();
        double newPos = Constants.Intake.SERVO_UP;
        if (servoPos == Constants.Intake.SERVO_UP) {
            newPos = Constants.Intake.SERVO_BOTTOM;
        }
        _intake.driveServo(newPos);
        SmartDashboard.putNumber("Intake/Servo", newPos);
        _isFinished = true;
        DriverStation.reportError("HoldCube init", false);
    }

}
