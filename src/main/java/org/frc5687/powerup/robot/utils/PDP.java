package org.frc5687.powerup.robot.utils;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PDP extends PowerDistributionPanel {
    private boolean _isCompetitionBot;
    private double[] cache;
    private Thread _thread;
    private PDPCacheUpdater _pdpCacheUpdater;

    public PDP(boolean isCompetitionBot) {
        super();
        _isCompetitionBot = isCompetitionBot;
        cache = new double[16];
        _pdpCacheUpdater = new PDPCacheUpdater(this);
        _thread = new Thread(_pdpCacheUpdater);
        _thread.start();
    }

    @Override
    public double getCurrent(int channel) {
        return getCurrent(channel, false);
    }

    public double getCurrent(int channel, boolean overrideCache) {
        if (overrideCache) {
            return super.getCurrent(channel);
        }
        return cache[channel];
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
        SmartDashboard.putNumber("PDP/Current/11", getCurrent(11));
        SmartDashboard.putNumber("PDP/Current/12(leftFrontSRX)", getCurrent(12));
        if (_isCompetitionBot) {
            SmartDashboard.putNumber("PDP/Current/13(climberSP)", getCurrent(13));
            SmartDashboard.putNumber("PDP/Current/10(intakeRightSP)", getCurrent(10));
        } else {
            SmartDashboard.putNumber("PDP/Current/10(climberSP)", getCurrent(10));
            SmartDashboard.putNumber("PDP/Current/13(intakeRightSP)", getCurrent(13));
        }
        SmartDashboard.putNumber("PDP/Current/14(carriageSP)", getCurrent(14));
        SmartDashboard.putNumber("PDP/Current/15(armSP)", getCurrent(15));
    }
    
    public boolean excessiveCurrent(int channel, double threshold) {
        double current = getCurrent(channel);
        if (current >= threshold) {
            DriverStation.reportError("PDP Channel: " + channel + " excessive at " + current, false);
            return true;
        }
        return false;
    }

    private class PDPCacheUpdater implements Runnable {
        private PDP _pdp;

        public PDPCacheUpdater(PDP pdp) {
            _pdp = pdp;
        }

        public void run() {
            while (true) {
                try {
                    for (int i = 0; i < 16; i++) {
                        _pdp.cache[i] = _pdp.getCurrent(i, true);
                    }
                    Thread.sleep(250);
                } catch (Exception e) {
                    DriverStation.reportError("PDPCacheUpdater exception: " + e.toString(), false);
                }
            }
        }
    }

}