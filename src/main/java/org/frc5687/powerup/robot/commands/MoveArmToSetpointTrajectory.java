package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import org.frc5687.powerup.robot.subsystems.Arm;
import java.io.File;

/**
 * Created by Ben Bernard on 1/28/2018.
 */
public class MoveArmToSetpointTrajectory extends Command {

    private int _target;
    private Arm _arm;
    private Trajectory _trajectory;
    private EncoderFollower _encoderFollower;
    private int _samples = 42;
    private int _max_velocity = 400;

    public MoveArmToSetpointTrajectory(Arm arm, int target) {
        requires(arm);
        _arm = arm;
        _target = target;
    }

    @Override
    protected boolean isFinished() {
        return _encoderFollower.isFinished();
    }


    @Override
    protected void initialize() {
        super.initialize();
        DriverStation.reportError("Starting MoveArmToSetpointTrajectory", false);

        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, _samples, 0.02, _max_velocity, 1000, 1000.0);
        double current = _arm.getAngle();

        Waypoint[] points = new Waypoint[] {
                new Waypoint(current, current, 0),
                new Waypoint(_target, _target, 0)
        };

        _trajectory = Pathfinder.generate(points, config);
        Pathfinder.writeToCSV(new File("/home/lvuser/bert.csv"), _trajectory);
        _encoderFollower = new EncoderFollower(_trajectory);
        _encoderFollower.configureEncoder((int) current, 4096, 4096 / Math.PI);
        _encoderFollower.configurePIDVA(1.0, 0.0, 0.0, 1 / _max_velocity, 0);
    }

    @Override
    protected void execute() {
        DriverStation.reportError("MoveArmToSetpointTrajectory at " + _arm.getAngle(), false);
        double speed = _encoderFollower.calculate(_arm.getAngle());
        SmartDashboard.putNumber("MoveArmToSetpointTrajectory/Requested Speed", speed);
        _arm.drive(speed);
    }
}
