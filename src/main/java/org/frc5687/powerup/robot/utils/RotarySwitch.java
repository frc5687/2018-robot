package org.frc5687.powerup.robot.utils;

/**
 * Created by Ben Bernard on 2/2/2018.
 */
import edu.wpi.first.wpilibj.AnalogPotentiometer;

/**
 * Created by Ben Bernard on 2/25/2017.
 */
public class RotarySwitch {

    private int _positions;
    private int _port;
    private double _tolerance;

    private double[] _values;
    private AnalogPotentiometer _pot;

    public RotarySwitch(int analogPort, double tolerance, double... values) {
        _port= analogPort;
        _positions = values.length;
        _tolerance = tolerance;
        _pot = new AnalogPotentiometer(_port);

        _values = new double[_positions];

        for (int i = 0; i<values.length; i++) {
            _values[i] = values[i];
        }

    }


    public int get() {
        double value = _pot.get();
        for (int i = 0; i < _positions; i++) {
            if (Math.abs(value - _values[i])<_tolerance) {
                return i;
            }
        }
        return -1;
    }

    public double getRaw() {
        return _pot.get();
    }



}
