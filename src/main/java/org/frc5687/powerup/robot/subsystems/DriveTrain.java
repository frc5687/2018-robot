package org.frc5687.powerup.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.DriveWith2Joysticks;

/**
 * Created by Baxter on 3/22/2017.
 */
public class DriveTrain extends Subsystem implements PIDSource {

    private VictorSP leftFrontMotor;
    private VictorSP leftRearMotor;
    private VictorSP rightFrontMotor;
    private VictorSP rightRearMotor;
    private Encoder rightEncoder;
    private Encoder leftEncoder;

    private AHRS imu;
    private OI oi;

    public DriveTrain(AHRS imu, OI oi) {
        leftFrontMotor = new VictorSP(RobotMap.DriveTrain.LEFT_FRONT_MOTOR);
        leftRearMotor = new VictorSP(RobotMap.DriveTrain.LEFT_REAR_MOTOR);
        rightFrontMotor = new VictorSP(RobotMap.DriveTrain.RIGHT_FRONT_MOTOR);
        rightRearMotor = new VictorSP(RobotMap.DriveTrain.RIGHT_REAR_MOTOR);

        leftFrontMotor.setInverted(Constants.DriveTrain.LEFT_MOTORS_INVERTED);
        leftRearMotor.setInverted(Constants.DriveTrain.LEFT_MOTORS_INVERTED);
        rightFrontMotor.setInverted(Constants.DriveTrain.RIGHT_MOTORS_INVERTED);
        rightRearMotor.setInverted(Constants.DriveTrain.RIGHT_MOTORS_INVERTED);

        rightEncoder = initializeEncoder(RobotMap.DriveTrain.RIGHT_ENCODER_CHANNEL_A, RobotMap.DriveTrain.RIGHT_ENCODER_CHANNEL_B, Constants.Encoders.RightDrive.REVERSED, Constants.Encoders.RightDrive.INCHES_PER_PULSE);
        leftEncoder = initializeEncoder(RobotMap.DriveTrain.LEFT_ENCODER_CHANNEL_A, RobotMap.DriveTrain.LEFT_ENCODER_CHANNEL_B, Constants.Encoders.LeftDrive.REVERSED, Constants.Encoders.LeftDrive.INCHES_PER_PULSE);

        this.imu = imu;
        this.oi = oi;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveWith2Joysticks(this, oi));
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        leftFrontMotor.set(leftSpeed);
        leftRearMotor.set(leftSpeed);
        rightFrontMotor.set(rightSpeed);
        rightRearMotor.set(rightSpeed);
    }


    private Encoder initializeEncoder(int channelA, int channelB, boolean reversed, double distancePerPulse) {
        Encoder encoder = new Encoder(channelA, channelB, reversed, Encoder.EncodingType.k4X);
        encoder.setDistancePerPulse(distancePerPulse);
        encoder.setMaxPeriod(Constants.Encoders.Defaults.MAX_PERIOD);
        encoder.setSamplesToAverage(Constants.Encoders.Defaults.SAMPLES_TO_AVERAGE);
        encoder.reset();
        return encoder;
    }

    public void resetDriveEncoders() {
        rightEncoder.reset();
        leftEncoder.reset();
    }

    public float getYaw() {
        return imu.getYaw();
    }

    public double getLeftDistance() {
        return leftEncoder.getDistance();
    }

    public long getLeftTicks() {
        return leftEncoder.get();
    }

    public double getLeftRate() {
        return leftEncoder.getRate();
    }

    public double getLeftSpeed() {
        return leftFrontMotor.getSpeed();
    }

    public double getRightDistance() {
        return rightEncoder.getDistance();
    }

    public long getRightTicks() {
        return rightEncoder.get();
    }

    public double getRightRate() {
        return rightEncoder.getRate();
    }

    public double getRightSpeed() {
        return rightFrontMotor.getSpeed();
    }

    public double getLeftRPS() {
        return getLeftRate() / (Constants.Encoders.Defaults.PULSES_PER_ROTATION * Constants.Encoders.Defaults.INCHES_PER_PULSE);
    }

    public double getRightRPS() {
        return getRightRate() / (Constants.Encoders.Defaults.PULSES_PER_ROTATION * Constants.Encoders.Defaults.INCHES_PER_PULSE);
    }


    /**
     * @return average of leftDistance and rightDistance
     */
    public double getDistance() {
        if (Math.abs(getRightTicks())<10) {
            return getLeftDistance();
        }
        if (Math.abs(getLeftTicks())<10) {
            return getRightDistance();
        }
        return (getLeftDistance() + getRightDistance()) / 2;
    }

    @Override
    public double pidGet() {
        return getDistance();
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return PIDSourceType.kDisplacement;
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
    }

    public void updateDashboard() {
        SmartDashboard.putNumber("DriveTrain/Distance/Right", getRightDistance());
        SmartDashboard.putNumber("DriveTrain/Distance/Left", getLeftDistance());

        SmartDashboard.putNumber("DriveTrain/Ticks/Right", getRightTicks());
        SmartDashboard.putNumber("DriveTrain/Ticks/Left", getLeftTicks());

        SmartDashboard.putNumber("DriveTrain/Rate/Right", getRightRate());
        SmartDashboard.putNumber("DriveTrain/Rate/Left", getLeftRate());

        SmartDashboard.putNumber("DriveTrain/Speed/Right", getRightSpeed());
        SmartDashboard.putNumber("DriveTrain/Speed/Left", getLeftSpeed());

        SmartDashboard.putNumber("DriveTrain/RPS/Right", getRightRPS());
        SmartDashboard.putNumber("DriveTrain/RPS/Left", getLeftRPS());

        SmartDashboard.putBoolean("DriveTrain/Inverted/Right", Constants.DriveTrain.RIGHT_MOTORS_INVERTED);
        SmartDashboard.putBoolean("DriveTrain/Inverted/Left", Constants.DriveTrain.LEFT_MOTORS_INVERTED);

        SmartDashboard.putNumber("IMU/yaw", imu.getYaw());
        SmartDashboard.putData("IMU", imu);
    }


    protected class EncoderPIDSource implements PIDSource {
        private DriveTrain _driveTrain;

        protected EncoderPIDSource(DriveTrain driveTrain) {
            _driveTrain = driveTrain;
        }

        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {

        }

        @Override
        public PIDSourceType getPIDSourceType() {
            return null;
        }

        @Override
        public double pidGet() {
            return 0;
        }
    }

    protected class UltrasonicPIDSource implements PIDSource {

        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {

        }

        @Override
        public PIDSourceType getPIDSourceType() {
            return null;
        }

        @Override
        public double pidGet() {
            return 0;
        }
    }


}
