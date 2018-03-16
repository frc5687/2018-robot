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
    private boolean canFinish;
    private long _timeout = 10000;
    private long _endMillis;


    public MoveArmToSetpointPID(Arm arm, double target) {
        requires(arm);
        _arm = arm;
        _target = target;
        canFinish = true;
    }

    public MoveArmToSetpointPID(Arm arm, double target, long timeout) {
        requires(arm);
        _arm = arm;
        _target = target;
        canFinish = true;
        _timeout = timeout;
    }

    public MoveArmToSetpointPID(Arm arm, double target, boolean delayFinish) {
        requires(arm);
        _arm = arm;
        _target = target;
        canFinish = false;
    }

    public void permitFinish() {
        canFinish = true;
    }

    @Override
    protected void end() {
        DriverStation.reportError("MoveArmToSetpointPID Ending", false);
        DriverStation.reportError("MoveArmToSetpointPID Ending", false);
        DriverStation.reportError("MoveArmToSetpointPID Ending", false);
        _arm.disable();
        _arm.drive(_arm.calculateHoldSpeed(true));
    }

    @Override
    protected boolean isFinished() {
        if (System.currentTimeMillis() >= _endMillis) {
            DriverStation.reportError("MoveArmToSetpointPID timed out at " + _endMillis + "ms", false);
            return true;
        }
        if (_arm.onTarget()) {
            if (canFinish) {
                DriverStation.reportError("MoveArmToSetpointPID completed at " + _arm.getAngle(), false);
                return true;
            }
            DriverStation.reportError("MoveArmToSetpointPID on target but can't finish", false);
        }

        return false;
    }


    @Override
    protected void initialize() {
        super.initialize();
        _endMillis = System.currentTimeMillis() + _timeout;
        DriverStation.reportError("Starting MoveArmToSetpointPID to " + _target + " for max " + _timeout + "ms", false);
        _arm.setSetpoint(_target);
        _arm.enable();
    }

    @Override
    protected void execute() {
        // DriverStation.reportError("MoveArmToSetpointPID at " + _arm.getAngle(), false);
    }
}
