package frc.robot.subsystems.Limelight;

import frc.robot.subsystems.Limelight.LimelightHelpers.LimelightResults;
import frc.robot.subsystems.Limelight.LimelightHelpers.LimelightTarget_Fiducial;

public class LimelightHardware implements LimelightIO {

    private final String name_;

    private LimelightResults currentResults_;
    private LimelightTarget_Fiducial[] fiducials_;

    private double tX_;
    private double tY_;
    private double tArea_;
    private boolean tValid_;

    /**
     * Creates a new Limelight implementation, this implementation is using the Limelight Lib with a Limelight.
     * This specifies a name.
     * @param name The name of the limelight.
     */
    public LimelightHardware(String name) {
        name_ = name;
        updateState(); // I dont know if this is needed,
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
    public double getTX() {
        return tX_;
    }


    @Override
    public double getTY() {
        return tY_;
    }


    @Override
    public double getTArea() {
        return tArea_;
    }


    @Override
    public boolean isValidTarget() {
        return tValid_;
    }

    @Override
    public boolean hasAprilTag(int id) { // if fids has the id at all (can see it)
        for (LimelightTarget_Fiducial fud : fiducials_) {
            if (fud.fiducialID == id)
                return true;
        }

        return false;
    }

    @Override
    public void updateState() {
        currentResults_ = LimelightHelpers.getLatestResults(name_);

        fiducials_ = currentResults_.targetingResults.targets_Fiducials;

        tX_ = LimelightHelpers.getTX(name_);
        tY_ = LimelightHelpers.getTY(name_);
        tArea_ = LimelightHelpers.getTA(name_);
        tValid_ = LimelightHelpers.getTV(name_);
    }

    @Override
    public void updateInputs(LimelightIOInputsAutoLogged inputs) {
        inputs.tX = tX_;
        inputs.tY = tY_;
        inputs.tArea = tArea_;
        inputs.tValid = tValid_;
    }
   
}