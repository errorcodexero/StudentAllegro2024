package frc.robot.subsystems.Limelight;

import frc.robot.subsystems.Limelight.LimelightHelpers.LimelightResults;
import frc.robot.subsystems.Limelight.structs.XeroFiducial;
import frc.robot.subsystems.Limelight.structs.XeroPoseEstimate;

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
    public void updateInputs(LimelightIOInputsAutoLogged inputs) {
        inputs.simpleX = LimelightHelpers.getTX(name_);
        inputs.simpleY = LimelightHelpers.getTY(name_);
        inputs.simpleXPixels = LimelightHelpers.getTargetPose3d_CameraSpace(name_).getX();
        inputs.simpleYPixels = LimelightHelpers.getTargetPose3d_CameraSpace(name_).getY();
        inputs.simpleArea = LimelightHelpers.getTA(name_);
        inputs.simpleValid = LimelightHelpers.getTV(name_);
        inputs.simpleID = (int) LimelightHelpers.getFiducialID(name_);

        inputs.basicPoseEstimate = XeroPoseEstimate.of(LimelightHelpers.getBotPoseEstimate_wpiBlue(name_));
        inputs.megatag2PoseEstimate = XeroPoseEstimate.of(LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(name_));

        LimelightResults results = LimelightHelpers.getLatestResults(name_);
        inputs.fiducials = XeroFiducial.fromLimelightArray(results.targets_Fiducials);
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