package org.frc5687.powerup.robot.utils;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.RobotMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ben Bernard on 2/2/2018.
 */
public class AutoChooser {
    private RotarySwitch positionSwitch;
    private RotarySwitch modeSwitch;
    private RotarySwitch coopSwitch;
    private Map<Integer, String> positionLabels;
    private Map<Integer, String> modeLabels;
    private Map<Integer, String> coopLabels;
    private Map<Integer, Integer> delayQuantities;

    public AutoChooser(boolean isCompetitionBot) {
        positionLabels = new HashMap<Integer, String>();
        positionLabels.put(1, "Far Left");
        positionLabels.put(2, "Near Left");
        positionLabels.put(3, "Center Left");
        positionLabels.put(4, "Center Right");
        positionLabels.put(5, "Near Right");
        positionLabels.put(6, "Far Right");

        modeLabels = new HashMap<Integer, String>();
        modeLabels.put(0, "Stay Put");
        modeLabels.put(1, "Cross The Line");
        modeLabels.put(2, "Switch Then Cube");
        modeLabels.put(3, "Scale Then Scale");
        modeLabels.put(4, "Switch Then Switch");
        modeLabels.put(5, "Scale Then Switch");
        modeLabels.put(6, "Scale Then Backoff");
        modeLabels.put(7, "Switch Only");

        coopLabels = new HashMap<Integer, String>();
        coopLabels.put(0, "0");
        coopLabels.put(1, "250");
        coopLabels.put(2, "500");
        coopLabels.put(3, "750");
        coopLabels.put(4, "1000");
        coopLabels.put(5, "1250");
        coopLabels.put(6, "1500");
        coopLabels.put(7, "1750");
        coopLabels.put(8, "2000");
        coopLabels.put(9, "Defensive");
        coopLabels.put(10, "Stay in Lane");

        delayQuantities = new HashMap<Integer, Integer>();
        delayQuantities.put(0, 0);
        delayQuantities.put(1, 250);
        delayQuantities.put(2, 500);
        delayQuantities.put(3, 750);
        delayQuantities.put(4, 1000);
        delayQuantities.put(5, 1250);
        delayQuantities.put(6, 1500);
        delayQuantities.put(7, 1750);
        delayQuantities.put(8, 2000);
        delayQuantities.put(9, 2500);
        delayQuantities.put(10, 3000);
        delayQuantities.put(11, 3500);
        delayQuantities.put(10, 3000);

        if (isCompetitionBot) {
            positionSwitch = new RotarySwitch(RobotMap.AutoChooser.POSITION_SWITCH,  Constants.RotarySwitch.TOLERANCE, 0.07692, 0.15384, 0.23076, 0.30768, 0.3846, 0.46152, 0.53844, 0.61536, 0.69228, 0.7692, 0.84612, 0.92304);
            modeSwitch = new RotarySwitch(RobotMap.AutoChooser.MODE_SWITCH,  Constants.RotarySwitch.TOLERANCE, 0.07692, 0.15384, 0.23076, 0.30768, 0.3846, 0.46152, 0.53844, 0.61536, 0.69228, 0.7692, 0.84612, 0.92304);
            coopSwitch = new RotarySwitch(RobotMap.AutoChooser.COOP_SWITCH, Constants.RotarySwitch.TOLERANCE, 0.07692, 0.15384, 0.23076, 0.30768, 0.3846, 0.46152, 0.53844, 0.61536, 0.69228, 0.7692, 0.84612, 0.92304);
        } else {
            positionSwitch = new RotarySwitch(RobotMap.AutoChooser.POSITION_SWITCH, Constants.RotarySwitch.TOLERANCE * 3, .092, .235, .505, .680, .823, .958);
            modeSwitch = new RotarySwitch(RobotMap.AutoChooser.MODE_SWITCH, Constants.RotarySwitch.TOLERANCE, .09, .17, .23, .31, .5, .59, .68, .75, .82, .91, .96);
            coopSwitch = new RotarySwitch(RobotMap.AutoChooser.COOP_SWITCH, Constants.RotarySwitch.TOLERANCE, 0.07692, 0.15384, 0.23076, 0.30768, 0.3846, 0.46152, 0.53844, 0.61536, 0.69228, 0.7692, 0.84612, 0.92304);
        }
    }


    public int positionSwitchValue(){
        return positionSwitch.get() + 1;
    }

    public int modeSwitchValue(){
        return modeSwitch.get();
    }

    public int coopSwitchValue(){
        return coopSwitch.get();
    }

    private String getPositionLabel() {
        int position = positionSwitchValue();
        return positionLabels.getOrDefault(position, "Unused");
    }

    private String getPositionLabel(int position) {
        return positionLabels.getOrDefault(position, "Unused");
    }

    private String getModeLabel() {
        int mode = modeSwitchValue();
        return modeLabels.getOrDefault(mode, "Unused");
    }

    private String getCoopLabel() {
        int coop = coopSwitchValue();
        return coopLabels.getOrDefault(coop, "Unused");
    }

    private String getModeLabel(int mode) {
        return modeLabels.getOrDefault(mode, "Unused");
    }

    public Integer getDelayMillis() {
        int val = coopSwitchValue();
        switch (val) {
            case Constants.AutoChooser.Coop.DEFENSE:
            case Constants.AutoChooser.Coop.STAY_IN_LANE:
                return delayQuantities.getOrDefault(0, 0);
            default:
                return delayQuantities.getOrDefault(val, 0);
        }
    }

    public boolean stayInYourOwnLane() {
        int val = coopSwitchValue();
        return val == Constants.AutoChooser.Coop.STAY_IN_LANE;
    }

    private Integer getDelayMillis(int val) {
        return delayQuantities.getOrDefault(val, 0);
    }

    public void updateDashboard(){
        SmartDashboard.putString("AutoChooser/Label/Position", getPositionLabel());
        SmartDashboard.putString("AutoChooser/Label/Mode", getModeLabel());
        SmartDashboard.putString("AutoChooser/Label/Coop", getCoopLabel());
        SmartDashboard.putNumber("AutoChooser/Label/Delay", getDelayMillis());
        SmartDashboard.putBoolean("AutoChooser/Label/stayInYourOwnLane", stayInYourOwnLane());
        SmartDashboard.putNumber("AutoChooser/Raw/Position", positionSwitch.getRaw());
        SmartDashboard.putNumber("AutoChooser/Raw/Mode", modeSwitch.getRaw());
        SmartDashboard.putNumber("AutoChooser/Raw/Coop", coopSwitch.getRaw());
        SmartDashboard.putNumber("AutoChooser/Numeric/Position", positionSwitchValue());
        SmartDashboard.putNumber("AutoChooser/Numeric/Mode", modeSwitchValue());
        SmartDashboard.putNumber("AutoChooser/Numeric/Coop", coopSwitchValue());
//        SmartDashboard.putString("AutoChooser/Selection", AutoGroup.getDescription(springSwitch.get(), gearSwitch.get(), hopperSwitch.get()));
  }
}
