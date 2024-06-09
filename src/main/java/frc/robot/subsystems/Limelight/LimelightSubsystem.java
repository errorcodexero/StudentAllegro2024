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

    public void setLed(int mode) {
        io_.setLed(mode);
    }
    
}
