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
    private TrajectoryFollower followerLeft = new TrajectoryFollower("left");
    private TrajectoryFollower followerRight = new TrajectoryFollower("right");
    private double starting_heading;
    public Path path;
    private DriveTrain _driveTrain;
    private AHRS _imu;
    public double lastHeading;
    public long lastExecute;
        
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

        starting_heading = _driveTrain.getCheesyYaw();

        followerLeft.configure(
                Constants.Auto.Drive.EncoderPID.kP,
                Constants.Auto.Drive.EncoderPID.kI,
                Constants.Auto.Drive.EncoderPID.kD,
                Constants.Auto.Drive.EncoderPID.kV.IPS,
                Constants.Auto.Drive.EncoderPID.kA.INCHES
        );
        followerRight.configure(
                Constants.Auto.Drive.EncoderPID.kP,
                Constants.Auto.Drive.EncoderPID.kI,
                Constants.Auto.Drive.EncoderPID.kD,
                Constants.Auto.Drive.EncoderPID.kV.IPS,
                Constants.Auto.Drive.EncoderPID.kA.INCHES
        );

        followerLeft.setTrajectory(path.getLeftWheelTrajectory());
        followerLeft.reset();
        followerRight.setTrajectory(path.getRightWheelTrajectory());
        followerRight.reset();

        lastHeading = followerLeft.getLastSegment().heading;

        lastExecute = System.currentTimeMillis();

        SmartDashboard.putBoolean("AADynamicPathCommand/finished", false);
    }

    private double calculateTurn() {
        double goalHeading = Math.toDegrees(followerLeft.getHeading());
        double observedHeading = ChezyMath.getDifferenceInAngleDegrees(_driveTrain.getCheesyYaw(), starting_heading);
        SmartDashboard.putNumber("AADynamicPathCommand/observedHeading", observedHeading);
        SmartDashboard.putNumber("AADynamicPathCommand/goalHeading", goalHeading);
        double angleDiff = ChezyMath.getDifferenceInAngleDegrees(observedHeading, goalHeading);
        SmartDashboard.putNumber("AADynamicPathCommand/angleDiff", angleDiff);

        double turn = Constants.Auto.Drive.EncoderPID.kT * angleDiff * -1; // multiply by -1 if self correcting, multiply by 1 if following turns

        // Attempts to cap the turn
        /*
        if (turn > 0) {
            turn = Math.min(Constants.Auto.Drive.AnglePID.MAX_DIFFERENCE, turn);
        } else {
            turn = Math.max(-Constants.Auto.Drive.AnglePID.MAX_DIFFERENCE, turn);
        }
        */

        return turn;
    }


    @Override
    protected void execute() {
        /*
         * Log time since last execute
         */
        long now = System.currentTimeMillis();
        SmartDashboard.putNumber("AADynamicPathCommand/timeSinceLastExec", now - lastExecute);
        lastExecute = now;

        /*
         * Log Left & Right Distance
         */
        Trajectory.Segment left = followerLeft.getSegment();
        Trajectory.Segment right = followerRight.getSegment();

        double distanceL = _driveTrain.getLeftDistance();
        double distanceR = _driveTrain.getRightDistance();

        double goalDistanceLeft = left.pos;
        double goalDistanceRight = right.pos;

        SmartDashboard.putNumber("AADynamicPathCommand/distanceL", distanceL);
        SmartDashboard.putNumber("AADynamicPathCommand/distanceR", distanceR);

        SmartDashboard.putNumber("AADynamicPathCommand/goalDistanceLeft", goalDistanceLeft);
        SmartDashboard.putNumber("AADynamicPathCommand/goalDistanceRight", goalDistanceRight);

        /*
         * Log Distance Error
         */

        double leftError = left.pos - distanceL;
        double rightError = right.pos - distanceR;

        SmartDashboard.putNumber("AADynamicPathCommand/errorLeft", leftError);
        SmartDashboard.putNumber("AADynamicPathCommand/errorRight", rightError);

        /*
         * Log Goal Velocity
         */

        /*
        double goalVelocityLeftIPS = left.vel;
        double goalVelocityLeftMotor = goalVelocityLeftIPS * Constants.Auto.Drive.EncoderPID.kV.IPS;

        double goalVelocityRightIPS = right.vel;
        double goalVelocityRightMotor  = goalVelocityRightIPS * Constants.Auto.Drive.EncoderPID.kV.IPS;

        SmartDashboard.putNumber("AADynamicPathCommand/goalVelocityLeftMotor", goalVelocityLeftMotor);
        SmartDashboard.putNumber("AADynamicPathCommand/goalVelocityLeftIPS", goalVelocityLeftIPS);

        SmartDashboard.putNumber("AADynamicPathCommand/goalVelocityRightMotor", goalVelocityRightMotor);
        SmartDashboard.putNumber("AADynamicPathCommand/goalVelocityRightIPS", goalVelocityRightIPS);

        */

        // Entirely feed forward
        //_driveTrain.tankDrive(goalVelocityLeftMotor, goalVelocityRightMotor);

        /*
         * Log Calculated Speed
         */

        double speedLeftMotor = followerLeft.calculate(distanceL, _driveTrain.getLeftRate());
        double speedRightMotor = followerRight.calculate(distanceR, _driveTrain.getRightRate());

        SmartDashboard.putNumber("AADynamicPathCommand/speedLeftMotor", speedLeftMotor);
        SmartDashboard.putNumber("AADynamicPathCommand/speedRightMotor", speedRightMotor);

        // Feed Forward + PID for Sides
        //_driveTrain.tankDrive(speedLeftMotor, speedRightMotor);

        /*
         * Log Turn
         */
        double turn = calculateTurn();
        double speedLeftMotorWithTurn = speedLeftMotor + turn;
        double speedRightMotorWithTurn = speedRightMotor - turn;

        _driveTrain.tankDrive(speedLeftMotorWithTurn, speedRightMotorWithTurn);

        SmartDashboard.putNumber("AADynamicPathCommand/turn", turn);
        SmartDashboard.putNumber("AADynamicPathCommand/speedLeftMotorWithTurn", speedLeftMotorWithTurn);
        SmartDashboard.putNumber("AADynamicPathCommand/speedRightMotorWithTurn", speedRightMotorWithTurn);

        /*
         * Drive
         */

        //_driveTrain.tankDrive(speedLeftMotorWithTurn, speedRightMotorWithTurn);
    }

    @Override
    protected void end() {
        SmartDashboard.putBoolean("AADynamicPathCommand/finished", true);
        DriverStation.reportError("DynamicPathCommand ended", false);
        _driveTrain.tankDrive(0, 0);
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
