package frc.robot.subsystems.Vision;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class VisionSubsystem extends SubsystemBase {

    private final VisionIO io_;

    public VisionSubsystem() {
        this(new VisionHardware());
    }

    public VisionSubsystem(VisionIO io) {
        io_ = io;
    }

    @Override
    public void periodic() {

    }
    
}
