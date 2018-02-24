package org.frc5687.powerup.robot.commands.auto;

import com.kauailabs.navx.frc.AHRS;
import com.team254.lib.trajectory.Path;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.TrajectoryFollower;
import com.team254.lib.util.ChezyMath;
import edu.wpi.first.wpilibj.DriverStation;
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
        
    public DynamicPathCommand(Robot robot) {
        _driveTrain = robot.getDriveTrain();
        _imu = robot.getIMU();
        _robot = robot;
        requires(_driveTrain);

        loadPath();
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
        _imu.reset();

        starting_heading = _driveTrain.getCheesyYaw();

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

        SmartDashboard.putBoolean("AADynamicPathCommand/finished", false);
    }

    private double calculateTurn() {
        double goalHeading = Math.toDegrees(followerLeft.getHeading());
        double observedHeading = ChezyMath.getDifferenceInAngleDegrees(_driveTrain.getCheesyYaw(), starting_heading);
        SmartDashboard.putNumber("AADynamicPathCommand/observedHeading", observedHeading);
        SmartDashboard.putNumber("AADynamicPathCommand/goalHeading", goalHeading);
        double angleDiff = ChezyMath.getDifferenceInAngleDegrees(observedHeading, goalHeading);
        SmartDashboard.putNumber("AADynamicPathCommand/angleDiff", angleDiff);

        double turn = Constants.Auto.Drive.EncoderPID.kT * angleDiff * 1; // multiply by -1 if self correcting, multiply by 1 if following turns

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


    @Override
    protected void execute() {
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

        /*
         * Drive
         */

        _driveTrain.setVelocityIPS(speedLeftMotorWithTurn, speedRightMotorWithTurn);
        //_driveTrain.setVelocityIPS(speedLeftMotor, speedRightMotor);
    }

    @Override
    protected void end() {
        SmartDashboard.putBoolean("AADynamicPathCommand/finished", true);
        DriverStation.reportError("DynamicPathCommand ended", false);
        _driveTrain.setPower(0, 0);
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
