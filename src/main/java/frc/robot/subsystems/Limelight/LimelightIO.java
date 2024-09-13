package frc.robot.subsystems.Limelight;

import org.littletonrobotics.junction.AutoLog;

import frc.robot.subsystems.Limelight.structs.XeroFiducial;
import frc.robot.subsystems.Limelight.structs.XeroPoseEstimate;

public interface LimelightIO {

    @AutoLog
    public class LimelightIOInputs {
        // General Values That Will be Replayed
        public int simpleID = 0;
        public double simpleX = 0.0;
        public double simpleY = 0.0;
        public double simpleXPixels = 0.0;
        public double simpleYPixels = 0.0;
        public double simpleArea = 0.0;
        public boolean simpleValid = false;

        public XeroFiducial[] fiducials = {};

        public XeroPoseEstimate poseEstimateBasic = new XeroPoseEstimate();
        public XeroPoseEstimate poseEstimateMegatag2 = new XeroPoseEstimate();

    }

    /**
     * Updates the inputs object with values from the hardware.
     * @param inputs The inputs to update
     */
    public abstract void updateInputs(LimelightIOInputsAutoLogged inputs);

    /**
     * Forces the indicator light on the limelight to be off.
     */
    public abstract void forceOff();

    /**
     * Forces the indicator light on the limelight to blink.
     */
    public abstract void forceBlink();

    /**
     * Forces the indicator light on the limelight to be on.
     */
    public abstract void forceOn();

    /**
     * Resets the indicator light on the limelight to be controlled by its own software/pipelines.
     */
    public abstract void resetLed();

    /**
     * Sets the id for tx/ty targeting. Ignore other targets.
     */
    public abstract void setPriorityTagID(int id);

    /**
     * Sets the filter list of valid april tags.
     * ex. {3, 4} would filter to only apriltags 3 and 4.
     */
    public abstract void setValidTags(int[] validIds);

    /**
     * Gives the robot orientation to the limelight for Megatag2 odometry.
     * Blue origion, CCW-positive, 0 degrees facing red alliance wall
     */
    public abstract void giveRobotOrientation(double yaw, double yawRate, double pitch, double pitchRate, double roll, double rollRate);

}
