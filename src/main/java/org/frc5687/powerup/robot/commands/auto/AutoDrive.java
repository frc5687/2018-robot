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

    private long settleTime = 40;
    private long settleEnd = 0;
    private boolean usePID;
    private boolean stopOnFinish;
    private double angle;

    private String debug;

    private DriveTrain driveTrain;
    private AHRS imu;

    private double kPdistance = 0.05; // .07;
    private double kIdistance = 0.0; // .001;
    private double kDdistance = 0.2; //.1;
    private double kTdistance = 0.5;

    private double kPangle = .1;
    private double kIangle = .01;
    private double kDangle = 0;
    private double kTangle;


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
     * @param angle The angle to drive, in degrees.  Pass 1000 to maintain robot's hading.
     * @param maxMillis Maximum time in millis to allow the command to run
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
        driveTrain.enableBrakeMode();
        if (usePID) {
            distancePID = new PIDListener();
            SmartDashboard.putNumber("AutoDrive/kP", kPdistance);
            SmartDashboard.putNumber("AutoDrive/kI", kIdistance);
            SmartDashboard.putNumber("AutoDrive/kD", kDdistance);
            SmartDashboard.putNumber("AutoDrive/kT", kTdistance);

            distanceController = new PIDController(kPdistance, kIdistance, kDdistance, speed, driveTrain, distancePID);
            distanceController.setAbsoluteTolerance(kTdistance);
            distanceController.setOutputRange(-speed, speed);
            distanceController.setSetpoint(distance);
            distanceController.enable();
        }

        anglePID = new PIDListener();
        angleController = new PIDController(kPangle, kIangle, kDangle, imu, anglePID);
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

        driveTrain.setPower(distanceFactor + angleFactor, distanceFactor - angleFactor, true);

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
            if (distanceController.onTarget()) {
                if (settleTime == 0) {
                    DriverStation.reportError("AutoDrive nosettle complete at " + driveTrain.getDistance() + " inches", false);
                    return true;
                }
                if (settleEnd > 0) {
                    if (System.currentTimeMillis() > settleEnd) {
                        DriverStation.reportError("AutoDrive settled at " + driveTrain.getDistance() + " inches", false);
                        return true;
                    }
                } else {
                    DriverStation.reportError("AutoDrive settling for " + settleTime + "ms", false);
                    settleEnd = System.currentTimeMillis() + settleTime;
                }
            } else {
                if (settleEnd > 0) {
                    DriverStation.reportError("AutoDrive unsettled at " + driveTrain.getDistance() + " inches", false);
                    settleEnd = 0;
                }
            }
        } else {
            DriverStation.reportError("AutoDrive nopid complete at " + driveTrain.getDistance() + " inches", false);
            return distance == 0 ? true : distance < 0 ? (driveTrain.getDistance() < distance) : (driveTrain.getDistance() >  distance);
        }
        return false;
    }

    @Override
    protected void end() {
        DriverStation.reportError("AutoDrive Finished (" + driveTrain.getDistance() + ", " + (driveTrain.getYaw() - angleController.getSetpoint()) + ") " + (debug==null?"":debug), false);
        angleController.disable();
        if (distanceController!=null) {
            distanceController.disable();
        }
        if (stopOnFinish) {
            DriverStation.reportError("Stopping at ." + driveTrain.getDistance(), false);
            driveTrain.enableBrakeMode();
            driveTrain.setPower(0, 0, true);
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
