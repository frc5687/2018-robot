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
        public static final double OUTTAKE_SPEED = -0.75;
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
                public static final double TOLERANCE = 1;
            }

            public class AnglePID {
                public static final double kP = 0.04;
                public static final double kI = 0.006;
                public static final double kD = 0.09;

                public static final double MAX_DIFFERENCE = 0.2;
                public static final double TOLERANCE = .5;
            }

        }

    }

    public class Encoders {

        public class Defaults {

            public static final boolean REVERSED = true; //TODO change to new robot specifications
            public static final int SAMPLES_TO_AVERAGE = 20;
            public static final int PULSES_PER_ROTATION = 1440;
            public static final double WHEEL_DIAMETER = 6;
            public static final double INCHES_PER_ROTATION = Math.PI * WHEEL_DIAMETER;
            public static final double SCALAR_RATIO = 8;
            //            public static final double INCHES_PER_PULSE_TONY = INCHES_PER_ROTATION * SCALAR_RATIO / PULSES_PER_ROTATION;
            public static final double INCHES_PER_PULSE = 0.1076;
            // public static final double INCHES_PER_PULSE_TONY = 0.12371134;
            public static final double MAX_PERIOD = 5;

        }

        public class RightDrive {

            public static final boolean REVERSED = Defaults.REVERSED;
            public static final double INCHES_PER_PULSE_TONY = 0.03609; //Encoders.Defaults.INCHES_PER_PULSE;
            public static final double INCHES_PER_PULSE_RHODY = 0.0406;

        }

        public class LeftDrive {

            public static final boolean REVERSED = Defaults.REVERSED;
            public static final double INCHES_PER_PULSE_TONY = Defaults.INCHES_PER_PULSE;
            public static final double INCHES_PER_PULSE_RHODY = 0.1145;

        }
    }

    public class Carriage {
        public static final double DEADBAND = 0.13;
        public static final boolean MOTOR_INVERTED = true;
        public static final int ENCODER_TOP = 967;
        public static final double RUNWAY = 25.5; // in
    }

    public class Arm {
        public static final double ENCODER_START = 0;
        public static final double ENCODER_MIDDLE = 133;
        public static final double ENCODER_TOP = 340;

        public static final double HOLD_SPEED = 0.15;
    }

}
