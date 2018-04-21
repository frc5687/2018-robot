package org.frc5687.powerup.robot.commands.testing;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.auto.AutoIntake;
import org.frc5687.powerup.robot.commands.auto.AutoZeroCarriage;
import org.frc5687.powerup.robot.commands.auto.paths.FarLeftToRightScaleDeadPartOne;
import org.frc5687.powerup.robot.subsystems.Carriage;
import org.frc5687.powerup.robot.subsystems.Lights;
import org.frc5687.powerup.robot.utils.PDP;

public class TestCarriage extends CommandGroup {
    private Carriage _carriage;
    private PDP _pdp;
    private Lights _lights;

    private State _state = State.ZERO;
    private long _endMillis;

    private static long _maxZeroMillis = 1500;
    private static long _maxBottomMillis = 2000;
    private static long _maxMiddleMillis = 1500;

    private final double _targetLiftingAmps = 3;

    private boolean _pass = true;

    private double _maxAmps = 0;

    public TestCarriage(Carriage carriage, PDP pdp, Lights lights){
        // Run the carriage up to the hall efect
        requires(carriage);

        _carriage = carriage;
        _pdp = pdp;
        _lights = lights;

    }

    @Override
    protected void initialize() {
        DriverStation.reportError("Starting carriage test", false);
        _state = State.ZERO;
        _maxAmps = 0;
        _endMillis = System.currentTimeMillis() + _maxZeroMillis;
        _carriage.zeroEncoder();
    }

    protected void execute() {

        switch (_state) {
            case ZERO:
                _lights.setBoth(Constants.Lights.TEST_RUNNING, Constants.Lights.TEST_RUNNING);
                _carriage.drive(Constants.Carriage.MAXIMUM_SPEED);
                _maxAmps = Math.max(_maxAmps, _pdp.getCurrent(RobotMap.PDP.CARRIAGE_SP));
                if (_carriage.isAtTop()) {
                    // At the top...
                    _carriage.zeroEncoder();
                    _carriage.drive(0);
                    DriverStation.reportError("Carriage hall-effect detected.  Zeroing carriage.", false);
                    _state = State.ZERODONE;
                } else if (System.currentTimeMillis() > _endMillis) {
                    DriverStation.reportError("Carriage hall-effect not detected within " + _maxZeroMillis + "ms.", false);
                    _carriage.drive(0);
                    _pass = false;
                    _state = State.ZERODONE;
                }
                break;
            case ZERODONE:
                if (_maxAmps < _targetLiftingAmps) {
                    _pass = false;
                    SmartDashboard.putBoolean("SelfTest/Carriage/Amps/Passed", false);
                    DriverStation.reportError("Target amperage not reached on carriage.  Expected " + _targetLiftingAmps + " but measured " + _maxAmps + ".", false);
                } else {
                    SmartDashboard.putBoolean("SelfTest/Carriage/Amps/Passed", true);
                    DriverStation.reportError("Amp draw passed on carriage.  Expected " + _targetLiftingAmps + " and measured  " + _maxAmps + ".", false);
                }
                _state = _pass ? State.BOTTOM : State.DONE;
                break;
            case BOTTOM:
                _carriage.setSetpoint(Constants.Carriage.ENCODER_BOTTOM_PROTO);
                _carriage.enable();
                _endMillis = System.currentTimeMillis() + _maxBottomMillis;
                _state = State.BOTTOMWAIT;
                break;
            case BOTTOMWAIT:
                if (_carriage.onTarget()) {
                    DriverStation.reportError("Carriage reached bottom.", false);
                    _carriage.disable();
                    _carriage.drive(0);
                    _pass = false;
                    _state = State.MIDDLE;
                } else if (System.currentTimeMillis() > _endMillis) {
                    DriverStation.reportError("Carriage didn't reach bottom within " + _maxBottomMillis + "ms.", false);
                    _carriage.disable();
                    _carriage.drive(0);
                    _pass = false;
                    _state = State.DONE;
                }
                break;
            case MIDDLE:
                _carriage.setSetpoint(Constants.Carriage.ENCODER_MIDDLE_PROTO);
                _carriage.enable();
                _endMillis = System.currentTimeMillis() + _maxMiddleMillis;
                _state = State.MIDDLEWAIT;
                break;
            case MIDDLEWAIT:
                if (_carriage.onTarget()) {
                    DriverStation.reportError("Carriage reached middle.", false);
                    _carriage.disable();
                    _carriage.drive(0);
                    _pass = false;
                    _state = State.WAIT;
                } else if (System.currentTimeMillis() > _endMillis) {
                    DriverStation.reportError("Carriage didn't reach middle within " + _maxMiddleMillis + "ms.", false);
                    _carriage.disable();
                    _carriage.drive(0);
                    _pass = false;
                    _state = State.DONE;
                }
                break;
            case WAIT:
                _carriage.disable();
                _carriage.drive(0);
                if (System.currentTimeMillis() > _endMillis) {
                    _state = State.DONE;
                }
                break;
        }


    }

    @Override
    protected void end() {
        _carriage.drive(0, true);
    }

    @Override
    protected boolean isFinished() {
        return _state == State.DONE;
    }

    private enum State {
        ZERO,
        ZERODONE,
        BOTTOM,
        BOTTOMWAIT,
        MIDDLE,
        MIDDLEWAIT,
        WAIT,
        DONE
    }

}
