package org.frc5687.powerup.robot;

public class RobotMap {

    public static final int IDENTITY_FLAG = 9;

    public class Lights {
        public static final int LEFT = PWM.navX18;
        public static final int RIGHT = PWM.navX19;
    }

    public class DriveTrain {
        public static final int LEFT_FRONT_MOTOR = PWM.RIO3;
        public static final int LEFT_REAR_MOTOR = PWM.RIO4;
        public static final int RIGHT_FRONT_MOTOR = PWM.RIO1;
        public static final int RIGHT_REAR_MOTOR = PWM.RIO2;

        public static final int LEFT_ENCODER_CHANNEL_A = DIO.RIO7;
        public static final int LEFT_ENCODER_CHANNEL_B = DIO.RIO8;
        public static final int RIGHT_ENCODER_CHANNEL_A = DIO.RIO5;
        public static final int RIGHT_ENCODER_CHANNEL_B = DIO.RIO6;
	}

    public class Intake {
        public static final int LEFT_MOTOR = PWM.RIO5 ;
        public static final int RIGHT_MOTOR = PWM.RIO7 ;
        public static final int IR_SIDE = ANALOG.RIO0; // Analog
        public static final int IR_BACK = ANALOG.RIO1; // Analog
        public static final int SERVO = PWM.RIO0;
    }

    public class Carriage {
        public static final int MOTOR = PWM.RIO8;
        public static final int ENCODER_A = DIO.RIO0; // YELLOW
        public static final int ENCODER_B = DIO.RIO1; // BLUE

        public static final int HALL_EFFECT_TOP = DIO.RIO2; // DIO
        public static final int HALL_EFFECT_BOTTOM = DIO.RIO3; // DIO
    }

    public class Arm {
        public static final int MOTOR = PWM.RIO6;
        public static final int HALL_EFFECT_STARTING_POSITION = DIO.RIO4;
        public static final int STARTING_POSITION_LED = 10;
        public static final int ENCODER_A = DIO.navX12;
        public static final int ENCODER_B = DIO.navX11;
        public static final int POTENTIOMETER = ANALOG.navX4;
    }

    public class Climber {
        public static final int MOTOR = PWM.RIO9;
    }

    public class AutoChooser {
        public static final int POSITION_SWITCH = ANALOG.RIO2;
        public static final int MODE_SWITCH = ANALOG.RIO3;
        public static final int DELAY_SWITCH = ANALOG.navX5;
    }

    public static class PWM {
        public static final int RIO0 = 0;
        public static final int RIO1 = 1;
        public static final int RIO2 = 2;
        public static final int RIO3 = 3;
        public static final int RIO4 = 4;
        public static final int RIO5 = 5;
        public static final int RIO6 = 6;
        public static final int RIO7 = 7;
        public static final int RIO8 = 8;
        public static final int RIO9 = 9;
        public static final int navX10 = 10;
        public static final int navX11 = 11;
        public static final int navX12 = 12;
        public static final int navX13 = 13;
        public static final int navX14 = 14;
        public static final int navX15 = 15;
        public static final int navX16 = 16;
        public static final int navX17 = 17;
        public static final int navX18 = 18;
        public static final int navX19 = 19;
    }

    public static class DIO {
        public static final int RIO0 = 0;
        public static final int RIO1 = 1;
        public static final int RIO2 = 2;
        public static final int RIO3 = 3;
        public static final int RIO4 = 4;
        public static final int RIO5 = 5;
        public static final int RIO6 = 6;
        public static final int RIO7 = 7;
        public static final int RIO8 = 8;
        public static final int RIO9 = 9;
        public static final int navX10 = 10;
        public static final int navX11 = 11;
        public static final int navX12 = 12;
        public static final int navX13 = 13;
        public static final int navX14 = 14;
        public static final int navX15 = 15;
        public static final int navX16 = 16;
        public static final int navX17 = 17;
        public static final int navX18 = 18;
        public static final int navX19 = 19;
    }

    public static class ANALOG {
        public static final int RIO0 = 0;
        public static final int RIO1 = 1;
        public static final int RIO2 = 2;
        public static final int RIO3 = 3;
        public static final int navX4 = 4;
        public static final int navX5 = 5;
        public static final int navX6 = 6;
        public static final int navX7 = 7;
    }

}
