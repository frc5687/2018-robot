package org.frc5687.powerup.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.DriveWith2Joysticks;

/**
 * Created by Baxter on 3/22/2017.
 */
public class Intake extends Subsystem {

    private VictorSP leftMotor;
    private VictorSP rightMotor;

    private OI oi;

    public Intake(OI oi) {
        leftMotor = new VictorSP(RobotMap.Intake.LEFT_MOTOR);
        rightMotor = new VictorSP(RobotMap.Intake.RIGHT_MOTOR);

        this.oi = oi;
    }

    @Override
    protected void initDefaultCommand() {

        //setDefaultCommand(new DriveWith2Joysticks(this, oi));
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        leftMotor.set(leftSpeed * (Constants.Intake.LEFT_MOTORS_INVERTED ? -1 : 1));
        rightMotor.set(rightSpeed * (Constants.Intake.RIGHT_MOTORS_INVERTED ? -1 : 1));
    }

}
