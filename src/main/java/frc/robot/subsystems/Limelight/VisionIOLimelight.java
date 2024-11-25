package frc.robot.subsystems.Limelight;

import java.util.ArrayList;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.subsystems.Limelight.LimelightHelpers.LimelightResults;
import frc.robot.subsystems.Limelight.LimelightHelpers.RawDetection;
import frc.robot.subsystems.Limelight.structs.XeroFiducial;
import frc.robot.subsystems.Limelight.structs.XeroGamepiece;
import frc.robot.subsystems.Limelight.structs.XeroPoseEstimate;

public class VisionIOLimelight implements VisionIO {

    private final String name_;

    /**
     * Creates a new Limelight implementation, this implementation is using the Limelight Lib with a Limelight.
     * This specifies a name.
     * @param name The name of the limelight.
     */
    public VisionIOLimelight(String name) {
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
    public void updateInputs(VisionIOInputsAutoLogged inputs) {
        // Simple Results
        inputs.simpleX = LimelightHelpers.getTX(name_);
        inputs.simpleY = LimelightHelpers.getTY(name_);
        inputs.simpleArea = LimelightHelpers.getTA(name_);
        inputs.simpleValid = LimelightHelpers.getTV(name_);
        inputs.simpleID = (int) LimelightHelpers.getFiducialID(name_);
        
        // Advanced Results
        LimelightResults results = LimelightHelpers.getLatestResults(name_);

        inputs.fiducials = XeroFiducial.fromLimelightArray(results.targets_Fiducials);
        inputs.gamepieces = XeroGamepiece.fromLimelightArray(results.targets_Detector);
        inputs.poseEstimate = XeroPoseEstimate.of(LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(name_));

        inputs.timestampSeconds = results.timestamp_RIOFPGA_capture;

        // Raw Corners
        RawDetection[] detections = LimelightHelpers.getRawDetections(name_);
        ArrayList<Translation2d> corners = new ArrayList<>();

        for (RawDetection detection : detections) {
            corners.add(new Translation2d(detection.corner0_X, detection.corner0_Y));
            corners.add(new Translation2d(detection.corner1_X, detection.corner1_Y));
            corners.add(new Translation2d(detection.corner2_X, detection.corner2_Y));
            corners.add(new Translation2d(detection.corner3_X, detection.corner3_Y));
        }

        inputs.rawCorners = corners.toArray(new Translation2d[0]);
    }

    @Override
    public void giveRobotOrientation(double yaw, double yawRate, double pitch, double pitchRate, double roll, double rollRate) {
        LimelightHelpers.SetRobotOrientation(name_, yaw, 0, 0, 0, 0, 0);
    }

}