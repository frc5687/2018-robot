package org.frc5687.powerup.robot.utils;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class JeVoisProxy {

    private double lastReadX;
    private double lastReadY;
    private boolean lastCubeDetected;

    private boolean initializedProperly;

    private JeVoisListener listener;
    private Thread listenerThread;

    public JeVoisProxy(SerialPort.Port port) {
        setup(port);
    }

    public void setup(SerialPort.Port port) {
        try {
            listener = new JeVoisListener(this, port);
            listenerThread = new Thread(listener);
            listenerThread.start();
            initializedProperly = true;
        } catch (Exception e) {
            DriverStation.reportError("Error while initializing JeVois Listener: " + e.getStackTrace().toString(), false);
            initializedProperly = false;
        }
        SmartDashboard.putBoolean("JeVois/initializedProperly", initializedProperly);
    }


    synchronized public double GetX() {
        return lastReadX;
    }

    synchronized public double GetY() {
        return lastReadY;
    }

    synchronized public boolean GetCubeDetected() {
        return lastCubeDetected;
    }

    synchronized protected void Set(double x, double y, boolean cubeDetected) {
        lastReadX = x;
        lastReadY = y;
        lastCubeDetected = cubeDetected;

        SmartDashboard.putNumber("JeVois/x", lastReadX);
        SmartDashboard.putNumber("JeVois/y", lastReadY);
        SmartDashboard.putBoolean("JeVois/cubeDetected", lastCubeDetected);
    }

    class BufferBuffer {
        private String _buffer;
        private String _delimeter;

        public BufferBuffer(String delimeter) {
            _buffer = "";
            _delimeter = delimeter;
        }

        public void addToBuffer(String str) {
            _buffer = _buffer.concat(str);
        }

        public boolean hasNext() {
            return _buffer.contains(_delimeter);
        }

        public String next() {
            if (hasNext()) {
                int indexOf = _buffer.indexOf(_delimeter);
                String packet = _buffer.substring(0, indexOf);
                _buffer = _buffer.substring(indexOf + _delimeter.length(), _buffer.length());
                return packet;
            }
            return "";
        }
    }

    protected class JeVoisListener implements Runnable {
        private SerialPort jevoisPort;
        private JeVoisProxy proxy;

        private BufferBuffer buffer;

        protected JeVoisListener(JeVoisProxy proxy, SerialPort.Port port) {
            this.proxy = proxy;
            jevoisPort = new SerialPort(115200, port);
            char[] delim = new char[] {'\r', '\n'};
            buffer = new BufferBuffer(new String(delim));
        }

        public void run() {
            while (true) {
                try {
                    String str = jevoisPort.readString();
                    SmartDashboard.putString("JeVois/data_raw", str);
                    buffer.addToBuffer(str);
                    if (!buffer.hasNext()) {
                        SmartDashboard.putBoolean("JeVois/buffer/hasNext", false);
                        continue;
                    } else {
                        SmartDashboard.putBoolean("JeVois/buffer/hasNext", true);
                    }
                    String data = buffer.next();
                    SmartDashboard.putString("JeVois/buffer/next", data);
                    String identifier_string = "$:";
                    // Check to see if the identifier_string prepends our payload
                    if (data.length() <= identifier_string.length() || !data.substring(0, identifier_string.length()).equals(identifier_string)) {
                        //DriverStation.reportError(String.format("Rejected %s", data), false);
                        continue;
                    }
                    String payload = data.substring(identifier_string.length(), data.length());
                    // Remove whitespace in the payload
                    payload = payload.replaceAll("\\s+", "");
                    SmartDashboard.putString("JeVois/payload", payload);

                    String[] a = payload.split(";");
                    double x = Double.parseDouble(a[0]);
                    double y = Double.parseDouble(a[1]);
                    boolean cubeDetected = Integer.parseInt(a[2]) == 1;
                    proxy.Set(x, y, cubeDetected);
                    Thread.sleep(1000 / 70);
                } catch (Exception e) {
                    DriverStation.reportError(e.toString(), true);
                }
            }
        }
    }
}
