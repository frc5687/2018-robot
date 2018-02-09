package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.DistanceFollower;
import jaci.pathfinder.followers.EncoderFollower;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.subsystems.Arm;
import java.io.File;

public class MoveArmToSetpointTrajectory extends Command {

    private double _target;
    private Arm _arm;
    private Trajectory _trajectory;
    private DistanceFollower _follower;
    private int mV = 138;

    public MoveArmToSetpointTrajectory(Robot robot, double target) {
        _arm = robot.getArm();
        requires(_arm);
        _target = target;
    }

    @Override
    protected boolean isFinished() {
        return _follower.isFinished();
    }


    @Override
    protected void initialize() {
        super.initialize();
        DriverStation.reportError("Starting MoveArmToSetpointTrajectory", false);

        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, 42, 0.02, mV, 200, 200.0);
        double current = _arm.getPot();

        Waypoint[] points = new Waypoint[] {
                new Waypoint(current, current, 0),
                new Waypoint(_target, _target, 0)
        };

        _trajectory = Pathfinder.generate(points, config);
        Pathfinder.writeToCSV(new File("/home/lvuser/bert.csv"), _trajectory);
        _follower = new DistanceFollower(_trajectory);
        _follower.configurePIDVA(
                Constants.Arm.Pot.kP,
                Constants.Arm.Pot.kI,
                Constants.Arm.Pot.kD,
                Constants.Arm.Pot.kV,
                Constants.Arm.Pot.kA
        );
    }

    @Override
    protected void execute() {
        DriverStation.reportError("MoveArmToSetpointTrajectory at " + _arm.getPot(), false);
        double speed = _follower.calculate(_arm.getPot()) * Constants.Arm.Pot.kV;
        try {
            SmartDashboard.putNumber("MoveArmToSetpointTrajectory/idealPos", _follower.getSegment().x);
        } catch (Exception e) {

        }
        SmartDashboard.putNumber("MoveArmToSetpointTrajectory/actualPos", _arm.getPot());
        SmartDashboard.putNumber("MoveArmToSetpointTrajectory/Requested Speed", speed);
        _arm.drive(speed);
    }
}
