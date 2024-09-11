package frc.robot.subsystems.Limelight;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

import frc.robot.subsystems.Limelight.structs.Fiducial;
import frc.robot.subsystems.Limelight.structs.VisionPoseEstimate;

public interface LimelightIO {

    public class LimelightIOInputs implements LoggableInputs {
        // General Values That Will be ReplayedO
        public double tX = 0.0;
        public double tY = 0.0;
        public double tXPixels = 0.0;
        public double tYPixels = 0.0;
        public double tArea = 0.0;
        public boolean tValid = false;
        public int fiducialID = 0;

        public Fiducial[] fiducials = {};

        public VisionPoseEstimate basicPoseEstimate = new VisionPoseEstimate();
        public VisionPoseEstimate megatag2PoseEstimate = new VisionPoseEstimate();

        @Override
        public void toLog(LogTable table) {   
            // Logs values that will be replayed.
            table.put("SimpleID", fiducialID);
            table.put("SimpleArea", tArea);
            table.put("SimpleX", tX);
            table.put("SimpleY", tY);
            table.put("SimpleXPixels", tXPixels);
            table.put("SimpleYPixels", tYPixels);
            table.put("SimpleValid", tValid);

            // Fiducials serialized with structs
            table.put("Fiducials", Fiducial.struct, fiducials);
            
            table.put("PoseEstimation/Basic", VisionPoseEstimate.struct, basicPoseEstimate);
            table.put("PoseEstimation/Megatag2", VisionPoseEstimate.struct, megatag2PoseEstimate);
        }
        
        @Override
        public void fromLog(LogTable table) {
            // Loads values from the log.
            fiducialID = table.get("FiducialID", fiducialID);
            tArea      = table.get("TArea", tArea);
            tX         = table.get("TX", tX);
            tY         = table.get("TY", tY);
            tXPixels   = table.get("TXPixels", tXPixels);
            tYPixels   = table.get("TYPixels", tYPixels);
            tValid     = table.get("TValid", tValid);

            // Fiducial List
            fiducials = table.get("Fiducials", Fiducial.struct, fiducials);

            basicPoseEstimate = table.get("PoseEstimation/Basic", VisionPoseEstimate.struct, basicPoseEstimate);
            megatag2PoseEstimate = table.get("PoseEstimation/Megatag2", VisionPoseEstimate.struct, megatag2PoseEstimate);

        }

    }

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
    
    /**
     * Updates the inputs object with values from the hardware.
     * @param inputs The inputs to update
     */
    public abstract void updateInputs(LimelightIOInputs inputs);

}
