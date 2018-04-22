package org.frc5687.powerup.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.commands.KillAll;
import org.frc5687.powerup.robot.commands.RumbleControllersForNMillis;
import org.frc5687.powerup.robot.commands.actions.ServoDown;
import org.frc5687.powerup.robot.commands.actions.ServoUp;
import org.frc5687.powerup.robot.commands.auto.*;
import org.frc5687.powerup.robot.subsystems.*;
import org.frc5687.powerup.robot.utils.AutoChooser;
import org.frc5687.powerup.robot.utils.JeVoisProxy;
import org.frc5687.powerup.robot.utils.LidarProxy;
import org.frc5687.powerup.robot.utils.PDP;
import sun.util.resources.cldr.or.CalendarData_or_IN;

public class Robot extends TimedRobot {

    private static Robot _instance;

    private Command autoCommand;

    private OI oi;
    private DriveTrain driveTrain;
    private Intake intake;
    private Carriage carriage;
    private Climber _climber;
    private Arm _arm;
    private Lights _lights;
    public AHRS imu;
    private UsbCamera camera0;
    private UsbCamera camera1;
    private PDP pdp;
    private AutoChooser _autoChooser;
    public JeVoisProxy jeVoisProxy;
    private LidarProxy lidarProxy;
    private DigitalInput _identityFlag;
    private boolean _isCompetitionBot;
    private long lastPeriod;
    private int ticksPerUpdate = 20;
    private int updateTick = 0;
    private boolean hasRumbledForEndgame;
    private boolean _manualLightFlashRequested;
    private boolean abortAuton;


    public Robot() {
    }

    @Override
    public void startCompetition() {
        super.startCompetition();
    }

    @Override
    public void robotInit() {
        _instance = this;
        _identityFlag = new DigitalInput(RobotMap.IDENTITY_FLAG);
        _isCompetitionBot = _identityFlag.get();
        imu = new AHRS(SPI.Port.kMXP, (byte) 100);
        pdp = new PDP(_isCompetitionBot);
        oi = new OI(this);
        //jeVoisProxy = new JeVoisProxy(SerialPort.Port.kUSB);
        //lidarProxy = new LidarProxy(SerialPort.Port.kMXP);
        driveTrain = new DriveTrain(this, imu, oi);
        carriage = new Carriage(oi, pdp, _isCompetitionBot);
        intake = new Intake(oi, pdp, _isCompetitionBot);
        _arm = new Arm(oi, pdp, intake, _isCompetitionBot);
        intake.setArm(_arm);
        _lights = new Lights(this);
        _climber = new Climber(oi, pdp, intake, _isCompetitionBot);
        _autoChooser = new AutoChooser(_isCompetitionBot);
        SmartDashboard.putString("Identity", (_isCompetitionBot ? "Diana" : "Jitterbug"));
        lastPeriod = System.currentTimeMillis();
        setPeriod(1 / Constants.CYCLES_PER_SECOND);

        try {
            if (isCompetitionBot()) {
                camera0 = CameraServer.getInstance().startAutomaticCapture(0);
            }
        } catch (Exception e) {
            DriverStation.reportError(e.getMessage(), true);
        }

        try {
            if (isCompetitionBot()) {
                camera1 = CameraServer.getInstance().startAutomaticCapture(1);
            }
        } catch (Exception e) {
            DriverStation.reportError(e.getMessage(), true);
        }


        oi.initializeButtons(this);
        LiveWindow.disableAllTelemetry();
        _lights.initialize();
    }

    public Arm getArm() { return _arm; }
    public DriveTrain getDriveTrain() { return driveTrain; }
    public Carriage getCarriage() { return carriage; }
    public Climber getClimber() { return _climber; }
    public Intake getIntake() { return intake; }
    public AHRS getIMU() { return imu; }
    public PDP getPDP() { return pdp; }
    public Lights getLights() { return _lights; }
    public JeVoisProxy getJeVoisProxy() { return jeVoisProxy; }
    public LidarProxy getLidarProxy() { return lidarProxy; }
    public OI getOI() { return oi; }



    @Override
    public void disabledInit() {

    }

    @Override
    public void autonomousInit() {
        imu.reset();
        driveTrain.resetDriveEncoders();
        driveTrain.enableBrakeMode();
        carriage.zeroEncoder();
        _manualLightFlashRequested = false;
        hasRumbledForEndgame = false;
        abortAuton = false;
        // Reset the lights slider in case it was left on
        SmartDashboard.putNumber("DB/Slider 0", 0.0);

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
        int coopMode = _autoChooser.coopSwitchValue();
        boolean stayInYourOwnLane = _autoChooser.stayInYourOwnLane();
        long autoDelayInMillis = _autoChooser.getDelayMillis();
        SmartDashboard.putNumber("Auto/SwitchSide", switchSide);
        SmartDashboard.putNumber("Auto/ScaleSide", scaleSide);
        SmartDashboard.putNumber("Auto/Position", autoPosition);
        SmartDashboard.putNumber("Auto/Mode", autoMode);
        SmartDashboard.putNumber("Auto/Coop", coopMode);
        DriverStation.reportError("Running AutoGroup with mode: " + autoMode + ", position: " + autoPosition + ", delay:" + Long.toString(autoDelayInMillis) + "ms, switchSide: " + switchSide + ", scaleSide: " + scaleSide + ", coop: " + coopMode, false);
        autoCommand = new AutoGroup(autoMode, autoPosition, switchSide, scaleSide, autoDelayInMillis, coopMode, this);
        autoCommand.start();
    }

    @Override
    public void teleopInit() {
        if (autoCommand != null) autoCommand.cancel();
        _manualLightFlashRequested = false;
        driveTrain.enableCoastMode();
        hasRumbledForEndgame = false;
        hasRumbledForEndgame = false;
    }

    @Override
    public void testInit() {
    }

    @Override
    public void robotPeriodic() {
        oi.pollConsole();
        updateDashboard();
        long now = System.currentTimeMillis();
        SmartDashboard.putNumber("millisSinceLastPeriodic", now - lastPeriod);
        lastPeriod = now;
    }

    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();
        driveTrain.enableCoastMode();
        intake.stopServo();
    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        if (abortAuton && autoCommand != null) {
            autoCommand.cancel();
        }
    }

    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();

        double matchTime = DriverStation.getInstance().getMatchTime();

        if (!hasRumbledForEndgame && matchTime <= Constants.OI.START_RUMBLE_AT) {
            new RumbleControllersForNMillis(oi, 2000, Constants.OI.RUMBLE_DURATION).start();
            hasRumbledForEndgame = true;
        }

        int operatorPOV = oi.getOperatorPOV();
        int driverPOV = oi.getDriverPOV();

        if (operatorPOV == 8) {
            new ServoUp(intake).start();
        } else if (operatorPOV == 4) {
            new ServoDown(intake).start();
        }

        if (driverPOV == 2 || operatorPOV == 2) {
            new KillAll(this).start();
        }

        _manualLightFlashRequested = operatorPOV == 6;
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
            _climber.updateDashboard();
            estimateIntakeHeight();

            oi.updateConsole();
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

    @Override
    protected void loopFunc() {
        try {
            super.loopFunc();
        } catch (Throwable throwable) {
            DriverStation.reportError("Unhandled exception: " + throwable.toString(), throwable.getStackTrace());
            System.exit(1);
        }
    }

    public double estimateIntakeHeight() {
        double carriageHeight = carriage.estimateHeight();
        double armHeight = _arm.estimateHeight();
        double intakeHeight = carriageHeight + armHeight;
        SmartDashboard.putNumber("Intake/CarriageHeight", carriageHeight);
        SmartDashboard.putNumber("Intake/ArmHeight", armHeight);
        SmartDashboard.putNumber("Intake/IntakeHeight", intakeHeight);
        return intakeHeight;
    }

    public boolean isInWarningPeriod() {
        double remaining = DriverStation.getInstance().getMatchTime();
        return (remaining < Constants.START_ALERT) && (remaining > Constants.END_ALERT);
    }

    public boolean isManualLightFlashRequested() {
        return _manualLightFlashRequested;
    }

    public void requestAbortAuton() {
        abortAuton = true;
        DriverStation.reportError("Auto abort requested", true);
    }

    public static void requestAbortAutonStatic() {
        _instance.requestAbortAuton();
    }

}
