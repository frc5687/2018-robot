package org.frc5687.powerup.robot.utils;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class JeVoisProxy {

    private int lastReadX;
    private int lastReadY;

    private JeVoisListener listener;
    private Thread listenerThread;

    public JeVoisProxy(SerialPort.Port port) {
        listener = new JeVoisListener(this, port);
        listenerThread = new Thread(listener);
        listenerThread.start();
    }


    synchronized public int GetX() {
        return lastReadX;
    }

    synchronized public int GetY() {
        return lastReadY;
    }

    synchronized protected void Set(int x, int y) {
        lastReadX = x;
        lastReadY = y;
        SmartDashboard.putNumber("JeVois/x", lastReadX);
        SmartDashboard.putNumber("JeVois/y", lastReadY);
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
                    int x = Integer.parseInt(a[0]);
                    int y = Integer.parseInt(a[1]);
                    proxy.Set(x, y);
                    Thread.sleep(1000 / 70);
                } catch (Exception e) {
                    DriverStation.reportError(e.toString(), true);
                }
            }
        }
    }
}
