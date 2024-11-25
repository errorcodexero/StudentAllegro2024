package frc.robot.subsystems.Limelight.structs;

import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.util.struct.StructSerializable;
import frc.robot.subsystems.Limelight.LimelightHelpers.LimelightTarget_Fiducial;

public class XeroFiducial implements StructSerializable {

    public final double id;
    public final double area;
    public final double x;
    public final double y;
    
    public XeroFiducial(
        double id,
        double area,
        double x,
        double y
    ) {
        this.id = id;
        this.area = area;
        this.x = x;
        this.y = y;
    }

    public XeroFiducial(PhotonTrackedTarget photonFid) {
        this(photonFid.getFiducialId(), photonFid.getArea(), photonFid.getYaw(), photonFid.getPitch());
    }

    public XeroFiducial(LimelightTarget_Fiducial llFid) {
        this(llFid.fiducialID, llFid.ta, llFid.tx, llFid.ty);
    }

    public static XeroFiducial[] fromLimelightArray(LimelightTarget_Fiducial[] llArray) {
        XeroFiducial[] arr = new XeroFiducial[llArray.length];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = new XeroFiducial(llArray[i]);
        }

        return arr;
    }

    public static final XeroFiducialStruct struct = new XeroFiducialStruct();
}