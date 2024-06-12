package frc.robot.subsystems.Limelight;

public class LimelightHardware implements LimelightIO {

    @Override
    public void forceOff() {
        LimelightHelpers.setLEDMode_ForceOff(null);
    }
    
    @Override
    public void forceBlink() {
        LimelightHelpers.setLEDMode_ForceBlink(null);
    }

    @Override
    public void forceOn() {
        LimelightHelpers.setLEDMode_ForceOn(null);
    }
   
}