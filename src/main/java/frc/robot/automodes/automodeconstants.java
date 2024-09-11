package frc.robot.automodes;

import frc.org.xero1425.Pose2dWithRotation;
import frc.org.xero1425.XeroMath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

public class automodeconstants {
    protected static Pose2dWithRotation mirror(Pose2dWithRotation pose, double fieldWidth) {
        Pose2dWithRotation mirroredPose = null;

        double mirroredHeading = XeroMath.normalizeAngleDegrees(180.0 - pose.getRotation().getDegrees());
        double mirroredRobotRotation = XeroMath.normalizeAngleDegrees(180.0 - pose.getRobotRotation().getDegrees());
        Translation2d mirroredTranslation = new Translation2d(fieldWidth - pose.getTranslation().getX(), pose.getTranslation().getY());
        
        mirroredPose = new Pose2dWithRotation(new Pose2d(mirroredTranslation, Rotation2d.fromDegrees(mirroredHeading)), Rotation2d.fromDegrees(mirroredRobotRotation));
        return mirroredPose;
    }
}
