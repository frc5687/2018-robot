package org.frc5687.powerup.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.RunLights;
import org.frc5687.powerup.robot.utils.Blinkin;
import org.frc5687.powerup.robot.utils.AutoChooser;
import org.frc5687.powerup.robot.subsystems.Intake;

/**
 * Created by Ben Bernard on 2/14/2018.
 */
public class Lights extends Subsystem {
    private Spark _left;
    private Spark _right;
    private Intake _intake;
    private int alliance;

    private AutoChooser autoChooser;
    public boolean cubeIsPresent = false;
    public boolean intakeRunning = false;
    public boolean atSwitchHeight = false;
    public boolean atScaleHeight = false;
    public boolean leftBlining = false;
    public boolean rightBlinking = false;
    private int _alert = 0;
    private int _turn = 0;
    private double[][] lightStatus = {{0.87, 0.61, -0.81, 0.69, 0.91, 0.65}};



    public Lights() {
        _left = new Spark(RobotMap.Lights.LEFT);
        _right = new Spark(RobotMap.Lights.RIGHT);

    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new RunLights(this));
    }

    public void runLights() {
        if (rightBlinking) {
            _left.setSpeed(lightStatus[_alert][getStatus()]);
            if (System.currentTimeMillis() % 1000 > 500) {
                _right.setSpeed((lightStatus[_alert][getStatus()]));
            }

            else {
                _right.setSpeed(0.99);
            }

            if (leftBlining) {

                _right.setSpeed(lightStatus[_alert][getStatus()]);

                if (System.currentTimeMillis() % 1000 > 500) {

                    _left.setSpeed((lightStatus[_alert][getStatus()]));

                } else {
                    
                    _left.setSpeed(0.99);
                }
            }

        }
        _right.setSpeed(lightStatus[_alert][getStatus()]);
        _left.setSpeed(lightStatus[_alert][getStatus()]);
        SmartDashboard.putNumber("Ligts/value", lightStatus[_alert][getStatus()]);
    }

    public void execute() {
        runLights();
    }

    public void setAlert(int alert) {
        _alert = alert;
    }
    private int getStatus() {
        //if (atScaleHeight) {
        //    return 5;
        //}

       // if (atSwitchHeight) {
        //    return 4;
        //}

        //if (cubeIsPresent) {
         //   return 3;
        //}

        //if (intakeRunning) {
         //       return 2;
          //  }

            if(DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Blue){
            return 0;
            }
            return 1;
    }

}
