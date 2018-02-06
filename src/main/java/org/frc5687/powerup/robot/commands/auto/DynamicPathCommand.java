package org.frc5687.powerup.robot.commands.auto;

import com.kauailabs.navx.frc.AHRS;
import com.team254.lib.trajectory.Path;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.TrajectoryFollower;
import com.team254.lib.util.ChezyMath;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.subsystems.DriveTrain;
import org.frc5687.powerup.robot.utils.Helpers;

public class DynamicPathCommand extends Command {
    private PIDController angleController;
    private PIDListener anglePID;
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

        anglePID = new PIDListener();
        angleController = new PIDController(Constants.Auto.Drive.AnglePID.kP, Constants.Auto.Drive.AnglePID.kI, Constants.Auto.Drive.AnglePID.kD, Robot.imu, anglePID);
        angleController.setInputRange(Constants.Auto.MIN_IMU_ANGLE, Constants.Auto.MAX_IMU_ANGLE);
        double maxAngleSpeed = 1.0 * Constants.Auto.Drive.AnglePID.MAX_DIFFERENCE;
        angleController.setOutputRange(-maxAngleSpeed, maxAngleSpeed);
        angleController.setContinuous();
        angleController.setSetpoint(starting_heading);
        angleController.enable();

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

        SmartDashboard.putNumber("AADynamicPathCommand/goalVelocityLeft", left.vel * Constants.Auto.Drive.EncoderPID.kV.IPS);
        SmartDashboard.putNumber("AADynamicPathCommand/goalVelocityRight", right.vel * Constants.Auto.Drive.EncoderPID.kV.IPS);
        SmartDashboard.putNumber("AADynamicPathCommand/errorLeft", left.pos - distanceL);
        SmartDashboard.putNumber("AADynamicPathCommand/errorRight", right.pos - distanceR);

        double speedLeft = followerLeft.calculate(distanceL);
        double speedRight = followerRight.calculate(distanceR);

        SmartDashboard.putNumber("AADynamicPathCommand/speedLeftIPS", speedLeft);
        SmartDashboard.putNumber("AADynamicPathCommand/speedRightIPS", speedRight);

        speedLeft *= Constants.Auto.Drive.EncoderPID.kV.IPS;
        speedRight *= Constants.Auto.Drive.EncoderPID.kV.IPS;

        /*
        if (speedLeft < 0) {
            speedLeft = speedLeft > -Constants.Auto.Drive.MIN_SPEED ? -Constants.Auto.Drive.MIN_SPEED : speedLeft;
        } else {
            speedLeft = speedLeft < Constants.Auto.Drive.MIN_SPEED ? Constants.Auto.Drive.MIN_SPEED : speedLeft;
        }

        if (speedRight < 0) {
            speedRight = speedRight > -Constants.Auto.Drive.MIN_SPEED ? -Constants.Auto.Drive.MIN_SPEED : speedRight;
        } else {
            speedRight = speedRight < Constants.Auto.Drive.MIN_SPEED ? Constants.Auto.Drive.MIN_SPEED : speedRight;
        }
        */

        //speedLeft = Helpers.applyMinSpeed(speedLeft, Constants.Auto.Drive.MIN_SPEED);
        //speedRight = Helpers.applyMinSpeed(speedRight, Constants.Auto.Drive.MIN_SPEED);

        SmartDashboard.putNumber("AADynamicPathCommand/speedLeft", speedLeft);
        SmartDashboard.putNumber("AADynamicPathCommand/speedRight", speedRight);

        double goalHeading = Math.toDegrees(followerLeft.getHeading());
        double observedHeading = ChezyMath.getDifferenceInAngleDegrees(-_driveTrain.getYaw(), starting_heading);
        SmartDashboard.putNumber("AADynamicPathCommand/observedHeading", observedHeading);
        SmartDashboard.putNumber("AADynamicPathCommand/goalHeading", goalHeading);
        double angleDiff = ChezyMath.getDifferenceInAngleDegrees(observedHeading, goalHeading);
        SmartDashboard.putNumber("AADynamicPathCommand/angleDiff", angleDiff);

        double turn = 0;//Constants.Auto.Drive.AnglePID.PATH_TURN * Constants.Auto.Drive.AnglePID.kV.IPS * angleDiff * -1;
        //angleController.setSetpoint(left.heading);
        //double turn = anglePID.get();

        SmartDashboard.putNumber("AADynamicPathCommand/turn", turn);
        /*
        if (turn > 0) {
            turn = Math.min(Constants.Auto.Drive.AnglePID.MAX_DIFFERENCE, turn);
        } else {
            turn = Math.max(-Constants.Auto.Drive.AnglePID.MAX_DIFFERENCE, turn);
        }
        */

        double requestedLeft = speedLeft + turn;
        double requestedRight = speedRight - turn;

        SmartDashboard.putNumber("AADynamicPathCommand/requestLeft", requestedLeft);
        SmartDashboard.putNumber("AADynamicPathCommand/requestedRight", requestedRight);

        _driveTrain.tankDrive(requestedLeft, requestedRight);
        SmartDashboard.putBoolean("AADynamicPathCommand/finished", false);
    }

    @Override
    protected void end() {
        //SmartDashboard.putNumber("AADynamicPathCommand/requestLeft", 0);
        //SmartDashboard.putNumber("AADynamicPathCommand/requestedRight", 0);
        SmartDashboard.putBoolean("AADynamicPathCommand/finished", true);
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
