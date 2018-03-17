package org.frc5687.powerup.robot.utils;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PDP extends PowerDistributionPanel {
    public PDP() {
        super();
    }

    public void updateDashboard() {
        SmartDashboard.putNumber("PDP/Current/0(intakeLeftSP)", getCurrent(0));
        SmartDashboard.putNumber("PDP/Current/1(rightBackSPX)", getCurrent(1));
        SmartDashboard.putNumber("PDP/Current/2(rightFrontSRX)", getCurrent(2));
        SmartDashboard.putNumber("PDP/Current/3(leftBackSpx)", getCurrent(3));
        SmartDashboard.putNumber("PDP/Current/4", getCurrent(4));
        SmartDashboard.putNumber("PDP/Current/5", getCurrent(5));
        SmartDashboard.putNumber("PDP/Current/6", getCurrent(6));
        SmartDashboard.putNumber("PDP/Current/7", getCurrent(7));
        SmartDashboard.putNumber("PDP/Current/8", getCurrent(8));
        SmartDashboard.putNumber("PDP/Current/9", getCurrent(9));
        SmartDashboard.putNumber("PDP/Current/10(intakeRightSP)", getCurrent(10));
        SmartDashboard.putNumber("PDP/Current/11", getCurrent(11));
        SmartDashboard.putNumber("PDP/Current/12(leftFrontSRX)", getCurrent(12));
        SmartDashboard.putNumber("PDP/Current/13(climberSP)", getCurrent(13));
        SmartDashboard.putNumber("PDP/Current/14(carriageSP)", getCurrent(14));
        SmartDashboard.putNumber("PDP/Current/15(armSP)", getCurrent(15));
    }
    
    public boolean excessiveCurrent(int channel, double threshold) {
        return getCurrent(channel) >= threshold;
    }

}