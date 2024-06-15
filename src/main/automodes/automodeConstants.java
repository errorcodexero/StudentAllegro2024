package frc.robot.automodes;

// import automodes.Pose2dWithRotation;
// import automodes.XeroMath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

public class automodeConstants {

    // Mirrors the given pose across the field width
    protected static Pose2dWithRotation mirror(Pose2dWithRotation pose, double fieldWidth) {
        // Normalize the heading and robot rotation angles
        double newHeading = XeroMath.normalizeAngleDegrees(180.0 - pose.getRotation().getDegrees());
        double newRobotRotation = XeroMath.normalizeAngleDegrees(180.0 - pose.getRobotRotation().getDegrees());

        // Mirror the translation across the field width
        Translation2d newTranslation = new Translation2d(fieldWidth - pose.getTranslation().getX(), pose.getTranslation().getY());

        // Create and return the new mirrored pose
        return new Pose2dWithRotation(
                new Pose2d(newTranslation, Rotation2d.fromDegrees(newHeading)), 
                Rotation2d.fromDegrees(newRobotRotation)
        );
    }
}
