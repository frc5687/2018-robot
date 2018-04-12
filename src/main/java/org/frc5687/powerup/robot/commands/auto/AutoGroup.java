package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.commands.*;
import org.frc5687.powerup.robot.commands.actions.IntakeToScale;
import org.frc5687.powerup.robot.commands.actions.IntakeToSwitch;
import org.frc5687.powerup.robot.commands.auto.paths.*;

/**
 * Created by Ben Bernard on 2/2/2018.
 */
public class AutoGroup extends CommandGroup {
    public AutoGroup(int mode, int position, int switchSide, int scaleSide, long delayInMillis, boolean stayInYourOwnLane, Robot robot) {
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
        int carriageIntakePosition;
        double armSwitchAngle;
        double armIntakeAngle;
        int carriageTopPosition;
        MoveArmToSetpointPID armPid;

        addSequential(new AutoWaitForMillis(delayInMillis));

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
                        farLeftToLeftScale(robot);
                        break;
                    case Constants.AutoChooser.Position.FAR_LEFT:
                        if (!stayInYourOwnLane) { // Traverse allowed
                            farLeftToRightScale(robot);
                        } else if (switchSide == Constants.AutoChooser.LEFT) { // Traverse not allowed. Go for switch
                            farLeftToLeftSwitch(robot);
                        } else {
                            buildAutoCross(robot);
                        }
                        break;
                    case -Constants.AutoChooser.Position.FAR_RIGHT:
                        if (!stayInYourOwnLane) { // Traverse allowed
                            farRightToLeftScale(robot);
                        } else if (switchSide == Constants.AutoChooser.RIGHT){ // Traverse !allowed. Go for switch
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
            case Constants.AutoChooser.Mode.SCALE_THEN_SCALE:
                switch (scaleFactor) {
                    case -Constants.AutoChooser.Position.FAR_LEFT:
                        farLeftToLeftScale(robot);
                        leftScaleToSecondCube(robot);
                        secondCubeToLeftScale(robot);
                        break;
                    case Constants.AutoChooser.Position.FAR_LEFT:
                        if (!stayInYourOwnLane) { // Traverse allowed
                            farLeftToRightScale(robot);
                        } else if (switchSide == Constants.AutoChooser.LEFT) { // Traverse !allowed. Get switch.
                            farLeftToLeftSwitch(robot);
                        } else {
                            buildAutoCross(robot);
                        }
                        break;
                    case -Constants.AutoChooser.Position.FAR_RIGHT:
                        if (!stayInYourOwnLane) { // Traverse allowed.
                            farRightToLeftScale(robot);
                        } else if (switchSide == Constants.AutoChooser.RIGHT) { // Traverse !allowed. Get switch.
                            farRightToRightSwitch(robot);
                        } else {
                            buildAutoCross(robot);
                        }
                        break;
                    case Constants.AutoChooser.Position.FAR_RIGHT:
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

                        // Revert to this if needed
                        centerLeftToLeftSwitch(robot);
                        if (robot.getCarriage().isHealthy()) {
                            addSequential(new AutoZeroCarriage(robot.getCarriage()));
                        }

                        // centerLeftToLeftSwitchThenPickupSecondCube(robot);

                        break;
                    case Constants.AutoChooser.Position.CENTER:
                        /*
                        Drive to right switch and deposit cube
                         */
                        DriverStation.reportError("Switch Then Pick Up Cube. Position 3. Right Side", false);
                        armSwitchAngle = robot.getCarriage().isHealthy() ? Constants.Arm.Pot.SWITCH_HEIGHT_WITH_CARRIAGE : Constants.Arm.Pot.SWITCH_HEIGHT_BROKEN_CARRIAGE;
                        addParallel(new MoveArmToSetpointPID(robot.getArm(), armSwitchAngle, true));
                        addSequential(new CenterLeftToRightSwitchForSecondCube(robot));
                        addSequential(new AutoEject(robot.getIntake(), Constants.Intake.SWITCH_DROP_SPEED));
                        /*
                        Move Carriage Down and backup
                         */
                        addParallel(new MoveCarriageToSetpointPIDButFirstZeroIt(robot.getCarriage(), carriageIntakePosition));
                        addSequential(new RightSwitchBackup(robot));
                        /*
                        Move Arm Down while aligning
                         */
                        addParallel(new MoveArmToSetpointPID(robot.getArm(), armIntakeAngle));
                        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -20, Constants.Auto.Align.SPEED));
                        /*
                        Intake second cube
                         */
                        addParallel(new AutoIntake(robot.getIntake()));
                        addSequential(new RightGoPickupCube(robot));
                        /*
                        Raise Carriage while backing up
                         */
                        addParallel(new MoveCarriageToSetpointPID(robot.getCarriage(), carriageTopPosition));
                        addSequential(new RightGoPickupCubeReversed(robot));
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
                        break;
                    case Constants.AutoChooser.Position.CENTER:
                        /*
                        Drive to right switch and deposit cube
                         */
                        DriverStation.reportError("Switch Then Switch. Position 3. Right Side", false);
                        armSwitchAngle = robot.getCarriage().isHealthy() ? Constants.Arm.Pot.SWITCH_HEIGHT_WITH_CARRIAGE : Constants.Arm.Pot.SWITCH_HEIGHT_BROKEN_CARRIAGE;
                        addParallel(new MoveArmToSetpointPID(robot.getArm(), armSwitchAngle, true));
                        addSequential(new CenterLeftToRightSwitchForSecondCube(robot));
                        addSequential(new AutoEject(robot.getIntake(), Constants.Intake.SWITCH_DROP_SPEED));
                        /*
                        Move Carriage Down and backup
                         */
                        carriageIntakePosition = robot.isCompetitionBot() ? Constants.Carriage.ENCODER_BOTTOM_COMP : Constants.Carriage.ENCODER_BOTTOM_PROTO;
                        addParallel(new MoveCarriageToSetpointPIDButFirstZeroIt(robot.getCarriage(), carriageIntakePosition));
                        addSequential(new RightSwitchBackup(robot));
                        /*
                        Move Arm Down while aligning
                         */
                        armIntakeAngle = robot.isCompetitionBot() ? Constants.Arm.Pot.INTAKE_COMP : Constants.Arm.Pot.INTAKE_PROTO;
                        addParallel(new MoveArmToSetpointPID(robot.getArm(), armIntakeAngle));
                        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -20, Constants.Auto.Align.SPEED));
                        /*
                        Intake second cube
                         */
                        addParallel(new AutoIntake(robot.getIntake()));
                        addSequential(new RightGoPickupCube(robot));
                        /*
                        Raise Carriage while backing up
                         */
                        carriageTopPosition = robot.isCompetitionBot() ? Constants.Carriage.ENCODER_TOP_COMP : Constants.Carriage.ENCODER_TOP_PROTO;
                        addParallel(new MoveCarriageToSetpointPID(robot.getCarriage(), carriageTopPosition));
                        addSequential(new RightGoPickupCubeReversed(robot));
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
                        break;
                    case -Constants.AutoChooser.Position.FAR_RIGHT:
                        buildAutoCross(robot);
                        break;
                    case Constants.AutoChooser.Position.FAR_RIGHT:
                        farRightToRightSwitch(robot);
                        break;
                }
                break;
            case 11:
                addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 90, 1.0, 2500, 2.0));
                break;
            case Constants.AutoChooser.Mode.SWITCH_DRIVE:
                buildSimpleSwitch(robot, switchFactor);
                break;
            case Constants.AutoChooser.Mode.SCALE_DRIVE:
                buildSimpleScale(robot, scaleFactor);
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
        addSequential(new CenterLeftToLeftSwitchForSecondCube(robot));
        // Move Carriage Down and Back Up
        //addParallel(new MoveCarriageToSetpointPIDButFirstZeroIt(robot, carriageIntakePosition));
        //addParallel(new MoveArmToSetpointPID(robot.getArm(), armIntakeAngle));
        //The previous two commands did not get to the intake reliably, so we switched to the below which should be
        //More reliable.
        addParallel(new IntakeToFloorButZeroCarriageFirst(robot.getCarriage(), robot.getArm()));
        addSequential(new LeftSwitchBackup(robot));
        // Move Arm Down while aligning
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 9, Constants.Auto.Align.SPEED, 1750));
        // Intake second cube
        addParallel(new AutoIntake(robot.getIntake()));
        addSequential(new LeftGoPickupCube(robot));
        // Raise Carriage while backing up
        addParallel(new MoveCarriageToSetpointPIDButWaitForNMillisFirst(robot.getCarriage(), carriageMiddleHeight, 55));
        addSequential(new LeftGoPickupCubeReversed(robot));
    }

    private void secondCubeComingFromLeftSwitchToLeftSwitch(Robot robot) {
        double armSwitchAngle = 91;
        addSequential(new AutoAlign(robot, -30));
        addParallel(new MoveArmToSetpointPID(robot.getArm(), armSwitchAngle));
        addParallel(new AutoEjectAfterNMillis(robot.getIntake(), Constants.Intake.SWITCH_DROP_SPEED,LeftOfPowerCubeZoneToLeftSwitch.duration - 600));
        addSequential(new LeftOfPowerCubeZoneToLeftSwitch(robot));
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

    private void farLeftToLeftScale(Robot robot) {
        addParallel(new PrepIntakeForScale(robot, 100, 3000, true));
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
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 149, Constants.Auto.Align.SPEED));
        /*
        Prepare intake
         */
        addSequential(new MoveArmToSetpointPID(robot.getArm(), Constants.Arm.Pot.INTAKE));
        /*
        Approach second cube and intake
         */
        addParallel(new AutoIntake(robot.getIntake()));
        addSequential(new LeftScaleToCube(robot));
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
        addSequential(new AutoAlign(robot, 22.8));
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
        addParallel(new AutoZeroCarriageThenLower(robot));
        addSequential(new FarLeftToRightScaleDeadPartOne(robot));
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), 89, Constants.Auto.Align.SPEED, 3000));
        addParallel(new PrepIntakeForScale(robot, 1600, false));
        addSequential(new FarLeftToRightScaleDeadPartTwo(robot));
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -25, Constants.Auto.Align.SPEED, 3000));
        addParallel(new AutoEjectAfterNMillis(robot.getIntake(), Constants.Intake.DROP_SPEED, FarLeftToRightScaleDeadPartThree.duration - 340));
        addSequential(new FarLeftToRightScaleDeadPartThree(robot));
        /*
        Go to intake position and turn towards second cube
         */
        addParallel(new MoveCarriageToSetpointPIDButWaitForNMillisFirst(robot.getCarriage(), Constants.Carriage.ENCODER_BOTTOM_COMP, 700));
        addSequential(new FarLeftToRightScaleDeadPartFour(robot));
        addParallel(new MoveArmToSetpointPID(robot.getArm(), Constants.Arm.Pot.INTAKE));
        addSequential(new AutoAlign(robot.getDriveTrain(), robot.getIMU(), -110, Constants.Auto.Align.SPEED, 4000));
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
