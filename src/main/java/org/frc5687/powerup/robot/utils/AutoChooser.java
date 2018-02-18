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
    private RotarySwitch delaySwitch;

    public AutoChooser(boolean isCompetitionBot) {
        if (isCompetitionBot) {
            positionSwitch = new RotarySwitch(RobotMap.AutoChooser.POSITION_SWITCH,  Constants.RotarySwitch.TOLERANCE, 0.07692, 0.15384, 0.23076, 0.30768, 0.3846, 0.46152, 0.53844, 0.61536, 0.69228, 0.7692, 0.84612, 0.92304);
            modeSwitch = new RotarySwitch(RobotMap.AutoChooser.MODE_SWITCH,  Constants.RotarySwitch.TOLERANCE, 0.07692, 0.15384, 0.23076, 0.30768, 0.3846, 0.46152, 0.53844, 0.61536, 0.69228, 0.7692, 0.84612, 0.92304);
            delaySwitch = new RotarySwitch(RobotMap.AutoChooser.DELAY_SWITCH, Constants.RotarySwitch.TOLERANCE, 0.07692, 0.15384, 0.23076, 0.30768, 0.3846, 0.46152, 0.53844, 0.61536, 0.69228, 0.7692, 0.84612, 0.92304);
        } else {
            positionSwitch = new RotarySwitch(RobotMap.AutoChooser.POSITION_SWITCH, Constants.RotarySwitch.TOLERANCE * 3, .092, .235, .505, .680, .823, .958);
            modeSwitch = new RotarySwitch(RobotMap.AutoChooser.MODE_SWITCH, Constants.RotarySwitch.TOLERANCE, .09, .17, .23, .31, .5, .59, .68, .75, .82, .91, .96);
            delaySwitch = new RotarySwitch(RobotMap.AutoChooser.DELAY_SWITCH, Constants.RotarySwitch.TOLERANCE, 0.07692, 0.15384, 0.23076, 0.30768, 0.3846, 0.46152, 0.53844, 0.61536, 0.69228, 0.7692, 0.84612, 0.92304);
        }
    }


    public int positionSwitchValue(){
        return positionSwitch.get() + 1;
    }

    public int modeSwitchValue(){
        return modeSwitch.get();
    }

    public int delaySwitchValue(){
        return delaySwitch.get();
    }


    public void updateDashboard(){
        SmartDashboard.putNumber("AutoChooser/Raw/Position", positionSwitch.getRaw());
        SmartDashboard.putNumber("AutoChooser/Raw/Mode", modeSwitch.getRaw());
        SmartDashboard.putNumber("AutoChooser/Raw/Delay", delaySwitch.getRaw());
        SmartDashboard.putNumber("AutoChooser/Numeric/Position", positionSwitchValue());
        SmartDashboard.putNumber("AutoChooser/Numeric/Mode", modeSwitchValue());
        SmartDashboard.putNumber("AutoChooser/Numeric/Delay", delaySwitch.get());
//        SmartDashboard.putString("AutoChooser/Selection", AutoGroup.getDescription(springSwitch.get(), gearSwitch.get(), hopperSwitch.get()));
  }
}
