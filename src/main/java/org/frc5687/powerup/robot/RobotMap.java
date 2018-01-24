package org.frc5687.powerup.robot;

public class RobotMap {

    public class DriveTrain {
        public static final int LEFT_FRONT_MOTOR = 3;
        public static final int LEFT_REAR_MOTOR = 4;
        public static final int RIGHT_FRONT_MOTOR = 0;
        public static final int RIGHT_REAR_MOTOR = 1;
    }

    public class Intake {
        public static final int LEFT_MOTOR = 5 ;
        public static final int RIGHT_MOTOR = 7 ;
        public static final int IR_SIDE = 0; // Analog
        public static final int IR_BACK = 1; // Analog
    }

    public class Carriage {
        public static final int MOTOR = 8;

        public static final int ENCODER_A = 0; // DIO
        public static final int ENCODER_B = 1; // DIO

        public static final int HALL_EFFECT_TOP = 2; // DIO
        public static final int HALL_EFFECT_BOTTOM = 3; // DIO
    }
    public class Arm {
        public static final int MOTOR = 6;
    }

}
