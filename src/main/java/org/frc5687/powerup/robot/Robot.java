package org.frc5687.powerup.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.frc5687.powerup.robot.subsystems.Carriage;
import org.frc5687.powerup.robot.subsystems.Arm;
import org.frc5687.powerup.robot.subsystems.DriveTrain;
import com.kauailabs.navx.*;
import org.frc5687.powerup.robot.subsystems.Intake;
import edu.wpi.first.wpilibj.CameraServer;
import org.frc5687.powerup.robot.utils.PDP;

public class Robot extends IterativeRobot  {

    // I really don't like the idea of public static refrences to subsystems...

    private Command autoCommand;

    private OI oi;
    private DriveTrain driveTrain;
    private Intake intake;
    private Carriage carriage;
    private Arm _arm;
    private AHRS imu;
    private UsbCamera camera;
    private PDP pdp;

    public Robot() {
    }

    @Override
    public void startCompetition() {
        super.startCompetition();
    }

    @Override
    public void robotInit() {

        imu = new AHRS(SPI.Port.kMXP);
        pdp = new PDP();
        oi = new OI();
        _arm = new Arm(oi);
        driveTrain = new DriveTrain(imu, oi);
        carriage = new Carriage(oi);
        intake = new Intake(oi);

        try {
            camera = CameraServer.getInstance().startAutomaticCapture(0);
        } catch (Exception e) {
            DriverStation.reportError(e.getMessage(), true);
        }

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
        pdp.updateDashboard();
        intake.updateDashboard();
        carriage.updateDashboard();
    }

}
