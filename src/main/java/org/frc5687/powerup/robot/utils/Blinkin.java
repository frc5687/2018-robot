package org.frc5687.powerup.robot.utils;

import edu.wpi.first.wpilibj.Spark;

/**
 * Created by Ben Bernard on 2/14/2018.
 */
public class Blinkin extends Spark {

    /**
     * Constructor.
     *
     * @param channel The PWM channel that the SPARK is attached to. 0-9 are on-board, 10-19 are on
     *                the MXP port
     */
    public Blinkin(int channel) {
        super(channel);
    }
}
