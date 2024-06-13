package frc.robot.subsystems.Limelight;

import java.util.ArrayList;
import java.util.List;

import frc.robot.subsystems.Limelight.LimelightHelpers.LimelightTarget_Fiducial;

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
    public void updateInputs(LimelightIOInputs inputs) {
        inputs.tX = LimelightHelpers.getTX(name_);
        inputs.tY = LimelightHelpers.getTY(name_);
        inputs.tArea = LimelightHelpers.getTA(name_);
        inputs.tValid = LimelightHelpers.getTV(name_);

        // Individual lists to add values from every fid into in order.
        ArrayList<Double> fidIdList = new ArrayList<>();
        ArrayList<String> fidFamilyList = new ArrayList<>();
        ArrayList<Double> fidTxList = new ArrayList<>();
        ArrayList<Double> fidTyList = new ArrayList<>();
        ArrayList<Double> fidTxPixelList = new ArrayList<>();
        ArrayList<Double> fidTyPixelList = new ArrayList<>();
        ArrayList<Double> fidTAreaList = new ArrayList<>();

        LimelightTarget_Fiducial[] fids = LimelightHelpers.getLatestResults(name_).targetingResults.targets_Fiducials;

        // Adds to all of the lists in order from all of the seen fiducials
        for (LimelightTarget_Fiducial fid : fids) {
            fidIdList.add(fid.fiducialID);
            fidFamilyList.add(fid.fiducialFamily);
            fidTxList.add(fid.tx);
            fidTyList.add(fid.ty);
            fidTxPixelList.add(fid.tx_pixels);
            fidTyPixelList.add(fid.ty_pixels);
            fidTAreaList.add(fid.ta);
        }

        // Coverts and saves those values as normal arrays of primitives.
        inputs.fiducialID = listToDoubleArray(fidIdList);
        inputs.fiducialFamily = listToStringArray(fidFamilyList);
        inputs.fiducialTX = listToDoubleArray(fidTxList);
        inputs.fiducialTY = listToDoubleArray(fidTyList);
        inputs.fiducialTXPixels = listToDoubleArray(fidTxPixelList);
        inputs.fiducialTYPixels = listToDoubleArray(fidTyPixelList);
        inputs.fiducialTArea = listToDoubleArray(fidTAreaList);
        
    }

    private double[] listToDoubleArray(List<Double> doubles) {
        return doubles.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private String[] listToStringArray(List<String> strings) {
        return strings.toArray(new String[0]);
    }
   
}