package org.frc5687.powerup.robot.utils;

/**
 * Created by Ben Bernard on 4/2/2018.
 */
public class MotorHealthChecker {
    private boolean _isMotorHealthy;
    private boolean _isEncoderHealthy;
    private PDP _pdp;
    private int _pdpPort;
    private double _minSpeed;
    private double _minCurrent;
    private int _healthCheckCycles;
    private int _motorHealthCheckCount;
    private int _encoderHealthCheckCount;

    double _minForwardSpeed = 0;
    double _minReverseSpeed = 0;
    double _minForwardRate = 0;
    double _minReverseRate = 0;


    public MotorHealthChecker(double minSpeed, double minCurrent, int healthCheckCycles, PDP pdp, int pdpPort,
                              double minForwardSpeed, double minForwardRate) {
        this(minSpeed, minCurrent, healthCheckCycles, pdp, pdpPort, minForwardSpeed, -minForwardSpeed, minForwardRate, -minForwardRate);
    }

    public MotorHealthChecker(double minSpeed, double minCurrent, int healthCheckCycles, PDP pdp, int pdpPort,
                              double minForwardSpeed, double minReverseSpeed, double minForwardRate, double minReverseRate) {
        this(minSpeed, minCurrent, healthCheckCycles, pdp, pdpPort);
        _minForwardSpeed = minForwardSpeed;
        _minForwardRate = minForwardRate;
        _minReverseSpeed = minReverseSpeed;
        _minReverseRate = minReverseRate;

    }

    public MotorHealthChecker(double minSpeed, double minCurrent, int healthCheckCycles, PDP pdp, int pdpPort) {
        _minCurrent = minCurrent;
        _minSpeed = minSpeed;
        _healthCheckCycles = healthCheckCycles;
        _isMotorHealthy = true;
        _motorHealthCheckCount = _healthCheckCycles;
        _pdp = pdp;
        _pdpPort = pdpPort;
    }

    public void checkHealth(double speed, double encoderRate) {
        checkHealth(speed);

        if (_minForwardSpeed==0 || _minForwardRate==0) { return; }

        // Check the encoder...
        Boolean rateOkay = null;
        if (speed > _minForwardSpeed) {
            rateOkay = new Boolean(encoderRate > _minForwardRate);
        } else if (speed < _minReverseSpeed) {
            rateOkay = new Boolean(encoderRate < _minReverseRate);
        }

        if (rateOkay != null) {
            if (rateOkay.booleanValue())
                _isEncoderHealthy = true;
            _encoderHealthCheckCount = _healthCheckCycles;
        } else {
            _encoderHealthCheckCount--;
            if (_encoderHealthCheckCount <= 0) {
                _isEncoderHealthy = false;
                _encoderHealthCheckCount = 0;
            }
        }
    }

    public void checkHealth(double speed) {
        double current = _pdp.getCurrent(_pdpPort);
        if (Math.abs(speed) > _minSpeed) {
            if (current > _minCurrent) {
                _isMotorHealthy = true;
                _motorHealthCheckCount = _healthCheckCycles;
            } else {
                _motorHealthCheckCount--;
                if (_motorHealthCheckCount <= 0) {
                    _isMotorHealthy = false;
                    _motorHealthCheckCount = 0;
                }
            }
        }
    }

    public boolean IsMotorHealthy() {
        return _isMotorHealthy;
    }

}
