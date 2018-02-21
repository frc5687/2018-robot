package org.frc5687.powerup.robot.utils;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PDP extends PowerDistributionPanel {
    public PDP() {
        super();
    }

    public void updateDashboard() {
        SmartDashboard.putNumber("PDP/Current/0", getCurrent(0));
        SmartDashboard.putNumber("PDP/Current/1", getCurrent(1));
        SmartDashboard.putNumber("PDP/Current/2", getCurrent(2));
        SmartDashboard.putNumber("PDP/Current/3", getCurrent(3));
        SmartDashboard.putNumber("PDP/Current/4", getCurrent(4));
        SmartDashboard.putNumber("PDP/Current/5", getCurrent(5));
        SmartDashboard.putNumber("PDP/Current/6", getCurrent(6));
        SmartDashboard.putNumber("PDP/Current/7", getCurrent(7));
        SmartDashboard.putNumber("PDP/Current/8", getCurrent(8));
        SmartDashboard.putNumber("PDP/Current/9", getCurrent(9));
        SmartDashboard.putNumber("PDP/Current/10", getCurrent(10));
        SmartDashboard.putNumber("PDP/Current/11", getCurrent(11));
        SmartDashboard.putNumber("PDP/Current/12", getCurrent(12));
        SmartDashboard.putNumber("PDP/Current/13", getCurrent(13));
        SmartDashboard.putNumber("PDP/Current/14", getCurrent(14));
        SmartDashboard.putNumber("PDP/Current/15", getCurrent(15));
    }
    public double carriageAmps(){
        return getCurrent(14);
    }

}