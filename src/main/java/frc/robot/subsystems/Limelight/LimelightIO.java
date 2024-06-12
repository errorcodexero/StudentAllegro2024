package frc.robot.subsystems.Limelight;

import java.util.logging.Logger;

import org.littletonrobotics.junction.AutoLog;

public interface LimelightIO {

    @AutoLog
    public class LimelightIOInputs {
        public double tX = 0.0;
        public double tY = 0.0;
        public double tArea = 0.0;
        public boolean tValid = false;
    }

    public abstract void forceOff();
    public abstract void forceBlink();
    public abstract void forceOn();
    public abstract void resetLed();

    public abstract double getTX();
    public abstract double getTY();
    public abstract double getTArea();
    
    public abstract boolean isValidTarget();
    public abstract boolean hasAprilTag(int id);

    public abstract void updateState();

    public abstract void updateInputs(LimelightIOInputsAutoLogged inputs);
}
