package org.frc5687.powerup.robot.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Logger {
    private String _filename;

    public Logger(String filename) {
        _filename = filename;
    }

    public void addLine(String s, boolean newLineBefore, boolean newLineAfter) {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(_filename, true));
            if (newLineBefore) { output.newLine(); }
            if (newLineAfter) { output.newLine(); }
            output.write(s);
            output.close();
        } catch (Exception e) {

        }
    }

    public void addLine(String s) {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(_filename, true));
            output.newLine();
            output.write(s);
            output.close();
        } catch (Exception e) {

        }
    }


}
