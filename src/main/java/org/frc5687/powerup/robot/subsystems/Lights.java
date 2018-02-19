package org.frc5687.powerup.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.DriveLights;

public class Lights extends Subsystem {
    private Robot _robot;
    private Spark _left;
    private Spark _right;
    private DriverStation.Alliance _alliance;
    public boolean cubeisPresent = false;
    public boolean intakeIsRunning = false;
    public boolean atSwitchHeight = false;
    public boolean atScaleHeight = false;
    //There is no constant for scale height so this is never changed
    public boolean rightBlinking = false;
    public boolean leftBlinking = false;

    public Lights(Robot robot) {
        _robot = robot;
        _left = new Spark(RobotMap.Lights.LEFT);
        _right = new Spark(RobotMap.Lights.RIGHT);
        _alliance = DriverStation.getInstance().getAlliance();
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveLights(_robot));
    }

    public void setLeft(double val) {
        _left.set(val);
    }

    public void setRight(double val) {
        _right.set(val);
    }

    public void setBoth(double leftVal, double rightVal) {
        setLeft(leftVal);
        setRight(rightVal);
    }

    public void setToColor(){
        if (atScaleHeight){
            setBoth(Constants.Lights.SOLID_PURPLE, Constants.Lights.SOLID_PURPLE);
            return;
        }

        if (atSwitchHeight){
            setBoth(Constants.Lights.SOLID_ORANGE, Constants.Lights.SOLID_ORANGE);
            return;
        }

        if (cubeisPresent){
            setBoth(Constants.Lights.SOLID_YELLOW, Constants.Lights.SOLID_YELLOW);
            return;
        }

        if (intakeIsRunning){
            setBoth(Constants.Lights.SOLID_GREEN, Constants.Lights.SOLID_GREEN);
            return;
        }

        setToAllianceColor();
    }

    public void updateAlliance() {
        _alliance = DriverStation.getInstance().getAlliance();
    }

    public DriverStation.Alliance getAlliance() {
        return _alliance;
    }

    public void setToAllianceColor() {
        if (_alliance == null) {
            updateAlliance();
        }
        if (getAlliance() == DriverStation.Alliance.Blue) {
            setBoth(Constants.Lights.SOLID_BLUE, Constants.Lights.SOLID_BLUE);
        } else if (getAlliance() == DriverStation.Alliance.Red) {
            setBoth(Constants.Lights.SOLID_RED, Constants.Lights.SOLID_RED);
        }
    }
}
