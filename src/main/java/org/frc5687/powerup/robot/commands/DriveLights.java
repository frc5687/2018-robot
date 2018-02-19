package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.subsystems.Lights;
import org.frc5687.powerup.robot.subsystems.Intake;

public class DriveLights extends Command {
    private Lights _lights;
    private Intake _intake;

    public DriveLights(Robot robot) {
        _lights = robot.getLights();
        requires(_lights);
        requires(_intake);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void initialize() {
        _lights.setToAllianceColor();
    }

    @Override
    protected void execute() {
        _lights.setToColor();
    }
}
