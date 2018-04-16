package org.frc5687.powerup.robot.utils;

/**
 * Created by Ben Bernard on 4/2/2018.
 */
public class MotorHealthChecker {
    private boolean _isHealthy;
    private PDP _pdp;
    private int _pdpPort;
    private double _minSpeed;
    private double _minCurrent;
    private int _healthCheckCycles;
    private int _healthCheckCount;


    public MotorHealthChecker(double minSpeed, double minCurrent, int healthCheckCycles, PDP pdp, int pdpPort) {
        _minCurrent = minCurrent;
        _minSpeed = minSpeed;
        _healthCheckCycles = healthCheckCycles;
        _isHealthy = true;
        _healthCheckCount = _healthCheckCycles;
        _pdp = pdp;
        _pdpPort = pdpPort;
    }

    public void checkHealth(double speed) {
        double current = _pdp.getCurrent(_pdpPort);
        if (Math.abs(speed) > _minSpeed) {
            if (current > _minCurrent) {
                _isHealthy = true;
                _healthCheckCount = _healthCheckCycles;
            } else {
                _healthCheckCount--;
                if (_healthCheckCount <= 0) {
                    _isHealthy = false;
                    _healthCheckCount = 0;
                }
            }
        }
    }

    public boolean IsHealthy() {
        return _isHealthy;
    }

}
