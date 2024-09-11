package frc.robot.automodes;

import java.util.Optional;
import frc.org.xero1425.Pose2dWithRotation;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class FourNoteDynamicConstants extends automodeconstants {

    public static final double LOW_MANUAL_TILT = 3.0;
    public static final double LOW_MANUAL_TILT_POS_TOL = 2.0;
    public static final double LOW_MANUAL_TILT_VEL_TOL = 2.0;
    public static final double LOW_MANUAL_UP_DOWN = 35.0;
    public static final double LOW_MANUAL_UP_DOWN_POS_TOL = 5.0;
    public static final double LOW_MANUAL_UP_DOWN_VEL_TOL = 5.0;
    public static final double LOW_MANUAL_SHOOTER = 65.0;
    public static final double LOW_MANUAL_SHOOTER_VEL_TOL = 5.0;
    public static final double INTAKE_DOWN_DELAY = 0.4;
    public static final double DISTANCE_SHOOT_2 = 0.5;
    public static final double DISTANCE_SHOOT_3 = 0.5;
    public static final double DISTANCE_SHOOT_4 = 0.5;

    public static final double PATH_1_VELOCITY = 3.0;
    public static final double PATH_1_ACCEL = 2.5;

    private static final Pose2dWithRotation SHOOT_POSE_BLUE = new Pose2dWithRotation(
        new Pose2d(1.50, 5.55, Rotation2d.fromDegrees(180.0)), Rotation2d.fromDegrees(0.0)
    );
    private static final Pose2dWithRotation COLLECT_1_POSE_BLUE = new Pose2dWithRotation(
        new Pose2d(2.40, 5.55, Rotation2d.fromDegrees(0.0)), Rotation2d.fromDegrees(0.0)
    );
    private static final Pose2dWithRotation COLLECT_2_POSE_BLUE = new Pose2dWithRotation(
        new Pose2d(2.50, 6.32, Rotation2d.fromDegrees(45.0)), Rotation2d.fromDegrees(45.0)
    );
    private static final Pose2dWithRotation COLLECT_3_POSE_BLUE = new Pose2dWithRotation(
        new Pose2d(2.30, 4.55, Rotation2d.fromDegrees(-45.0)), Rotation2d.fromDegrees(-45.0)
    );

    private static final Pose2dWithRotation SHOOT_POSE_RED = new Pose2dWithRotation(
        new Pose2d(1.50, 5.55, Rotation2d.fromDegrees(180.0)), Rotation2d.fromDegrees(0.0)
    );
    private static final Pose2dWithRotation COLLECT_1_POSE_RED = new Pose2dWithRotation(
        new Pose2d(2.40, 5.55, Rotation2d.fromDegrees(0.0)), Rotation2d.fromDegrees(0.0)
    );
    private static final Pose2dWithRotation COLLECT_2_POSE_RED = new Pose2dWithRotation(
        new Pose2d(2.50, 6.32, Rotation2d.fromDegrees(45.0)), Rotation2d.fromDegrees(45.0)
    );
    private static final Pose2dWithRotation COLLECT_3_POSE_RED = new Pose2dWithRotation(
        new Pose2d(2.30, 4.55, Rotation2d.fromDegrees(-45.0)), Rotation2d.fromDegrees(-45.0)
    );

    public static Pose2dWithRotation getShootPose(double fieldWidth) throws Exception {
        Optional<Alliance> alliance = DriverStation.getAlliance();
        if (alliance.isEmpty()) {
            throw new Exception("Cannot initialize an automode before the alliance is determined.");
        }
        return (alliance.get() == Alliance.Blue) ? SHOOT_POSE_BLUE : mirror(SHOOT_POSE_RED, fieldWidth);
    }

    public static Pose2dWithRotation getCollect1Pose(double fieldWidth) throws Exception {
        Optional<Alliance> alliance = DriverStation.getAlliance();
        if (alliance.isEmpty()) {
            throw new Exception("Cannot initialize an automode before the alliance is determined.");
        }
        return (alliance.get() == Alliance.Blue) ? COLLECT_1_POSE_BLUE : mirror(COLLECT_1_POSE_RED, fieldWidth);
    }

    public static Pose2dWithRotation getCollect2Pose(double fieldWidth) throws Exception {
        Optional<Alliance> alliance = DriverStation.getAlliance();
        if (alliance.isEmpty()) {
            throw new Exception("Cannot initialize an automode before the alliance is determined.");
        }
        return (alliance.get() == Alliance.Blue) ? COLLECT_2_POSE_BLUE : mirror(COLLECT_2_POSE_RED, fieldWidth);
    }

    public static Pose2dWithRotation getCollect3Pose(double fieldWidth) throws Exception {
        Optional<Alliance> alliance = DriverStation.getAlliance();
        if (alliance.isEmpty()) {
            throw new Exception("Cannot initialize an automode before the alliance is determined.");
        }
        return (alliance.get() == Alliance.Blue) ? COLLECT_3_POSE_BLUE : mirror(COLLECT_3_POSE_RED, fieldWidth);
    }

    public static void configureLowManualControls(double posTol, double shooterVelTol, double tilt, double tiltPosTol, double tiltVelTol, double shooter, double shooterVelTol2, boolean flag) {
        throw new UnsupportedOperationException("Method 'configureLowManualControls' is not implemented.");
    }
}
