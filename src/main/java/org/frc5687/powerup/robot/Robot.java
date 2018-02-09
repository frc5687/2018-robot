package org.frc5687.powerup.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.commands.CarriageZeroEncoder;
import org.frc5687.powerup.robot.commands.TestDriveTrainSpeed;
import org.frc5687.powerup.robot.commands.auto.*;
import org.frc5687.powerup.robot.subsystems.*;
import org.frc5687.powerup.robot.utils.AutoChooser;
import org.frc5687.powerup.robot.utils.JeVoisProxy;
import org.frc5687.powerup.robot.utils.PDP;

public class Robot extends IterativeRobot  {

    // I really don't like the idea of public static refrences to subsystems...

    private Command autoCommand;

    private OI oi;
    private DriveTrain driveTrain;
    private Intake intake;
    private Carriage carriage;
    private Climber _climber;
    private Arm _arm;
    public static AHRS imu;
    private UsbCamera camera;
    private PDP pdp;
    private AutoChooser _autoChooser;
    public static JeVoisProxy jeVoisProxy;


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
        jeVoisProxy = new JeVoisProxy(SerialPort.Port.kUSB);
        _arm = new Arm(oi);
        driveTrain = new DriveTrain(imu, oi);
        carriage = new Carriage(oi);
        intake = new Intake(oi);
        _climber = new Climber(oi);
        _autoChooser = new AutoChooser();

        try {
            camera = CameraServer.getInstance().startAutomaticCapture(0);
        } catch (Exception e) {
            DriverStation.reportError(e.getMessage(), true);
        }


        oi.initializeButtons(this);

        //autoCommand = new AutoAlign(driveTrain, imu, 45.0, 0.5);
        //autoCommand = new AutoDrive(driveTrain, 120.0, 0.5, "Cross auto line");
        // autoCommand = new AutoAlign(driveTrain, imu, 45.0, 0.5s);
        // autoCommand = new AutoDriveSimple(driveTrain, 120.0, 0.5);
        //autoCommand = new AutoDrive(driveTrain, 168.0, .5, true, true, 500000, "cross auto");
        //autoCommand = new TestDriveTrainSpeed(driveTrain);
        //autoCommand = new TestDriveTrainSpeed(driveTrain, 288.0, 1.0, true, true, 8000, "null zone");
    }
    public Arm getArm() { return _arm; }
    public DriveTrain getDriveTrain() { return driveTrain; }
    public Carriage getCarriage() { return carriage; }
    public Climber getClimber() { return _climber; }
    public Intake getIntake() { return intake; }
    public AHRS getIMU() { return imu; }


    @Override
    public void disabledInit() {

    }

    @Override
    public void autonomousInit() {
        imu.reset();
        driveTrain.resetDriveEncoders();
        carriage.zeroEncoder();
        String gameData = DriverStation.getInstance().getGameSpecificMessage();
        /*
        int switchSide = 0;
        int scaleSide = 0;
        if (gameData.length()>0) {
           switchSide = gameData.charAt(0)=='L' ? Constants.AutoChooser.LEFT : Constants.AutoChooser.RIGHT;
        }
        if (gameData.length()>1) {
            scaleSide = gameData.charAt(1)=='L' ? Constants.AutoChooser.LEFT : Constants.AutoChooser.RIGHT;
        }

        int autoPosition = _autoChooser.positionSwitchValue() + 1;
        int autoMode = _autoChooser.modeSwitchValue() + 1;
        SmartDashboard.putNumber("Auto/SwitchSide", switchSide);
        SmartDashboard.putNumber("Auto/ScaleSide", scaleSide);
        SmartDashboard.putNumber("Auto/Position", autoPosition);
        SmartDashboard.putNumber("Auto/Mode", autoMode);

        autoCommand = new AutoGroup(autoMode, autoPosition, switchSide, scaleSide, this);
        */
        autoCommand = new AutoIntake(intake);
        autoCommand.start();
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
        _autoChooser.updateDashboard();
    }

}
