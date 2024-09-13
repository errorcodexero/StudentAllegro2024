package frc.robot.subsystems.TargetTracker;

import java.util.Optional;
import java.util.function.Supplier;

import org.littletonrobotics.junction.AutoLogOutput;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Limelight.Limelight;
import frc.robot.util.AprilTags;

public class TargetTrackerSubsystem extends SubsystemBase {

    public record TargetData(int id, double distanceFromRobot, Rotation2d angleFromRobot) {}

    private Limelight ll_ = new Limelight();
    
    @AutoLogOutput
    private Alliance alliance_;

    @AutoLogOutput
    private boolean allianceReady_;

    private boolean canSeeTarget_;

    @AutoLogOutput
    private double distanceToTargetVision_;

    @AutoLogOutput
    private double distanceToTargetOdometry_;

    private Pose3d targetPose3d_;
    private Pose2d targetPose2d_;

    private int speakerCenterTagID_;

    private Supplier<Pose2d> robotPoseSupplier_;

    public TargetTrackerSubsystem(Supplier<Pose2d> robotPoseSupplier) {
        
        ll_ = new Limelight();

        speakerCenterTagID_ = AprilTags.SPEAKER_CENTER.getId(alliance_);

        robotPoseSupplier_ = robotPoseSupplier;

    }

    private void setupAlliance(Alliance alliance) {
        alliance_ = alliance;
        speakerCenterTagID_ = AprilTags.SPEAKER_CENTER.getId(alliance);
        targetPose3d_ = AprilTags.SPEAKER_CENTER.getPose3d(alliance);
        targetPose2d_ = AprilTags.SPEAKER_CENTER.getPose2d(alliance);
        ll_.setPriorityTagID(speakerCenterTagID_);
    }

    @Override
    public void periodic() {

        if (alliance_ == null) {
            Optional<Alliance> newAlliance = DriverStation.getAlliance();
            
            if (newAlliance.isEmpty()) {
                allianceReady_ = false;
                return;
            }

            setupAlliance(newAlliance.get());
            allianceReady_ = true;
        }

        canSeeTarget_ = ll_.hasAprilTag(speakerCenterTagID_);
        
        
        
    }

    public Optional<TargetData> getTargetData() {
        if (allianceReady_) {
            TargetData data = new TargetData(speakerCenterTagID_, canSeeTarget_ ? distanceToTargetVision_ : distanceToTargetOdometry_, new Rotation2d());
            return Optional.of(data);
        }
        
        return Optional.empty();
    }

    private Optional<Double> calculateDistanceToTargetVision() {
        Optional<Double> ty = ll_.getSpecificY(speakerCenterTagID_);

        if (ty.isEmpty()) {
            return Optional.empty();
        }

        double heightBetweenCameraAndTarget = targetPose3d_.getZ() - TargetTrackerConstants.CAMERA_HEIGHT;
        double angleFromParallelToTarget = ty.get() + TargetTrackerConstants.CAMERA_ANGLE;

        return Optional.of(heightBetweenCameraAndTarget / Math.tan(Units.degreesToRadians(angleFromParallelToTarget)));
    }

    private double calculateDistanceToTargetOdometry() {
        return robotPoseSupplier_.get().getTranslation().getDistance(targetPose2d_.getTranslation());
    }
    
}
