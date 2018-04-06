package org.frc5687.powerup.robot.utils;

import edu.wpi.first.wpilibj.AnalogInput;
import org.frc5687.powerup.robot.Constants;

public abstract class IR extends AnalogInput{
    public IR(int channel){
       super(channel);
    }
    public abstract double getDistance();
}