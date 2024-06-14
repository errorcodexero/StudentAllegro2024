package frc.robot.subsystems.Limelight;

import frc.robot.subsystems.Limelight.LimelightHelpers.LimelightTarget_Fiducial;

public class LimelightHardware implements LimelightIO {

    private final String name_;
    private final boolean demoFids = true;

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

        if (demoFids) {

            LimelightTarget_Fiducial[] demoFids = new LimelightTarget_Fiducial[3];
            demoFids[0] = new LimelightTarget_Fiducial();
            demoFids[0].fiducialID = 1;
            demoFids[0].fiducialFamily = "april";
            demoFids[0].tx = 25.1;
            demoFids[0].ty = 50.1;

            demoFids[1] = new LimelightTarget_Fiducial();
            demoFids[1].fiducialID = 2;
            demoFids[1].fiducialFamily = "april";
            demoFids[1].tx = 50.2;
            demoFids[1].ty = 100.2;

            demoFids[2] = new LimelightTarget_Fiducial();
            demoFids[2].fiducialID = 3;
            demoFids[2].fiducialFamily = "april";
            demoFids[2].tx = 100.3;
            demoFids[2].ty = 200.3;

            inputs.fiducials = demoFids;
        } else {
            inputs.fiducials = LimelightHelpers.getLatestResults(name_).targetingResults.targets_Fiducials;
        }
        
    }

}