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
    }

    public class Carriage {
        public static final double DEADBAND = 0.05;
    }

    public class Arm {
        public static final int ENCODER_BOTTOM = 0;
        public static final int ENCODER_MIDDLE = 133;
        public static final int ENCODER_TOP = 350;
    }

}
