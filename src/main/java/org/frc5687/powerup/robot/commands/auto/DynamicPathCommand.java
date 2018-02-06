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
import org.frc5687.powerup.robot.utils.Helpers;

public class DynamicPathCommand extends Command {
    private TrajectoryFollower followerLeft = new TrajectoryFollower();
    private TrajectoryFollower followerRight = new TrajectoryFollower();
    private double starting_heading;
    public Path path;
    private DriveTrain _driveTrain;
    private AHRS _imu;
        
    public DynamicPathCommand(Robot robot) {
        _driveTrain = robot.getDriveTrain();
        _imu = robot.getIMU();
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
        _driveTrain.resetDriveEncoders();
        _imu.reset();

        starting_heading = -_driveTrain.getYaw();
        followerLeft.configure(
                Constants.Auto.Drive.EncoderPID.kP,
                Constants.Auto.Drive.EncoderPID.kI,
                Constants.Auto.Drive.EncoderPID.kD,
                Constants.Auto.Drive.EncoderPID.kV.IPS,
                Constants.Auto.Drive.EncoderPID.kA
        );
        followerRight.configure(
                Constants.Auto.Drive.EncoderPID.kP,
                Constants.Auto.Drive.EncoderPID.kI,
                Constants.Auto.Drive.EncoderPID.kD,
                Constants.Auto.Drive.EncoderPID.kV.IPS,
                Constants.Auto.Drive.EncoderPID.kA
        );

        followerLeft.setTrajectory(path.getLeftWheelTrajectory());
        followerLeft.reset();
        followerRight.setTrajectory(path.getRightWheelTrajectory());
        followerRight.reset();
    }


    @Override
    protected void execute() {
        double distanceL = _driveTrain.getLeftDistance();
        double distanceR = _driveTrain.getRightDistance();
        SmartDashboard.putNumber("AADynamicPathCommand/distanceL", distanceL);
        SmartDashboard.putNumber("AADynamicPathCommand/distanceR", distanceR);

        Trajectory.Segment left = followerLeft.getSegment();
        Trajectory.Segment right = followerRight.getSegment();

        SmartDashboard.putNumber("AADynamicPathCommand/errorLeft", left.pos - distanceL);
        SmartDashboard.putNumber("AADynamicPathCommand/errorRight", right.pos - distanceR);

        double speedLeft = followerLeft.calculate(distanceL);
        double speedRight = followerRight.calculate(distanceR);

        speedLeft *= Constants.Auto.Drive.EncoderPID.kV.IPS;
        speedRight *= Constants.Auto.Drive.EncoderPID.kV.IPS;

        /*
        if (speedLeft < 0) {
            speedLeft = speedLeft > -MOTOR_MIN ? -MOTOR_MIN : speedLeft;
        } else {
            speedLeft = speedLeft < MOTOR_MIN ? MOTOR_MIN : speedLeft;
        }

        if (speedRight < 0) {
            speedRight = speedRight > -MOTOR_MIN ? -MOTOR_MIN : speedRight;
        } else {
            speedRight = speedRight < MOTOR_MIN ? MOTOR_MIN : speedRight;
        }
        */

        speedLeft = Helpers.applyMinSpeed(speedLeft, Constants.Auto.Drive.MIN_SPEED);
        speedRight = Helpers.applyMinSpeed(speedRight, Constants.Auto.Drive.MIN_SPEED);

        double goalHeading = Math.toDegrees(followerLeft.getHeading());
        double observedHeading = ChezyMath.getDifferenceInAngleDegrees(-_driveTrain.getYaw(), starting_heading);
        double angleDiff = ChezyMath.getDifferenceInAngleDegrees(observedHeading, goalHeading);
        SmartDashboard.putNumber("AADynamicPathCommand/angleDiff", angleDiff);

        double turn = 0.8 * (-1.0 / 80.0) * angleDiff;
        turn *= Constants.Auto.Drive.AnglePID.kV.IPS;
        double requestedLeft = speedLeft + turn;
        double requestedRight = speedRight - turn;

        SmartDashboard.putNumber("AADynamicPathCommand/requestLeft", requestedLeft);
        SmartDashboard.putNumber("AADynamicPathCommand/requestedRight", requestedRight);

        _driveTrain.tankDrive(requestedLeft, requestedRight);
    }

    @Override
    protected void end() {
        SmartDashboard.putNumber("AADynamicPathCommand/requestLeft", 0);
        SmartDashboard.putNumber("AADynamicPathCommand/requestedRight", 0);
        DriverStation.reportError("DynamicPathCommand ended", false);
        DriverStation.reportError("DynamicPathCommand ended", false);
        DriverStation.reportError("DynamicPathCommand ended", false);
        DriverStation.reportError("DynamicPathCommand ended", false);
        DriverStation.reportError("DynamicPathCommand ended", false);
        _driveTrain.tankDrive(0, 0);
    }

    @Override
    protected void interrupted() {
        end();
    }

    @Override
    protected boolean isFinished() {
        return followerLeft.isFinishedTrajectory() && 
                followerRight.isFinishedTrajectory();
    }

    public boolean isReversed() {
        return false;
    }

}
