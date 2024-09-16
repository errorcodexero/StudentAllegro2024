package frc.robot.util;

import java.util.Optional;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public enum AprilTags {

    SOURCE_END(9, 2),
    SOURCE_SIDE(10, 1),
    SPEAKER_CENTER(4, 7),
    SPEAKER_SIDE(3, 8),
    STAGE_CENTER(13, 14),
    STAGE_LEFT(11, 15),
    STAGE_RIGHT(12, 16),
    AMP(5, 6);

    private static AprilTagFieldLayout fieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2024Crescendo);

    /**
     * Gets the pose of a apriltag from its ID. You will have to handle yourself what will happen if the tag wasnt found.
     * @param id The id to search for.
     * @return An Optional type of a Pose3d.
     */
    public static Optional<Pose3d> byID(int id) {
        return fieldLayout.getTagPose(id);
    }

    private int red;
    private Pose3d redPose;
    
    private int blue;
    private Pose3d bluePose;

    private AprilTags(int red, int blue) {
        this.red = red;
        this.redPose = initPose(red);

        this.blue = blue;
        this.bluePose = initPose(blue);
    }

    public int getId(Alliance alliance) {
        return alliance == Alliance.Red ? red : blue;
    }

    public Pose3d getPose3d(Alliance alliance) {
        return alliance == Alliance.Red ? redPose : bluePose;
    }

    public Pose2d getPose2d(Alliance alliance) {
        return getPose3d(alliance).toPose2d();
    }

    private Pose3d initPose(int id) {
        Optional<Pose3d> pose = fieldLayout.getTagPose(id);

        if (pose.isPresent()) {
            return pose.get();
        }

        System.err.println("Error! Field layout pose for apriltag id " + id + " doesnt exist! Assuming zero pose!");
        return new Pose3d();
    }
}