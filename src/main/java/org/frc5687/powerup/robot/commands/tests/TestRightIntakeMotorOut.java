package org.frc5687.powerup.robot.commands.tests;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.subsystems.Intake;
import org.frc5687.powerup.robot.utils.PDP;

public class TestRightIntakeMotorOut extends Command {
    private Intake _intake;
    private PDP _pdp;
    private long _timeout = 1000;
    private long _endtime;

    public TestRightIntakeMotorOut(Intake intake, PDP pdp) {
        _intake = intake;
        _pdp = pdp;
    }

    public TestRightIntakeMotorOut(Intake intake, PDP pdp, long timeout) {
        _intake = intake;
        _pdp = pdp;
        _timeout = timeout;
    }

    @Override
    protected void initialize() {
        _endtime = System.currentTimeMillis() + _timeout;
        _intake.drive(0, -Constants.Intake.TEST_INTAKE_SPEED);
        DriverStation.reportError("Running right intake out for one second", false);
    }

    @Override
    protected void execute() {
        double current = _pdp.getCurrent(RobotMap.PDP.INTAKE_LEFT_SP);
        if (current > Constants.Intake.PDP_OPERATING_MAX) {
            DriverStation.reportError("Right Intake Motor Drawing too much current: " + Double.toString(current), false);
        }
        if (current < Constants.Intake.PDP_OPERATING_MIN) {
            DriverStation.reportError("Right Intake Motor not drawing enough current: " + Double.toString(current), false);
        }
    }

    @Override
    protected boolean isFinished() {
        return _endtime >= System.currentTimeMillis();
    }

    @Override
    protected void end() {
        _intake.drive(0, 0);
    }
}
