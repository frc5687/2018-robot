package org.frc5687.powerup.robot.subsystems;

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
    private Blinkin _left;
    private Blinkin _right;
    private Intake _intake;

    private AutoChooser autoChooser;

    private int _status = 0;
    private int _alert = 0;
    private int _turn = 0;
    private double[][] lightStatus;

    public Lights(Intake _intake) {
        _left = new Blinkin(RobotMap.Lights.LEFT);
        _right = new Blinkin(RobotMap.Lights.RIGHT);
        lightStatus = new double[][]{{0.19, 0.71, 0.69, 0.61}};
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new RunLights(this));
    }
    public void runLights(){

        _left.set(lightStatus[_status][_alert]);
        _right.set(lightStatus[_status][_alert]);
        SmartDashboard.putNumber("Ligts/value", lightStatus[_status][_alert] );
    }

    public void execute() {
        runLights();
        if((_intake.isIntaking()) && _status == 0){
            _status = 1;
        }
        if (_intake.cubeIsDetected() && _status == 1){
            _status = 2;
        }
        if (_intake.isOutaking() && _status == 2 ){
            _status = 3;
        }
        if (_intake.cubeIsDetected() == false && _status == 3){
            _status = 0;
        }




    }

}
