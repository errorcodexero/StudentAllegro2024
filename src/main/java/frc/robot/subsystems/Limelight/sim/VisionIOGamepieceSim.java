package frc.robot.subsystems.Limelight.sim;

import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;
import org.photonvision.estimation.TargetModel;
import org.photonvision.simulation.SimCameraProperties;
import org.photonvision.simulation.VisionTargetSim;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import frc.robot.subsystems.Limelight.VisionIOInputsAutoLogged;
import frc.robot.subsystems.Limelight.VisionIOPhoton;

public class VisionIOGamepieceSim extends VisionIOPhoton {

    private static final Translation2d[] NOTE_INITIAL_POSITIONS = new Translation2d[] {
        new Translation2d(2.9, 4.1),
        new Translation2d(2.9, 5.55),
        new Translation2d(2.9, 7),
        new Translation2d(8.27, 0.75),
        new Translation2d(8.27, 2.43),
        new Translation2d(8.27, 4.1),
        new Translation2d(8.27, 5.78),
        new Translation2d(8.27, 7.46),
        new Translation2d(13.64, 4.1),
        new Translation2d(13.64, 5.55),
        new Translation2d(13.64, 7),
    };

    private final XeroPhotonSim sim_;
    private final Supplier<Pose2d> robotPoseSupplier_;

    public VisionIOGamepieceSim(String name, Transform3d robotToCamera, Supplier<Pose2d> robotPoseSupplier) {
        super(name, robotToCamera);
        
        sim_ = new XeroPhotonSim(name, camera_, SimCameraProperties.LL2_960_720(), robotToCamera_);
        sim_.enableStreamChannels(true, true);
        
        // Setup Shape of Targets
        TargetModel noteModel = new TargetModel(0.3556);

        for (Translation2d pos : NOTE_INITIAL_POSITIONS) {
            Pose3d notePose = new Pose3d(
                new Translation3d(pos.getX(), pos.getY(), 0.0),
                new Rotation3d(0.0, Math.PI / 2, 0.0)
            );

            sim_.addVisionTargets(new VisionTargetSim(notePose, noteModel));
        }

        sim_.start();

        robotPoseSupplier_ = robotPoseSupplier;
    }

    @Override
    public void updateInputs(VisionIOInputsAutoLogged inputs) {
        sim_.update(robotPoseSupplier_.get());
        super.updateInputs(inputs);
    }

}
