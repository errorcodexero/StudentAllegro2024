package frc.robot.subsystems.Limelight.structs;

import edu.wpi.first.util.struct.StructSerializable;
import frc.robot.subsystems.Limelight.LimelightHelpers.LimelightTarget_Fiducial;

public class Fiducial implements StructSerializable {

    public final double id;
    public final double area;
    public final double x;
    public final double y;
    public final double xPixels;
    public final double yPixels;
    public final double ts;

    public Fiducial(
        double id,
        double area,
        double x,
        double y,
        double xPixels,
        double yPixels,
        double ts
    ) {
        this.id = id;
        this.area = area;
        this.x = x;
        this.y = y;
        this.xPixels = xPixels;
        this.yPixels = yPixels;
        this.ts = ts;
    }

    public Fiducial(LimelightTarget_Fiducial llFid) {
        this.id = llFid.fiducialID;
        this.area = llFid.ta;
        this.x = llFid.tx;
        this.y = llFid.ty;
        this.xPixels = llFid.tx_pixels;
        this.yPixels = llFid.ty_pixels;
        this.ts = llFid.ts;
    }

    public static Fiducial[] fromLimelightArray(LimelightTarget_Fiducial[] llArray) {
        Fiducial[] arr = new Fiducial[llArray.length];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = new Fiducial(llArray[i]);
        }

        return arr;
    }

    public static final FiducialStruct struct = new FiducialStruct();
}