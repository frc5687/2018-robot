package org.frc5687.powerup.robot.utils;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Created by Ben Bernard on 3/15/2018.
 */
public class AxisButton extends Button {
    private final GenericHID m_joystick;
    private final int m_axisNumber;
    private double _minRange;
    private double _maxRange;

    public AxisButton(GenericHID joystick, int axisNumber, double minRange, double maxRange) {
        this.m_joystick = joystick;
        this.m_axisNumber = axisNumber;
        _minRange = minRange;
        _maxRange = maxRange;
    }

    public boolean get() {
        double val = this.m_joystick.getRawAxis(this.m_axisNumber);
        return val >= _minRange && val <= _maxRange;
    }


}
