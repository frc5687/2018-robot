package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.commands.FinishArmPid;
import org.frc5687.powerup.robot.commands.MoveArmToSetpointPID;
import org.frc5687.powerup.robot.commands.MoveCarriageToSetpointPID;
import org.frc5687.powerup.robot.commands.auto.paths.*;

/**
 * Created by Ben Bernard on 2/2/2018.
 */
public class AutoGroup extends CommandGroup {
    public AutoGroup(int mode, int position, int switchSide, int scaleSide, Robot robot) {
        super();
        int switchFactor = switchSide * (position );
        int scaleFactor = scaleSide * (position);

        if (robot.getCarriage().isHealthy()) {
            addSequential(new AutoZeroCarriage(robot.getCarriage()));
        }
        //addSequential(new MoveCarriageToSetpointPID(robot.getCarriage(), Constants.Carriage.ENCODER_CLEAR_BUMPERS_PROTO));

        // Start with the "always" operations
        // addParallel(new CarriageZeroEncoder(robot.getCarriage()));
        //addParallel(new MoveCarriageToSetpointPID(robot.getCarriage(), Constants.Carriage.ENCODER_TOP_PROTO));
        // addParallel(new MoveArmToSetpointPID(robot.getArm(), Constants.Arm.ENCODER_FENCE));
        double distance = 0.0;
        MoveArmToSetpointPID armPid;

        switch (mode) {
            case Constants.AutoChooser.Mode.STAY_PUT:
                // Nothing to do here but look sad
                //armPid = new MoveArmToSetpointPID(robot.getArm(), 86, true);
                addParallel(new MoveCarriageToSetpointPID(robot.getCarriage(), -789));
                //addParallel(armPid);
                //addSequential(new FinishArmPid(armPid));
                break;

            case Constants.AutoChooser.Mode.CROSS_AUTOLINE:
                /*
                distance = 120;
                if (position>0 && position<5) { distance = 80.0; }

                if (switchSide==Constants.AutoChooser.LEFT) {
                    addParallel(new AutoDrive(robot.getDriveTrain(), distance, 0.75, true, true, 5000, "auto"));
                } else {
                    addParallel(new AutoDrive(robot.getDriveTrain(), distance, 0.75, true, true, 5000, "auto"));
                }
                */
                DynamicPathCommand path;
                switch (position) {
                    case 1:
                        path = new CrossAutoLine(robot);
                        addSequential(path);
                        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -path.lastHeading, 0.5));
                        break;
                    case 2:
                        path = new CrossAutoLine(robot);
                        addSequential(path);
                        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -path.lastHeading, 0.5));
                        break;
                    case 3:
                        path = new CrossAutoLineToLeftOfPowerCube(robot);
                        addSequential(path);
                        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -path.lastHeading, 0.5));
                        break;
                    case 4:
                        path = new CrossAutoLine(robot);
                        addSequential(path);
                        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -path.lastHeading, 0.5));
                        break;
                    case 5:
                        path = new CrossAutoLine(robot);
                        addSequential(path);
                        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -path.lastHeading, 0.5));
                        break;
                    case 6:
                        path = new CrossAutoLine(robot);
                        addSequential(path);
                        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -path.lastHeading, 0.5));
                        break;
                }
                break;

            case Constants.AutoChooser.Mode.SWITCH_ONLY:
                SmartDashboard.putString("Auto/Mode", "Switch Only");
                switch(switchFactor) {
                    case -Constants.AutoChooser.Position.FAR_LEFT: // Position 1, left side
                        break;
                    case Constants.AutoChooser.Position.FAR_LEFT:  // Position 1, right side
                        break;
                    case -Constants.AutoChooser.Position.MID_LEFT: // Position 2, left side:
                        straightSwitch(robot);
                        break;
                    case Constants.AutoChooser.Position.MID_LEFT: // Position 2, right side
                        break;
                    case -Constants.AutoChooser.Position.CENTER: // Position 3, left side
                        DriverStation.reportError("Switch Only. Position 3. Left Side", false);
                        if (robot.getCarriage().isHealthy()) {
                            // If the Carriage is working
                            armPid = new MoveArmToSetpointPID(robot.getArm(), 86, true);
                            addParallel(new MoveCarriageToSetpointPID(robot.getCarriage(), -789));
                            addParallel(armPid);
                            addSequential(new LeftSwitchCenter(robot));
                            addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 0, 0.5));
                            addSequential(new AutoEject(robot.getIntake()));
                            addSequential(new FinishArmPid(armPid));
                            addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), -60.0, 0.8, true, true, 2000,"retreat"));
                        } else {
                            DriverStation.reportError("Switch Only. Position 3. Left Side. Unhealthy Carriage", false);
                            // If the Carriage is not working...
                            armPid = new MoveArmToSetpointPID(robot.getArm(), 72, true);
                            addParallel(armPid);
                            addSequential(new CenterToLeftSwitchTarget(robot));
                            addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 0, 0.6));
                            addSequential(new AutoEject(robot.getIntake()));
                            addSequential(new FinishArmPid(armPid));
                            addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), -60.0, 0.8, true, true, 2000,"retreat"));
                        }
                        break;
                    case Constants.AutoChooser.Position.CENTER: // Position 3, right side
                        DriverStation.reportError("Switch Only. Position 3. Right Side", false);
                        if (robot.getCarriage().isHealthy()) {
                            // If the Carriage is working
                            armPid = new MoveArmToSetpointPID(robot.getArm(), 86, true);
                            addParallel(new MoveCarriageToSetpointPID(robot.getCarriage(), -789));
                            addParallel(armPid);
                            addSequential(new RightSwitchCenterFast(robot));
                            //addSequential(new CenterToRightSwitchTarget(robot));
                            addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 0, 0.5));
                            addSequential(new AutoEject(robot.getIntake()));
                            addSequential(new FinishArmPid(armPid));
                            addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), -60.0, 0.8, true, true, 2000,"retreat"));
                        } else if (!robot.getCarriage().isHealthy() && robot.getArm().isHealthy()){
                            // If the Carriage is not working but that arm is
                            armPid = new MoveArmToSetpointPID(robot.getArm(), 72, true);
                            addParallel(armPid);
                            addSequential(new RightSwitchCenterFast(robot));
                            //addSequential(new CenterToRightSwitchTarget(robot));
                            addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 0, 0.5));
                            addSequential(new AutoEject(robot.getIntake()));
                            addSequential(new FinishArmPid(armPid));
                            addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), -60.0, 0.8, true, true, 2000,"retreat"));
                        } else {
                            // Assuming the drive train still works on this borked bot..
                            addSequential(new CenterRightSwitchExp(robot));
                            addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 0, 0.5));
                            addSequential(new AutoEject(robot.getIntake()));
                            addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), -60.0, 0.8, true, true, 2000,"retreat"));
                        }

                        break;
                    case -Constants.AutoChooser.Position.NEAR_RIGHT: // Position 4, left side
                        break;
                    case Constants.AutoChooser.Position.NEAR_RIGHT: // Position 4, right side
                        straightSwitch(robot);
                        break;
                    case -Constants.AutoChooser.Position.MID_RIGHT: // Position 5, left side
                        break;
                    case Constants.AutoChooser.Position.MID_RIGHT: // Position 5, right side
                        break;
                    case -Constants.AutoChooser.Position.FAR_RIGHT: // Position 6, left side
                        break;
                    case Constants.AutoChooser.Position.FAR_RIGHT: // Position 6, right side
                        break;
                }
                break;
            case Constants.AutoChooser.Mode.SCALE_ONLY:
                SmartDashboard.putString("Auto/Mode", "Scale Only");
                switch (scaleFactor) {
                    case Constants.AutoChooser.Position.CENTER:
                        addSequential(new CenterRightScaleSlow(robot));
                        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 0, 0.5));
                        if (robot.getCarriage().isHealthy() && robot.getArm().isHealthy()) {
                            addSequential(new MoveCarriageToSetpointPID(robot.getCarriage(), -5));
                            addSequential(new MoveArmToSetpointPID(robot.getArm(), 163));
                        }
                        addSequential(new AutoEject(robot.getIntake()));
                        break;
                    case Constants.AutoChooser.Position.FAR_RIGHT:
                        if (robot.getCarriage().isHealthy() && robot.getArm().isHealthy()) {
                            addParallel(new PrepIntakeForScale(robot, 200.0, 10000));
                        }
                        addSequential(new SixToScale(robot));
                        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -20, 0.5));
                        addSequential(new AutoEject(robot.getIntake()));
                        break;
                }
        }
    }

    private void straightSwitch(Robot robot) {
        double distance = 95.0;
        addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), distance, 0.4, true, true, 5000, "auto"));
        addSequential(new AutoEject(robot.getIntake()));
    }

}
