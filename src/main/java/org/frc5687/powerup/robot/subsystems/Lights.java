package org.frc5687.powerup.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.RunLights;
import org.frc5687.powerup.robot.utils.Blinkin;

/**
 * Created by Ben Bernard on 2/14/2018.
 */
public class Lights extends Subsystem {
    private Blinkin _left;
    private Blinkin _right;

    private int _status = 0;
    private int _alert = 0;
    private int _turn = 0;
    private double[][] lightStatus;

    public Lights() {
        _left = new Blinkin(RobotMap.Lights.LEFT);
        _right = new Blinkin(RobotMap.Lights.RIGHT);
        lightStatus = new double[5][5];
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new RunLights(this));
    }
    public void runLights(){
        _left.set(lightStatus[_status][_alert]);
        _right.set(lightStatus[_status][_alert]);
    }

    public void execute() {



    }

}
