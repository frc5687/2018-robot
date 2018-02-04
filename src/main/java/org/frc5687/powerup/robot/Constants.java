package org.frc5687.powerup.robot;

public class Constants {

    public class DriveTrain {
        public static final double DEADBAND = 0.15;
        public static final boolean LEFT_MOTORS_INVERTED = false;
        public static final boolean RIGHT_MOTORS_INVERTED = true;
    }

    public class Intake {
        public static final double DEADBAND = 0.05;
        public static final boolean LEFT_MOTORS_INVERTED = true;
        public static final boolean RIGHT_MOTORS_INVERTED = false;
        public static final double DROP_SPEED = -0.75;
        public static final double OUTTAKE_SPEED = -0.75;
        public static final double SERVO_BOTTOM = 0.0;
        public static final double SERVO_UP = 1.0;
        public static final long EJECT_TIME = 250;

        public static final double HOLD_SPEED = 0.1;
    }

    public class Auto {
        public static final double MIN_IMU_ANGLE = -180;
        public static final double MAX_IMU_ANGLE = 180;

        public class Align {

            public static final double SPEED = 0.6;

            public static final double kP = 0.04;
            public static final double kI = 0.006;
            public static final double kD = 0.1;
            public static final double TOLERANCE = .5;
            public static final double MAX_OUTPUT = 0;
            /*
             *time the angle must be on target for to be considered steady
             */
            public static final double STEADY_TIME = 100;

        }

        public class Drive {

            public static final double SPEED = 1.0;
            public static final double MAX_VEL = 0.0; // m/s

            public static final long STEADY_TIME = 100;
            public static final long ALIGN_STEADY_TIME = 100;

            public class IRPID {
                public static final double kP = 0.05;
                public static final double kI = 0.00;
                public static final double kD = 0.03;
                public static final double TOLERANCE = .5;

                /**
                 * a in the voltage-to-distance equation distance = a * voltage ^ b
                 */
                public static final double TRANSFORM_COEFFICIENT = 27.385;
                /**
                 * b in the voltage-to-distance equation distance = a * voltage ^ b
                 */
                public static final double TRANSFORM_POWER = -1.203;
            }

            public class EncoderPID {
                public static final double kP = 0.05;
                public static final double kI = 0;
                public static final double kD = .02;
                public static final double kV = 1.0 / Drive.MAX_VEL;
                public static final double kA = 0.0;
                public static final double TOLERANCE = 1;
            }

            public class AnglePID {
                public static final double kP = 0.04;
                public static final double kI = 0.006;
                public static final double kD = 0.09;

                public static final double MAX_DIFFERENCE = 0.4;
                public static final double TOLERANCE = .5;
            }

        }

    }

    public class Encoders {

        public class Defaults {

            public static final boolean REVERSED = true; //TODO change to new robot specifications
            public static final int SAMPLES_TO_AVERAGE = 20;
            public static final int PULSES_PER_ROTATION = 1024;
            public static final double WHEEL_DIAMETER = 6;
            public static final double INCHES_PER_ROTATION = Math.PI * WHEEL_DIAMETER;
            public static final double SCALAR_RATIO = 8;
            //            public static final double INCHES_PER_PULSE_TONY = INCHES_PER_ROTATION * SCALAR_RATIO / PULSES_PER_ROTATION;
            public static final double INCHES_PER_PULSE = INCHES_PER_ROTATION/PULSES_PER_ROTATION;
            // public static final double INCHES_PER_PULSE_TONY = 0.12371134;
            public static final double MAX_PERIOD = 5;
            public static final double TRACK = 0.6096; // 24 in

        }

        public class RightDrive {

            public static final boolean REVERSED = Defaults.REVERSED;
            public static final double INCHES_PER_PULSE = Defaults.INCHES_PER_PULSE; //Encoders.Defaults.INCHES_PER_PULSE;

        }

        public class LeftDrive {

            public static final boolean REVERSED = Defaults.REVERSED;
            public static final double INCHES_PER_PULSE = Defaults.INCHES_PER_PULSE;

        }
    }

    public class IR {
        public static final int DETECTION_THRESHOLD = 600;
    }

    public class Carriage {
        public static final double DEADBAND = 0.13;
        public static final boolean MOTOR_INVERTED = true;
        public static final int ENCODER_TOP = 967;
        // public static
        public static final double RUNWAY = 25.5; // in

        public static final int CLEAR_BUMPERS = 250;

        public static final double HOLD_SPEED = 0.05;
    }

    public class Arm {
        public static final double ENCODER_START = 0;
        public static final double ENCODER_MIDDLE = 133;
        public static final double ENCODER_FENCE = 90;
        public static final double ENCODER_TOP = 340;

        public static final double HOLD_SPEED = 0.1;
    }

    public class Climber {
        public static final boolean MOTOR_INVERT = true;
        public static final double WIND_SPEED = 1.0;
        public static final double UNWIND_SPEED = -1.0;
    }

    public class RotarySwitch {
        public static final double TOLERANCE = 0.06;
    }

    public class AutoChooser {
        public static final int LEFT = -1;
        public static final int RIGHT = 1;

        public class Position {
            public static final int FAR_LEFT = 1;
            public static final int MID_LEFT = 2;
            public static final int CENTER = 3;
            public static final int NEAR_RIGHT = 4;
            public static final int MID_RIGHT = 5;
            public static final int FAR_RIGHT = 6;
        }

        public class Mode {
            public static final int STAY_PUT = 0;
            public static final int CROSS_AUTOLINE = 1;
            public static final int SWITCH_ONLY = 2;
            public static final int SCALE_ONLY = 3;
            public static final int SWITCH_THEN_SCALE = 4;
            public static final int SCALE_THEN_SWITCH = 5;
            public static final int SWITCH_OR_SCALE = 6;
        }
    }

}
