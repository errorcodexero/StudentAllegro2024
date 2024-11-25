package frc.robot.subsystems.Limelight.structs;

import java.nio.ByteBuffer;

import edu.wpi.first.util.struct.Struct;

public class XeroGamepieceStruct implements Struct<XeroGamepiece> {

	@Override
	public String getSchema() {
		return "double angleX; double angleY; double confidence; double area";
	}

	@Override
	public int getSize() {
		return kSizeDouble * 4;
	}

	@Override
	public Class<XeroGamepiece> getTypeClass() {
		return XeroGamepiece.class;
	}

	@Override
	public String getTypeString() {
		return "struct:XeroGamepiece";
	}

	@Override
	public void pack(ByteBuffer bb, XeroGamepiece value) {
		bb.putDouble(value.angleX);
		bb.putDouble(value.angleY);
		bb.putDouble(value.confidence);
		bb.putDouble(value.area);
	}

	@Override
	public XeroGamepiece unpack(ByteBuffer bb) {
		return new XeroGamepiece(
            bb.getDouble(),
            bb.getDouble(),
            bb.getDouble(),
            bb.getDouble()
        );
	}

}
