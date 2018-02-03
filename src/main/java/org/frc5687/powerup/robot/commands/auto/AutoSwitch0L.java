package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.subsystems.Arm;
import org.frc5687.powerup.robot.subsystems.Carriage;
import org.frc5687.powerup.robot.subsystems.DriveTrain;

public class AutoSwitch0L extends Command {
    private DriveTrain _driveTrain;
    private Carriage _carriage;
    private Arm _arm;

    private PIDListener _distancePID;
    private PIDController _distanceController;
    private PIDListener _anglePID;
    private PIDController _angleController;

    private long _endMillis;
    private long _maxMillis;

    private double _angle;
    private double _speed;
    private double _distance = 168;

    public AutoSwitch0L(DriveTrain driveTrain, Carriage carriage, Arm arm, double angle, double speed, long maxMillis) {
        requires(driveTrain);
        requires(carriage);
        requires(arm);
        _driveTrain = driveTrain;
        _carriage = carriage;
        _arm = arm;
        _angle = angle;
        _speed = speed;
        _maxMillis = maxMillis;
    }

    @Override
    protected void initialize() {
        this._endMillis = _maxMillis == 0 ? Long.MAX_VALUE : System.currentTimeMillis() + _maxMillis;
        _driveTrain.resetDriveEncoders();
        _distancePID = new PIDListener();
        SmartDashboard.putNumber("AutoSwitch0L/kP", Constants.Auto.Drive.EncoderPID.kP);
        SmartDashboard.putNumber("AutoSwitch0L/kI", Constants.Auto.Drive.EncoderPID.kI);
        SmartDashboard.putNumber("AutoSwitch0L/kD", Constants.Auto.Drive.EncoderPID.kD);
        SmartDashboard.putNumber("AutoSwitch0L/kT", Constants.Auto.Drive.EncoderPID.TOLERANCE);
        _distanceController = new PIDController(Constants.Auto.Drive.EncoderPID.kP, Constants.Auto.Drive.EncoderPID.kI, Constants.Auto.Drive.EncoderPID.kD, _driveTrain, _distancePID);
        _distanceController.setAbsoluteTolerance(Constants.Auto.Drive.EncoderPID.TOLERANCE);
        _distanceController.setOutputRange(-_speed, _speed);
        _distanceController.setSetpoint(_distance);
        _distanceController.enable();

        _anglePID = new PIDListener();
        _angleController = new PIDController(Constants.Auto.Drive.AnglePID.kP, Constants.Auto.Drive.AnglePID.kI, Constants.Auto.Drive.AnglePID.kD, _driveTrain, _anglePID);
        _angleController.setInputRange(Constants.Auto.MIN_IMU_ANGLE, Constants.Auto.MAX_IMU_ANGLE);
        double maxSpeed = _speed * Constants.Auto.Drive.AnglePID.MAX_DIFFERENCE;
        SmartDashboard.putNumber("AutoSwitch0L/angleMaxSpeed", maxSpeed);
        SmartDashboard.putNumber("AutoSwitch0L/setPoint", _driveTrain.getYaw());
        _angleController.setOutputRange(-maxSpeed, maxSpeed);
        _angleController.setContinuous();
        // If an angle is supplied, use that as our setpoint.  Otherwise get the current heading and stick to it!
        _angleController.setSetpoint(_angle == 1000 ? _driveTrain.getYaw() : _angle);
        _angleController.enable();

    }

    @Override
    protected void execute() {
        double distanceFactor = 0;
        double angleFactor = 0;
        synchronized (_distancePID) {
            distanceFactor = _distancePID.get();
        }
        synchronized (_anglePID) {
            angleFactor = _anglePID.get();
        }
        SmartDashboard.putNumber("AutoSwitch0L/distanceFactor", distanceFactor);
        SmartDashboard.putNumber("AutoSwitch0L/angleFactor", angleFactor);

        SmartDashboard.putBoolean("AutoSwitch0L/onTarget", _distanceController == null ? false : _distanceController.onTarget());
        SmartDashboard.putNumber("AutoSwitch0L/imu", _driveTrain.getYaw());
        SmartDashboard.putNumber("AutoSwitch0L/distance", _driveTrain.pidGet());
        SmartDashboard.putNumber("AutoSwitch0L/turnPID", _anglePID.get());
    }

    @Override
    protected void end() {
        DriverStation.reportError(String.format("AutoSwitch0L Finished (%s, %s)", _driveTrain.getDistance(), (_driveTrain.getYaw() - _angleController.getSetpoint())), false);
        _angleController.disable();
        if (_distanceController != null) {
            _distanceController.disable();
        }
        _driveTrain.tankDrive(0, 0);
    }

    @Override
    protected boolean isFinished() {
        if (_maxMillis>0 && _endMillis!=Long.MAX_VALUE && System.currentTimeMillis() > _endMillis) {
            DriverStation.reportError("AutoDrive for " + _maxMillis + " timed out.", false);
            return true;
        }
        return _distanceController.onTarget();
    }

    private class PIDListener implements PIDOutput {

        private double value;

        public double get() {
            return value;
        }

        @Override
        public void pidWrite(double output) {
            synchronized (this) {
                value = output;
            }
        }

    }
}
