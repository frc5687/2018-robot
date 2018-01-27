package org.frc5687.powerup.robot.commands.Servo;


import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.subsystems.Intake;

public class MoveToClimb extends Command {
    private boolean isFinished;
    private Intake intake;


    public MoveToClimb (Intake intake) {
        requires(intake);
    }

    @Override
    protected void execute() {
        intake.driveServo(Constants.Intake.SERVO_CLIMB_POSITION);
        isFinished = true;
    }

    @Override
    protected boolean isFinished() {
        return isFinished;
    }
}
