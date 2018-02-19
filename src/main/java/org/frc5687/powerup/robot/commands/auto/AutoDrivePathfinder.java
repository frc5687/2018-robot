package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.subsystems.DriveTrain;

import java.io.File;

public class AutoDrivePathfinder extends Command {
    private DriveTrain _driveTrain;
    private Trajectory _trajectory;
    private EncoderFollower _leftEncoderFollower;
    private EncoderFollower _rightEncoderFollower;
    private long endTime;

    public AutoDrivePathfinder(DriveTrain driveTrain, Waypoint[] points) {
        requires(driveTrain);
        _driveTrain = driveTrain;

        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_FAST, 0.02, Constants.Auto.Drive.MaxVel.MPS, Constants.Auto.Drive.MaxAcceleration.METERS, Constants.Auto.Drive.MaxJerk.METERS);
        _trajectory = Pathfinder.generate(points, config);

        Pathfinder.writeToCSV(new File("/home/lvuser/bert.csv"), _trajectory);

        TankModifier modifier = new TankModifier(_trajectory).modify(Constants.Encoders.Defaults.Track.METERS);

        _leftEncoderFollower = new EncoderFollower(modifier.getLeftTrajectory());
        _rightEncoderFollower = new EncoderFollower(modifier.getRightTrajectory());

        _leftEncoderFollower.configureEncoder((int) _driveTrain.getLeftTicks(), Constants.Encoders.Defaults.PULSES_PER_ROTATION, Constants.Encoders.Defaults.WheelDiameter.METERS);
        _rightEncoderFollower.configureEncoder((int) _driveTrain.getRightTicks(), Constants.Encoders.Defaults.PULSES_PER_ROTATION, Constants.Encoders.Defaults.WheelDiameter.METERS);

        _leftEncoderFollower.configurePIDVA(Constants.Auto.Drive.EncoderPID.kP, Constants.Auto.Drive.EncoderPID.kI, Constants.Auto.Drive.EncoderPID.kD, Constants.Auto.Drive.EncoderPID.kV.MPS, Constants.Auto.Drive.EncoderPID.kA.METERS);
        _rightEncoderFollower.configurePIDVA(Constants.Auto.Drive.EncoderPID.kP, Constants.Auto.Drive.EncoderPID.kI, Constants.Auto.Drive.EncoderPID.kD, Constants.Auto.Drive.EncoderPID.kV.MPS, Constants.Auto.Drive.EncoderPID.kA.METERS);
    }
    @Override
    protected void initialize(){
        endTime = System.currentTimeMillis() + 15000;
    }

    @Override
    protected void execute() {
        double leftSpeed = _leftEncoderFollower.calculate((int) _driveTrain.getLeftTicks());
        double rightSpeed = _rightEncoderFollower.calculate((int) _driveTrain.getRightTicks());

        double heading = -Robot.imu.getYaw();
        double desired_heading = Pathfinder.r2d(_leftEncoderFollower.getHeading());

        double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - heading);
        SmartDashboard.putNumber("AutoDrivePF/angleDifferent", angleDifference);
        double turn = 0.8 * (-1.0 / 80.0) * angleDifference;
        SmartDashboard.putNumber("AutoDrivePF/turn", turn);

        leftSpeed += turn;
        rightSpeed -= turn;

        SmartDashboard.putNumber("AutoDrivePF/leftSpeed", leftSpeed);
        SmartDashboard.putNumber("AutoDrivePF/rightSpeed", rightSpeed);

        _driveTrain.tankDrive(leftSpeed, rightSpeed);
    }

    @Override
    protected void end() {
        _driveTrain.tankDrive(0, 0);
    }

    @Override
    protected boolean isFinished() {
        if(System.currentTimeMillis()<endTime){
            DriverStation.reportError("AutoDrivePathfinder timed out",false);
        }
        boolean finished = _leftEncoderFollower.isFinished() && _rightEncoderFollower.isFinished();
        SmartDashboard.putBoolean("AutoDrivePF/finished", finished);
        return finished;
    }
}
