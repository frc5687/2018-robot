package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import org.frc5687.powerup.robot.subsystems.Arm;
import org.frc5687.powerup.robot.subsystems.DriveTrain;
import org.opencv.core.KeyPoint;

/**
 * Created by Ben Bernard on 1/28/2018.
 */
public class MoveArmToSetpointPID extends Command {

    private double _target;
    private Arm _arm;



    public MoveArmToSetpointPID(Arm arm, double target) {
        requires(arm);
        _arm = arm;
        _target = target;
    }

    @Override
    protected boolean isFinished() {
        if (_arm.onTarget()) {
            _arm.disable();
            return true;
        }
        return false;
    }


    @Override
    protected void initialize() {
        super.initialize();

        DriverStation.reportError("Starting MoveArmToSetpointPID", false);
        _arm.setSetpoint(_target);
        _arm.enable();
    }

    @Override
    protected void execute() {
        DriverStation.reportError("MoveArmToSetpointPID at " + _arm.getAngle(), false);
    }
}
