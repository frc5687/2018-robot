package org.frc5687.powerup.robot;

public class Constants {
    public static final int CYCLES_PER_SECOND = 50;
    public static final double START_ALERT = 32;
    public static final double END_ALERT = 28;
    public static final int HEALTH_CHECK_CYCLES = 10;

    /*
    Used by AutoAlignExperimental to select which types of turns to do.
    shortest will turn left or right, whichever will get it to the target angle quickest. This is possible with wpilib's pidcontroller thanks to the setContinuous flag.
     leftOnly, or, counterClockwise only, will turn to the target angle by only going counterClockwise
     rightOnly, or, clockwise only, wil turn to the target angle by only going clockwise.
     */
    public enum typeOfTurn {
        shortest,
        leftOnly,
        rightOnly
    }

    /*
    Used in AutoAlign to select which types of turns to do.
    bothSides will turn normally, or "in place" (hah!)
    leftOnly will use the pidOut to drive the left side of the drivetrain, but the right side will be in 0in/s talon velocity pid mode.
    rightOnly will use the pidOut to drive the right side of the drivetrain, but the left side will be in 0in/s talon velocity pid mode.
     */
    public enum DriveTrainBehavior {
        bothSides,
        leftOnly,
        rightOnly
    }


    public class Lights {
        // Values obtained from page 16- of http://www.revrobotics.com/content/docs/REV-11-1105-UM.pdf
        public static final double SOLID_BLUE = 0.87;
        public static final double PULSING_BLUE = -0.09;
        public static final double BEATING_BLUE = 0.23;

        public static final double SOLID_RED = 0.61;
        public static final double PULSING_RED = -0.11;
        public static final double BEATING_RED = 0.25;

        public static final double SOLID_GREEN = 0.77;
        public static final double PULSING_GREEN = 0.77; // replace
        public static final double BEATING_GREEN = 0.00; // unused

        public static final double SOLID_PURPLE = 0.91;
        public static final double PULSING_PURPLE = 0.05;
        public static final double BEATING_PURPLE = 0.00;

        public static final double SOLID_ORANGE = 0.06;
        public static final double PULSING_ORANGE = 0.07;
        public static final double BEATING_ORANGE = 0.08;

        public static final double SOLID_YELLOW = 0.69;
        public static final double PULSING_YELLOW = 0.10;
        public static final double BEATING_YELLOW = 0.11;

        public static final double SOLID_BLACK = 0.99;

        public static final double SOLID_HOT_PINK = 0.57;

        public static final double CONFETTI = -0.87;


        public static final double PLATE_DETECTED = -0.07;  // Gold strobe
        public static final double CUBE_SECURED = -0.81;  // White shot

        public static final double CUBE_DETECTED = -0.47;  // Forest twinkle
        public static final double INTAKE_IN =  -0.91; // green blue strobe;
        public static final double INTAKE_OUT = -0.93; // red strobe!

        public static final double GIMME_CUBE = SOLID_GREEN;

        public static final double TELEOP_BLUE = SOLID_BLACK;
        public static final double TELEOP_RED = SOLID_BLACK;

        public static final double AUTO_BLUE =  PULSING_BLUE;
        public static final double AUTO_RED = PULSING_RED;

        public static final double DISABLED_BLUE = BEATING_BLUE;
        public static final double DISABLD_RED = BEATING_RED;

        public static final double DEFAULT = BEATING_PURPLE;
        public static final double CLIMBER_UP = -0.85;  // American Up
        public static final double CLIMBER_HOLD = -0.89; // Party stobe
        public static final double CLIMBER_DOWN = PULSING_BLUE;
        public static final double TIME_WARNING = -.07;
        public static final double TEST_RUNNING = 0;
        public static final double TEST_WAITING = 0;

        public static final double TEST_PASSED = SOLID_GREEN;
        public static final double TEST_FAILED = SOLID_RED;
    }
    public class OI {
        public static final double RUMBLE_INTENSITY = 1;
        public static final double END_GAME_START = 30; // In seconds
        public static final double START_RUMBLE_AT = END_GAME_START;
        public static final double RUMBLE_DURATION = 2; //In seconds
        public static final double END_RUMBLE_AT = END_GAME_START - RUMBLE_DURATION;
    }
    public class DriveTrain {
        public static final double DEADBAND = 0.0;
        public static final boolean LEFT_MOTORS_INVERTED = false;
        public static final boolean RIGHT_MOTORS_INVERTED = true;
        public static final double SENSITIVITY = 0.75;
        public static final double TALL_CAP_HEIGHT =  72.0;
        public static final int TEST_TIME  = 1000;
        public static final double MIN_AMPS = 2.0;
        public static final double MONITOR_THRESHOLD_SPEED = 0.1;
        public static final double MONITOR_THRESHOLD_AMPS = 1.0;
    }

    public class Intake {
        public static final double DEADBAND = 0.05;
        public static final boolean LEFT_MOTORS_INVERTED = true;
        public static final boolean RIGHT_MOTORS_INVERTED_COMP = false;
        public static final boolean RIGHT_MOTORS_INVERTED_PROTO = true;
        public static final double DROP_SPEED = -0.75;
        public static final double OUTTAKE_SPEED = -0.75;
        public static final double SERVO_BOTTOM = 0;
        public static final double SERVO_UP = 1;
        public static final long EJECT_TIME = 350;

        public static final double HOLD_SPEED = 0.36;
        public static final double INTAKE_SPEED = 0.75;
        public static final double SENSITIVITY = 0.5;
        public static final long SETTLE_TIME = 1500;
        public static final double PLATE_MINIMUM_CLARANCE = 24.0;
        public static final double SCALE_DROP_SPEED = -0.9;
        public static final double SCALE_TRAVERSE_DROP_SPEED = -0.85;
        public static final double SCALE_SHOOT_SPEED = -0.70;
        public static final double SCALE_SHOOT_SPEED_SECOND_CUBE = -0.75;
        public static final double SWITCH_DROP_SPEED = -0.22;

        public static final double HC_MIN_SPEED = 0.1;
        public static final double HC_MIN_CURRENT = 0.5;

        public class SIDE_IR {
            public static final boolean ENABLED = false;
            public static final int DETECTION_THRESHOLD = 1200;
        }

        public class DOWN_IR {
            public static final boolean ENABLED = true;
            public static final int DETECTION_THRESHOLD = 800;
        }

        public class BACK_IR {
            public static final boolean ENABLED = true;
            public static final int SECURED_LOW_END = 1500;
            public static final int SECURED_HIGH_END = Integer.MAX_VALUE;
            public static final int DETECTED_LOW_END = 500;
            public static final int DETECTED_HIGH_END = Integer.MAX_VALUE;
        }

        public class UP_IR {
            public static final boolean ENABLED = true;
            public static final int PLATE_DETECTION_THRESHOLD = 550;
            public static final double MIN_ARM_ANGLE = 151;
        }
    }

    public class Auto {
        public static final double MIN_IMU_ANGLE = -180;
        public static final double MAX_IMU_ANGLE = 180;

        public static final double MAX_PITCH = 10.0;
        public static final double MAX_ROLL = 10.0;

        public class Align {

            public static final double SPEED = 1.0;

            public static final double kP = 0.04;
            public static final double kI = 0.0;
            public static final double kD = 0.3;
            public static final double TOLERANCE = 1.0; // 0.5
            public static final double MINIMUM_SPEED = 0.5;
            public static final double MAX_OUTPUT = 0;
            /*
             *time the angle must be on target for to be considered steady
             */
            public static final double STEADY_TIME = 60;

        }

        public class Drive {

            public static final double SPEED = 1.0;

            public static final double MIN_SPEED = 0.25;

            public class MaxVel {
                public static final double MPS = 2.33; // Meters Per Second
                public static final double IPS = 130; // Inches Per Second
            }

            public class MaxAcceleration {
                public static final double METERS = 2.0; // Meters Per Second Squared
                public static final double INCHES = 80.0;
            }

            public class MaxJerk {
                public static final double METERS = 6.0; // Meters Per Second Cubed
                public static final double INCHES = 200.0;
            }

            public static final long STEADY_TIME = 100;
            public static final long ALIGN_STEADY_TIME = 100;

            public class TrajectoryFollowing {
                public class Talon {
                    public static final double kP = 0.3; // Talon doesn't use kP
                    public static final double kI = 0.001;//02;
                    public static final double kD = 0.0;
                    public static final double kF = 0.35; // 0.28 works well
                }

                public class Cheese {
                    public static final double kP = 8;//1.06;//0.001;//1.70;//0.80;
                    public static final double kI = 0.0;
                    public static final double kD = 0.1;//.3;
                    public static final double kT = 0.4; // Used for turning correction. -0.68 works well
                    public class kV {
                        public static final double MPS = 1.0 / MaxVel.MPS;
                        public static final double IPS = 1.0;// / MaxVel.IPS;
                    }
                    public class kA {
                        public static final double METERS = 1.0 / MaxAcceleration.METERS;
                        public static final double INCHES = 1.0;//1.0 / MaxAcceleration.INCHES;
                    }
                }
            }

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
                public static final double kP = 6;//1.06;//0.001;//1.70;//0.80;
                public static final double kI = 0.0;
                public static final double kD = 0.0;//.3;
                public static final double kT = 4; // Used for turning correction
                public class kV {
                    public static final double MPS = 1.0 / MaxVel.MPS;
                    public static final double IPS = 1.0;// / MaxVel.IPS;
                }
                public class kA {
                    public static final double METERS = 1.0 / MaxAcceleration.METERS;
                    public static final double INCHES = 0.0;//1.0 / MaxAcceleration.INCHES;
                }
                public static final double TOLERANCE = 1;
            }

            public class AnglePID {
                public static final double kP = 0.4;
                public static final double kI = 0.006;
                public static final double kD = 0.09;
                public class kV {
                    public static final double MPS = 1.0 / MaxVel.MPS;
                    public static final double IPS = 1.0 / MaxVel.IPS;
                }
                public static final double PATH_TURN = 0.4; // 1.0
                public static final double MAX_DIFFERENCE = 0.4;
                public static final double TOLERANCE = .5;
            }

        }

    }

    public class Encoders {

        public class Defaults {

            public static final boolean REVERSED = true; //TODO change to new robot specifications
            public static final int SAMPLES_TO_AVERAGE = 20;
            public static final int PULSES_PER_ROTATION = 4096; // 1024 in quad mode. talon is 4096.

            public class WheelDiameter {
                public static final double INCHES = 6;
                public static final double METERS = 0.1524;
            }
            public class DistancePerRotation {
                public static final double INCHES = Math.PI * WheelDiameter.INCHES;
                public static final double METERS = Math.PI * WheelDiameter.METERS;
            }

            public class DistancePerPulse {
                public static final double INCHES = DistancePerRotation.INCHES / PULSES_PER_ROTATION;
                public static final double METERS = DistancePerRotation.METERS / PULSES_PER_ROTATION;
            }

            public static final double SCALAR_RATIO = 8;
            public static final double MAX_PERIOD = 5;

            public class Track {
                public static final double INCHES = 24;
                public static final double METERS = 0.6096;
            }

        }

        public class RightDrive {
            public static final boolean REVERSED = Defaults.REVERSED;
            public static final double INCHES_PER_PULSE = Defaults.DistancePerPulse.INCHES;

        }

        public class LeftDrive {
            public static final boolean REVERSED = Defaults.REVERSED;
            public static final double INCHES_PER_PULSE = Defaults.DistancePerPulse.INCHES;

        }
    }

    public class IR {
        public static final int DETECTION_THRESHOLD = 600;
    }

    public class Carriage {
        public static final double PDP_EXCESSIVE_CURRENT = 100.0;
        public static final double HC_MIN_CURRENT = .5;
        public static final double DEADBAND = 0.13;
        public static final boolean MOTOR_INVERTED = true;
        public static final double HC_MIN_SPEED = 0.1; // Minimum speed to triggr the amp/isHalthy check
        public class HoldSpeeds {
            public static final double PAST_TOP_PROTO = 0.5;
            public static final double PAST_TOP_GRETA = 0.5;

            public static final double PAST_NEG_20_PROTO = 0.6;
            public static final double PAST_NEG_20_GRETA = 0.5;

            public static final double PAST_NEG_50_PROTO = 0.6;
            public static final double PAST_NEG_50_GRETA = 0.5;

            public static final double PAST_NEG_100_PROTO = 0.5;
            public static final double PAST_NEG_100_GRETA = 0.5;

            public static final double PAST_NEG_200_PROTO = 0.42;
            public static final double PAST_NEG_200_GRETA = 0.0;

            public static final double PAST_NEG_300_PROTO = 0.42;
            public static final double PAST_NEG_300_GRETA = 0.0;

            public static final double PAST_NEG_400_PROTO = 0.2;
            public static final double PAST_NEG_400_GRETA = 0.0;

            public static final double PAST_NEG_500_PROTO = 0.15;
            public static final double PAST_NEG_500_GRETA = 0.05;

            public static final double PAST_NEG_600_PROTO = 0.15;
            public static final double PAST_NEG_600_GRETA = -0.2;

            public static final double PAST_NEG_700_PROTO = 0.0;
            public static final double PAST_NEG_700_GRETA = -0.0; // TODO: Too low

            public static final double PAST_NEG_800_PROTO = 0.0;
            public static final double PAST_NEG_800_GRETA = -0.0; // TODO: Too low

            public static final double PAST_NEG_900_PROTO = -0.45;
            public static final double PAST_NEG_900_GRETA = -0.45;
        }
        public static final double SENSITIVITY = 0.2;
        public static final double ZERO_SPEED = 1.00;

        public static final double ZONE_SPEED_LIMIT = 0.75;

        public static final int ENCODER_TOP_PROTO = 0;
        public static final int ENCODER_MIDDLE_PROTO = -480;
        public static final int ENCODER_CLEAR_BUMPERS_PROTO = -717;
        public static final int ENCODER_DRIVE_PROTO = -500;
        public static final int ENCODER_BOTTOM_PROTO = -967;
        public static final int ENCODER_RANGE_PROTO = ENCODER_TOP_PROTO - ENCODER_BOTTOM_PROTO;
        public static final int ENCODER_TOP_COMP = 0;
        public static final int ENCODER_MIDDLE_COMP = -443;
        public static final int ENCODER_CLEAR_BUMPERS_COMP = -702;
        public static final int ENCODER_DRIVE_COMP = -530; // -394
        public static final int ENCODER_BOTTOM_COMP = -960;
        public static final int ENCODER_RANGE_COMP = ENCODER_TOP_COMP - ENCODER_BOTTOM_COMP;


        // public static
        public static final double RUNWAY = 25.5; // in


        public static final int START_TOP_ZONE_COMP = -100;
        public static final int START_TOP_ZONE_PROTO = -100;
        public static final int START_BOTTOM_ZONE_COMP = -600;
        public static final int START_BOTTOM_ZONE_PROTO = -600;

        public static final double BOTTOM_INCHES = 23.0;
        public static final double TOP_INCHES = 48.0;
        public static final double RANGE_INCHES = TOP_INCHES - BOTTOM_INCHES;

        public static final double MAXIMUM_SPEED  = 1.0;
        public static final double MINIMUM_SPEED  = -.7;
    }

    public class Arm {
        public static final double PDP_EXCESSIVE_CURRENT = 40.0;
        public static final double MIN_AMPS = 3.5;

        public static final boolean MOTOR_INVERTED_PROTO = false;
        public static final boolean MOTOR_INVERTED_COMP = true;
        public static final double HC_MIN_SPEED = 0.25;
        public static final double HC_MIN_CURRENT = 0.5;

        public class Encoder {
            public static final double ENCODER_START = 0;
            public static final double ENCODER_MIDDLE = 133;
            public static final double ENCODER_FENCE = 90;
            public static final double ENCODER_TOP = 340;
        }
        public static final double SENSITIVITY = 0.75;

        public static final double MAX_SPEED = 0.75;
        public static final double MIN_SPEED = -.75;
        public static final double PID_MAX_SPEED = 0.9;
        public static final double PID_MIN_SPEED = -0.9;


        public class Pot {
            public static final double TOP_COMP = 166.0;
            public static final double TOP_PROTO = 166.0; // TODO: Tune

            public static final double BOTTOM_COMP = 33.0;
            public static final double BOTTOM_PROTO = 33.0; // TODO: Tune

            public static final double INTAKE_COMP = 49.0;
            public static final double INTAKE_PROTO = 47.0;

            public static final double DRIVE_COMP = 41.0;
            public static final double DRIVE_PROTO = 45.0;

            public static final double SWITCH_HEIGHT_COMP = 85.0;
            public static final double SWITCH_HEIGHT_PROTO = 85.0;

            public static final double TOP = 170.5;
            public static final double BOTTOM = 31.8;

            public static final double TOLERANCE = 4.2;
            public static final double SWITCH_HEIGHT = 50.0; // @Carriage Top
            public static final double INTAKE = 47.0;
            public static final double DRIVE = 33.0;

            public static final double SCALE = 150.0;
            public static final double SCALE_MAX = 163.0;
            public static final double SWITCH_HEIGHT_WITH_CARRIAGE = 100;
            public static final double SWITCH_HEIGHT_BROKEN_CARRIAGE = 72; // I guess this shouldn't be lower, but I'm just removing a magic number..
            public static final double switchHeightWithCarriageHalfwayUp = 91;
            public static final double switchHeightWithCarriageAllTheWayUp = 80;
        }

        public static final double LENGTH = 34.0;

        public class HoldSpeeds {
            public static final double PAST_160_CUBE_PROTO = 0.2;
            public static final double PAST_160_CUBE_GRETA = 0.25;
            public static final double PAST_160_NO_CUBE_PROTO = 0.2;
            public static final double PAST_160_NO_CUBE_GRETA = 0.25;

            public static final double PAST_150_CUBE_PROTO = 0.2;
            public static final double PAST_150_CUBE_GRETA = 0.25;
            public static final double PAST_150_NO_CUBE_PROTO = 0.2;
            public static final double PAST_150_NO_CUBE_GRETA = 0.25;

            public static final double PAST_90_CUBE_PROTO = 0.0;
            public static final double PAST_90_CUBE_GRETA = 0.1;
            public static final double PAST_90_NO_CUBE_PROTO = 0.0;
            public static final double PAST_90_NO_CUBE_GRETA = 0.1;

            public static final double PAST_55_CUBE_PROTO = 0.0;
            public static final double PAST_55_CUBE_GRETA = 0.15;
            public static final double PAST_55_NO_CUBE_PROTO = 0.0;
            public static final double PAST_55_NO_CUBE_GRETA = 0.1;

            public static final double PAST_BOTTOM_CUBE_PROTO = -0.1;
            public static final double PAST_BOTTOM_CUBE_GRETA = -0.10;
            public static final double PAST_BOTTOM_NO_CUBE_PROTO = -0.1;
            public static final double PAST_BOTTOM_NO_CUBE_GRETA = -0.10;
        }
    }

    public class Climber {
        public static final double PDP_EXCESSIVE_CURRENT = 100.0;
        public static final boolean MOTOR_INVERT = false;
        public static final double WIND_SPEED = 1.0;
        public static final double UNWIND_SPEED = -1.0;
        public static final double HOLD_SPEED = 0.30; // Number pulled out of thin air by JohnZ
        public static final double HC_MIN_SPEED = 0.1;
        public static final double HC_MIN_CURRENT = 0.5;
    }

    public class RotarySwitch {
        public static final double TOLERANCE = 0.02;
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
            public static final int SWITCH_THEN_PICKUP_CUBE = 2;
            public static final int SCALE_THEN_SCALE = 3;
            public static final int SWITCH_THEN_SWITCH = 4;
            public static final int SCALE_THEN_SWITCH = 5;
            public static final int SCALE_THEN_BACKOFF = 6;
            public static final int SWITCH_ONLY = 7;
            public static final int SCALE_ONLY = 8;
            public static final int EXPERIMENTAL_SWITCH_THEN_SWITCH = 9;
            public static final int SWITCH_DRIVE = 1001;
            public static final int SCALE_DRIVE = 1003;
            public static final int EXPERIMENTAL_SCALE_THEN_SCALE = 10;
        }

        public class Coop {
            public static final int DELAY_0 = 0;
            public static final int DELAY_1 = 1;
            public static final int DELAY_2 = 2;
            public static final int DELAY_3 = 3;
            public static final int DELAY_4 = 4;
            public static final int DELAY_5 = 5;
            public static final int DELAY_6 = 6;
            public static final int DELAY_7 = 7;
            public static final int DELAY_8 = 8;
            public static final int DELAY_9 = 9;
            public static final int DEFENSE = 10;
            public static final int STAY_IN_LANE = 11;
        }


    }
    public class Limits {
        /***
         * Minimum time (in milliseconds) it should take to go from 0 to 1 (stop to full)
         */
        public static final double TIME_OF_ACCEL = .25; //in seconds
        public static final double TIME_OF_ACCEL_TALL = .5; //in seconds

        /***
         * Maximum accelerations per cycle (ie, seconds to full speed
         */
        public static final double ACCELERATION_CAP = TIME_OF_ACCEL == 0 ? 1 : (1  / (TIME_OF_ACCEL * CYCLES_PER_SECOND));
        public static final double ACCELERATION_CAP_TALL =  TIME_OF_ACCEL_TALL == 0 ? 1 : (1 / (TIME_OF_ACCEL_TALL * CYCLES_PER_SECOND));
    }

}
