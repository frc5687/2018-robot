package org.frc5687.powerup.robot.utils;

/**
 * Created by Ben Bernard on 3/14/2018.
 */
import edu.wpi.first.wpilibj.Joystick;

/**
 * Utility class to make it easier to set (and read) the state of a joystick light
 */
public class JoystickLight {

    private final Joystick m_joystick;
    private final int m_outputNumber;
    private boolean m_value = false;

    /**
     * Create a joystick button for triggering commands.
     *
     * @param joystick     The Joystick object that has the lights (e.g. Joystick, Launchpad, etc)
     * @param outputNumber The output number
     */
    public JoystickLight(Joystick joystick, int outputNumber) {
        m_joystick = joystick;
        m_outputNumber = outputNumber;
    }

    /**
     * Gets the value of the joystick button.
     *
     * @return The value of the joystick button
     */
    public boolean get() {
        return m_value;
    }

    public void set(boolean value) {
        m_joystick.setOutput(m_outputNumber, m_value = value);
    }

}