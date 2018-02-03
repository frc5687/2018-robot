package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.commands.CarriageZeroEncoder;

/**
 * Created by Ben Bernard on 2/2/2018.
 */
public class AutoGroup extends CommandGroup {
    public AutoGroup(int mode, int position, int switchSide, int scaleSide, Robot robot) {
        super();

        // Start with the "always" operations
        addParallel(new CarriageZeroEncoder(robot.getCarriage()));

        switch (mode) {
            case Constants.AutoChooser.Mode.STAY_PUT:
                // Nothing to do here but look sad
                break;

            case Constants.AutoChooser.Mode.CROSS_AUTOLINE:
                // addSequential(new AutoCrossBaseline(robot.getDriveTrain()));
                break;

            case Constants.AutoChooser.Mode.SWITCH_ONLY:
                if (switchSide==Constants.AutoChooser.LEFT) {

                } else {

                }

                // addSequential(new AutoCrossBaseline(robot.getDriveTrain()));
                break;



        }



    }

}
