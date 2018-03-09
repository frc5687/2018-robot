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
        /*
        if (robot.getCarriage().isHealthy()) {
            addParallel(new AutoZeroCarriage(robot.getCarriage()));
        }
        */
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
                        buildAutoCross(robot);
                        break;
                    case 2:
                        buildAutoCross(robot);
                        break;
                    case 3:
                        path = new CrossAutoLineToLeftOfPowerCube(robot);
                        addSequential(path);
                        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), path.lastHeading, 0.5));
                        //addSequential(new AutoZeroCarriage(robot.getCarriage())); Unsure about where this will end up, so I'm not auto zeroing here
                        break;
                    case 4:
                        buildAutoCross(robot);
                        break;
                    case 5:
                        buildAutoCross(robot);
                        break;
                    case 6:
                        buildAutoCross(robot);
                        break;
                }
                break;

            case Constants.AutoChooser.Mode.SWITCH_ONLY:
                SmartDashboard.putString("Auto/Mode", "Switch Only");
                double armTarget;
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
                        armTarget = robot.getCarriage().isHealthy() ? 100 : 72;
                        armPid = new MoveArmToSetpointPID(robot.getArm(), armTarget, true);
                        if (robot.getArm().isHealthy()) {
                            addParallel(armPid);
                        }
                        //addParallel(new EjectWhenSwitchDetected(robot));
                        path = new CenterLeftToLeftSwitch(robot);
                        addSequential(path);
                        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), path.lastHeading, 0.5));
                        addSequential(new AutoEject(robot.getIntake(), -0.62));
                        if (robot.getCarriage().isHealthy()) {
                            addSequential(new AutoZeroCarriage(robot.getCarriage()));
                        }
                        if (robot.getArm().isHealthy()) {
                            addParallel(new FinishArmPid(armPid));
                        }
                        break;
                    case Constants.AutoChooser.Position.CENTER: // Position 3, right side
                        DriverStation.reportError("Switch Only. Position 3. Right Side", false);
                        armTarget = robot.getCarriage().isHealthy() ? 100 : 72;
                        armPid = new MoveArmToSetpointPID(robot.getArm(), armTarget, true);
                        if (robot.getArm().isHealthy()) {
                            addParallel(armPid);
                        }
                        //addParallel(new EjectWhenSwitchDetected(robot));
                        path = new CenterLeftToRightSwitch(robot);
                        addSequential(path);
                        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), path.lastHeading, 0.5));
                        addSequential(new AutoEject(robot.getIntake(), -0.62));
                        if (robot.getCarriage().isHealthy()) {
                            addParallel(new AutoZeroCarriage(robot.getCarriage()));
                        }
                        if (robot.getArm().isHealthy()) {
                            addParallel(new FinishArmPid(armPid));
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
                    case -Constants.AutoChooser.Position.FAR_LEFT:
                        addParallel(new AutoZeroCarriageThenLower(robot));
                        addSequential(new FarLeftToLeftScale(robot));
                        break;
                    case Constants.AutoChooser.Position.FAR_LEFT:
                        buildAutoCross(robot);
                        break;
                    case Constants.AutoChooser.Position.CENTER:
                        // Sit here as we don't have anything
                        break;
                    case -Constants.AutoChooser.Position.FAR_RIGHT:
                        buildAutoCross(robot);
                        break;
/*                        addParallel(new AutoZeroCarriageThenLower(robot));
                        addSequential(new FarRightToLeftScalePartOne(robot));
                        //addSequential(new FarRightToLeftScalePartTwo(robot));
                        addParallel(new PrepIntakeForScale(robot, 20.0, 5000, false));
                        addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), 60, 0.3, true, true, -90, 10000, ""));
                        addSequential(new FarRightToLeftScalePartThree(robot));
                        addSequential(new AutoEject(robot.getIntake(), -1.0));
                        break;*/
                    case Constants.AutoChooser.Position.FAR_RIGHT:
                        if (robot.getArm().isHealthy()) {
                            addParallel(new PrepIntakeForScale(robot, 180.0, 10000, true));
                        }
                        path = new FarRightToRightScale(robot);
                        addSequential(path);
                        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), Math.toDegrees(-path.lastHeading), 0.5));
                        addSequential(new AutoEject(robot.getIntake()));
                        addSequential(new FarRightToRightScalePartTwo(robot));
                        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -120, 0.5));
                        //addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 90, 0.5));
                        //addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), 4, 0.3, true, true, 2000, ""));
                        if (robot.getArm().isHealthy()) {
                            addSequential(new MoveArmToSetpointPID(robot.getArm(), 42));
                        }
                        if (robot.getCarriage().isHealthy()) {
                            double ENCODER_BOTTOM = robot.getCarriage().isCompetitionBot() ? Constants.Carriage.ENCODER_BOTTOM_COMP : Constants.Carriage.ENCODER_BOTTOM_PROTO;
                            addSequential(new MoveCarriageToSetpointPID(robot.getCarriage(), ENCODER_BOTTOM));
                        }
                        addParallel(new AutoIntake(robot.getIntake()));
                        addSequential(new FarRightToRightScalePartThree(robot));
                        addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), -6.0, 0.3, true, true, 2000,"retreat"));
                        addSequential(new MoveArmToSetpointPID(robot.getArm(), 100));
                        addSequential(new AutoEject(robot.getIntake()));
                        //addSequential(new FarRightToRightScalePartThree(robot));
                        break;
                }
                break;
        }
    }

    private void buildAutoCross(Robot robot) {
        DynamicPathCommand path = new CrossAutoLine(robot);
        addSequential(path);
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), path.lastHeading, 0.5));
        addSequential(new AutoZeroCarriage(robot.getCarriage()));
        return;

    }
    private void straightSwitch(Robot robot) {
        double distance = 95.0;
        addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), distance, 0.4, true, true, 5000, "auto"));
        addSequential(new AutoEject(robot.getIntake()));
    }

}
