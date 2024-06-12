package frc.robot.subsystems.Limelight;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LimelightSubsystem extends SubsystemBase {

    private final LimelightIO io_;

    public LimelightSubsystem() {
        this(new LimelightHardware());
    }

    public LimelightSubsystem(LimelightIO io) {
        io_ = io;
    }

    @Override
    public void periodic() {

    }

    public void setLedOff() {
        io_.forceOff();;
    }

    public void setLedBlink() {
        io_.forceBlink();
    }

    public void setLedOn() {
        io_.forceOn();
    }
    
}
