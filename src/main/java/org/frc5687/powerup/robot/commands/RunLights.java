package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import javafx.scene.effect.Light;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.subsystems.Lights;

/**
 * Created by Ben Bernard on 2/16/2018.
 */
public class RunLights extends Command {
    private Lights _lights;

    public RunLights (Lights lights) {
        _lights = lights;
    }

    @Override
    protected void execute() {
        _lights.execute();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
