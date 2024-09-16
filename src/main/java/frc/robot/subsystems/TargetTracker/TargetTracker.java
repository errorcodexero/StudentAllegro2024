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
    private Rotation2d angleToTarget_;

    public TargetTracker(Supplier<Pose2d> robotPoseSupplier) {
        robotPoseSupplier_ = robotPoseSupplier;
    }

    @Override
    public void periodic() {
        
        Pose2d targetPose = AprilTags.SPEAKER_CENTER.getPose2d(getAlliance());
        Pose2d robotPose = robotPoseSupplier_.get();

        // The pose of the robot, but facing the back of the robot, AKA where the shooter is pointed and where the limelight is looking.
        Pose2d backFacingRobotPose = new Pose2d(robotPose.getTranslation(), robotPose.getRotation().unaryMinus());

        angleToTarget_ = targetPose.relativeTo(backFacingRobotPose).getTranslation().getAngle();
        distanceToTargetMeters_ = robotPose.getTranslation().getDistance(targetPose.getTranslation());

        Logger.recordOutput(getName() + "/RobotPose", robotPose);

        Logger.recordOutput(getName() + "/AngleCorrected", new Pose2d(robotPose.getTranslation(), robotPose.getRotation().plus(angleToTarget_)));
        Logger.recordOutput(getName() + "/AngleCorrected2", new Pose2d(robotPose.getTranslation(), robotPose.getRotation().rotateBy(angleToTarget_)));

        Logger.recordOutput(getName() + "/TargetPose", targetPose);

    }

    private Alliance getAlliance() {
        return DriverStation.getAlliance().orElse(Alliance.Red);
    }
    
}
