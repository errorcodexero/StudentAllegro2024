package frc.robot.subsystems.TargetTracker;

import java.util.Optional;

import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class TargetTrackerConstants {

    public static final double CAMERA_ANGLE = 40.0; // The angle that the camera is positioned.
    public static final double CAMERA_HEIGHT = 0.165; // The height that the camera is at from the floor.

    public static class AprilTags {
        public static final int BLUE_SOURCE_SIDE = 1;
        public static final int BLUE_SOURCE_END = 2;
        public static final int RED_SPEAKER_SIDE = 3;
        public static final int RED_SPEAKER_CENTER = 4;
        public static final int RED_AMP = 5;
        public static final int BLUE_AMP = 6;
        public static final int BLUE_SPEAKER_CENTER = 7;
        public static final int BLUE_SPEAKER_SIDE = 8;
        public static final int RED_SOURCE_END = 9;
        public static final int RED_SOURCE_SIDE = 10;
        public static final int RED_STAGE_LEFT = 11;
        public static final int RED_STAGE_RIGHT = 12;
        public static final int RED_CENTER_STAGE = 13;
        public static final int BLUE_CENTER_STAGE = 14;
        public static final int BLUE_STAGE_LEFT = 15;
        public static final int BLUE_STAGE_RIGHT = 16;

        /**
         * Gets the april tag id of the speaker center for the specified alliance.
         * @param alliance Optional that can be fetched using DriverStation.getAlliance() :3
         * @return The id for the april tag
         */
        public static int getSpeakerCenter(Optional<Alliance> alliance) {
            if (alliance.isEmpty()) {
                return 0;
            }

            return switch (alliance.get()) {
                case Blue -> BLUE_SPEAKER_CENTER;
                case Red -> RED_SPEAKER_CENTER;
            };
        }
    }
}
