package org.frc5687.powerup.robot;

public class RobotMap {


    public class DriveTrain {
        public static final int LEFT_FRONT_MOTOR = 3;
        public static final int LEFT_REAR_MOTOR = 4;
        public static final int RIGHT_FRONT_MOTOR = 1;
        public static final int RIGHT_REAR_MOTOR = 2;

        public static final int LEFT_ENCODER_CHANNEL_A = 7;
        public static final int LEFT_ENCODER_CHANNEL_B = 8;
        public static final int RIGHT_ENCODER_CHANNEL_A = 5;
        public static final int RIGHT_ENCODER_CHANNEL_B = 6;
	}

    public class Intake {
        public static final int LEFT_MOTOR = 5 ;
        public static final int RIGHT_MOTOR = 7 ;
        public static final int IR_SIDE = 0; // Analog
        public static final int IR_BACK = 1; // Analog
        public static final int SERVO = 0;
    }

    public class Carriage {
        public static final int MOTOR = 8;
        public static final int ENCODER_A = 0; // YELLOW
        public static final int ENCODER_B = 1; // BLUE

        public static final int HALL_EFFECT_TOP = 2; // DIO
        public static final int HALL_EFFECT_BOTTOM = 3; // DIO
    }

    public class Arm {
        public static final int MOTOR = 6;
        public static final int HALL_EFFECT_STARTING_POSITION = 4;
        public static final int STARTING_POSITION_LED = 10;
        public static final int ENCODER_A = 11;
        public static final int ENCODER_B = 12;
    }

    public class Climber {
        public static final int MOTOR = 9;
    }

}
