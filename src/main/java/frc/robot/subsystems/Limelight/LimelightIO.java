package frc.robot.subsystems.Limelight;

public interface LimelightIO {
    public abstract void forceOff();
    public abstract void forceBlink();
    public abstract void forceOn();
    public abstract void resetLed();
    public abstract boolean hasAprilTag(int id);
    public default void update() {}
}
