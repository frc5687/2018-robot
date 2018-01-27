package org.frc5687.powerup.robot;

public class Constants {

    public class DriveTrain {
        public static final double DEADBAND = 0.15;
        public static final boolean LEFT_MOTORS_INVERTED = true;
        public static final boolean RIGHT_MOTORS_INVERTED = false;
    }

    public class Intake {
        public static final double DEADBAND = 0.05;
        public static final boolean LEFT_MOTORS_INVERTED = true;
        public static final boolean RIGHT_MOTORS_INVERTED = false;
        public static final double SERVO_BOTTOM = 0.48;
        public static final double SERVO_UP = 0.77;
        public static final double SERVO_CLIMB_POSITION = 0.08;
    }

    public class Carriage {
        public static final double DEADBAND = 0.05;
    }

}
