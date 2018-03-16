package org.frc5687.powerup.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.DriveLights;
import org.frc5687.powerup.robot.OI;

public class Lights extends Subsystem {
    private Robot _robot;
    private Spark _left;
    private Spark _right;
    private OI _oi;
    private DriverStation.Alliance _alliance;

    private double _mainLeftColor;
    private double _mainRightColor;
    private double _alertLeftColor;
    private double _alertRightColor;

    public Lights(Robot robot, OI oi) {
        _robot = robot;
        _oi = oi;
        _left = new Spark(RobotMap.Lights.LEFT);
        _right = new Spark(RobotMap.Lights.RIGHT);
    }

    public void initialize() {
        _alliance = DriverStation.getInstance().getAlliance();
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveLights(_robot));
    }

    public void setLeft(double val) {
        // DriverStation.reportError("Setting left to " + val, false);
        _left.set(val);
    }

    public void setRight(double val) {
        // DriverStation.reportError("Setting right to " + val, false);
        _right.set(val);
    }

    public void setBoth(double leftVal, double rightVal) {
        setLeft(leftVal);
        setRight(rightVal);
    }

    public void setColors() {
        Intake intake = _robot.getIntake();
        Climber climber = _robot.getClimber();
        if (_robot.isInWarningPeriod()) {
            _mainLeftColor = _mainRightColor = Constants.Lights.TIME_WARNING;
        }else if (_oi.getLightsFlashing() && System.currentTimeMillis()%1000/Constants.Lights.FLASHING_FREQUENCY > (1000/Constants.Lights.FLASHING_FREQUENCY)/2 ) {
        } else if (intake.isPlateDetected() && _robot.estimateIntakeHeight() >= Constants.Intake.PLATE_MINIMUM_CLARANCE) {
            _mainLeftColor = _mainRightColor = Constants.Lights.PLATE_DETECTED;
        } else if (intake.cubeIsSecured()) {
            _mainLeftColor = _mainRightColor = Constants.Lights.CUBE_SECURED;
        } else if (intake.isIntaking() && intake.cubeIsDetected()) {
            _mainLeftColor = _mainRightColor = Constants.Lights.CUBE_DETECTED;
        } else if (intake.isIntaking()) {
            _mainLeftColor = _mainRightColor = Constants.Lights.INTAKE_IN;
        } else if (intake.isEjecting()) {
            _mainLeftColor = _mainRightColor = Constants.Lights.INTAKE_OUT;
        } else if (climber.getDirection()>Constants.Climber.HOLD_SPEED) {
            _mainLeftColor = _mainRightColor = Constants.Lights.CLIMBER_UP;
        } else if (climber.getDirection()>0) {
            _mainLeftColor = _mainRightColor = Constants.Lights.CLIMBER_HOLD;
        } else if (climber.getDirection()<0) {
            _mainLeftColor = _mainRightColor = Constants.Lights.CLIMBER_DOWN;
        } else if (DriverStation.getInstance().isOperatorControl()) {
            if (SmartDashboard.getNumber("DB/Slider 0", 0.0) != 0.0) {
                _mainLeftColor = _mainRightColor = SmartDashboard.getNumber("DB/Slider 0", 0.0);
            } else {
                _mainLeftColor = _mainRightColor = (_alliance == DriverStation.Alliance.Blue) ? Constants.Lights.TELEOP_BLUE : Constants.Lights.TELEOP_RED;
            }
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
