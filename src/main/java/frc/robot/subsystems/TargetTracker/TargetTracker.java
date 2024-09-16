package frc.robot.subsystems.TargetTracker;

import java.util.function.Supplier;

import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.AprilTags;

public class TargetTracker extends SubsystemBase {

    private Supplier<Pose2d> robotPoseSupplier_;

    @AutoLogOutput(key = "TargetTracker/DistanceToTargetMeters")
    private double distanceToTargetMeters_;

    @AutoLogOutput(key = "TargetTracker/RotationToTarget")
    private Rotation2d rotationToTarget_;

    public TargetTracker(Supplier<Pose2d> robotPoseSupplier) {
        robotPoseSupplier_ = robotPoseSupplier;
    }

    @Override
    public void periodic() {

        Pose2d robotPose = robotPoseSupplier_.get();
        Pose2d targetPose = AprilTags.SPEAKER_CENTER.getPose2d(getAlliance());

        Pose2d backFacingRobotPose = new Pose2d(robotPose.getTranslation(), robotPose.getRotation().rotateBy(Rotation2d.fromDegrees(180)));

        Pose2d targetPoseRelativeToRobotBack = targetPose.relativeTo(backFacingRobotPose);

        rotationToTarget_ = targetPoseRelativeToRobotBack.getTranslation().getAngle();
        distanceToTargetMeters_ = targetPoseRelativeToRobotBack.getTranslation().getNorm();

        Logger.recordOutput(getName() + "/Explanation/BackfacingRobotPose", backFacingRobotPose);
        Logger.recordOutput(getName() + "/Explanation/AngleCorrected", new Pose2d(
            robotPose.getTranslation(),
            robotPose.getRotation().rotateBy(rotationToTarget_)
        ));
        Logger.recordOutput(getName() + "/Explanation/TargetPose", targetPose);

    }

    private Alliance getAlliance() {
        return DriverStation.getAlliance().orElse(Alliance.Red);
    }
    
}
