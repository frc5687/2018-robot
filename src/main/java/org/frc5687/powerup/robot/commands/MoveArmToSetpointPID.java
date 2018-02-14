package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.subsystems.Arm;
import org.frc5687.powerup.robot.subsystems.DriveTrain;
import org.opencv.core.KeyPoint;

/**
 * Created by Ben Bernard on 1/28/2018.
 */
public class MoveArmToSetpointPID extends Command {

    private double _target;
    private Arm _arm;
    private boolean _wait;


    public MoveArmToSetpointPID(Arm arm, double target, boolean wait) {
        requires(arm);
        _arm = arm;
        _target = target;
        _wait = wait;
    }

    public void stopWaiting() {
        _wait = false;
    }

    @Override
    protected void end() {
        DriverStation.reportError("MoveArmToSetpointPID Ending", false);
        DriverStation.reportError("MoveArmToSetpointPID Ending", false);
        DriverStation.reportError("MoveArmToSetpointPID Ending", false);
        _arm.disable();
    }

    @Override
    protected boolean isFinished() {
        if (!_wait || _arm.onTarget()) {
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
