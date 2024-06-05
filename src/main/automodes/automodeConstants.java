package frc.robot.automodes;
import org.xero1425.Pose2dWithRotation;
import org.xero1425.XeroMath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

public class automodeConstants {
    //The FRC people were annoying and made the game field mirrored
    static protected Pose2dWithRotation mirror(Pose2dWithRotation pose, double fieldWidth) {
        // Create a new Pose2dWithRotation instance for the mirrored pose
        Pose2dWithRotation mirroredPose = null;

        // Normalize the angles for the new mirrored rotation and robot rotation
        double newHeading = XeroMath.normalizeAngleDegrees(180.0 - pose.getRotation().getDegrees());
        double newRobotRotation = XeroMath.normalizeAngleDegrees(180.0 - pose.getRobotRotation().getDegrees());

        // Calculate the new translation by mirroring the x-coordinate across the field width
        Translation2d newTranslation = new Translation2d(fieldWidth - pose.getTranslation().getX(), pose.getTranslation().getY());

        // Construct the mirrored pose with the new translation and rotation values
        mirroredPose = new Pose2dWithRotation(new Pose2d(newTranslation, Rotation2d.fromDegrees(newHeading)), Rotation2d.fromDegrees(newRobotRotation));

        // Return the mirrored pose
        return mirroredPose;
    }
}
