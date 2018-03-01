package org.frc5687.powerup.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.DriveWith2Joysticks;
import org.frc5687.powerup.robot.utils.Helpers;

/**
 * Created by Baxter on 3/22/2017.
 */
public class DriveTrain extends Subsystem implements PIDSource {

    private TalonSRX leftMaster; // left front motor
    private VictorSPX leftFollower; // left rear motor
    private TalonSRX rightMaster; // right front motor
    private VictorSPX rightFollower; // right rear motor

    private Robot _robot;

    private AHRS imu;
    private OI oi;

    public double HIGH_POW = 1.0;
    public double LOW_POW = -HIGH_POW;

    private double _priorLeft = 0;
    private double _priorRight = 0;

    public DriveTrain(Robot robot,  AHRS imu, OI oi) {
        _robot = robot;

        // Motor Initialization
        leftMaster = new TalonSRX(RobotMap.CAN.TalonSRX.LEFT_FRONT);
        leftFollower = new VictorSPX(RobotMap.CAN.VictorSPX.LEFT_BACK);
        rightMaster = new TalonSRX(RobotMap.CAN.TalonSRX.RIGHT_FRONT);
        rightFollower = new VictorSPX(RobotMap.CAN.VictorSPX.RIGHT_BACK);

        // Setup slaves to follow their master
        leftFollower.follow(leftMaster);
        rightFollower.follow(rightMaster);

        // Setup motors
        leftMaster.configPeakOutputForward(HIGH_POW, 0);
        leftFollower.configPeakOutputForward(HIGH_POW, 0);
        rightMaster.configPeakOutputForward(HIGH_POW, 0);
        rightFollower.configPeakOutputForward(HIGH_POW, 0);

        leftMaster.configPeakOutputReverse(LOW_POW, 0);
        leftFollower.configPeakOutputReverse(LOW_POW, 0);
        rightMaster.configPeakOutputReverse(LOW_POW, 0);
        rightFollower.configPeakOutputReverse(LOW_POW, 0);

        leftMaster.configNominalOutputForward(0.0, 0);
        leftFollower.configNominalOutputForward(0.0, 0);
        rightMaster.configNominalOutputForward(0.0, 0);
        rightFollower.configNominalOutputForward(0.0, 0);

        leftMaster.configNominalOutputReverse(0.0, 0);
        leftFollower.configNominalOutputReverse(0.0, 0);
        rightMaster.configNominalOutputReverse(0.0, 0);
        rightFollower.configNominalOutputReverse(0.0, 0);

        leftMaster.setInverted(Constants.DriveTrain.LEFT_MOTORS_INVERTED);
        leftFollower.setInverted(Constants.DriveTrain.LEFT_MOTORS_INVERTED);
        rightMaster.setInverted(Constants.DriveTrain.RIGHT_MOTORS_INVERTED);
        rightFollower.setInverted(Constants.DriveTrain.RIGHT_MOTORS_INVERTED);

        enableBrakeMode();

        leftMaster.config_kP(0, Constants.Auto.Drive.TrajectoryFollowing.Talon.kP, 0);
        leftMaster.config_kI(0, Constants.Auto.Drive.TrajectoryFollowing.Talon.kI, 0);
        leftMaster.config_kD(0, Constants.Auto.Drive.TrajectoryFollowing.Talon.kD, 0);
        leftMaster.config_kF(0, Constants.Auto.Drive.TrajectoryFollowing.Talon.kF, 0);

        rightMaster.config_kP(0, Constants.Auto.Drive.TrajectoryFollowing.Talon.kP, 0);
        rightMaster.config_kI(0, Constants.Auto.Drive.TrajectoryFollowing.Talon.kI, 0);
        rightMaster.config_kD(0, Constants.Auto.Drive.TrajectoryFollowing.Talon.kD, 0);
        rightMaster.config_kF(0, Constants.Auto.Drive.TrajectoryFollowing.Talon.kF, 0);

        // Encoders

        leftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        rightMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);

        leftMaster.setSensorPhase(true);
        rightMaster.setSensorPhase(true);

        //leftMaster.configMotionProfileTrajectoryPeriod(10, 0);
        //leftMaster.setStatusFramePeriod(StatusFrame.Status_10_MotionMagic, 10, 0);

        resetDriveEncoders();

        this.imu = imu;
        this.oi = oi;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveWith2Joysticks(this, oi));
    }

    public void enableBrakeMode() {
        leftMaster.setNeutralMode(NeutralMode.Brake);
        leftFollower.setNeutralMode(NeutralMode.Brake);
        rightMaster.setNeutralMode(NeutralMode.Brake);
        rightFollower.setNeutralMode(NeutralMode.Brake);
        SmartDashboard.putString("DriveTrain/neutralMode", "Brake");
    }

    public void enableCoastMode() {
        leftMaster.setNeutralMode(NeutralMode.Coast);
        leftFollower.setNeutralMode(NeutralMode.Coast);
        rightMaster.setNeutralMode(NeutralMode.Coast);
        rightFollower.setNeutralMode(NeutralMode.Coast);
        SmartDashboard.putString("DriveTrain/neutralMode", "Coast");
    }
    public void setPower(double leftSpeed, double rightSpeed) {
        setPower(leftSpeed, rightSpeed, false);
    }

    public void setPower(double leftSpeed, double rightSpeed, boolean overrideCaps) {
        if (!overrideCaps) {
            SmartDashboard.putNumber("DriveTrain/Speed/RightRaw", rightSpeed);
            SmartDashboard.putNumber("DriveTrain/Speed/LeftRaw", leftSpeed);
            double cap = (_robot.estimateIntakeHeight() > Constants.DriveTrain.TALL_CAP_HEIGHT) ?  Constants.Limits.ACCELERATION_CAP_TALL : Constants.Limits.ACCELERATION_CAP;
            SmartDashboard.putNumber("DriveTrain/Cap", cap);

            leftSpeed = Math.min(leftSpeed, _priorLeft + cap);
            leftSpeed = Math.max(leftSpeed, _priorLeft - cap);

            // Limit change in rightSpeed to +/- ACCELERATION_CAP
            rightSpeed = Math.min(rightSpeed, _priorRight + cap);
            rightSpeed = Math.max(rightSpeed, _priorRight - cap);
        }
        leftMaster.set(ControlMode.PercentOutput, leftSpeed);
        rightMaster.set(ControlMode.PercentOutput, rightSpeed);
        SmartDashboard.putNumber("DriveTrain/Speed/Right", rightSpeed);
        SmartDashboard.putNumber("DriveTrain/Speed/Left", leftSpeed);

        _priorLeft = leftSpeed;
        _priorRight = rightSpeed;
    }

    public void setVelocity(double left, double right) {
        leftMaster.set(ControlMode.Velocity, left);
        rightMaster.set(ControlMode.Velocity, right);
    }

    public void setVelocityIPS(double left, double right) {
        setVelocity(Helpers.ips2talon(left), Helpers.ips2talon(right));
    }

    public void resetDriveEncoders() {
        leftMaster.setSelectedSensorPosition(0,0,0);
        rightMaster.setSelectedSensorPosition(0, 0, 0);
    }

    public float getYaw() {
        return imu.getYaw();
    }

    /**
     * Get the number of ticks since the last reset
     * @return
     */
    public long getLeftTicks() {
        return leftMaster.getSelectedSensorPosition(0);
    }

    /**
     * The left distance in Inches since the last reset.
     * @return
     */
    public double getLeftDistance() {
        return getLeftTicks() * Constants.Encoders.LeftDrive.INCHES_PER_PULSE;
    }

    /**
     * Get the number of ticks in the past 100ms
     * @return
     */
    public double getLeftRate() {
        return leftMaster.getSelectedSensorVelocity(0);
    }

    /**
     * Get the number of inches in the past 100ms
     * @return
     */
    public double getLeftSpeed() {
        return getLeftRate() * Constants.Encoders.LeftDrive.INCHES_PER_PULSE;
    }

    public double getLeftVelocityIPS() {
        return getLeftSpeed() * 10;
    }

    public double getLeftRPS() {
        return getLeftRate() * 10 / Constants.Encoders.Defaults.PULSES_PER_ROTATION;
    }

    /**
     * Get the number of pulses since the last reset
     * @return
     */
    public long getRightTicks() {
        return rightMaster.getSelectedSensorPosition(0);
    }

    /**
     *
     * @return The distance traveled in inches.
     */
    public double getRightDistance() {
        return getRightTicks() * Constants.Encoders.RightDrive.INCHES_PER_PULSE;
    }

    /**
     * Get the number of pulses in the last 100ms.
     * @return
     */
    public double getRightRate() {
        return rightMaster.getSelectedSensorVelocity(0);
    }

    /**
     * Get the number of inches traveled in the last 100ms
     * @return
     */
    public double getRightSpeed() {
        return getRightRate() * Constants.Encoders.RightDrive.INCHES_PER_PULSE;
    }

    public double getRightVelocityIPS() {
        return getRightSpeed() * 10;
    }

    public double getRightRPS() {
        return getRightRate() * 10 / Constants.Encoders.Defaults.PULSES_PER_ROTATION;
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

    public void updateDashboard() {
        SmartDashboard.putNumber("DriveTrain/observedHeadingCheesy", -getYaw());
        SmartDashboard.putNumber("DriveTrain/Distance/Right", getRightDistance());
        SmartDashboard.putNumber("DriveTrain/Distance/Left", getLeftDistance());

        SmartDashboard.putNumber("DriveTrain/Ticks/Right", getRightTicks());
        SmartDashboard.putNumber("DriveTrain/Ticks/Left", getLeftTicks());

        SmartDashboard.putNumber("DriveTrain/Rate/Right", getRightRate());
        SmartDashboard.putNumber("DriveTrain/Rate/Left", getLeftRate());

        SmartDashboard.putNumber("DriveTrain/RPS/Right", getRightRPS());
        SmartDashboard.putNumber("DriveTrain/RPS/Left", getLeftRPS());

        SmartDashboard.putBoolean("DriveTrain/Inverted/Right", Constants.DriveTrain.RIGHT_MOTORS_INVERTED);
        SmartDashboard.putBoolean("DriveTrain/Inverted/Left", Constants.DriveTrain.LEFT_MOTORS_INVERTED);

        SmartDashboard.putNumber("IMU/yaw", imu.getYaw());
        SmartDashboard.putData("IMU", imu);
    }

    @Override
    public void periodic() {

    }
}
