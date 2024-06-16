package frc.robot.subsystems.Limelight;

import edu.wpi.first.math.util.Units;
import frc.robot.subsystems.Limelight.LimelightHelpers.LimelightResults;

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
    public void setPriorityTagID(int id) {
        LimelightHelpers.setPriorityTagID(name_, id);
    }

    @Override
    public void updateInputs(LimelightIOInputs inputs) {
        inputs.tX = LimelightHelpers.getTX(name_);
        inputs.tY = LimelightHelpers.getTY(name_);
        inputs.tArea = LimelightHelpers.getTA(name_);
        inputs.tValid = LimelightHelpers.getTV(name_);
        inputs.fiducialID = (int) LimelightHelpers.getFiducialID(name_);
        inputs.predictedBotPose = LimelightHelpers.getBotPose3d(name_);
        inputs.jsonDump = LimelightHelpers.getJSONDump(name_);

        LimelightResults results = LimelightHelpers.getLatestResults(name_);
        inputs.fiducials = results.targetingResults.targets_Fiducials;
    }

}