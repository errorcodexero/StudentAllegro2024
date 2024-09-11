package frc.robot.subsystems.Limelight.structs;

import java.nio.ByteBuffer;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.util.struct.Struct;

public class PoseEstimateStruct implements Struct<VisionPoseEstimate> {

    @Override
    public Class<VisionPoseEstimate> getTypeClass() {
        return VisionPoseEstimate.class;
    }

    @Override
    public String getTypeString() {
        return "struct:PoseEstimate";
    }

    @Override
    public String getSchema() {
        return "Pose2d pose;double timestamp;boolean valid";
    }

    @Override
    public int getSize() {
        return Pose2d.struct.getSize() + kSizeDouble + kSizeBool;
    }

    @Override
    public void pack(ByteBuffer bb, VisionPoseEstimate value) {
        Pose2d.struct.pack(bb, value.pose);
        bb.putDouble(value.timestamp);
        bb.put((byte) (value.valid ? 1 : 0));
    }

    @Override
    public VisionPoseEstimate unpack(ByteBuffer bb) {
        return new VisionPoseEstimate(Pose2d.struct.unpack(bb), bb.getDouble(), bb.get() == 1);
    }
    
}
