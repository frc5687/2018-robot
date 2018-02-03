package org.frc5687.powerup.robot.utils;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.RobotMap;

/**
 * Created by Ben Bernard on 2/2/2018.
 */
public class AutoChooser {
    private RotarySwitch positionSwitch;
    private RotarySwitch modeSwitch;

    public AutoChooser() {
        positionSwitch = new RotarySwitch(RobotMap.AutoChooser.POSITION_SWITCH, Constants.RotarySwitch.TOLERANCE, .092, .235, .505, .680, .823, .958);
        modeSwitch = new RotarySwitch(RobotMap.AutoChooser.MODE_SWITCH,  .02, .09, .17, .23, .31, .5, .59, .68, .75, .82, .91, .96);
    }


    public int positionSwitchValue(){
        return positionSwitch.get();
    }

    public int modeSwitchValue(){
        return modeSwitch.get();
    }


    public void updateDashboard(){
        SmartDashboard.putNumber("AutoChooser/Raw/Position", positionSwitch.getRaw());
        SmartDashboard.putNumber("AutoChooser/Raw/Mode", modeSwitch.getRaw());
        SmartDashboard.putNumber("AutoChooser/Numeric/Position", positionSwitch.get());
        SmartDashboard.putNumber("AutoChooser/Numeric/Mode", modeSwitch.get());
//        SmartDashboard.putString("AutoChooser/Selection", AutoGroup.getDescription(springSwitch.get(), gearSwitch.get(), hopperSwitch.get()));
  }
}
