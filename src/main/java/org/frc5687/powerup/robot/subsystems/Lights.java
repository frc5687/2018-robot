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

    private AutoChooser autoChooser;

    private int _status = 0;
    private int _alert = 0;
    private int _turn = 0;
    private double[][] lightStatus;

    public Lights(Intake _intake) {
        _left = new Talon(RobotMap.Lights.LEFT);
        _right = new Talon(RobotMap.Lights.RIGHT);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new RunLights(this));
    }

    public void runLights() {
        _right.set(lightStatus[_status][ _alert]);
        SmartDashboard.putNumber("Ligts/value", lightStatus[_status][_alert]);
    }

    public void execute() {
        runLights();
    }

    public void setAlert(int alert) {
        _alert = alert;
    }

    public void setStatus(int status) {
        _status = status;
    }


}
