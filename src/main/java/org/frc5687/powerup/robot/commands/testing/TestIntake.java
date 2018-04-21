package org.frc5687.powerup.robot.commands.testing;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.auto.paths.FarLeftToRightScaleDeadPartOne;
import org.frc5687.powerup.robot.subsystems.Carriage;
import org.frc5687.powerup.robot.subsystems.Intake;
import org.frc5687.powerup.robot.subsystems.Lights;
import org.frc5687.powerup.robot.utils.PDP;

public class TestIntake extends Command {
    private Intake _intake;
    private PDP _pdp;
    private Lights _lights;

    private State _state = State.LEFT;
    private long _endMillis;

    private static long _runMillis = 500;

    private final double _targetAmps = 3;

    private boolean _pass = true;

    private double _maxAmps = 0;

    public TestIntake(Intake intake, PDP pdp, Lights lights){
        // Run the carriage up to the hall efect
        requires(intake);

        _intake = intake;
        _pdp = pdp;
        _lights = lights;

    }

    @Override
    protected void initialize() {
        DriverStation.reportError("Starting intake test", false);
        _state = State.LEFT;
        _maxAmps = 0;
        _endMillis = System.currentTimeMillis() + _runMillis;
    }

    protected void execute() {

        switch (_state) {
            case LEFT:
                _lights.setBoth(Constants.Lights.TEST_RUNNING, Constants.Lights.TEST_RUNNING);
                _intake.drive(Constants.Intake.INTAKE_SPEED, 0);
                _maxAmps = Math.max(_maxAmps, _pdp.getCurrent(RobotMap.PDP.INTAKE_LEFT_SP));
                if (System.currentTimeMillis() > _endMillis) {
                    _intake.drive(0, 0);
                    _state = State.LEFTDONE;
                }
                break;
            case LEFTDONE:
                if (_maxAmps < _targetAmps) {
                    _pass = false;
                    SmartDashboard.putBoolean("SelfTest/Intake/Left/Amps/Passed", false);
                    DriverStation.reportError("Target amperage not reached on left intake.  Expected " + _targetAmps + " but measured " + _maxAmps + ".", false);
                } else {
                    _pass = true;
                    SmartDashboard.putBoolean("SelfTest/Intake/Left/Amps/Passed", true);
                    DriverStation.reportError("Amp draw passed on left intake.  Expected " + _targetAmps + " and measured  " + _maxAmps + ".", false);
                }
                _maxAmps = 0;
                _endMillis = System.currentTimeMillis() + _runMillis;
                _state = State.RIGHT;
                break;
            case RIGHT:
                _lights.setBoth(Constants.Lights.TEST_RUNNING, Constants.Lights.TEST_RUNNING);
                _intake.drive(0, Constants.Intake.INTAKE_SPEED);
                _maxAmps = Math.max(_maxAmps, _pdp.getCurrent(RobotMap.PDP.INTAKE_RIGHT_SP_PROTO));
                if (System.currentTimeMillis() > _endMillis) {
                    _intake.drive(0, 0);
                    _state = State.RIGHTDONE;
                }
                break;
            case RIGHTDONE:
                if (_maxAmps < _targetAmps) {
                    _pass = false;
                    SmartDashboard.putBoolean("SelfTest/Intake/Right/Amps/Passed", false);
                    DriverStation.reportError("Target amperage not reached on right intake.  Expected " + _targetAmps + " but measured " + _maxAmps + ".", false);
                } else {
                    _pass = true;
                    SmartDashboard.putBoolean("SelfTest/Intake/Right/Amps/Passed", true);
                    DriverStation.reportError("Amp draw passed on right intake.  Expected " + _targetAmps + " and measured  " + _maxAmps + ".", false);
                }
                _endMillis = System.currentTimeMillis() + _runMillis;
                _state = State.WAIT;
                break;
            case WAIT:
                if (!_intake.cubeIsSecured()) {
                    SmartDashboard.putBoolean("SelfTest/Intake/CubeIR", false);
                    DriverStation.reportError("Cube not detected .", false);
                } else {
                    SmartDashboard.putBoolean("SelfTest/Intake/CubeIR", true);
                    DriverStation.reportError("Cube detected .", false);
                }
                _intake.drive(0,0 );
                if (System.currentTimeMillis() > _endMillis) {
                    _state = State.DONE;
                }
                break;
        }
    }

    @Override
    protected void end() {
        _intake.drive(0, 0);
    }

    @Override
    protected boolean isFinished() {
        return _state == State.DONE;
    }

    private enum State {
        LEFT,
        LEFTDONE,
        RIGHT,
        RIGHTDONE,
        WAIT,
        DONE
    }

}
