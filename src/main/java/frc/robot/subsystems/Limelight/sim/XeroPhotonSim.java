package frc.robot.subsystems.Limelight.sim;

import org.littletonrobotics.junction.Logger;
import org.photonvision.PhotonCamera;
import org.photonvision.simulation.PhotonCameraSim;
import org.photonvision.simulation.SimCameraProperties;
import org.photonvision.simulation.VisionSystemSim;
import org.photonvision.simulation.VisionTargetSim;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Transform3d;

public class XeroPhotonSim {

    private final VisionSystemSim simWorld_;

    private final PhotonCamera camera_;
    private final SimCameraProperties cameraProps_;
    private final Transform3d robotToCamera_;

    // Configurations
    private boolean wireframeEnabled_ = true;
    private boolean rawStream_ = false;
    private boolean processedStream_ = false;

    private boolean started_ = false;

    public XeroPhotonSim(String name, PhotonCamera camera, SimCameraProperties cameraProps, Transform3d robotToCamera) {
        // Create vision sim
        simWorld_ = new VisionSystemSim(name);

        camera_ = camera;
        cameraProps_ = cameraProps;
        robotToCamera_ = robotToCamera;

        Logger.recordOutput("CameraSim/" + name + "/HorizontalFovDegrees", cameraProps.getHorizFOV().getDegrees());
        Logger.recordOutput("CameraSim/" + name + "/DiagonalFovDegrees", cameraProps.getDiagFOV().getDegrees());
        Logger.recordOutput("CameraSim/" + name + "/VerticalFovDegrees", cameraProps.getVertFOV().getDegrees());
    }

    public void enableWireframe(boolean enabled) {
        if (started_) {
            throw new UnsupportedOperationException("You cannot configure a Photon camera sim after starting it!");
        }

        wireframeEnabled_ = enabled;
    }

    public void enableStreamChannels(boolean raw, boolean processed) {
        if (started_) {
            throw new UnsupportedOperationException("You cannot configure a Photon camera sim after starting it!");
        }

        rawStream_ = raw;
        processedStream_ = processed;
    }

    public void addAprilTags(AprilTagFieldLayout tagLayout) {
        simWorld_.addAprilTags(tagLayout);
    }

    public void addVisionTargets(VisionTargetSim... targets) {
        simWorld_.addVisionTargets(targets);
    }

    /**
     * Starts the camera simulation. Ensure you configure the simulation before starting it!
     */
    public void start() {
        PhotonCameraSim simcam = new PhotonCameraSim(camera_, cameraProps_);
        simcam.enableDrawWireframe(wireframeEnabled_);
        simcam.enableRawStream(rawStream_);
        simcam.enableProcessedStream(processedStream_);

        simWorld_.addCamera(simcam, robotToCamera_);
        started_ = true;
    }

    /**
     * Updates the simulated world. Must be called every loop for accurate simulation.
     * @param pose The current field-relative pose of the robot
     */
    public void update(Pose2d pose) {
        simWorld_.update(pose);
    }
}
