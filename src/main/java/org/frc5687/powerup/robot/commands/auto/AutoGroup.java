package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.commands.*;
import org.frc5687.powerup.robot.commands.actions.IntakeToFloor;
import org.frc5687.powerup.robot.commands.actions.IntakeToDrive;
import org.frc5687.powerup.robot.commands.actions.IntakeToScale;
import org.frc5687.powerup.robot.commands.actions.IntakeToSwitch;
import org.frc5687.powerup.robot.commands.auto.paths.*;

/**
 * Created by Ben Bernard on 2/2/2018.
 */
public class AutoGroup extends CommandGroup {
    public AutoGroup(int mode, int position, int switchSide, int scaleSide, long delayInMillis, int coopMode, Robot robot) {
        super();
        int switchFactor = switchSide * (position );
        int scaleFactor = scaleSide * (position);
        boolean stayInYourOwnLane = coopMode == Constants.AutoChooser.Coop.STAY_IN_LANE;
        boolean defensive = coopMode == Constants.AutoChooser.Coop.DEFENSE;
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
        int carriageIntakePosition;
        double armSwitchAngle;
        double armIntakeAngle;
        int carriageTopPosition;
        MoveArmToSetpointPID armPid;

        if (delayInMillis != 0) {
            addSequential(new AutoWaitForMillis(delayInMillis));
        }

        switch (mode) {
            case Constants.AutoChooser.Mode.STAY_PUT:
                // Nothing to do here but look sad
                addSequential(new AutoZeroCarriageThenLower(robot));
                break;
            case Constants.AutoChooser.Mode.CROSS_AUTOLINE:
                switch (position) {
                    case 1:
                    case 2:
                    case 4:
                    case 5:
                    case 6:
                        buildAutoCross(robot);
                        break;
                    case 3:
                        addSequential(new CrossAutoLineToLeftOfPowerCube(robot));
                        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 0, Constants.Auto.Align.SPEED, 2000));
                        //addSequential(new AutoZeroCarriage(robot.getCarriage())); Unsure about where this will end up, so I'm not auto zeroing here
                        break;
                }
                break;
            case Constants.AutoChooser.Mode.SWITCH_ONLY:
                SmartDashboard.putString("Auto/Mode", "Switch Only");
                switch(switchFactor) {
                    case -Constants.AutoChooser.Position.FAR_LEFT: // Position 1, left side
                        farLeftToLeftSwitch(robot);
                        break;
                    case Constants.AutoChooser.Position.FAR_LEFT:  // Position 1, right side
                        buildAutoCross(robot);
                        break;
                    case -Constants.AutoChooser.Position.MID_LEFT: // Position 2, left side:
                        straightSwitch(robot);
                        break;
                    case Constants.AutoChooser.Position.MID_LEFT: // Position 2, right side
                        buildAutoCross(robot);
                        break;
                    case -Constants.AutoChooser.Position.CENTER: // Position 3, left side
                        DriverStation.reportError("Switch Only. Position 3. Left Side", false);
                        centerLeftToLeftSwitch(robot);
                        if (robot.getCarriage().isHealthy()) {
                            addSequential(new AutoZeroCarriage(robot.getCarriage()));
                        }
                        break;
                    case Constants.AutoChooser.Position.CENTER: // Position 3, right side
                        DriverStation.reportError("Switch Only. Position 3. Right Side", false);
                        centerLeftToRightSwitch(robot);
                        if (robot.getCarriage().isHealthy()) {
                            addParallel(new AutoZeroCarriage(robot.getCarriage()));
                        }
                        break;
                    case -Constants.AutoChooser.Position.NEAR_RIGHT: // Position 4, left side
                        buildAutoCross(robot);
                        break;
                    case Constants.AutoChooser.Position.NEAR_RIGHT: // Position 4, right side
                        straightSwitch(robot);
                        break;
                    case -Constants.AutoChooser.Position.MID_RIGHT: // Position 5, left side
                    case Constants.AutoChooser.Position.MID_RIGHT: // Position 5, right side
                        buildAutoCross(robot);
                        break;
                    case -Constants.AutoChooser.Position.FAR_RIGHT: // Position 6, left side
                        /*
                        armTarget = robot.getCarriage().isHealthy() ? 100 : 72;
                        armPid = new MoveArmToSetpointPID(robot.getArm(), armTarget, true);
                        if (robot.getArm().isHealthy()) {
                            addParallel(armPid);
                        }
                        addSequential(new FarRightToLeftSwitchBehind(robot));
                        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -179, 0.5));
                        addSequential(new AutoEject(robot.getIntake()));
                        if (robot.getCarriage().isHealthy()) {
                            addParallel(new AutoZeroCarriage(robot.getCarriage()));
                        }
                        */
                        buildAutoCross(robot);
                        break;
                    case Constants.AutoChooser.Position.FAR_RIGHT: // Position 6, right side
                        farRightToRightSwitch(robot);
                        break;
                }
                break;
            case Constants.AutoChooser.Mode.SCALE_ONLY:
                SmartDashboard.putString("Auto/Mode", "Scale Only");
                switch (scaleFactor) {
                    case -Constants.AutoChooser.Position.FAR_LEFT:
                        DriverStation.reportError("AUTO: Left scale only from far left", false);
                        farLeftToLeftScale(robot);
                        break;
                    case Constants.AutoChooser.Position.FAR_LEFT:
                        if (!(stayInYourOwnLane || defensive)) { // Traverse allowed
                            DriverStation.reportError("AUTO: Right scale only from far left", false);
                            farLeftToRightScale(robot);
                        } else if (defensive) {
                            // Drop off the cube...then
                            DriverStation.reportError("AUTO: Defensive from far left", false);
                            farLeftDefensive(robot, switchSide == Constants.AutoChooser.LEFT);
                        } else if (switchSide == Constants.AutoChooser.LEFT) { // Traverse not allowed. Go for switch
                            DriverStation.reportError("AUTO: Left switch from far left", false);
                            farLeftToLeftSwitch(robot);
                        } else {
                            DriverStation.reportError("AUTO: Auto cross from far left", false);
                            buildAutoCross(robot);
                        }
                        break;
                    case -Constants.AutoChooser.Position.FAR_RIGHT:
                        if (!(stayInYourOwnLane || defensive)) { // Traverse allowed
                            DriverStation.reportError("AUTO: Left scale only from far right", false);
                            farRightToLeftScale(robot);
                        } else if (defensive) {
                            DriverStation.reportError("AUTO: Defensive from far right", false);
                            farRightDefensive(robot, switchSide == Constants.AutoChooser.RIGHT);
                        } else if (switchSide == Constants.AutoChooser.RIGHT){ // Traverse !allowed. Go for switch
                            DriverStation.reportError("AUTO: RIght switch from far right", false);
                            farRightToRightSwitch(robot);
                        } else {
                            DriverStation.reportError("AUTO: Cross the line from far right", false);
                            buildAutoCross(robot);
                        }
                        break;
                    case Constants.AutoChooser.Position.FAR_RIGHT:
                        DriverStation.reportError("AUTO: Right scale only from far right", false);
                        farRightToRightScale(robot);
                        break;
                }
                break;
            case Constants.AutoChooser.Mode.SCALE_THEN_SCALE:
                switch (scaleFactor) {
                    case -Constants.AutoChooser.Position.FAR_LEFT:
                        DriverStation.reportError("AUTO: Left scale x 2 from far left", false);
                        farLeftToLeftScale(robot);
                        leftScaleToSecondCube(robot);
                        secondCubeToLeftScale(robot);
                        break;
                    case Constants.AutoChooser.Position.FAR_LEFT:
                        if (!(stayInYourOwnLane || defensive)) { // Traverse allowed
                            DriverStation.reportError("AUTO: Right scale from far left", false);
                            farLeftToRightScale(robot);
                        } else if (defensive) {
                            // Drop off the cube...then
                            DriverStation.reportError("AUTO: Defensive from far left", false);
                            farLeftDefensive(robot, switchSide == Constants.AutoChooser.LEFT);
                        } else if (switchSide == Constants.AutoChooser.LEFT) { // Traverse not allowed. Go for switch
                            DriverStation.reportError("AUTO: Left switch from far left", false);
                            farLeftToLeftSwitch(robot);
                        } else {
                            DriverStation.reportError("AUTO: Cross the line from far left", false);
                            buildAutoCross(robot);
                        }
                        break;
                    case -Constants.AutoChooser.Position.FAR_RIGHT:
                        if (!(stayInYourOwnLane || defensive)) { // Traverse allowed
                            DriverStation.reportError("AUTO: Left scale from far right", false);
                            farRightToLeftScale(robot);
                        } else if (defensive) {
                            DriverStation.reportError("AUTO: Defensive from far right", false);
                            farRightDefensive(robot, switchSide == Constants.AutoChooser.RIGHT);
                        } else if (switchSide == Constants.AutoChooser.RIGHT){ // Traverse !allowed. Go for switch
                            DriverStation.reportError("AUTO: RIght switch from far right", false);
                            farRightToRightSwitch(robot);
                        } else {
                            DriverStation.reportError("AUTO: Cross the line from far right", false);
                            buildAutoCross(robot);
                        }
                        break;
                    case Constants.AutoChooser.Position.FAR_RIGHT:
                        DriverStation.reportError("AUTO: Right scale only from far right", false);
                        farRightToRightScale(robot);
                        /*
                        // Second cube not competition ready, so it is commented out.
                        int carriageBottom = robot.isCompetitionBot() ? Constants.Carriage.ENCODER_BOTTOM_COMP : Constants.Carriage.ENCODER_BOTTOM_PROTO;
                        addParallel(new MoveCarriageToSetpointPID(robot.getCarriage(), carriageBottom));
                        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -157, Constants.Auto.Align.SPEED));
                        addSequential(new MoveArmToSetpointPID(robot.getArm(), 48));

                        addParallel(new AutoIntake(robot.getIntake()));
                        addSequential(new RightScaleToSecondCubePartOne(robot));
                        */
                        break;
                }
                break;
            case Constants.AutoChooser.Mode.SCALE_THEN_SWITCH:
                switch (scaleFactor) {
                    case -Constants.AutoChooser.Position.FAR_LEFT:
                        farLeftToLeftScale(robot);
                        leftScaleToSecondCube(robot);
                        switch (switchFactor) {
                            case -Constants.AutoChooser.Position.FAR_LEFT:
                                secondCubeToLeftSwitch(robot);
                                break;
                            case Constants.AutoChooser.Position.FAR_LEFT:
                                secondCubeToLeftScale(robot);
                                break;
                        }
                        break;
                    case Constants.AutoChooser.Position.FAR_LEFT:
                        if (!stayInYourOwnLane) { // Traverse allowed.
                            farLeftToRightScale(robot);
                        } else if (switchSide == Constants.AutoChooser.LEFT) { // Traverse !allowed. Get switch
                            farLeftToLeftSwitch(robot);
                        } else {
                            buildAutoCross(robot);
                        }
                        break;
                    case -Constants.AutoChooser.Position.FAR_RIGHT:
                        if (!stayInYourOwnLane) { // Traverse allowed
                            farRightToLeftScale(robot);
                        } else if (switchSide == Constants.AutoChooser.RIGHT) { // Traverse !allowed. Get switch
                            farRightToRightSwitch(robot);
                        } else {
                            buildAutoCross(robot);
                        }
                        break;
                    case Constants.AutoChooser.Position.FAR_RIGHT:
                        farRightToRightScale(robot);
                        break;
                }
                break;
            case Constants.AutoChooser.Mode.SWITCH_THEN_PICKUP_CUBE:
                carriageIntakePosition = robot.isCompetitionBot() ? Constants.Carriage.ENCODER_BOTTOM_COMP : Constants.Carriage.ENCODER_BOTTOM_PROTO;
                armIntakeAngle = robot.isCompetitionBot() ? Constants.Arm.Pot.INTAKE_COMP : Constants.Arm.Pot.INTAKE_PROTO;
                carriageTopPosition = robot.isCompetitionBot() ? Constants.Carriage.ENCODER_TOP_COMP : Constants.Carriage.ENCODER_TOP_PROTO;

                switch (switchFactor) {
                    case -Constants.AutoChooser.Position.FAR_LEFT:
                        farLeftToLeftSwitch(robot);
                        break;
                    case Constants.AutoChooser.Position.FAR_LEFT:
                        buildAutoCross(robot);
                        break;
                    case -Constants.AutoChooser.Position.CENTER:
                        DriverStation.reportError("Switch Then Pick Up Cube. Position 3. Left Side", false);
                        /*
                        // Revert to this if needed
                        centerLeftToLeftSwitch(robot);
                        if (robot.getCarriage().isHealthy()) {
                            addSequential(new AutoZeroCarriage(robot.getCarriage()));
                        }
                        */
                        centerLeftToLeftSwitchThenPickupSecondCube(robot);

                        break;
                    case Constants.AutoChooser.Position.CENTER:
                        DriverStation.reportError("Switch Then Pick Up Cube. Position 3. Right Side", false);

                        /*
                        // Revert to this if needed
                        centerLeftToRightSwitch(robot);
                        if (robot.getCarriage().isHealthy()) {
                            addSequential(new AutoZeroCarriage(robot.getCarriage()));
                        }
                        */
                        centerLeftToRightSwitchThenPickupSecondCube(robot);
                        break;
                    case -Constants.AutoChooser.Position.FAR_RIGHT:
                        buildAutoCross(robot);
                        break;
                    case Constants.AutoChooser.Position.FAR_RIGHT:
                        farRightToRightSwitch(robot);
                        break;
                }
                break;
            case Constants.AutoChooser.Mode.SWITCH_THEN_SWITCH:
                carriageIntakePosition = robot.isCompetitionBot() ? Constants.Carriage.ENCODER_BOTTOM_COMP : Constants.Carriage.ENCODER_BOTTOM_PROTO;
                armIntakeAngle = robot.isCompetitionBot() ? Constants.Arm.Pot.INTAKE_COMP : Constants.Arm.Pot.INTAKE_PROTO;
                carriageTopPosition = robot.isCompetitionBot() ? Constants.Carriage.ENCODER_TOP_COMP : Constants.Carriage.ENCODER_TOP_PROTO;
                switch (switchFactor) {
                    case -Constants.AutoChooser.Position.FAR_LEFT:
                        farLeftToLeftSwitch(robot);
                    case Constants.AutoChooser.Position.FAR_LEFT:
                        buildAutoCross(robot);
                        break;
                    case -Constants.AutoChooser.Position.CENTER:
                        DriverStation.reportError("Switch Then Pick Up Cube. Position 3. Left Side", false);
                        /*
                        // Revert to this if needed
                        centerLeftToLeftSwitch(robot);
                        if (robot.getCarriage().isHealthy()) {
                            addSequential(new AutoZeroCarriage(robot.getCarriage()));
                        }
                        */
                        centerLeftToLeftSwitchThenPickupSecondCube(robot);
                        secondCubeComingFromLeftSwitchToLeftSwitch(robot);
                        leftSwitchToThirdCube(robot);
                        thirdCubeNearLeftSwitchToLeftSwitch(robot);
                        break;
                    case Constants.AutoChooser.Position.CENTER:
                        DriverStation.reportError("Switch Then Pick Up Cube. Position 3. Right Side", false);
                        /*
                        // Revert to this if needed
                        centerLeftToRightSwitch(robot);
                        if (robot.getCarriage().isHealthy()) {
                            addSequential(new AutoZeroCarriage(robot.getCarriage()));
                        }
                        */
                        centerLeftToRightSwitchThenPickupSecondCube(robot);
                        secondCubeComingFromRightSwitchToRightSwitch(robot);
                        break;
                    case -Constants.AutoChooser.Position.FAR_RIGHT:
                        buildAutoCross(robot);
                        break;
                    case Constants.AutoChooser.Position.FAR_RIGHT:
                        farRightToRightSwitch(robot);
                        break;
                }
                break;
            case Constants.AutoChooser.Mode.SCALE_THEN_BACKOFF:
                switch (scaleFactor) {
                    case -Constants.AutoChooser.Position.FAR_LEFT:
                        // Far Left with Scale on Left Side
                        farLeftToLeftScale(robot);
                        leftScaleBackup(robot);
                        break;
                    case Constants.AutoChooser.Position.FAR_LEFT:
                        // Far Left with Scale on Right Side
                        if (!stayInYourOwnLane) {
                            // Allowed to traverse
                            farLeftToRightScale(robot);
                        } else if (switchSide == Constants.AutoChooser.LEFT) {
                            farLeftToLeftSwitch(robot);
                        } else {
                            buildAutoCross(robot);
                        }
                        break;
                    case -Constants.AutoChooser.Position.FAR_RIGHT:
                        // Far Right with Scale on Left Side
                        if (!stayInYourOwnLane) {
                            // Allowed to traverse
                            farRightToLeftScale(robot);
                        } else if (switchSide == Constants.AutoChooser.RIGHT) {
                            farRightToRightSwitch(robot);
                        } else {
                            buildAutoCross(robot);
                        }
                        break;
                    case Constants.AutoChooser.Position.FAR_RIGHT:
                        // Far Right with Scale on Right Side
                        farRightToRightScale(robot);
                        rightScaleBackup(robot);
                        break;
                }
                break;
            case Constants.AutoChooser.Mode.SWITCH_DRIVE:
                buildSimpleSwitch(robot, switchFactor);
                break;
            case Constants.AutoChooser.Mode.SCALE_DRIVE:
                buildSimpleScale(robot, scaleFactor);
                break;
            case Constants.AutoChooser.Mode.EXPERIMENTAL_SWITCH_THEN_SWITCH:
                switch (switchFactor) {
                    case -Constants.AutoChooser.Position.FAR_LEFT:
                        farLeftToLeftSwitch(robot);
                    case Constants.AutoChooser.Position.FAR_LEFT:
                        buildAutoCross(robot);
                        break;
                    case -Constants.AutoChooser.Position.CENTER:
                        DriverStation.reportError("Switch Then Switch Then Switch. Position 3. Left Side", false);
                        centerLeftToLeftSwitchThenPickupSecondCube(robot);
                        secondCubeComingFromLeftSwitchToLeftSwitch(robot);
                        leftSwitchToThirdCube(robot);
                        thirdCubeNearLeftSwitchToLeftSwitch(robot);
                        break;
                    case Constants.AutoChooser.Position.CENTER:
                        DriverStation.reportError("Switch Then Pick Up Cube. Position 3. Right Side", false);
                        experimentalCenterLeftToRightSwitchThenPickupSecondCube(robot);
                        experimentalSecondCubeComingFromRightSwitchToRightSwitch(robot);
                        experimentalRightSwitchIntakeThirdCube(robot);
                        experimentalRightSwitchDepositThirdCube(robot);
                        break;
                    case -Constants.AutoChooser.Position.FAR_RIGHT:
                        buildAutoCross(robot);
                        break;
                    case Constants.AutoChooser.Position.FAR_RIGHT:
                        farRightToRightSwitch(robot);
                        break;
                }
                break;
            case Constants.AutoChooser.Mode.EXPERIMENTAL_SCALE_THEN_SCALE:
                switch (scaleFactor) {
                    case -Constants.AutoChooser.Position.FAR_LEFT:
                        DriverStation.reportError("AUTO: Left scale x 2 from far left", false);
                        experimentalFarLeftToLeftScale(robot);
                        experimentalLeftScaleToSecondCube(robot);
                        experimentalSecondCubeToLeftScale(robot);
                        break;
                    case Constants.AutoChooser.Position.FAR_LEFT:
                        if (!(stayInYourOwnLane || defensive)) { // Traverse allowed
                            DriverStation.reportError("AUTO: Right scale from far left", false);
                            farLeftToRightScale(robot);
                        } else if (defensive) {
                            // Drop off the cube...then
                            DriverStation.reportError("AUTO: Defensive from far left", false);
                            farLeftDefensive(robot, switchSide == Constants.AutoChooser.LEFT);
                        } else if (switchSide == Constants.AutoChooser.LEFT) { // Traverse not allowed. Go for switch
                            DriverStation.reportError("AUTO: Left switch from far left", false);
                            farLeftToLeftSwitch(robot);
                        } else {
                            DriverStation.reportError("AUTO: Cross the line from far left", false);
                            buildAutoCross(robot);
                        }
                        break;
                    case -Constants.AutoChooser.Position.FAR_RIGHT:
                        if (!(stayInYourOwnLane || defensive)) { // Traverse allowed
                            DriverStation.reportError("AUTO: Left scale from far right", false);
                            farRightToLeftScale(robot);
                        } else if (defensive) {
                            DriverStation.reportError("AUTO: Defensive from far right", false);
                            farRightDefensive(robot, switchSide == Constants.AutoChooser.RIGHT);
                        } else if (switchSide == Constants.AutoChooser.RIGHT){ // Traverse !allowed. Go for switch
                            DriverStation.reportError("AUTO: RIght switch from far right", false);
                            farRightToRightSwitch(robot);
                        } else {
                            DriverStation.reportError("AUTO: Cross the line from far right", false);
                            buildAutoCross(robot);
                        }
                        break;
                    case Constants.AutoChooser.Position.FAR_RIGHT:
                        DriverStation.reportError("AUTO: Right scale only from far right", false);
                        farRightToRightScale(robot);
                        /*
                        // Second cube not competition ready, so it is commented out.
                        int carriageBottom = robot.isCompetitionBot() ? Constants.Carriage.ENCODER_BOTTOM_COMP : Constants.Carriage.ENCODER_BOTTOM_PROTO;
                        addParallel(new MoveCarriageToSetpointPID(robot.getCarriage(), carriageBottom));
                        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -157, Constants.Auto.Align.SPEED));
                        addSequential(new MoveArmToSetpointPID(robot.getArm(), 48));

                        addParallel(new AutoIntake(robot.getIntake()));
                        addSequential(new RightScaleToSecondCubePartOne(robot));
                        */
                        break;
                }
                break;
        }
    }

    private void buildAutoCross(Robot robot) {
        DynamicPathCommand path = new CrossAutoLine(robot);
        addSequential(path);
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), path.lastHeading, Constants.Auto.Align.SPEED, 2000));
        addSequential(new AutoZeroCarriage(robot.getCarriage()));
        return;

    }

    private void straightSwitch(Robot robot) {
        double distance = 95.0;
        addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), distance, 0.4, true, true, 5000, "auto"));
        addSequential(new AutoEject(robot.getIntake()));
    }

    private void centerLeftToLeftSwitch(Robot robot) {
        double armTarget = robot.getCarriage().isHealthy() ? Constants.Arm.Pot.SWITCH_HEIGHT_WITH_CARRIAGE : Constants.Arm.Pot.SWITCH_HEIGHT_BROKEN_CARRIAGE;
        MoveArmToSetpointPID armPid = new MoveArmToSetpointPID(robot.getArm(), armTarget, true);
        if (robot.getArm().isHealthy()) {
            addParallel(armPid);
        }
        //addParallel(new EjectWhenSwitchDetected(robot));
        addSequential(new CenterLeftToLeftSwitch(robot));
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 0, Constants.Auto.Align.SPEED));
        addSequential(new AutoEject(robot.getIntake(), Constants.Intake.SWITCH_DROP_SPEED));
        if (robot.getArm().isHealthy()) {
            addParallel(new FinishArmPid(armPid));
        }
    }

    private void centerLeftToLeftSwitchThenPickupSecondCube(Robot robot) {
        int carriageIntakePosition = robot.isCompetitionBot() ? Constants.Carriage.ENCODER_BOTTOM_COMP : Constants.Carriage.ENCODER_BOTTOM_PROTO;
        double armIntakeAngle = robot.isCompetitionBot() ? Constants.Arm.Pot.INTAKE_COMP : Constants.Arm.Pot.INTAKE_PROTO;
        double armSwitchAngle = robot.getCarriage().isHealthy() ? Constants.Arm.Pot.SWITCH_HEIGHT_WITH_CARRIAGE : Constants.Arm.Pot.SWITCH_HEIGHT_BROKEN_CARRIAGE;
        int carriageMiddleHeight = robot.isCompetitionBot() ? Constants.Carriage.ENCODER_MIDDLE_COMP : Constants.Carriage.ENCODER_MIDDLE_PROTO;
        // Drive to left switch and deposit cube
        addParallel(new MoveArmToSetpointPID(robot.getArm(), armSwitchAngle, true));
        addParallel(new AutoEjectAfterNMillis(robot.getIntake(), Constants.Intake.SWITCH_DROP_SPEED, CenterLeftToLeftSwitchForSecondCube.duration - 290));
        // Should end up facing 16.750
        addSequential(new CenterLeftToLeftSwitchForSecondCube(robot));
        // Move Carriage Down and Back Up
        //addParallel(new MoveCarriageToSetpointPIDButFirstZeroIt(robot, carriageIntakePosition));
        //addParallel(new MoveArmToSetpointPID(robot.getArm(), armIntakeAngle));
        //The previous two commands did not get to the intake reliably, so we switched to the below which should be
        //More reliable.
        addParallel(new IntakeToFloorButZeroCarriageFirst(robot.getCarriage(), robot.getArm()));
        addSequential(new LeftSwitchBackup(robot));
        // Move Arm Down while aligning
        // Would save a lot of time without this
        //////addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 18.8, Constants.Auto.Align.SPEED, 1750));
        // Intake second cube
        addParallel(new AutoIntake(robot.getIntake()));
        // TODO: might want to adjust the angle on this since we changed the approach to this spot.
        addSequential(new LeftGoPickupCube(robot, true));
        // Raise Carriage while backing up
        addParallel(new MoveCarriageToSetpointPIDButWaitForNMillisFirst(robot.getCarriage(), carriageMiddleHeight, 55));
    }

    private void secondCubeComingFromLeftSwitchToLeftSwitch(Robot robot) {
        double armSwitchAngle = 91;
        //addSequential(new LeftGoPickupCubeReversed(robot));
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -30, Constants.Auto.Align.SPEED, 2000, 1.0, Constants.DriveTrainBehavior.leftOnly));
        addParallel(new MoveArmToSetpointPID(robot.getArm(), armSwitchAngle));
        addParallel(new AutoEjectAfterNMillis(robot.getIntake(), Constants.Intake.SWITCH_DROP_SPEED,LeftOfPowerCubeZoneToLeftSwitch.duration));
        addSequential(new LeftOfPowerCubeZoneToLeftSwitch(robot));
    }

    private void leftSwitchToThirdCube(Robot robot) {
        addParallel(new IntakeToFloorButWaitNMillisFirst(robot.getCarriage(), robot.getArm(), 550));
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 48, Constants.Auto.Align.SPEED, 3000, 1.0, Constants.DriveTrainBehavior.rightOnly));

        addParallel(new AutoIntake(robot.getIntake()));
        addSequential(new LeftSideOfPowerCubeZoneIntakeThirdCube(robot, true));
    }

    private void thirdCubeNearLeftSwitchToLeftSwitch(Robot robot) {
        double armSwitchAngle = 91;
        addParallel(new MoveArmToSetpointPID(robot.getArm(), armSwitchAngle));
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -20, Constants.Auto.Align.SPEED, 3000, 1.0, Constants.DriveTrainBehavior.leftOnly));
        addParallel(new AutoEjectAfterNMillis(robot.getIntake(), Constants.Intake.SWITCH_DROP_SPEED, LeftOfPowerCubeZoneToLeftSwitchForThirdCube.duration - 60));
        addSequential(new LeftOfPowerCubeZoneToLeftSwitchForThirdCube(robot));
    }

    private void centerLeftToRightSwitch(Robot robot) {
        double armTarget = robot.getCarriage().isHealthy() ? Constants.Arm.Pot.SWITCH_HEIGHT_WITH_CARRIAGE : Constants.Arm.Pot.SWITCH_HEIGHT_BROKEN_CARRIAGE;
        MoveArmToSetpointPID armPid = new MoveArmToSetpointPID(robot.getArm(), armTarget, true);
        if (robot.getArm().isHealthy()) {
            addParallel(armPid);
        }
        //addParallel(new EjectWhenSwitchDetected(robot));
        addSequential(new CenterLeftToRightSwitch(robot));
        //addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 0, Constants.Auto.Align.SPEED));
        addSequential(new AutoEject(robot.getIntake(), Constants.Intake.SWITCH_DROP_SPEED));
        if (robot.getArm().isHealthy()) {
            addParallel(new FinishArmPid(armPid));
        }
    }

    private void centerLeftToRightSwitchThenPickupSecondCube(Robot robot) {
        /*
        Drive to right switch and deposit cube
         */
        DriverStation.reportError("Switch Then Pick Up Cube. Position 3. Right Side", false);
        int carriageIntakePosition = robot.isCompetitionBot() ? Constants.Carriage.ENCODER_BOTTOM_COMP : Constants.Carriage.ENCODER_BOTTOM_PROTO;
        double armIntakeAngle = robot.isCompetitionBot() ? Constants.Arm.Pot.INTAKE_COMP : Constants.Arm.Pot.INTAKE_PROTO;
        double armSwitchAngle = robot.getCarriage().isHealthy() ? Constants.Arm.Pot.SWITCH_HEIGHT_WITH_CARRIAGE : Constants.Arm.Pot.SWITCH_HEIGHT_BROKEN_CARRIAGE;
        double carriageTopPosition = Constants.Carriage.ENCODER_TOP_COMP;
        addParallel(new MoveArmToSetpointPID(robot.getArm(), armSwitchAngle, true));
        addParallel(new AutoEjectAfterNMillis(robot.getIntake(), Constants.Intake.SWITCH_DROP_SPEED, CenterLeftToRightSwitchForSecondCube.duration - 200));
        addSequential(new CenterLeftToRightSwitchForSecondCube(robot));
        class PrepIntake extends CommandGroup {
            public PrepIntake(Robot robot) {
                addSequential(new MoveCarriageToSetpointPIDButFirstZeroIt(robot.getCarriage(), carriageIntakePosition));
                addSequential(new MoveArmToSetpointPID(robot.getArm(), armIntakeAngle));
            }
        }
        /*
        Move Carriage Down and backup
         */
        addParallel(new PrepIntake(robot));
        addSequential(new RightSwitchBackup(robot));
        /*
        Move Arm Down while aligning
         */
        // -12.9
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -12.9, Constants.Auto.Align.SPEED));
        /*
        Intake second cube
         */
        addParallel(new AutoIntake(robot.getIntake()));
        addSequential(new RightGoPickupCube(robot, true));
        addParallel(new AbortIfNoCubeDetected(robot));
        /*
        Raise Carriage while backing up
         */
        addParallel(new MoveCarriageToSetpointPID(robot.getCarriage(), carriageTopPosition));
        addSequential(new RightGoPickupCubeReversed(robot));
        addSequential(new AbortIfCubeNotSecured(robot));
    }

    private void secondCubeComingFromRightSwitchToRightSwitch(Robot robot) {
        /*
        Align towards switch
        Ends up at x: 50, y: 139.5
         */
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 16, Constants.Auto.Align.SPEED, 2000, 2.2));
        /*
        Align arm while going to switch and eject
         */
        addParallel(new MoveArmToSetpointPID(robot.getArm(), Constants.Arm.Pot.switchHeightWithCarriageAllTheWayUp));
        addSequential(new RightOfPowerCubeZoneToRightSwitch(robot));
        addSequential(new AutoEject(robot.getIntake(), Constants.Intake.SWITCH_DROP_SPEED));
    }

    private void experimentalCenterLeftToRightSwitchThenPickupSecondCube(Robot robot) {
        int carriageIntakePosition = robot.isCompetitionBot() ? Constants.Carriage.ENCODER_BOTTOM_COMP : Constants.Carriage.ENCODER_BOTTOM_PROTO;
        double armIntakeAngle = robot.isCompetitionBot() ? Constants.Arm.Pot.INTAKE_COMP : Constants.Arm.Pot.INTAKE_PROTO;
        double armSwitchAngle = robot.getCarriage().isHealthy() ? Constants.Arm.Pot.SWITCH_HEIGHT_WITH_CARRIAGE : Constants.Arm.Pot.SWITCH_HEIGHT_BROKEN_CARRIAGE;
        // Drive to right switch and deposit cube
        addParallel(new MoveArmToSetpointPID(robot.getArm(), armSwitchAngle));
        addParallel(new AutoEjectAfterNMillis(robot.getIntake(), Constants.Intake.SWITCH_DROP_SPEED, ExperimentalCenterLeftToRightSwitchForSecondCube.duration - 290));
        addSequential(new ExperimentalCenterLeftToRightSwitchForSecondCube(robot));
        addParallel(new IntakeToFloorButZeroCarriageFirst(robot.getCarriage(), robot.getArm()));
        addSequential(new ExperimentalRightSwitchBackup(robot));
        // Move Arm Down
        // Intake second cube
        addParallel(new AutoIntake(robot.getIntake()));
        addSequential(new ExperimentalRightGoPickupCube(robot, true));
    }

    private void experimentalSecondCubeComingFromRightSwitchToRightSwitch(Robot robot) {
        double armSwitchAngle = 91;
        addParallel(new MoveArmToSetpointPID(robot.getArm(), armSwitchAngle));
        addSequential(new ExperimentalRightGoPickupCubeReversed(robot));
        addParallel(new AutoEjectAfterNMillis(robot.getIntake(), Constants.Intake.SWITCH_DROP_SPEED, ExperimentalRightSwitchDepositSecondCube.duration - 290));
        addSequential(new ExperimentalRightSwitchDepositSecondCube(robot));
    }

    private void experimentalRightSwitchIntakeThirdCube(Robot robot) {
        addParallel(new IntakeToFloorButWaitNMillisFirst(robot.getCarriage(), robot.getArm(), 500));
        addSequential(new ExperimentalRightSwitchBackupForThirdCube(robot));
        addParallel(new AutoIntake(robot.getIntake()));
        addSequential(new ExperimentalRightGoPickupThirdCube(robot, true));
    }

    private void experimentalRightSwitchDepositThirdCube(Robot robot) {
        double armSwitchAngle = 91;
        addParallel(new MoveArmToSetpointPID(robot.getArm(), armSwitchAngle));
        addSequential(new ExperimentalRightGoPickupCubeReversed(robot));
        addParallel(new AutoEjectAfterNMillis(robot.getIntake(), Constants.Intake.SWITCH_DROP_SPEED, ExperimentalRightSwitchDepositThirdCube.duration - 290));
        addSequential(new ExperimentalRightSwitchDepositThirdCube(robot));
    }

    private void farRightToRightSwitch(Robot robot) {
        double armTarget = robot.getCarriage().isHealthy() ? Constants.Arm.Pot.SWITCH_HEIGHT_WITH_CARRIAGE : Constants.Arm.Pot.SWITCH_HEIGHT_BROKEN_CARRIAGE;
        MoveArmToSetpointPID armPid = new MoveArmToSetpointPID(robot.getArm(), armTarget, true);
        if (robot.getArm().isHealthy()) {
            addParallel(armPid);
        }
        addSequential(new FarRightToRightSwitchSide(robot));
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -90, Constants.Auto.Align.SPEED));
        addSequential(new AutoEject(robot.getIntake()));
        if (robot.getCarriage().isHealthy()) {
            addParallel(new AutoZeroCarriage(robot.getCarriage()));
        }
    }

    private void farLeftToLeftSwitch(Robot robot) {
        double armTarget = robot.getCarriage().isHealthy() ? Constants.Arm.Pot.SWITCH_HEIGHT_WITH_CARRIAGE : Constants.Arm.Pot.SWITCH_HEIGHT_BROKEN_CARRIAGE;
        addParallel(new MoveArmToSetpointPID(robot.getArm(), armTarget));
        addSequential(new FarRightToRightSwitchSide(robot));
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 90, Constants.Auto.Align.SPEED));
        addSequential(new AutoEject(robot.getIntake()));
        addParallel(new AutoZeroCarriage(robot.getCarriage()));
    }

    private void leftScaleBackup(Robot robot) {
        addParallel(new IntakeToFloorButWaitNMillisFirst(robot.getCarriage(), robot.getArm(), 1000));
        addSequential(new LeftScaleBackup(robot));
    }

    private void rightScaleBackup(Robot robot) {
        addParallel(new IntakeToFloorButWaitNMillisFirst(robot.getCarriage(), robot.getArm(), 1000));
        addSequential(new RightScaleBackup(robot));
    }

    private void farLeftToLeftScale(Robot robot) {
        //addParallel(new PrepIntakeForScale(robot, 100, 3000, true));
        addParallel(new MoveArmToSetpointPID(robot.getArm(), Constants.Arm.Pot.SCALE));
        addParallel(new MoveCarriageToSetpointPIDButWaitForNInchesFirst(robot.getDriveTrain(), robot.getCarriage(), Constants.Carriage.ENCODER_TOP_COMP, 140));
        addSequential(new FarLeftToLeftScale(robot));
        // Faster path makes it so we don't need auto aline, except if we exclude it we need to turn to 105deg to get 2nd cube
        // Timeout used to be 1000, but because of too high scrub we would time out.
        // We changed the min. speed for auto align, so we don't "need" a greater timeout, but we haven't been able to test
        // it, so if it appears that we're stalling for too long, bump up the min. speed and or decrease this timeout
        addSequential(new AutoAlign(robot, 40, Constants.Auto.Align.SPEED, 2500, 1.0));
        addSequential(new AutoEject(robot.getIntake(), Constants.Intake.SCALE_DROP_SPEED));
    }

    private void leftScaleToSecondCube(Robot robot) {
        /*
        Align towards second cube
         */
        addParallel(new MoveCarriageToSetpointPID(robot.getCarriage(), Constants.Carriage.ENCODER_BOTTOM_COMP));
        // should be 149
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 160, Constants.Auto.Align.SPEED));
        /*
        Prepare intake
         */
        addSequential(new MoveArmToSetpointPID(robot.getArm(), Constants.Arm.Pot.INTAKE));
        /*
        Approach second cube and intake
         */
        addParallel(new AutoIntake(robot.getIntake()));
        addSequential(new LeftScaleToCube(robot, true));
        addSequential(new AbortIfNoCubeDetected(robot));
    }

    private void secondCubeToLeftScale(Robot robot) {
        /*
        Go back to the scale while raising the carriage to drive config
         */
        addParallel(new MoveCarriageToSetpointPID(robot.getCarriage(), Constants.Carriage.ENCODER_DRIVE_COMP));
        addParallel(new MoveArmToSetpointPID(robot.getArm(), Constants.Arm.Pot.SCALE));
        addSequential(new LeftScaleToCubeReversed(robot));
        /*
        Prepare intake
         */
        /*
        Rotate towards scale
         */
        //addSequential(new AutoAlign(robot, -140, 1500, 7));
        addParallel(new MoveCarriageToSetpointPID(robot.getCarriage(), Constants.Carriage.ENCODER_TOP_COMP));
        addSequential(new AbortIfCubeNotSecured(robot));
        addSequential(new AutoAlign(robot, 22.8));
        addSequential(new AutoEject(robot.getIntake(), Constants.Intake.SCALE_SHOOT_SPEED_SECOND_CUBE));
    }

    private void experimentalFarLeftToLeftScale(Robot robot) {
        //addParallel(new PrepIntakeForScale(robot, 100, 3000, true));
        addParallel(new MoveArmToSetpointPID(robot.getArm(), Constants.Arm.Pot.SCALE_MAX));
        addParallel(new MoveCarriageToSetpointPIDButWaitForNInchesFirst(robot.getDriveTrain(), robot.getCarriage(), Constants.Carriage.ENCODER_TOP_COMP, 136));
        //addSequential(new ExperimentalFarLeftToLeftScale(robot));
        addSequential(new ExperimentalFarLeftToLeftScaleWithTightTurnFour(robot));
        addSequential(new AutoEject(robot.getIntake(), Constants.Intake.SCALE_DROP_SPEED));
        //addParallel(new AutoEjectAfterNMillis(robot.getIntake(), Constants.Intake.SCALE_DROP_SPEED, FarLeftToLeftScaleWithTightTurn.duration - 20));
        // Faster path makes it so we don't need auto align, except if we exclude it we need to turn to 105deg to get 2nd cube
        // Timeout used to be 1000, but because of too high scrub we would time out.
        // We changed the min. speed for auto align, so we don't "need" a greater timeout, but we haven't been able to test
        // it, so if it appears that we're stalling for too long, bump up the min. speed and or decrease this timeout
        //addSequential(new AutoAlign(robot, 40, Constants.Auto.Align.SPEED, 2500, 1.0));
    }

    private void experimentalLeftScaleToSecondCube(Robot robot) {
        /*
        Align towards second cube
         */
        addParallel(new MoveCarriageToSetpointPID(robot.getCarriage(), Constants.Carriage.ENCODER_BOTTOM_COMP));
        //addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 135, Constants.Auto.Align.SPEED));
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 90, Constants.Auto.Align.SPEED, 2000, Constants.Auto.Align.TOLERANCE, Constants.DriveTrainBehavior.rightOnly));
        /*
        Prepare intake
         */
        class AlignAndPrepareIntake extends CommandGroup {
            AlignAndPrepareIntake(Robot robot) {
                addParallel(new MoveArmToSetpointPID(robot.getArm(), Constants.Arm.Pot.INTAKE));
                addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 158, Constants.Auto.Align.SPEED, 2000, Constants.Auto.Align.TOLERANCE, Constants.DriveTrainBehavior.bothSides));
            }
        }
        addSequential(new AlignAndPrepareIntake(robot));
        /*
        Approach second cube and intake
         */
        addParallel(new AutoIntake(robot.getIntake()));
        addSequential(new ExperimentalLeftScaleToCubeAlternativeTwo(robot));
        //addSequential(new ExperimentalLeftScaleToCube(robot));
        addSequential(new AbortIfNoCubeDetected(robot));
    }

    private void experimentalSecondCubeToLeftScale(Robot robot) {
        /*
        Go back to the scale while raising the carriage to drive config
         */
        addParallel(new MoveCarriageToSetpointPID(robot.getCarriage(), Constants.Carriage.ENCODER_DRIVE_COMP));
        addParallel(new MoveArmToSetpointPID(robot.getArm(), Constants.Arm.Pot.SCALE));
        //addSequential(new ExperimentalLeftScaleToCubeReversed(robot));
        addSequential(new ExperimentalLeftScaleToCubeAlternativeTwoReverse(robot));
        /*
        Prepare intake
         */
        /*
        Rotate towards scale
         */
        //addSequential(new AutoAlign(robot, -140, 1500, 7));
        addParallel(new MoveCarriageToSetpointPID(robot.getCarriage(), Constants.Carriage.ENCODER_TOP_COMP));
        addSequential(new AbortIfCubeNotSecured(robot));
        //addSequential(new AutoAlign(robot, 22.8));
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 45, Constants.Auto.Align.SPEED, 2000, Constants.Auto.Align.TOLERANCE, Constants.DriveTrainBehavior.rightOnly));
        addSequential(new AutoEject(robot.getIntake(), Constants.Intake.SCALE_SHOOT_SPEED_SECOND_CUBE));
    }

    private void secondCubeToLeftSwitch(Robot robot) {
        /*
        Move back very slightly
         */
        addSequential(new MoveCarriageToSetpointPID(robot.getCarriage(), Constants.Carriage.ENCODER_MIDDLE_COMP));
        addSequential(new MoveArmToSetpointPID(robot.getArm(), Constants.Arm.Pot.switchHeightWithCarriageHalfwayUp));
        addSequential(new AutoEject(robot, Constants.Intake.SWITCH_DROP_SPEED));
    }

    private void farLeftToRightScale(Robot robot) {
        addSequential(new FarLeftToRightScaleDeadPartOne(robot));
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 88, Constants.Auto.Align.SPEED, 5000, 1.0, Constants.DriveTrainBehavior.leftOnly));
        addParallel(new MoveArmToSetpointPID(robot.getArm(), Constants.Arm.Pot.SCALE_MAX));
        //addParallel(new MoveCarriageToSetpointPIDButWaitForNInchesFirst(robot.getDriveTrain(), robot.getCarriage(), Constants.Carriage.ENCODER_TOP_COMP, 100));
        addSequential(new FarLeftToRightScaleDeadPartTwo(robot));
        class RaiseCarriageAfterWaitingNMillis extends CommandGroup {
            public RaiseCarriageAfterWaitingNMillis(Robot robot, long millis) {
                addSequential(new AutoWaitForMillis(millis));
                addSequential(new AutoZeroCarriage(robot.getCarriage()));
            }
        }
        addParallel(new RaiseCarriageAfterWaitingNMillis(robot, 300));
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -25, Constants.Auto.Align.SPEED, 3000, 1.0, Constants.DriveTrainBehavior.bothSides));
        addParallel(new AutoEjectAfterNMillis(robot.getIntake(), Constants.Intake.SCALE_TRAVERSE_DROP_SPEED, FarLeftToRightScaleDeadPartThree.duration - 220));
        addSequential(new FarLeftToRightScaleDeadPartThree(robot));
        /*
        Go to intake position and turn towards second cube
         */
        addParallel(new MoveCarriageToSetpointPIDButWaitForNMillisFirst(robot.getCarriage(), Constants.Carriage.ENCODER_BOTTOM_COMP, 700));
        addSequential(new FarLeftToRightScaleDeadPartFour(robot));
        addParallel(new MoveArmToSetpointPID(robot.getArm(), Constants.Arm.Pot.INTAKE));
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -110, Constants.Auto.Align.SPEED, 4000));
    }


    private void farLeftDefensive(Robot robot, boolean doSwitch) {
        addParallel(new AutoZeroCarriageThenLower(robot));
        // addSequential(new FarLeftToRightScaleDeadPartOne(robot));
        addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), 210, 0.75, true, true, 6000, "pass the switch"));
        addParallel(new IntakeToFloor(robot.getCarriage(), robot.getArm()));
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 90, Constants.Auto.Align.SPEED, 4000));
        addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), 52, 0.75, true, true, 3000, "goes halfway accross field"));
        addParallel(new AutoEject(robot.getIntake(), Constants.Intake.DROP_SPEED));
        addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), -54, 0.75, true, true, 3000, "moves back"));
        addSequential(new IntakeToFloor(robot.getCarriage(), robot.getArm()));
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 150, Constants.Auto.Align.SPEED, 3000));
        addParallel(new AutoIntake(robot.getIntake()));
        addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), 12, 0.6, true, true, 2000, "attack cube"));
        if (doSwitch) {
            DriverStation.reportError("Attacking swithc", false);
            addSequential(new MoveCarriageToSetpointPID(robot.getCarriage(), Constants.Carriage.ENCODER_TOP_COMP, 2000));
            addSequential(new IntakeToSwitch(robot.getCarriage(), robot.getArm()));
            addSequential(new AutoEject(robot.getIntake(), Constants.Intake.OUTTAKE_SPEED));
        }
        addParallel(new IntakeToDrive(robot.getCarriage(), robot.getArm()));
        addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), -24, 0.6, true, true, 2000, "reposition"));
    }

    private void farRightDefensive(Robot robot, boolean doSwitch) {
        addParallel(new AutoZeroCarriageThenLower(robot));
        // addSequential(new FarLeftToRightScaleDeadPartOne(robot));
        addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), 214, 0.75, true, true, 6000, "pass the switch"));
        addParallel(new IntakeToFloor(robot.getCarriage(), robot.getArm()));
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -90, Constants.Auto.Align.SPEED, 4000));
        addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), 78, 0.75, true, true, 3000, "goes halfway accross field"));
        addParallel(new AutoEject(robot.getIntake(), Constants.Intake.DROP_SPEED));
        addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), -60, 0.75, true, true, 3000, "moves back"));
        addSequential(new IntakeToFloor(robot.getCarriage(), robot.getArm()));
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -150, Constants.Auto.Align.SPEED, 3000));
        addParallel(new AutoIntake(robot.getIntake()));
        addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), 24, 0.6, true, true, 2000, "attack cube"));
        if (doSwitch) {
            DriverStation.reportError("Attacking swithc", false);
            addSequential(new MoveCarriageToSetpointPID(robot.getCarriage(), Constants.Carriage.ENCODER_TOP_COMP, 2000));
            addSequential(new IntakeToSwitch(robot.getCarriage(), robot.getArm()));
            addSequential(new AutoEject(robot.getIntake(), Constants.Intake.OUTTAKE_SPEED));
        }
        addParallel(new IntakeToDrive(robot.getCarriage(), robot.getArm()));
        addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), -24, 0.6, true, true, 2000, "reposition"));
    }

    private void farRightToLeftScale(Robot robot) {
        addParallel(new AutoZeroCarriageThenLower(robot));
        addSequential(new FarRightToLeftScaleDeadPartOne(robot));
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -90, Constants.Auto.Align.SPEED, 4000));
        addParallel(new PrepIntakeForScale(robot, 1600, false));
        addSequential(new FarRightToLeftScaleDeadPartTwo(robot));
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 18, Constants.Auto.Align.SPEED, 2000));
        addSequential(new FarRightToLeftScaleDeadPartThree(robot));
        addSequential(new AutoEject(robot.getIntake()));
    }

    private void farRightToRightScale(Robot robot) {
        if (robot.getArm().isHealthy()) {
            addParallel(new PrepIntakeForScale(robot, 50, 1500, true, false));
        }
        addSequential(new FarRightToRightScale(robot));
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -45, Constants.Auto.Align.SPEED, 700));
        addSequential(new AutoEject(robot.getIntake(), Constants.Intake.SCALE_DROP_SPEED));
    }

    private void buildSimpleSwitch(Robot robot, int switchFactor) {
        double clear = 30.0;
        double traverse = 79.0;
        double approach = 24.0;

        double angle = switchFactor < 0 ? -45 : 45;

        double retreat1 = 24.0;

        double cube2Angle = switchFactor < 0 ? 90 : -90;
        double cube2Distance = 38.0;

        double cube3Angle = switchFactor < 0 ? 55 : -55;
        double cube3Distance = 32.0;

        switch (switchFactor) {
            case Constants.AutoChooser.Position.CENTER:
            case -Constants.AutoChooser.Position.CENTER:
                // Move away from wall while zeroing the carriage
                addParallel(new AutoZeroCarriage(robot.getCarriage()));
                addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), clear, 0.75, true, true, 5000, "Clear wall"));
                // Turn towards correct side
                addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), angle, Constants.Auto.Align.SPEED, 2000));

                // Approach while raising arm
                addParallel(new IntakeToSwitch(robot.getCarriage(), robot.getArm()));
                addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), traverse, 0.75, true, true, 5000, "Approach switch"));

                /*
                // Turn towards switch
                addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 0, Constants.Auto.Align.SPEED, 2000));

                //
                addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), approach, 0.5, true, true, 5000, "Approach switch"));

                // Eject
                addSequential(new AutoEject(robot.getIntake(), Constants.Intake.OUTTAKE_SPEED));

                // Back away
                addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), -retreat1, 0.5, true, true, 5000, "Retreat from 1st cube"));

                // Turn while starting to intake
                addParallel(new IntakeToFloor(robot.getCarriage(), robot.getArm()));
                addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), cube2Angle, Constants.Auto.Align.SPEED, 2000));

                // Auto-intake and approach cube2
                addParallel(new AutoIntake(robot.getIntake()));
                addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), cube2Distance, 0.5, true, true, 2000, "Approach 2nd cube"));

                // Retreat while raising intake
                addParallel(new IntakeToScale(robot.getCarriage(), robot.getArm()));
                addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), -cube2Distance, 0.75, true, true, 2000, "Retrieve 2nd cube"));

                // Turn towards switch
                addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 0, Constants.Auto.Align.SPEED, 2000));

                // Approach again...
                addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), retreat1, 0.75, true, true, 2000, "Approach with 2nd cube"));

                // Eject
                addSequential(new AutoEject(robot.getIntake(), Constants.Intake.OUTTAKE_SPEED));

                // Back away
                addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), -retreat1, 0.75, true, true, 2000, "Retreat from 2nd cube"));

                // Turn while starting to intake
                addParallel(new IntakeToFloor(robot.getCarriage(), robot.getArm()));
                addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), cube3Angle, Constants.Auto.Align.SPEED, 2000));

                // Auto-intake and approach cube2
                addParallel(new AutoIntake(robot.getIntake()));
                addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), cube3Distance, 0.75, true, true, 2000, "Approach 3rd cube"));

                // Retreat while raising intake
                addParallel(new IntakeToSwitch(robot.getCarriage(), robot.getArm()));
                addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), -cube3Distance, 0.75, true, true, 2000, "Retrieve 3rd cube"));

                // Turn towards switch
                addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 0, Constants.Auto.Align.SPEED, 2000));

                // Approach again...
                addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), retreat1, 0.75, true, true, 2000, "Approach with 3rd cube"));

                // Eject
                addSequential(new AutoEject(robot.getIntake(), Constants.Intake.OUTTAKE_SPEED));
                /*
                */
                break;
        }
        //addSequential(new AutoEject(robot.getIntake()));
    }


    private void buildSimpleScale(Robot robot, int scaleFactor) {
        // Only relevant for far right and far left positions
        final double X = 280.0;
        final double Y = 20.0;

        final double S = 208.0;
        final double T = 180;
        final double N = 48.0;

        switch (scaleFactor) {
            case -Constants.AutoChooser.Position.FAR_LEFT:
            case Constants.AutoChooser.Position.FAR_RIGHT:

                // Left to left scale, right to right scale
                double angle = scaleFactor < 0 ? 40 : -40;

                // Drive forward X inches while autozeroing and prepping
                addParallel(new AutoZeroCarriageThenLower(robot));
                addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), X, 0.75, true, true, 5000, "AutoDrive to scale"));

                // turn to angle ? deg while raising intake
                addParallel(new IntakeToScale(robot.getCarriage(), robot.getArm()));
                addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), angle, Constants.Auto.Align.SPEED, 2000));

                // Drive forward Y inches
                addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), Y, 0.5, true, true, 5000, "Approach"));

                // eject
                addSequential(new AutoEject(robot.getIntake(), Constants.Intake.DROP_SPEED));
                break;

            case Constants.AutoChooser.Position.FAR_LEFT:
            case -Constants.AutoChooser.Position.FAR_RIGHT:
                double traverseAngle = scaleFactor < 0 ? -90 : 40;

                // Left to right scale or right to left scale
                // Drive forward S inches while autozeroing and securing
                addParallel(new AutoZeroCarriageThenLower(robot));
                addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), S, 0.75, true, true, 5000, "AutoDrive past switch"));

                // turn to angle deg
                addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), traverseAngle, Constants.Auto.Align.SPEED, 2000));

                // Drive forward T inches
                addParallel(new PrepIntakeForTraverse(robot, T/2, 5000));
                addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), T, 0.75, true, true, 5000, "AutoTraverse"));

                // turn to angle 0 deg
                addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 0, Constants.Auto.Align.SPEED, 2000));
                // Raise intake
                addSequential(new IntakeToScale(robot.getCarriage(), robot.getArm()));

                // Drive forward N inches
                addSequential(new AutoDrive(robot.getDriveTrain(), robot.getIMU(), N, 0.5, true, true, 5000, "Approach"));

                // eject
                addSequential(new AutoEject(robot.getIntake(), Constants.Intake.DROP_SPEED));

                break;
        }
    }

}
