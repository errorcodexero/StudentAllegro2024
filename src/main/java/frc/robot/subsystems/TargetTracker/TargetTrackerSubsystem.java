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
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Limelight.LimelightSubsystem;
import frc.robot.subsystems.TargetTracker.TargetTrackerConstants.AprilTags;

public class TargetTrackerSubsystem extends SubsystemBase {

    private LimelightSubsystem ll_;

    private AprilTagFieldLayout fieldLayout_;
    private Pose2d targetPose2d_;
    private Pose3d targetPose3d_;
    private int targetNumber_;

    private boolean canSeeTarget_;
    private double distanceToTarget_;

    /**
     * PLACEHOLDER!!!! DO NOT USE ON A REAL ROBOT!!!!!!!!!!!!!!!!!!!!111!!!!1111!!
     */
    private Pose2d robotPose2d_ = new Pose2d();

    public TargetTrackerSubsystem(LimelightSubsystem ll) {

        this.ll_ = ll;
        fieldLayout_ = AprilTagFieldLayout.loadField(AprilTagFields.k2024Crescendo);
        targetNumber_ = AprilTags.getSpeakerCenter(DriverStation.getAlliance());
        targetPose2d_ = getAprilTagPose2d(targetNumber_);
        targetPose3d_ = getAprilTagPose3d(targetNumber_);
        canSeeTarget_ = false;

        ll_.setPriorityTagID(targetNumber_);

    }

    @Override
    public void periodic() {
        if (ll_.isValidTarget() && ll_.getFiducialID() == targetNumber_) {
            canSeeTarget_ = true;
            distanceToTarget_ = calculateDistanceToTargetVision();
        } else {
            canSeeTarget_ = false;
            distanceToTarget_ = calculateDistanceToTargetOdometry();
        }

        Logger.recordOutput("TargetTracker/DistanceToTargetMeters", distanceToTarget_);
        Logger.recordOutput("TargetTracker/CanSeeTarget", ll_.getFiducialID() == targetNumber_);
        Logger.recordOutput("TargetTracker/TargetToLookFor", targetNumber_);

    }

    private double calculateDistanceToTargetVision() {
        double heightBetweenCameraAndTarget = targetPose3d_.getZ() - TargetTrackerConstants.CAMERA_HEIGHT;
        double angleFromParallelToTarget = ll_.getTY() + TargetTrackerConstants.CAMERA_ANGLE;

        return heightBetweenCameraAndTarget / Math.tan(Units.degreesToRadians(angleFromParallelToTarget));
    }

    private double calculateDistanceToTargetOdometry() {
        return robotPose2d_.getTranslation().getDistance(targetPose2d_.getTranslation());
    }

    public double getDistanceToTarget() {
        return distanceToTarget_;
    }

    public int getTargetNumber() {
        return targetNumber_;
    }

    public boolean canSeeTarget() {
        return canSeeTarget_;
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

    
}
