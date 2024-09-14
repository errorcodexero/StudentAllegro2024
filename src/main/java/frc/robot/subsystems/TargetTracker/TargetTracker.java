package frc.robot.subsystems.TargetTracker;

import java.util.function.Supplier;

import org.littletonrobotics.junction.AutoLogOutput;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.AprilTags;

public class TargetTracker extends SubsystemBase {

    private Supplier<Pose2d> robotPoseSupplier_;

    @AutoLogOutput(key = "TargetTracker/TargetPoseAbsolute")
    private Pose3d targetPoseAbsolute_;

    public TargetTracker(Supplier<Pose2d> robotPoseSupplier) {
        robotPoseSupplier_ = robotPoseSupplier;
    }

    @Override
    public void periodic() {
        
        targetPoseAbsolute_ = AprilTags.SPEAKER_CENTER.getPose3d(getAlliance());

    }

    private Alliance getAlliance() {
        return DriverStation.getAlliance().orElse(Alliance.Red);
    }
    
}
