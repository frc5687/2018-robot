package org.frc5687.powerup.robot.commands.auto;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.Constants.Auto.Drive;
import org.frc5687.powerup.robot.subsystems.DriveTrain;

public class AutoDrive extends Command {

    private double distance;
    private double speed;
    private PIDController distanceController;
    private PIDController angleController;
    private PIDListener distancePID;
    private PIDListener anglePID;
    private long endMillis;
    private long maxMillis;

    private boolean usePID;
    private boolean stopOnFinish;
    private double angle;

    private String debug;

    private DriveTrain driveTrain;
    private AHRS imu;

    public AutoDrive(DriveTrain driveTrain, AHRS imu, double distance, double speed, String debug) {
        this(driveTrain, imu, distance, speed, false, true, 0, debug);
    }

    public AutoDrive(DriveTrain driveTrain, AHRS imu, double distance, double speed, long maxMillis, String debug) {
        this(driveTrain, imu, distance, speed, false, true, 1000, maxMillis, debug);
    }

    public AutoDrive(DriveTrain driveTrain, AHRS imu, double distance, double speed, boolean usePID, boolean stopOnFinish, long maxMillis, String debug) {
        this(driveTrain, imu, distance, speed, usePID, stopOnFinish, 1000, maxMillis, debug);
    }

    /***
     * Drives for a set distance at a set speed.
     * @param distance Distance to drive
     * @param speed Speed to drive
     * @param usePID Whether to use pid or not
     * @param stopOnFinish Whether to stop the motors when we are done
     */
    public AutoDrive(DriveTrain driveTrain, AHRS imu, double distance, double speed, boolean usePID, boolean stopOnFinish, double angle, long maxMillis, String debug) {
        requires(driveTrain);
        this.speed = speed;
        this.distance = distance;
        this.usePID = usePID;
        this.stopOnFinish = stopOnFinish;
        this.angle = angle;
        this.maxMillis = maxMillis;
        this.debug = debug;
        this.driveTrain = driveTrain;
        this.imu = imu;
    }

    @Override
    protected void initialize() {
        this.endMillis = maxMillis == 0 ? Long.MAX_VALUE : System.currentTimeMillis() + maxMillis;
        driveTrain.resetDriveEncoders();
        if (usePID) {
            distancePID = new PIDListener();
            SmartDashboard.putNumber("AutoDrive/kP", Drive.EncoderPID.kP);
            SmartDashboard.putNumber("AutoDrive/kI", Drive.EncoderPID.kI);
            SmartDashboard.putNumber("AutoDrive/kD", Drive.EncoderPID.kD);
            SmartDashboard.putNumber("AutoDrive/kT", Drive.EncoderPID.TOLERANCE);

            distanceController = new PIDController(Drive.EncoderPID.kP, Drive.EncoderPID.kI, Drive.EncoderPID.kD, driveTrain, distancePID);
            //        distanceController.setPID(SmartDashboard.getNumber("DB/Slider 0", 0), SmartDashboard.getNumber("DB/Slider 1", 0), SmartDashboard.getNumber("DB/Slider 2", 0));
            distanceController.setAbsoluteTolerance(Drive.EncoderPID.TOLERANCE);
            distanceController.setOutputRange(-speed, speed);
            distanceController.setSetpoint(distance);
            distanceController.enable();
        }

        anglePID = new PIDListener();
        angleController = new PIDController(Drive.AnglePID.kP, Drive.AnglePID.kI, Drive.AnglePID.kD, imu, anglePID);
//        angleController.setPID(SmartDashboard.getNumber("DB/Slider 0", 0), SmartDashboard.getNumber("DB/Slider 1", 0), SmartDashboard.getNumber("DB/Slider 2", 0));
        angleController.setInputRange(Constants.Auto.MIN_IMU_ANGLE, Constants.Auto.MAX_IMU_ANGLE);
        double maxSpeed = speed * Drive.AnglePID.MAX_DIFFERENCE;
        SmartDashboard.putNumber("AutoDrive/angleMaxSpeed", maxSpeed);
        SmartDashboard.putNumber("AutoDrive/setPoint", driveTrain.getYaw());
        angleController.setOutputRange(-maxSpeed, maxSpeed);
        angleController.setContinuous();
        // If an angle is supplied, use that as our setpoint.  Otherwise get the current heading and stick to it!
        angleController.setSetpoint(angle==1000?driveTrain.getYaw():angle);
        angleController.enable();

        DriverStation.reportError("Auto Drive initialized: " + (debug==null?"":debug), false);
    }

    @Override
    protected void execute() {
        double distanceFactor = 0;
        double angleFactor = 0;
        if (usePID) {
            synchronized (distancePID) {
                distanceFactor = distancePID.get();
            }
        } else {
            distanceFactor = distance > 0 ? speed : -speed;
        }

        synchronized (anglePID) {
            angleFactor = anglePID.get();
        }
        SmartDashboard.putNumber("AutoDrive/distanceFactor", distanceFactor);
        SmartDashboard.putNumber("AutoDrive/angleFactor", angleFactor);

        driveTrain.setPower(distanceFactor + angleFactor, distanceFactor - angleFactor);

        SmartDashboard.putBoolean("AutoDrive/onTarget", distanceController == null ? false : distanceController.onTarget());
        SmartDashboard.putNumber("AutoDrive/imu", driveTrain.getYaw());
        SmartDashboard.putNumber("AutoDrive/distance", driveTrain.pidGet());
        SmartDashboard.putNumber("AutoDrive/turnPID", anglePID.get());
    }

    @Override
    protected boolean isFinished() {
        if (maxMillis>0 && endMillis!=Long.MAX_VALUE && System.currentTimeMillis() > endMillis) {
            DriverStation.reportError("AutoDrive for " + maxMillis + " timed out.", false);
            return true; }
        if (usePID) {
            return distanceController.onTarget();
        } else {

            return distance == 0 ? true : distance < 0 ? (driveTrain.getDistance() < distance) : (driveTrain.getDistance() >  distance);
        }
    }

    @Override
    protected void end() {
        DriverStation.reportError("AutoDrive Finished (" + driveTrain.getDistance() + ", " + (driveTrain.getYaw() - angleController.getSetpoint()) + ") " + (debug==null?"":debug), false);
        angleController.disable();
        if (distanceController!=null) {
            distanceController.disable();
        }
        if (stopOnFinish) {
            DriverStation.reportError("Stopping.", false);
            //driveTrain.setPower(0, 0, true);
        }
    }

    private class PIDListener implements PIDOutput {

        private double value;

        public double get() {
            return value;
        }

        @Override
        public void pidWrite(double output) {
            synchronized (this) {
                value = output;
            }
        }

    }

}
