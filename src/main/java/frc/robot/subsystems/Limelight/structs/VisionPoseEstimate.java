package frc.robot.subsystems.Limelight.structs;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.util.struct.StructSerializable;
import frc.robot.subsystems.Limelight.LimelightHelpers.PoseEstimate;

public class VisionPoseEstimate implements StructSerializable {

    public final Pose2d pose;
    public final double timestamp;
    public final boolean valid;

    public VisionPoseEstimate(Pose2d pose, double timestamp, boolean valid) {
        this.pose = pose;
        this.timestamp = timestamp;
        this.valid = valid;
    }

    public VisionPoseEstimate() {
        this(new Pose2d(), 0.0, false);
    }

    public static VisionPoseEstimate of(PoseEstimate llPoseEstimate) {
        return llPoseEstimate != null ? new VisionPoseEstimate(llPoseEstimate.pose, llPoseEstimate.timestampSeconds, true) : new VisionPoseEstimate();
    }

    public static final VisionPoseEstimateStruct struct = new VisionPoseEstimateStruct();
}
