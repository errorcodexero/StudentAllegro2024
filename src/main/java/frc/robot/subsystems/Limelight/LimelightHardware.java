package frc.robot.subsystems.Limelight;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class LimelightHardware implements LimelightIO {

    private final NetworkTable llTable_ = NetworkTableInstance.getDefault().getTable("limelight");
    private final NetworkTableEntry llLed_ = llTable_.getEntry("ledMode");

    @Override
    public void setLed(int mode) {
        llLed_.setNumber(mode);
    }
    
}