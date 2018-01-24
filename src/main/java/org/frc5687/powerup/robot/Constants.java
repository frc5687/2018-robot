package org.frc5687.powerup.robot;

public class Constants {

    public class DriveTrain {
        public static final double DEADBAND = 0.15;
        public static final boolean LEFT_MOTORS_INVERTED = true;
        public static final boolean RIGHT_MOTORS_INVERTED = false;
    }

    public class Intake {
        public static final boolean LEFT_MOTORS_INVERTED = true;
        public static final boolean RIGHT_MOTORS_INVERTED = false;
    }

    public class InfraRedConstants {
        public static final int DETECTION_THRESHOLD = 600;
        public static final int CAPTURED_OPTIMAL = 1400; // Optimal position of boulder for storing bolder and traversing defenses
        public static final int CAPTURED_TOLERANCE = 100; // Tolerance in either direction of boulder position for carrying boulder
    }

}
