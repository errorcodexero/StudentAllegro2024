package frc.robot.subsystems.TargetTracker;

import java.util.Optional;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Limelight.LimelightSubsystem;
import frc.robot.subsystems.TargetTracker.TargetTrackerConstants.AprilTags;

public class TargetTrackerSubsystem extends SubsystemBase {

    public record TargetData(int id, double distanceFromRobot, double angleFromRobot) {}

    private LimelightSubsystem ll_;

    private Optional<Alliance> alliance_;
    private boolean ready_;

    private AprilTagFieldLayout fieldLayout_;
    private Pose2d targetPose2d_;
    private Pose3d targetPose3d_;
    private int speakerCenterTagID_;

    private boolean canSeeTarget_;
    private double distanceToTarget_;

    /**
     * PLACEHOLDER!!!! DO NOT USE ON A REAL ROBOT!!!!!!!!!!!!!!!!!!!!111!!!!1111!!
     */
    private Pose2d robotPose2d_ = new Pose2d();

    public TargetTrackerSubsystem(LimelightSubsystem ll) {
        
        this.ll_ = ll;
        fieldLayout_ = AprilTagFieldLayout.loadField(AprilTagFields.k2024Crescendo);
        canSeeTarget_ = false;
        speakerCenterTagID_ = 0;

    }

    private void setupAlliance(Alliance alliance) {
        alliance_ = Optional.of(alliance);
        speakerCenterTagID_ = AprilTags.getSpeakerCenter(alliance_);
        targetPose2d_ = getAprilTagPose2d(speakerCenterTagID_);
        targetPose3d_ = getAprilTagPose3d(speakerCenterTagID_);
        ll_.setPriorityTagID(speakerCenterTagID_);
    }

    @Override
    public void periodic() {

        if (alliance_ == null) {
            Optional<Alliance> newAlliance = DriverStation.getAlliance();
            
            if (newAlliance.isEmpty()) {
                ready_ = false;
                return;
            }

            setupAlliance(newAlliance.get());
            ready_ = true;
        }

        canSeeTarget_ = ll_.hasAprilTag(speakerCenterTagID_);
        distanceToTarget_ = calculateDistanceToTargetVision().orElse(calculateDistanceToTargetOdometry());

        Logger.recordOutput(logPath("DistanceToTargetMeters"), distanceToTarget_);
        Logger.recordOutput(logPath("CanSeeTarget"), ll_.hasAprilTag(speakerCenterTagID_));
        Logger.recordOutput(logPath("TargetToLookFor"), speakerCenterTagID_);

    }


    

    public Optional<TargetData> getTargetData() {
        if (ready_) {
            TargetData data = new TargetData(speakerCenterTagID_, distanceToTarget_, -1d);
            return Optional.of(data);
        }
        
        return Optional.empty();
    }

    private Optional<Double> calculateDistanceToTargetVision() {
        Optional<Double> ty = ll_.getTY(speakerCenterTagID_);

        if (ty.isEmpty()) {
            return Optional.empty();
        }

        double heightBetweenCameraAndTarget = targetPose3d_.getZ() - TargetTrackerConstants.CAMERA_HEIGHT;
        double angleFromParallelToTarget = ty.get() + TargetTrackerConstants.CAMERA_ANGLE;

        return Optional.of(heightBetweenCameraAndTarget / Math.tan(Units.degreesToRadians(angleFromParallelToTarget)));
    }

    private double calculateDistanceToTargetOdometry() {
        return robotPose2d_.getTranslation().getDistance(targetPose2d_.getTranslation());
    }

    private Pose2d getAprilTagPose2d(int id) {
        return getAprilTagPose3d(id).toPose2d();
    }

    private Pose3d getAprilTagPose3d(int id) {
        Optional<Pose3d> pose3d = fieldLayout_.getTagPose(id);

        if (pose3d.isEmpty()) {
            System.err.println("Error! Field layout pose for apriltag id " + id + " doesnt exist! Assuming Zero. FIX THIS");
        }

        return pose3d.orElse(new Pose3d(0, 0, 0, new Rotation3d(0, 0, 0)));
    }

    private String logPath(String name) {
        return this.getName() + "/" + name;
    }
    
}
