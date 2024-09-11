package frc.robot.subsystems.Limelight;

import frc.robot.subsystems.Limelight.LimelightHelpers.LimelightResults;
import frc.robot.subsystems.Limelight.structs.Fiducial;
import frc.robot.subsystems.Limelight.structs.VisionPoseEstimate;

/*
 * Are you wondering what the heck a fiducial is?
 * 
 * I sure have the answer for you!
 * 
 * In the Limelight code, and here, I will be referring to tags like april tags, as fiducials.
 * 
 */

public class LimelightHardware implements LimelightIO {

    private final String name_;

    /**
     * Creates a new Limelight implementation, this implementation is using the Limelight Lib with a Limelight.
     * This specifies a name.
     * @param name The name of the limelight.
     */
    public LimelightHardware(String name) {
        name_ = name;
    }


    /**
     * Creates a new Limelight implementation, this implementation is using the Limelight Lib with a Limelight.
     * This uses the default "limelight" name.
     * @param name The name of the limelight.
     */
    public LimelightHardware() {
        this("limelight");
    }

    @Override
    public void forceOff() {
        LimelightHelpers.setLEDMode_ForceOff(name_);
    }
    
    @Override
    public void forceBlink() {
        LimelightHelpers.setLEDMode_ForceBlink(name_);
    }

    @Override
    public void forceOn() {
        LimelightHelpers.setLEDMode_ForceOn(name_);
    }

    @Override
    public void resetLed() {
        LimelightHelpers.setLEDMode_PipelineControl(name_);
    }

    @Override
    public void setPriorityTagID(int id) {
        LimelightHelpers.setPriorityTagID(name_, id);
    }

    @Override
    public void updateInputs(LimelightIOInputs inputs) {
        inputs.tX = LimelightHelpers.getTX(name_);
        inputs.tY = LimelightHelpers.getTY(name_);
        inputs.tXPixels = LimelightHelpers.getTargetPose3d_CameraSpace(name_).getX();
        inputs.tYPixels = LimelightHelpers.getTargetPose3d_CameraSpace(name_).getY();
        inputs.tArea = LimelightHelpers.getTA(name_);
        inputs.tValid = LimelightHelpers.getTV(name_);
        inputs.fiducialID = (int) LimelightHelpers.getFiducialID(name_);

        inputs.basicPoseEstimate = VisionPoseEstimate.of(LimelightHelpers.getBotPoseEstimate_wpiBlue(name_));
        inputs.megatag2PoseEstimate = VisionPoseEstimate.of(LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(name_));

        LimelightResults results = LimelightHelpers.getLatestResults(name_);
        inputs.fiducials = Fiducial.fromLimelightArray(results.targets_Fiducials);
    }


    @Override
    public void setValidTags(int[] validIds) {
        LimelightHelpers.SetFiducialIDFiltersOverride(name_, validIds);
    }

    @Override
    public void giveRobotOrientation(double yaw, double yawRate, double pitch, double pitchRate, double roll, double rollRate) {
        LimelightHelpers.SetRobotOrientation(name_, yaw, 0, 0, 0, 0, 0);
    }

}