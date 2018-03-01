package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.subsystems.DriveTrain;

public class DriveTrainSelfTest extends Command{
    private DriveTrain driveTrain;
    private long endTimeMillis;

    public DriveTrainSelfTest(DriveTrain driveTrain, long endTimeMillis) {
        requires(driveTrain);

        this.driveTrain = driveTrain;
        this.endTimeMillis = endTimeMillis;
    }

    @Override
    protected void initialize(){
    endTimeMillis = System.currentTimeMillis();
    driveTrain.setPower(1,1);
    
    }

    @Override
    protected boolean isFinished(){
        return System.currentTimeMillis() > endTimeMillis +1000;
    }
}
