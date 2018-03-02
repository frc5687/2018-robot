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

    private double _mainLeftColor;
    private int _mainRightColor;
    private int _alertLeftColor;
    private int _alertRightColor;

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


    public void setColors() {
        Intake intake = _robot.getIntake();

        if (intake.isPlateDetected()) {
            _mainLeftColor = _mainRightColor = Constants.Lights.PLATE_DETECTED;
        } else if (intake.cubeIsSecured()) {
            _mainLeftColor = _mainRightColor = Constants.Lights.CUBE_SECURED;
        } else if (intake.isRunning() && intake.cubeIsDetected()) {
            _mainLeftColor = _mainRightColor = Constants.Lights.CUBE_DETECTED;
        } else if (intake.isRunning()) {
            _mainLeftColor = _mainRightColor = Constants.Lights.INTAKE_RUNNING;
        } else if (DriverStation.getInstance().isOperatorControl()) {
            _mainLeftColor = _mainRightColor =(_alliance == DriverStation.Alliance.Blue) ?  Constants.Lights.TELEOP_BLUE : Constants.Lights.TELEOP_RED;
        } else if (DriverStation.getInstance().isAutonomous()) {
            _mainLeftColor = _mainRightColor =(_alliance == DriverStation.Alliance.Blue) ?  Constants.Lights.AUTO_BLUE : Constants.Lights.AUTO_RED;
        } else if (DriverStation.getInstance().isDisabled()) {
            _mainLeftColor = _mainRightColor =(_alliance == DriverStation.Alliance.Blue) ?  Constants.Lights.DISABLED_BLUE : Constants.Lights.DISABLD_RED;
        } else {
            _mainLeftColor = _mainRightColor = Constants.Lights.DEFAULT;
        }

        setLeft(_mainLeftColor);
        setRight(_mainRightColor);
    }
}
