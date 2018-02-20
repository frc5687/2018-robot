package org.frc5687.powerup.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.commands.auto.*;
import org.frc5687.powerup.robot.subsystems.*;
import org.frc5687.powerup.robot.utils.AutoChooser;
import org.frc5687.powerup.robot.utils.JeVoisProxy;
import org.frc5687.powerup.robot.utils.PDP;

public class Robot extends TimedRobot {

    // I really don't like the idea of public static refrences to subsystems...

    private Command autoCommand;

    private OI oi;
    private DriveTrain driveTrain;
    private Intake intake;
    private Carriage carriage;
    private Climber _climber;
    private Arm _arm;
    public AHRS imu;
    private UsbCamera camera;
    private PDP pdp;
    private AutoChooser _autoChooser;
    public JeVoisProxy jeVoisProxy;
    private DigitalInput _identityFlag;
    private boolean _isCompetitionBot;
    private long lastPeriod;
    private int ticksPerUpdate = 5;
    private int updateTick = 0;


    public Robot() {
    }

    @Override
    public void startCompetition() {
        super.startCompetition();
    }

    @Override
    public void robotInit() {
        _identityFlag = new DigitalInput(RobotMap.IDENTITY_FLAG);
        _isCompetitionBot = _identityFlag.get();
        imu = new AHRS(SPI.Port.kMXP);
        pdp = new PDP();
        oi = new OI(this);
        jeVoisProxy = new JeVoisProxy(SerialPort.Port.kUSB);
        _arm = new Arm(oi, _isCompetitionBot);
        driveTrain = new DriveTrain(imu, oi);
        carriage = new Carriage(oi, _isCompetitionBot);
        intake = new Intake(oi);
        _climber = new Climber(oi);
        _autoChooser = new AutoChooser(_isCompetitionBot);
        SmartDashboard.putString("Identity", (_isCompetitionBot ? "Diana" : "Jitterbug"));
        lastPeriod = System.currentTimeMillis();
        //setPeriod(0.01);

        try {
            camera = CameraServer.getInstance().startAutomaticCapture(0);
        } catch (Exception e) {
            DriverStation.reportError(e.getMessage(), true);
        }


        oi.initializeButtons(this);
        LiveWindow.disableAllTelemetry();

    }

    public Arm getArm() { return _arm; }
    public DriveTrain getDriveTrain() { return driveTrain; }
    public Carriage getCarriage() { return carriage; }
    public Climber getClimber() { return _climber; }
    public Intake getIntake() { return intake; }
    public AHRS getIMU() { return imu; }
    public JeVoisProxy getJeVoisProxy() { return jeVoisProxy; }


    @Override
    public void disabledInit() {

    }

    @Override
    public void autonomousInit() {
        imu.reset();
        driveTrain.resetDriveEncoders();
        carriage.zeroEncoder();
        String gameData = DriverStation.getInstance().getGameSpecificMessage();
        if (gameData==null) { gameData = ""; }
        int retries = 100;
        while (gameData.length() < 2 && retries > 0) {
            DriverStation.reportError("Gamedata is " + gameData + " retrying " + retries, false);
            try {
                Thread.sleep(5);
                gameData = DriverStation.getInstance().getGameSpecificMessage();
                if (gameData==null) { gameData = ""; }
            } catch (Exception e) {
            }
            retries--;
        }
        SmartDashboard.putString("Auto/gameData", gameData);
        DriverStation.reportError("gameData before parse: " + gameData, false);
        int switchSide = 0;
        int scaleSide = 0;
        if (gameData.length()>0) {
           switchSide = gameData.charAt(0)=='L' ? Constants.AutoChooser.LEFT : Constants.AutoChooser.RIGHT;
        }
        if (gameData.length()>1) {
            scaleSide = gameData.charAt(1)=='L' ? Constants.AutoChooser.LEFT : Constants.AutoChooser.RIGHT;
        }
        int autoPosition = _autoChooser.positionSwitchValue();
        int autoMode = _autoChooser.modeSwitchValue();
        SmartDashboard.putNumber("Auto/SwitchSide", switchSide);
        SmartDashboard.putNumber("Auto/ScaleSide", scaleSide);
        SmartDashboard.putNumber("Auto/Position", autoPosition);
        SmartDashboard.putNumber("Auto/Mode", autoMode);
        DriverStation.reportError("Running AutoGroup with mode: " + autoMode + ", position: " + autoPosition + ", switchSide: " + switchSide + ", scaleSide: " + scaleSide, false);
        autoCommand = new AutoGroup(autoMode, autoPosition, switchSide, scaleSide, this);
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
        long now = System.currentTimeMillis();
        SmartDashboard.putNumber("millisSinceLastPeriodic", now - lastPeriod);
        lastPeriod = now;
    }

    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();
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
        updateTick++;
        if (updateTick == ticksPerUpdate) {
            pdp.updateDashboard();
            _autoChooser.updateDashboard();
            _arm.updateDashboard();
            carriage.updateDashboard();
            driveTrain.updateDashboard();
            intake.updateDashboard();
            updateTick = 0;
        }
    }
    public boolean pickConstant(boolean competitionValue, boolean practiceValue){
        return _isCompetitionBot ? competitionValue : practiceValue;
    }

    public int pickConstant(int competitionValue, int practiceValue){
        return _isCompetitionBot ? competitionValue : practiceValue;
    }


    public double pickConstant(double competitionValue, double practiceValue){
        return _isCompetitionBot ? competitionValue : practiceValue;
    }

    public boolean isCompetitionBot(){
        return _isCompetitionBot;
    }
}
