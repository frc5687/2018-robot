package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.subsystems.DriveTrain;

public class AutoDrivePathfinder extends Command {
    private DriveTrain _driveTrain;
    private double _target;
    private Trajectory _trajectory;
    private EncoderFollower _leftEncoderFollower;
    private EncoderFollower _rightEncoderFollower;

    public AutoDrivePathfinder(DriveTrain driveTrain, double target) {
        requires(driveTrain);
        _driveTrain = driveTrain;
        _target = target;

        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_FAST, 0.02, Constants.Auto.Drive.MAX_VEL, 1000, 1000.0);
        double current = 0;

        Waypoint[] points = new Waypoint[] {
                new Waypoint(current, current, 0),
                new Waypoint(_target, _target, 0)
        };

        _trajectory = Pathfinder.generate(points, config);

        TankModifier modifier = new TankModifier(_trajectory).modify(Constants.Encoders.Defaults.TRACK);

        _leftEncoderFollower = new EncoderFollower(modifier.getLeftTrajectory());
        _rightEncoderFollower = new EncoderFollower(modifier.getRightTrajectory());

        _leftEncoderFollower.configureEncoder((int) _driveTrain.getLeftTicks(), Constants.Encoders.Defaults.PULSES_PER_ROTATION, Constants.Encoders.Defaults.WHEEL_DIAMETER);
        _rightEncoderFollower.configureEncoder((int) _driveTrain.getRightTicks(), Constants.Encoders.Defaults.PULSES_PER_ROTATION, Constants.Encoders.Defaults.WHEEL_DIAMETER);

        _leftEncoderFollower.configurePIDVA(Constants.Auto.Drive.EncoderPID.kP, Constants.Auto.Drive.EncoderPID.kI, Constants.Auto.Drive.EncoderPID.kD, Constants.Auto.Drive.EncoderPID.kV, Constants.Auto.Drive.EncoderPID.kA);
        _rightEncoderFollower.configurePIDVA(Constants.Auto.Drive.EncoderPID.kP, Constants.Auto.Drive.EncoderPID.kI, Constants.Auto.Drive.EncoderPID.kD, Constants.Auto.Drive.EncoderPID.kV, Constants.Auto.Drive.EncoderPID.kA);
    }

    @Override
    protected void execute() {
        double leftSpeed = _leftEncoderFollower.calculate((int) _driveTrain.getLeftTicks());
        double rightSpeed = _rightEncoderFollower.calculate((int) _driveTrain.getRightTicks());

        double heading = Robot.imu.getYaw();
        double desired_heading = Pathfinder.r2d(_leftEncoderFollower.getHeading());

        double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - heading);
        double turn = 0.8 * (-1.0 / 80.0) * angleDifference;

        _driveTrain.tankDrive(leftSpeed + turn, rightSpeed - turn);
    }

    @Override
    protected boolean isFinished() {
        return _leftEncoderFollower.isFinished() && _rightEncoderFollower.isFinished();
    }
}
