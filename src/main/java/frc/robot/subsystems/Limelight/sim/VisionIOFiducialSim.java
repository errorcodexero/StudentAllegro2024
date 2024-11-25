package frc.robot.subsystems.Limelight.sim;

import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;
import org.photonvision.simulation.SimCameraProperties;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import frc.robot.subsystems.Limelight.VisionIOInputsAutoLogged;
import frc.robot.subsystems.Limelight.VisionIOPhoton;

public class VisionIOFiducialSim extends VisionIOPhoton {

    private final XeroPhotonSim sim_;

    private final Supplier<Pose2d> robotPoseSupplier_;

    private static final Transform3d botToCam = new Transform3d(
        new Translation3d(-0.321, 0, 0.13),
        new Rotation3d(0, Units.degreesToRadians(-40), Units.degreesToRadians(180))
    );

    public VisionIOFiducialSim(String name, Supplier<Pose2d> robotPoseSupplier, AprilTagFieldLayout layout) {
        super(name, botToCam);
        Logger.recordOutput("AprilCamPos", botToCam);

        sim_ = new XeroPhotonSim(name, camera_, SimCameraProperties.LL2_960_720(), robotToCamera_);
        sim_.enableStreamChannels(true, true);
        sim_.addAprilTags(layout);
        sim_.start();

        robotPoseSupplier_ = robotPoseSupplier;
    }

    @Override
    public void updateInputs(VisionIOInputsAutoLogged inputs) {
        sim_.update(robotPoseSupplier_.get());
        super.updateInputs(inputs);
    }
    
}
