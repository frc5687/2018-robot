package org.frc5687.powerup.robot.commands.testing;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.frc5687.powerup.robot.OI;
/**
 * Command to prompt the operator to confirm a result using the gamepad buttons.
 */
public class ConfirmTest extends Command {

    private String _message;
    private String _success;
    private String _failure;
    private boolean _clear;
    private String _key;
    private OI _oi;

    public ConfirmTest(OI oi, String message, String success, String failure) {
        _oi = oi;
        _message = message;
        _success = success;
        _failure = failure;
        _clear = false;
    }

    public ConfirmTest(OI oi,  String message, String key) {
        _oi = oi;
        _message = message;
        _key = key;
        _clear = false;
    }

    @Override
    protected void initialize() {
        DriverStation.reportError(_message, false);
        SmartDashboard.putString("SelfTest/Message", _message);
    }

    @Override
    protected void execute() {
        if (!_clear) {
            _clear = !_oi.isStartPressed();
        }
    }

    @Override
    protected boolean isFinished() {
        if (_clear) {
            if (_oi.isStartPressed()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void end() {
    }
}