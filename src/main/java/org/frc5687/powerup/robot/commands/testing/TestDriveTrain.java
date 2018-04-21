package org.frc5687.powerup.robot.commands.testing;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.subsystems.DriveTrain;
import org.frc5687.powerup.robot.subsystems.Lights;
import org.frc5687.powerup.robot.utils.PDP;

/**
 * Created by Ben Bernard on 4/20/2018.
 */
public class TestDriveTrain extends Command {
    private static double kTOLERANCE = 0.25;
    private double _runSpeed;
    private int _runMillis;
    private double _targetAmps;
    private int _targetTicks;

    private State _state = State.RIGHTFRONT;
    private long _endMillis;

    private double _maxAmps = 0;

    private DriveTrain _driveTrain;
    private PDP _pdp;
    private Lights _lights;

    public TestDriveTrain(DriveTrain driveTrain, PDP pdp, Lights lights) {
        requires(driveTrain);

        _driveTrain = driveTrain;
        _pdp = pdp;
        _lights = lights;

        _runSpeed = 1.0;
        _runMillis = 500;
        _targetAmps = 3;
        _targetTicks = 16000;


    }

    @Override
    protected void initialize() {
        DriverStation.reportError("Starting drivetrain test", false);
        _state = State.RIGHTFRONT;
        _maxAmps = 0;
        _endMillis = System.currentTimeMillis() + _runMillis;
        _driveTrain.resetDriveEncoders();
    }
    boolean pass=true;


    protected void execute() {

        switch (_state) {
            case RIGHTFRONT:
                _lights.setBoth(Constants.Lights.TEST_RUNNING, Constants.Lights.TEST_RUNNING);
                _driveTrain.setPower(0, _runSpeed);
                _maxAmps = Math.max(_maxAmps, _pdp.getCurrent(RobotMap.PDP.RIGHT_FRONT_SRX));
                if (System.currentTimeMillis() > _endMillis) {
                    _driveTrain.setPower(0, 0);
                    _state = State.RIGHTFRONTDONE;
                }
                break;
            case RIGHTFRONTDONE:
                _driveTrain.setPower(0, 0);
                report("right front", _driveTrain.getRightTicks());
                _state = State.RIGHTREAR;
                break;
            case RIGHTREAR:
                _driveTrain.setPower(0, _runSpeed);
                _maxAmps = Math.max(_maxAmps, _pdp.getCurrent(RobotMap.PDP.RIGHT_REAR_SPX));
                if (System.currentTimeMillis() > _endMillis) {
                    _driveTrain.setPower(0, 0);
                    _state = State.RIGHTREARDONE;
                }
                break;
            case RIGHTREARDONE:
                _driveTrain.setPower(0, 0);
                report("right rear", _driveTrain.getRightTicks());
                _state = State.LEFTFRONT;
                break;

            case LEFTFRONT:
                _driveTrain.setPower(_runSpeed, 0);
                _maxAmps = Math.max(_maxAmps, _pdp.getCurrent(RobotMap.PDP.LEFT_FRONT_SRX));
                if (System.currentTimeMillis() > _endMillis) {
                    _driveTrain.setPower(_runSpeed, 0);
                    _state = State.LEFTFRONTDONE;
                }
                break;
            case LEFTFRONTDONE:
                _driveTrain.setPower(0, 0);
                report("left front", _driveTrain.getLeftTicks());
                _state = State.LEFTREAR;
                break;
            case LEFTREAR:
                _driveTrain.setPower(_runSpeed, 0);
                _maxAmps = Math.max(_maxAmps, _pdp.getCurrent(RobotMap.PDP.LEFT_REAR_SPX));
                if (System.currentTimeMillis() > _endMillis) {
                    _driveTrain.setPower(0, 0);
                    _state = State.LEFTREARDONE;
                }
                break;
            case LEFTREARDONE:
                _driveTrain.setPower(0, 0);
                report("left rear", _driveTrain.getLeftTicks());
                _state = State.WAIT;
                break;
            case WAIT:
                _driveTrain.setPower(0, 0);
                if (System.currentTimeMillis() > _endMillis) {
                    _state = State.DONE;
                }
                break;
        }

    }
    private void report(String side, long ticks) {
        if (_maxAmps < _targetAmps) {
            pass = false;
            SmartDashboard.putBoolean("SelfTest/Drivetrain/" + side + "/Amps/Passed", false);
            DriverStation.reportError("Target amperage not reached on " + side  + ".  Expected " + _targetAmps + " but measured " + _maxAmps + ".", false);
        } else {
            SmartDashboard.putBoolean("SelfTest/Drivetrain/" + side + "/Amps/Passed", true);
            DriverStation.reportError("Amp draw passed on " + side  + ".  Expected " + _targetAmps + " and measured  " + _maxAmps + ".", false);
        }
        SmartDashboard.putNumber("SelfTest/Drivetrain/" + side + "/Amps/Measured", _maxAmps);
        if (ticks < _targetTicks) {
            pass = false;
            SmartDashboard.putBoolean("SelfTest/Drivetrain/" + side + "/Ticks/Passed", false);
            DriverStation.reportError("Target ticks not reached on " + side + ".  Expected " + _targetTicks + " but measured " + ticks + ".", false);
        } else {
            SmartDashboard.putBoolean("SelfTest/Drivetrain/" + side + "/Ticks/Passed", true);
            DriverStation.reportError("Target ticks reached on " + side + ".  Expected " + _targetTicks + " and measured " + ticks + ".", false);
        }
        _lights.setBoth(pass ? Constants.Lights.TEST_PASSED : Constants.Lights.TEST_FAILED);

        SmartDashboard.putNumber("SelfTest/Drivetrain/" + side + "/Ticks/Measured", ticks);
        _driveTrain.resetDriveEncoders();
        _maxAmps = 0;
        _endMillis = System.currentTimeMillis() + _runMillis;
    }
    @Override
    protected void end() {
        _driveTrain.setPower(0, 0);
    }

    @Override
    protected boolean isFinished() {
        return _state == State.DONE;
    }


        private enum State {
            RIGHTFRONT,
            RIGHTFRONTDONE,
            RIGHTREAR,
            RIGHTREARDONE,
            LEFTFRONT,
            LEFTFRONTDONE,
            LEFTREAR,
            LEFTREARDONE,
            WAIT,
            DONE
        }

}
