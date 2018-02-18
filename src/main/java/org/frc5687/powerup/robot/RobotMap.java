package org.frc5687.powerup.robot;


import org.frc5687.powerup.robot.Constants;

public class RobotMap {

    public static final int IDENTITY_FLAG = 9;

    public class DriveTrain {
        public static final int LEFT_FRONT_MOTOR = PWM.Rio3;
        public static final int LEFT_REAR_MOTOR = PWM.Rio4;
        public static final int RIGHT_FRONT_MOTOR = PWM.Rio1;
        public static final int RIGHT_REAR_MOTOR = PWM.Rio2;

        public static final int LEFT_ENCODER_CHANNEL_A = DIO.Rio7;
        public static final int LEFT_ENCODER_CHANNEL_B = DIO.Rio8;
        public static final int RIGHT_ENCODER_CHANNEL_A = DIO.Rio5;
        public static final int RIGHT_ENCODER_CHANNEL_B = DIO.Rio6;
	}

    public class Intake {
        public static final int LEFT_MOTOR = PWM.Rio5 ;
        public static final int RIGHT_MOTOR = PWM.Rio7 ;
        public static final int IR_SIDE = ANALOG.Rio0; // Analog
        public static final int IR_BACK = ANALOG.Rio1; // Analog
        public static final int SERVO = PWM.Rio0;
    }

    public class Carriage {
        public static final int MOTOR = PWM.Rio8;
        public static final int ENCODER_A = DIO.Rio0; // YELLOW
        public static final int ENCODER_B = DIO.Rio1; // BLUE

        public static final int HALL_EFFECT_TOP = DIO.Rio2; // DIO
        public static final int HALL_EFFECT_BOTTOM = DIO.Rio3; // DIO
    }

    public class Arm {
        public static final int MOTOR = PWM.Rio6;
        public static final int HALL_EFFECT_STARTING_POSITION = DIO.Rio4;
        public static final int STARTING_POSITION_LED = 10;
        public static final int ENCODER_A = DIO.NavX12;
        public static final int ENCODER_B = DIO.NavX11;
        public static final int POTENTIOMETER = ANALOG.NavX4;
    }

    public class Climber {
        public static final int MOTOR = PWM.Rio9;
    }

    public class AutoChooser {
        public static final int POSITION_SWITCH = ANALOG.Rio2;
        public static final int MODE_SWITCH = ANALOG.Rio3;
        public static final int DELAY_SWITCH = ANALOG.NavX5;
    }

    public static class PWM {
        public static final int Rio0 = 0;
        public static final int Rio1 = 1;
        public static final int Rio2 = 2;
        public static final int Rio3 = 3;
        public static final int Rio4 = 4;
        public static final int Rio5 = 5;
        public static final int Rio6 = 6;
        public static final int Rio7 = 7;
        public static final int Rio8 = 8;
        public static final int Rio9 = 9;
        public static final int NavX10 = 10;
        public static final int NavX11 = 11;
        public static final int NavX12 = 12;
        public static final int NavX13 = 13;
        public static final int NavX14 = 14;
        public static final int NavX15 = 15;
        public static final int NavX16 = 16;
        public static final int NavX17 = 17;
        public static final int NavX18 = 18;
        public static final int NavX19 = 19;
    }

    public static class DIO {
        public static final int Rio0 = 0;
        public static final int Rio1 = 1;
        public static final int Rio2 = 2;
        public static final int Rio3 = 3;
        public static final int Rio4 = 4;
        public static final int Rio5 = 5;
        public static final int Rio6 = 6;
        public static final int Rio7 = 7;
        public static final int Rio8 = 8;
        public static final int Rio9 = 9;
        public static final int NavX10 = 10;
        public static final int NavX11 = 11;
        public static final int NavX12 = 12;
        public static final int NavX13 = 13;
        public static final int NavX14 = 14;
        public static final int NavX15 = 15;
        public static final int NavX16 = 16;
        public static final int NavX17 = 17;
        public static final int NavX18 = 18;
        public static final int NavX19 = 19;
    }

    public static class ANALOG {
        public static final int Rio0 = 0;
        public static final int Rio1 = 1;
        public static final int Rio2 = 2;
        public static final int Rio3 = 3;
        public static final int NavX4 = 4;
        public static final int NavX5 = 5;
        public static final int NavX6 = 6;
        public static final int NavX7 = 7;
    }

}
