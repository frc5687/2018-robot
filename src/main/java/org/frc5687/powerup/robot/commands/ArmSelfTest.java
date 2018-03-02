package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.subsystems.Arm;
import org.frc5687.powerup.robot.subsystems.Carriage;
import org.frc5687.powerup.robot.utils.PDP;
import org.frc5687.powerup.robot.commands.MoveCarriageToSetpointPID;

public class ArmSelfTest extends Command {
    private Arm arm;
    private Carriage carriage;
    private PDP pdp;
    private long upMillis;
    private long downMillis;
    private long stopMillis;
    private boolean finished;
    private double amps;

    public ArmSelfTest(Arm arm, Carriage carriage){
     requires(arm);
     requires(carriage);

     this.arm = arm;
     this.carriage = carriage;
    }

    @Override
    protected void initialize() {
        finished = false;
        upMillis = System.currentTimeMillis() + 125;
        stopMillis = System.currentTimeMillis() + 250;
        downMillis = System.currentTimeMillis() + 375;
        amps = 0;
    }

    @Override
    protected void execute() {
        if (System.currentTimeMillis()< upMillis){
            arm.drive(0.75);
            amps = Math.max(amps, pdp.getCurrent(RobotMap.PDP.ARM_SP));
        }
        else if (System.currentTimeMillis()< stopMillis){
            arm.drive(0);
        }
        else {
            arm.drive(-0.75);
            amps = Math.max(amps, pdp.getCurrent(RobotMap.PDP.ARM_SP));
        }
    }

    @Override
    protected void end() {
        if(amps < Constants.Arm.MIN_AMPS){

        }
    }

    @Override
    public boolean isFinished() {
        return System.currentTimeMillis() > downMillis;
    }
}
