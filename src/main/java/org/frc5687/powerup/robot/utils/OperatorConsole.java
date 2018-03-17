package org.frc5687.powerup.robot.utils;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Created by Ben Bernard on 3/14/2018.
 */
public class OperatorConsole extends Joystick {

    public static enum Axes {
        A(0),
        B(1),
        C(2),
        D(3);

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
        C(3),
        D(4),
        E(5),
        F(6),
        G(7),
        H(8),
        I(9),
        J(10),
        K(11),
        L(12),
        M(13),
        N(14);

        private final int number;
        Buttons(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }
    }

    /**
     * Enumeration for the various buttons
     */
    public static enum LEDs {
        A(1),
        B(2),
        C(3),
        D(4),
        E(5),
        F(6),
        G(7),
        H(8),
        I(9),
        J(10),
        K(11);

        private final int number;
        LEDs(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }
    }


    /**
     * Construct an instance of a joystick. The joystick index is the USB port on the drivers
     * station.
     *
     * @param port The port on the Driver Station that the joystick is plugged into.
     */
    public OperatorConsole(int port) {
        super(port);
    }


}
