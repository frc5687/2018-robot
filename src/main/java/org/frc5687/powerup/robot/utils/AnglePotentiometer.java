package org.frc5687.powerup.robot.utils;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/**
 * Created by Ben Bernard on 2/15/2018.
 */
public class AnglePotentiometer implements PIDSource {
    private double _bottomValue;
    private double _minAngle;
    private AnalogPotentiometer _potentiometer;
    private double _scale;

    public AnglePotentiometer(int channel, double bottomAngle, double bottomValue, double topAngle, double topValue) {
        _scale = (topAngle - bottomAngle) / topValue - bottomValue ;
        _minAngle = bottomAngle;
        _bottomValue = bottomValue;

        _potentiometer =  new AnalogPotentiometer(channel);
    }

    public double get() {
        return _minAngle + (_potentiometer.get() - _bottomValue) * _scale;
    }

    public double getRaw() {
        return _potentiometer.get();
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return PIDSourceType.kDisplacement;
    }

    @Override
    public double pidGet() {
        return get();
    }
}
