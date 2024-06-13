package frc.robot.subsystems.TargetTracker;

import java.util.Optional;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Limelight.LimelightSubsystem;
import frc.robot.subsystems.TargetTracker.TargetTrackerConstants.AprilTags;

public class TargetTrackerSubsystem extends SubsystemBase {

    private LimelightSubsystem ll_;

    private int targetNumber_;
    private Pose2d targetPose2d_;

    /**
     * PLACEHOLDER!!!! DO NOT USE ON A REAL ROBOT!!!!!!!!!!!!!!!!!!!!111!!!!1111!!
     */
    private Pose2d robotPose2d_ = new Pose2d();

    public TargetTrackerSubsystem(LimelightSubsystem ll) {

        this.ll_ = ll;
        targetNumber_ = AprilTags.getSpeakerCenter(DriverStation.getAlliance());
        targetPose2d_ = getAprilTagPose2d(targetNumber_);

    }

    @Override
    public void periodic() {
        targetPose2d_ = getAprilTagPose2d(targetNumber_);
    }

    public int getTargetNumber() {
        return targetNumber_;
    }

    private Pose2d getAprilTagPose2d(int id) {
        Optional<Pose3d> pose3d = AprilTagFieldLayout.loadField(AprilTagFields.k2024Crescendo).getTagPose(id);

        if (!pose3d.isPresent()) {
            return null;
        }

        return pose3d.get().toPose2d();
        
    }

    
}
