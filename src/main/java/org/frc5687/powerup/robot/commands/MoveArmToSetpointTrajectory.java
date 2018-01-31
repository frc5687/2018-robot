package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;
import org.frc5687.powerup.robot.subsystems.Arm;

/**
 * Created by Ben Bernard on 1/28/2018.
 */
public class MoveArmToSetpointTrajectory extends Command {

    private int _target;
    private Arm _arm;
    private Trajectory _trajectory;
    private int _segment = 0;

    public MoveArmToSetpointTrajectory(Arm arm, int target) {
        requires(arm);
        _arm = arm;
        _target = target;
    }

    @Override
    protected boolean isFinished() {
        return _segment >= 49;
    }


    @Override
    protected void initialize() {
        super.initialize();
        DriverStation.reportError("Starting MoveArmToSetpointPID", false);

        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, 50, 0.02, 0.3, 2.0, 60.0);
        double current = _arm.getAngle();

        Waypoint[] points = new Waypoint[] {
                new Waypoint(current, current, 0),
                new Waypoint(_target, _target, 0)
        };

        _trajectory = Pathfinder.generate(points, config);
        _segment = 0;
    }

    @Override
    protected void execute() {
        DriverStation.reportError("MoveArmToSetpointTrajectory at " + _arm.getAngle(), false);
        Trajectory.Segment segment = _trajectory.get(_segment);
        SmartDashboard.putNumber("MoveArmToSetpointTrajectory/velocity", segment.velocity);
        _arm.drive(segment.velocity);
        _segment++;
        // Read the

        /*
        for (int i = 0; i < _trajectory.length(); i++) {
            Trajectory.Segment seg = _trajectory.get(i);

            System.out.printf("%f,%f,%f,%f,%f,%f,%f,%f\n",
                    seg.dt, seg.x, seg.y, seg.position, seg.velocity,
                    seg.acceleration, seg.jerk, seg.heading);
        }
        */

    }
}
