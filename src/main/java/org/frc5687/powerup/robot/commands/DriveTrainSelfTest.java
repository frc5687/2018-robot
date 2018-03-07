package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.subsystems.DriveTrain;
import org.frc5687.powerup.robot.utils.PDP;

public class DriveTrainSelfTest extends Command{
    private DriveTrain driveTrain;
    private PDP pdp;
    private long endTimeMillis;
    private double frontLeftAmps;
    private double frontRightAmps;
    private double rearRightAmps;
    private double rearLeftAmps;

    public DriveTrainSelfTest(DriveTrain driveTrain, long endTimeMillis) {
        requires(driveTrain);

        this.pdp = pdp;
        this.driveTrain = driveTrain;
        this.endTimeMillis = endTimeMillis;
    }

    @Override
    protected void initialize(){
    endTimeMillis = System.currentTimeMillis() + Constants.DriveTrain.TEST_TIME;
    driveTrain.setPower(1,1);
    frontLeftAmps = 0;
    frontRightAmps =0;
    rearLeftAmps =0;
    rearRightAmps = 0;
    }

    @Override
    protected void execute() {
        frontLeftAmps = Math.max(frontLeftAmps, pdp.getCurrent(RobotMap.PDP.LEFT_FRONT_SRX));
        frontRightAmps = Math.max(frontRightAmps, pdp.getCurrent(RobotMap.PDP.RIGHT_FRONT_SRX));
        rearLeftAmps = Math.max(rearLeftAmps, pdp.getCurrent(RobotMap.PDP.LEFT_REAR_SPX));
        rearRightAmps = Math.max(rearRightAmps, pdp.getCurrent(RobotMap.PDP.RIGHT_REAR_SPX));
    }

    @Override
    protected void end() {
        if (frontLeftAmps < Constants.DriveTrain.MIN_AMPS){
            DriverStation.reportError( "Less than " + frontLeftAmps + "amps drawn on front left drive motor", false);
        }

        if (frontRightAmps < Constants.DriveTrain.MIN_AMPS){
            DriverStation.reportError("less than" +frontRightAmps + "amps drawn on front right drive motor", false);
        }

        if (rearLeftAmps < Constants.DriveTrain.MIN_AMPS){
            DriverStation.reportError("Less than " + rearLeftAmps + "amps drawn on rear left drive motor", false);
        }

        if (rearRightAmps < Constants.DriveTrain.MIN_AMPS){
            DriverStation.reportError("Less than "+ rearRightAmps + "amps drawn on rear right drive motor", false);
        }
        driveTrain.setPower(0,0);
    }

    @Override
    protected boolean isFinished(){
        return System.currentTimeMillis() > endTimeMillis;
    }
}
