package org.frc5687.powerup.robot.commands.auto;

import com.kauailabs.navx.frc.AHRS;
import com.team254.lib.trajectory.Path;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.TrajectoryFollower;
import com.team254.lib.util.ChezyMath;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.subsystems.DriveTrain;

public class DynamicPathCommand extends Command {
    private TrajectoryFollower followerLeft = new TrajectoryFollower("left");
    private TrajectoryFollower followerRight = new TrajectoryFollower("right");
    private double starting_heading;
    public Path path;
    private DriveTrain _driveTrain;
    private AHRS _imu;
    public double lastHeading;
    private Robot _robot;
    public boolean turnInverted;
    private Thread _thread;

    public double getkT() {
        return Constants.Auto.Drive.TrajectoryFollowing.Cheese.kT;
    }

    class PeriodicRunnable implements java.lang.Runnable {
        private DynamicPathCommand _d;
        long lastRun = 0;

        public PeriodicRunnable(DynamicPathCommand d) {
            _d = d;
        }

        public void run() {
            while (true) {
                try {
                    long now = System.currentTimeMillis();
                    if (now >= lastRun + 10) {
                        DriverStation.reportError("PeriodicRunnable.run() waited enough", false);
                        lastRun = now;
                        _d.processSegment();
                        Thread.sleep(5);
                    } else {
                        Thread.sleep(1);
                    }
                } catch (Exception e) {
                    DriverStation.reportError(e.toString(), true);
                    DriverStation.reportError(e.getStackTrace().toString(), true);
                }
            }
        }
    }
        
    public DynamicPathCommand(Robot robot) {
        _driveTrain = robot.getDriveTrain();
        _imu = robot.getIMU();
        _robot = robot;
        requires(_driveTrain);

        loadPath();

        if (isReversed()) {
            path.reverse();
        }
        _thread = new Thread(new PeriodicRunnable(this));
    }

    public Path getPath() {
        return null;
    }

    protected boolean loadPath() {
        path = getPath();

        return path != null;
    }

    @Override
    protected void initialize() {
        DriverStation.reportError("Starting DynamicPathCommand", false);
        _driveTrain.resetDriveEncoders();
        //_imu.reset();

        starting_heading = _driveTrain.getYaw();

        followerLeft.configure(
                Constants.Auto.Drive.TrajectoryFollowing.Cheese.kP,
                Constants.Auto.Drive.TrajectoryFollowing.Cheese.kI,
                Constants.Auto.Drive.TrajectoryFollowing.Cheese.kD,
                Constants.Auto.Drive.TrajectoryFollowing.Cheese.kV.IPS,
                Constants.Auto.Drive.TrajectoryFollowing.Cheese.kA.INCHES
        );
        followerRight.configure(
                Constants.Auto.Drive.TrajectoryFollowing.Cheese.kP,
                Constants.Auto.Drive.TrajectoryFollowing.Cheese.kI,
                Constants.Auto.Drive.TrajectoryFollowing.Cheese.kD,
                Constants.Auto.Drive.TrajectoryFollowing.Cheese.kV.IPS,
                Constants.Auto.Drive.TrajectoryFollowing.Cheese.kA.INCHES
        );

        followerLeft.setTrajectory(path.getLeftWheelTrajectory());
        followerLeft.reset();
        followerRight.setTrajectory(path.getRightWheelTrajectory());
        followerRight.reset();

        lastHeading = followerLeft.getLastSegment().heading;

        _thread.start();

        SmartDashboard.putBoolean("AADynamicPathCommand/finished", false);
    }

    public void configureFollowerkP(double kp) {
        followerLeft.configurekP(kp);
        followerRight.configurekP(kp);
    }

    public void configurekT(double kt) {

    }

    private double calculateTurn() {
        double goalHeading = followerLeft.getNavxHeading(); // Example: this is 30deg
        //double observedHeading = ChezyMath.getDifferenceInAngleDegrees(_driveTrain.getCheesyYaw(), starting_heading);
        double observedHeading = _driveTrain.getYaw(); // Example: this is 20deg.
        SmartDashboard.putNumber("AADynamicPathCommand/observedHeading", observedHeading);
        SmartDashboard.putNumber("AADynamicPathCommand/goalHeading", goalHeading);
        // Example: We want to be heading 30deg. We are heading 20deg. Our angleDiff of 30deg - 20deg yields 10deg.
        double angleDiff = ChezyMath.getDifferenceInAngleDegrees(observedHeading, goalHeading);
        SmartDashboard.putNumber("AADynamicPathCommand/angleDiff", angleDiff);
        SmartDashboard.putNumber("AADynamicPathCommand/_kT", getkT());
        // Example: angleDiff is 10deg. We multiply that by kT, which if we pretend is 0.8, yields 8. This means we will
        // end up increasing our left speed by 8ips, which will help us turn to the right.
        double turn = getkT() * angleDiff; // multiply by -1 if self correcting, multiply by 1 if following turns
        // Attempts to cap the turn
        /*
        if (turn > 0) {
            turn = Math.min(Constants.Auto.Drive.AnglePID.MAX_DIFFERENCE, turn);
        } else {
            turn = Math.max(-Constants.Auto.Drive.AnglePID.MAX_DIFFERENCE, turn);
        }
        */

        return turn;//turn;
    }

    /**
     * Called by the notifier every 0.02s
     */
    protected void processSegment() {
        DriverStation.reportError("Running processSegment()", false);
        /*
         * Calculate Speed
         */

        double distanceL = _driveTrain.getLeftDistance();
        double distanceR = _driveTrain.getRightDistance();

        double speedLeftMotor = followerLeft.calculate(distanceL, _driveTrain.getLeftVelocityIPS());
        double speedRightMotor = followerRight.calculate(distanceR, _driveTrain.getRightVelocityIPS());

        /*
         * Calculate Speed with Turn Correction
         */
        double turn = calculateTurn();
        double speedLeftMotorWithTurn = speedLeftMotor + turn;
        double speedRightMotorWithTurn = speedRightMotor - turn;

        SmartDashboard.putNumber("AADynamicPathCommand/turn", turn);
        SmartDashboard.putNumber("AADynamicPathCommand/left/totalIPS", speedLeftMotorWithTurn);
        SmartDashboard.putNumber("AADynamicPathCommand/right/totalIPS", speedRightMotorWithTurn);

        /*
         * Drive
         */

        _driveTrain.setVelocityIPS(speedLeftMotorWithTurn, speedRightMotorWithTurn);
    }

    @Override
    protected void end() {
        SmartDashboard.putBoolean("AADynamicPathCommand/finished", true);
        DriverStation.reportError("DynamicPathCommand ended", false);
        _driveTrain.setPower(0, 0);
        _thread.stop();
        DriverStation.reportError("ran stop() method on notifier", false);
    }

    @Override
    protected void interrupted() {
        end();
    }

    @Override
    protected boolean isFinished() {
        return followerLeft.isFinishedTrajectory() && followerRight.isFinishedTrajectory();
    }

    public boolean isReversed() {
        return false;
    }
}
