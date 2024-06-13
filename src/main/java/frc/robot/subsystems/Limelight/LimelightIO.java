package frc.robot.subsystems.Limelight;

import org.littletonrobotics.junction.AutoLog;

import frc.robot.subsystems.Limelight.LimelightHelpers.LimelightTarget_Fiducial;

public interface LimelightIO {

    @AutoLog
    public class LimelightIOInputs {
        public double tX = 0.0;
        public double tY = 0.0;
        public double tArea = 0.0;
        public boolean tValid = false;
        public String fids = "";
    }

    public abstract void forceOff();
    public abstract void forceBlink();
    public abstract void forceOn();
    public abstract void resetLed();
    
    public abstract void updateInputs(LimelightIOInputsAutoLogged inputs);

}
