package org.frc5687.powerup.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.DriveLights;

public class Lights extends Subsystem {
    private Robot _robot;
    private Servo _left;
    private Servo _right;
    private boolean isRedAlliance;

    public Lights(Robot robot) {
        _robot = robot;
        _left = new Servo(RobotMap.Lights.LEFT);
        _right = new Servo(RobotMap.Lights.RIGHT);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveLights(_robot));
    }

    public void setLeft(Servo left) {
        _left = left;
    }

    public void setRight(Servo right) {
        _right = right;
    }
}
