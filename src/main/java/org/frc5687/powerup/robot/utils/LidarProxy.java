package org.frc5687.powerup.robot.utils;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LidarProxy {
    private double lastReadDistance;
    private String raw;
    private LidarListener _listener;
    private Thread _thread;
    private boolean _initializedProperly;

    public LidarProxy(SerialPort.Port port) {
        setup(port);
        raw = "";
    }

    private void setup(SerialPort.Port port) {
        try {
            _listener = new LidarListener(this, port);
            _thread = new Thread(_listener);
            _thread.start();
            _initializedProperly = true;
        } catch (Exception e) {
            _initializedProperly = false;
            DriverStation.reportError("LidarProxy could not intialize properly. " + e.getStackTrace().toString(), false);
        }
        SmartDashboard.putBoolean("Lidar/initializedProperly", _initializedProperly);
    }

    public double get() {
        return lastReadDistance;
    }

    public String getRaw() {
        return raw;
    }

    protected class LidarListener implements Runnable {
        private SerialPort _port;
        private LidarProxy _proxy;

        protected LidarListener(LidarProxy proxy, SerialPort.Port port) {
            _proxy = proxy;
            _port = new SerialPort(115200, port);
            //_port.setReadBufferSize(9);
        }

        public void run() {
            while (true) {
                try {
                    //byte[] read = _port.read(_port.getBytesReceived());
                    SmartDashboard.putNumber("Lidar/_port.getBytesReceived()", _port.getBytesReceived());
                    //SmartDashboard.putNumber("Lidar/readLength", read.length);
                    /*
                    SmartDashboard.putNumber("Lidar/bytes/1", read[0]);
                    SmartDashboard.putNumber("Lidar/bytes/2", read[1]);
                    SmartDashboard.putNumber("Lidar/bytes/3", read[2]);
                    SmartDashboard.putNumber("Lidar/bytes/4", read[3]);
                    SmartDashboard.putNumber("Lidar/bytes/5", read[4]);
                    SmartDashboard.putNumber("Lidar/bytes/6", read[5]);
                    SmartDashboard.putNumber("Lidar/bytes/7", read[6]);
                    SmartDashboard.putNumber("Lidar/bytes/8", read[7]);
                    SmartDashboard.putNumber("Lidar/bytes/9", read[8]);
                    */
                } catch (Exception e) {
                    DriverStation.reportError("LidarListener exception: " + e.getStackTrace().toString(), false);
                }
            }
        }
    }
}
