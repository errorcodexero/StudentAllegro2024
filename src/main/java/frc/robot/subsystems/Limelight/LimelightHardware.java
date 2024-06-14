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
        inputs.fiducials = LimelightHelpers.getLatestResults(name_).targetingResults.targets_Fiducials;
    }

    private double[] listToDoubleArray(List<Double> doubles) {
        return doubles.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private String[] listToStringArray(List<String> strings) {
        return strings.toArray(new String[0]);
    }
   
}