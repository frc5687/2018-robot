package org.frc5687.powerup.robot.commands.testing;

import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.subsystems.Arm;
import org.frc5687.powerup.robot.subsystems.Carriage;
import org.frc5687.powerup.robot.utils.PDP;
import org.frc5687.powerup.robot.commands.MoveCarriageToSetpointPID;
import edu.wpi.first.wpilibj.DriverStation;

import static java.lang.Math.abs;


public class ArmMotorTest extends Command {
    private Arm arm;
    private Carriage carriage;
    private PDP pdp;
    private long upMillis;
    private long downMillis;
    private long stopMillis;
    private boolean finished;
    private double amps;
    private double bottomAngle;
    private double topAngle;
    private double startAngle;

    public ArmMotorTest(Arm arm, Carriage carriage){
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
        topAngle = 0;
        startAngle = arm.getAngle();
    }

    @Override
    protected void execute() {
        if (System.currentTimeMillis()< upMillis){
            arm.drive(0.75);
            amps = Math.max(amps, pdp.getCurrent(RobotMap.PDP.ARM_SP));
            topAngle = Math.max(topAngle, arm.getAngle());

        }
        else if (System.currentTimeMillis()< stopMillis){
            arm.drive(0);
            bottomAngle = topAngle;
        }
        else {
            arm.drive(-0.75);
            amps = Math.max(amps, pdp.getCurrent(RobotMap.PDP.ARM_SP));
            bottomAngle = Math.min(bottomAngle, arm.getAngle());
        }
    }

    @Override
    protected void end() {
        if (amps < Constants.Arm.MIN_AMPS) {
            DriverStation.reportError(amps + " amps drawn", false);
        }
        if (abs(startAngle - topAngle) < 20) {
            DriverStation.reportError("angle change " + (startAngle - topAngle), false);
        }
        if (abs(topAngle - bottomAngle) > 20) {
            DriverStation.reportError("angle change " + (bottomAngle - topAngle), false);
        }
    }

    @Override
    public boolean isFinished() {
        return System.currentTimeMillis() > downMillis;
    }
}
