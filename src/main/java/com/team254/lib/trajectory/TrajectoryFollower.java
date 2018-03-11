package com.team254.lib.trajectory;

import com.team254.lib.util.ChezyMath;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * PID + Feedforward controller for following a Trajectory.
 *
 * @author Jared341
 */
public class TrajectoryFollower {

  private double kp_;
  private double ki_;  // Not currently used, but might be in the future.
  private double kd_;
  private double kv_;
  private double ka_;
  private double last_error_;
  private double current_heading = 0;
  private int current_segment;
  private Trajectory profile_;
  private String _name;

  public TrajectoryFollower(String name) {
    _name = name;
  }

  public void configure(double kp, double ki, double kd, double kv, double ka) {
    kp_ = kp;
    ki_ = ki;
    kd_ = kd;
    kv_ = kv;
    ka_ = ka;
  }

  public void configurekP(double kp) {
    kp_ = kp;
  }

  public void configurekI(double ki) {
    ki_ = ki;
  }

  public void configurekD(double kd) {
    kd_ = kd;
  }

  public void configurekV(double kv) {
    kv_ = kv;
  }

  public void configurekA(double ka) {
    ka_ = ka;
  }

  public void reset() {
    last_error_ = 0.0;
    current_segment = 0;
  }

  public void setTrajectory(Trajectory profile) {
    profile_ = profile;
  }

  public double calculate(double distance_so_far, double velocityIPS) {
    if (current_segment < profile_.getNumSegments()) {
      Trajectory.Segment segment = profile_.getSegment(current_segment);
      double posError = segment.pos - distance_so_far;
      double velError = segment.vel - velocityIPS;
      double kp = kp_ * posError;
      double kd = kd_ * ((posError - last_error_) / segment.dt - segment.vel);
      double kv = kv_ * segment.vel;
      double ka = ka_ * segment.acc;
      //double output = kp + kd + kv + ka + (kv_ * velError);// If you want to correct velocity in here
      double output = kp + kd + kv;

      /*
      double output = kp_ * error + kd_ * ((error - last_error_)
              / segment.dt - segment.vel) + (kv_ * segment.vel
              + ka_ * segment.acc);
      */

      last_error_ = posError;
      current_heading = segment.heading;
      current_segment++;
      SmartDashboard.putNumber("TF/" + _name + "/kp", kp);
      SmartDashboard.putNumber("TF/" + _name + "/kd", kd);
      SmartDashboard.putNumber("TF/" + _name + "/kv", kv);
      SmartDashboard.putNumber("TF/" + _name + "/velError", velError);
      SmartDashboard.putNumber("TF/" + _name + "/velErrorOutput", (kv_ * velError));
      SmartDashboard.putNumber("TF/" + _name + "/velActual", velocityIPS);
      SmartDashboard.putNumber("TF/" + _name + "/posError", posError);
      SmartDashboard.putNumber("TF/" + _name + "/posActual", distance_so_far);
      SmartDashboard.putNumber("TF/" + _name + "/posGoal", segment.pos);
      SmartDashboard.putNumber("TF/" + _name + "/output", output);
      SmartDashboard.putNumber("TF/" + _name + "/goalVelIPS", segment.vel);
      SmartDashboard.putNumber("TF/" + _name + "/current_segment", current_segment);
      //DriverStation.reportError(_name + " kp: " + kp + "kv: " + kv + " output: " + output, false);
      //System.out.println("so far: " + distance_so_far + "; output: " + output);
      return output;
    } else {
      return 0;
    }
  }

  public double getNavxHeading() {
    return ChezyMath.boundAngleNeg180to180Degrees(-Math.toDegrees(current_heading));
  }

  public double getHeading() {
    return current_heading;
  }

  public boolean isFinishedTrajectory() {
    return current_segment >= profile_.getNumSegments();
  }

  public Trajectory.Segment getSegment() {
    return profile_.getSegment(current_segment);
  }

  public Trajectory.Segment getLastSegment() {
    return profile_.getSegment(profile_.getNumSegments() - 1);
  }

  public double getLastHeadingInNavxUnits() {
    return ChezyMath.boundAngleNeg180to180Degrees(-Math.toDegrees(getLastSegment().heading));
  }
}
