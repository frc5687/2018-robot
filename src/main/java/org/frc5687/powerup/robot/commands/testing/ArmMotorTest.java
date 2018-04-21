package org.frc5687.powerup.robot.commands.testing;

import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.subsystems.Arm;
import org.frc5687.powerup.robot.subsystems.Carriage;
import org.frc5687.powerup.robot.utils.PDP;
import org.frc5687.powerup.robot.commands.MoveCarriageToSetpointPID;
import edu.wpi.first.wpilibj.DriverStation;

import static java.lang.Math.abs;


public class ArmMotorTest extends Command {
    private Arm _arm;
    private Carriage _carriage;
    private PDP _pdp;
    private double _runSpeed;
    private double _runMillis;
    private double _targetAmps;
    private double _targetTicks;
    private double _endMillis;
    private int _targetTicks;
    private double _maxAmps = 0;
    private Lights _lights;
    private double _currentAngle;
    private double _increacedAngle;
    public double _decreacedAngle;

    public ArmMotorTest(Arm arm, PDP pdp, Carriage carriage, Lights lights) {
        requires(arm);
        requires(carriage);

        _arm = arm;
        _pdp = pdp;
        _runSpeed = 1.0;
        _runMillis = 500;
        _targetAmps = 3;
        _targetTicks = 16000;
        _lights = lights;


    }

    @Override
    protected void initialize() {
        DriverStation.reportError("Startig arm self test", false);
        _state = State.MoveUp;
        _maxAmps = 0;
        _endMillis = System.currentTimeMillis() + _runMillis;
        _arm.zeroEncoder;
        _currentAngle = _arm.getAngle();
    }
    boolean pass=true;


    protected void execute() {

        switch (_state) {
            case MoveUp:
                _lights.setBoth(Constants.Lights.TEST_RUNNING, Constants.Lights.TEST_RUNNING);
                _arm.drive(0.2)
                _maxAmps = Math.max(_maxAmps, _pdp.getCurrent(RobotMap.PDP.ARM_SP));
                if (System.currentTimeMillis() > _endMillis) {
                    _arm.drive(0);
                    _state = State.MOVEUPDONE;
                }
                break;
            case MOVEUPDONE:
                _arm.drive(0);
                report("Move arm up");
                _increacedAngle = _arm.getangle();
                _state = State.MOVEDOWN;
                break;
            case MOVEDOWN:
                _arm.drive(-0.2)
                _maxAmps = Math.max(_maxAmps, _pdp.getCurrent(RobotMap.PDP.ARM_SP));
                if (System.currentTimeMillis() > _endMillis) {
                    _arm.drive(0);
                    _state = State.MOVEDOWNDONE;
                }
                break;
            case MOVEDOWNDONE:
                _arm.drive(0);
                report("Move arm down");
                _state = State.MOVETOSTART;
                _arm.setSetpoint(Constants.Arm.Encoder.ENCODER_START);
                break;
            case MOVETOSTART:
                if (_arm.isOnTarget()) {
                    _state = State.MOVETOTOP;
                }
                break;
            case MOVETOTOP:
                if(arm.isOnTarget()){
                    report("left front", _driveTrain.getLeftTicks());
                    _state = State.DONE;
                }
                break;
        }

    }
    private void report(String side) {
        if (_maxAmps < _targetAmps) {
            pass = false;
            SmartDashboard.putBoolean("SelfTest/Arm/" + side + "/Amps/Passed", false);
            DriverStation.reportError("Target amperage not reached on " + side  + ".  Expected " + _targetAmps + " but measured " + _maxAmps + ".", false);
        } else {
            SmartDashboard.putBoolean("SelfTest/Arm/" + side + "/Amps/Passed", true);
            DriverStation.reportError("Amp draw passed on " + side  + ".  Expected " + _targetAmps + " and measured  " + _maxAmps + ".", false);
        }
        SmartDashboard.putNumber("SelfTest/Arm/" + side + "/Amps/Measured", _maxAmps);
        _arm.zeroEncodert();
        _maxAmps = 0;
        _endMillis = System.currentTimeMillis() + _runMillis;
    }
    @Override
    protected void end() {
        _arm.drive(0);
    }

    @Override
    protected boolean isFinished() {
        return _state == State.DONE;
    }


    private enum State {
        MoveUp,
        MOVEUPDONE,
        MOVEDOWN,
        MOVEDOWNDONE,
        MOVETOSTART,
        MOVETOTOP,
        DONE
    }