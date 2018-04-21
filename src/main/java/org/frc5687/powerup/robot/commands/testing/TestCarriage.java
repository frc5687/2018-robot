package org.frc5687.powerup.robot.commands.testing;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.commands.auto.AutoZeroCarriage;

public class TestCarriage extends CommandGroup {
    public TestCarriage(Robot robot){
        // Run the carriage up to the hall efect


        addSequential(new TestCarriageMotor(robot.getCarriage()));
        addSequential(new AutoZeroCarriage(robot.getCarriage()));
    }

    private enum State {
        ZERO,
        ZERODONE,
        MIDDLE,
        MIDDLEDONE,
        BOTTOM,
        BOTTOMDONE,
        WAIT,
        DONE
    }

}
