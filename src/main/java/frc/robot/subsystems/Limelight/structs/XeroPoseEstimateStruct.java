package frc.robot.subsystems.Limelight.structs;

import java.nio.ByteBuffer;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.util.struct.Struct;

public class XeroPoseEstimateStruct implements Struct<XeroPoseEstimate> {

    @Override
    public String getSchema() {
        return "Pose2d pose; double timestamp; double avgTagArea; double avgTagDist; float tagCount; float valid";
    }

    @Override
    public int getSize() {
        return Pose2d.struct.getSize() + kSizeDouble * 3 + kSizeFloat * 2;
    }

    @Override
    public Class<XeroPoseEstimate> getTypeClass() {
        return XeroPoseEstimate.class;
    }

    @Override
    public String getTypeString() {
        return "struct:VisionPoseEstimate";
    }

    @Override
    public Struct<?>[] getNested() {
        return new Struct<?>[] {Pose2d.struct};
    }

    @Override
    public void pack(ByteBuffer bb, XeroPoseEstimate value) {
        Pose2d.struct.pack(bb, value.pose);
        bb.putDouble(value.timestamp);
        bb.putDouble(value.avgTagArea);
        bb.putDouble(value.avgTagDist);
        bb.putFloat(value.tagCount);
        bb.putFloat(value.valid ? 1 : 0);
    }

    @Override
    public XeroPoseEstimate unpack(ByteBuffer bb) {
        return new XeroPoseEstimate(Pose2d.struct.unpack(bb), bb.getDouble(), bb.getDouble(), bb.getDouble(), (int) bb.getFloat(), bb.getFloat() == 1 ? true : false);
    }

    
    
}
