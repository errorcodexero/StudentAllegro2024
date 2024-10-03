package frc.robot.util;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;

public class ComponentVisualizer {

    private static final double UPDOWN_LENGTH = Units.inchesToMeters(8);

    private final String logkey_;

    public ComponentVisualizer(String logkey) {
        logkey_ = logkey;
    }
    
    public void update(double updownAngle, double tiltAngle) {
        Pose3d updownPose = new Pose3d(0.203, 0.0, 0.212, new Rotation3d(0, Units.degreesToRadians(updownAngle), 0));

        Pose3d tiltPose = updownPose.transformBy(
            new Transform3d(
                new Translation3d(UPDOWN_LENGTH, 0, 0),
                new Rotation3d(0.0, Units.degreesToRadians(tiltAngle), 0.0)
            )
        );

        Pose3d climberOrigin = new Pose3d(
            0.0,
            Units.inchesToMeters(9.5),
            Units.inchesToMeters(10),
            new Rotation3d(0, Units.degreesToRadians(-110), 0)
        );

        Pose3d climberPose = climberOrigin.transformBy(
            new Transform3d(
                new Translation3d(0.2, 0, 0),
                new Rotation3d()
            )
        );

        Pose3d empty = new Pose3d(100, 100, 100, new Rotation3d());

        Logger.recordOutput(logkey_, updownPose, tiltPose, empty, empty, climberPose);
    }

}
