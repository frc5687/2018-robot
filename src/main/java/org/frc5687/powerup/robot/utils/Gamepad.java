package org.frc5687.powerup.robot.utils;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Copied by Caleb on 1/13/2017.
 */

/**
 * Handle input from standard Joysticks connected to the Driver Station. This
 * class handles standard input that comes from the Driver Station. Each time a
 * value is requested the most recent value is returned. There is a single class
 * instance for each joystick and the mapping of ports to hardware buttons
 * depends on the code in the driver station.
 */
public class Gamepad extends Joystick {
    /**
     * Enumeration for the various analog axes
     */
    public static enum Axes {
        LEFT_X(0),
        LEFT_Y(1),
        LEFT_TRIGGER(2),
        RIGHT_TRIGGER(3),
        RIGHT_X(4),
        RIGHT_Y(5),
        D_PAD_HORIZONTAL(6),
        D_PAD_VERTICAL(7);

        private final int number;
        Axes(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }
    }

    /**
     * Enumeration for the various buttons
     */
    public static enum Buttons {
        A(1),
        B(2),
        X(3),
        Y(4),
        LEFT_BUMPER(5),
        RIGHT_BUMPER(6),
        BACK(7),
        START(8),
        LEFT_STICK(9),
        RIGHT_STICK(10);

        private final int number;
        Buttons(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }
    }

    /**
     * Constructor
     * @param port the driver station port the gamepad is connected to
     */
    public Gamepad(int port) {
        super(port);
    }

    /**
     * Gets the raw value for the specified axis
     * @param axis the desired gamepad axis
     * @return double the analog value for the axis
     */
    public double getRawAxis(Axes axis) {
        return super.getRawAxis(axis.getNumber());
    }

    /**
     * Checks if the specified button is pressed
     * @param button the desired gamepad button
     * @return bool true if the button is pressed
     */
    public boolean getRawButton(Buttons button) {
        return super.getRawButton(button.getNumber());
    }
}
