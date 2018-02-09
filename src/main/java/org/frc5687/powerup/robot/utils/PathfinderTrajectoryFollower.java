package org.frc5687.powerup.robot.utils;

import jaci.pathfinder.Trajectory;

/**
 * PID + Feedforward controller for following a Pathfinder Trajectory.
 *
 * A fork of 254's TrajectoryFollower
 */
public class PathfinderTrajectoryFollower {

    private double kp_;
    private double ki_;  // Not currently used, but might be in the future.
    private double kd_;
    private double kv_;
    private double ka_;
    private double last_error_;
    private double current_heading = 0;
    private int current_segment;
    private Trajectory profile_;

    public PathfinderTrajectoryFollower() {

    }

    public void configure(double kp, double ki, double kd, double kv, double ka) {
        kp_ = kp;
        ki_ = ki;
        kd_ = kd;
        kv_ = kv;
        ka_ = ka;
    }

    public void reset() {
        last_error_ = 0.0;
        current_segment = 0;
    }

    public void setTrajectory(Trajectory profile) {
        profile_ = profile;
    }

    public double calculate(double distance_so_far) {
        if (current_segment < profile_.length()) {
            Trajectory.Segment segment = profile_.get(current_segment);
            double error = segment.position - distance_so_far;
            double output = kp_ * error + kd_ * ((error - last_error_)
                    / segment.dt - segment.velocity) + (kv_ * segment.velocity
                    + ka_ * segment.acceleration);

            last_error_ = error;
            current_heading = segment.heading;
            current_segment++;
            //System.out.println("so far: " + distance_so_far + "; output: " + output);
            return output;
        } else {
            return 0;
        }
    }

    public double getHeading() {
        return current_heading;
    }

    public boolean isFinishedTrajectory() {
        return current_segment >= profile_.length();
    }

    public Trajectory.Segment getSegment() {
        return profile_.get(current_segment);
    }

    public Trajectory.Segment getLastSegment() {
        return profile_.get(profile_.length() - 1);
    }
}
