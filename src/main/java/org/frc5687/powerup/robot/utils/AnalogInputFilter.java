package org.frc5687.powerup.robot.utils;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CircularBuffer;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class AnalogInputFilter implements PIDSource {
    private AnalogInput m_source;
    private CircularBuffer m_inputs;
    private int m_inputs_target_size;

    public AnalogInputFilter(AnalogInput source) {
        this(source, 6);
    }

    public AnalogInputFilter(AnalogInput source, int samples) {
        m_source = source;
        m_inputs_target_size = samples;
        m_inputs = new CircularBuffer(m_inputs_target_size);
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
        m_source.setPIDSourceType(pidSource);
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return m_source.getPIDSourceType();
    }

    @Override
    public double pidGet() {

        // Rotate the inputs
        m_inputs.addFirst(pidGetSource());
        // Calculate the new value
        double sum = 0;
        for (int i = 0; i < m_inputs_target_size; i++) {
            sum += m_inputs.get(i);
        }
        double retVal = sum / (double) m_inputs_target_size;

        return retVal;
    }

    public double get() {
        double sum = 0;
        for (int i = 0; i < m_inputs_target_size; i++) {
            sum += m_inputs.get(i);
        }
        double retVal = sum / (double) m_inputs_target_size;

        return retVal;
    }

    protected double pidGetSource() {
        return m_source.getValue();
    }

}
