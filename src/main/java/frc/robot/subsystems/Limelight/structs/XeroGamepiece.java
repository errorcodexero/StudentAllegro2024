package frc.robot.subsystems.Limelight.structs;

import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.util.struct.StructSerializable;
import frc.robot.subsystems.Limelight.LimelightHelpers.LimelightTarget_Detector;

public class XeroGamepiece implements StructSerializable {

    public final double angleX;
    public final double angleY;
    public final double confidence;
    public final double area;
    
    public XeroGamepiece(
        double angleX,
        double angleY,
        double confidence,
        double area
    ) {
        this.angleX = angleX;
        this.angleY = angleY;
        this.confidence = confidence;
        this.area = area;
    }

    public XeroGamepiece(PhotonTrackedTarget gp) {
        // Assume 1.0 confidence because photon doesnt give us a value for this. Calculate using distance/pitch/area instead?
        this(gp.getYaw(), gp.getPitch(), 1.0, gp.getArea());
    }

    public XeroGamepiece(LimelightTarget_Detector gp) {
        this(gp.tx, gp.ty, gp.confidence, gp.ta);
    }

    public static XeroGamepiece[] fromLimelightArray(LimelightTarget_Detector[] llArray) {
        XeroGamepiece[] arr = new XeroGamepiece[llArray.length];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = new XeroGamepiece(llArray[i]);
        }

        return arr;
    }

    public static final XeroGamepieceStruct struct = new XeroGamepieceStruct();
}
