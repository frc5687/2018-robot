package org.frc5687.powerup.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.frc5687.powerup.robot.subsystems.DriveTrain;
import com.kauailabs.navx.*;

public class Robot extends IterativeRobot  {

    // I really don't like the idea of public static refrences to subsystems...

    private Command autoCommand;

    private OI oi;
    private DriveTrain driveTrain;
    private AHRS imu;

    public Robot() {
    }

    @Override
    public void startCompetition() {
        super.startCompetition();
    }

    @Override
    public void robotInit() {

        imu = new AHRS(SPI.Port.kMXP);
        oi = new OI();
        driveTrain = new DriveTrain(imu, oi);
    }

    @Override
    public void disabledInit() {

    }

    @Override
    public void autonomousInit() {
        if (autoCommand != null) {
            autoCommand.start();
        }
    }

    @Override
    public void teleopInit() {
        if (autoCommand != null) autoCommand.cancel();
    }

    @Override
    public void testInit() {
    }

    @Override
    public void robotPeriodic() {
        updateDashboard();
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void testPeriodic() {
    }

    public void updateDashboard() {
    }

}
