package org.frc5687.powerup.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.RunLights;
import org.frc5687.powerup.robot.utils.Blinkin;
import org.frc5687.powerup.robot.utils.AutoChooser;
import org.frc5687.powerup.robot.subsystems.Intake;

/**
 * Created by Ben Bernard on 2/14/2018.
 */
public class Lights extends Subsystem {
    private Talon _left;
    private Talon _right;
    private Intake _intake;
    public boolean cubeIsPresent;
    public int alliance;
    public boolean intakeRunning;

    private AutoChooser autoChooser;

    private int _alert = 0;
    private int _turn = 0;
    private double[][] lightStatus = {{0.87, 0.61, -0.81, 0.69, 0.91, 0.65}};


    public Lights() {
        _left = new Talon(RobotMap.Lights.LEFT);
        _right = new Talon(RobotMap.Lights.RIGHT);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new RunLights(this));
    }

    public void runLights() {
        _right.set(lightStatus[_alert][getStatus()]);
        SmartDashboard.putNumber("Ligts/value", lightStatus[_alert][getStatus()]);
    }

    public void execute() {
        runLights();
    }

    public void setAlert(int alert) {
        _alert = alert;
    }
    private int getStatus() {
        if (cubeIsPresent) {
            return 3;
        }
        if (intakeRunning) {
                return 2;
            }
            return alliance;
    }

}
