package frc.robot.subsystems.Limelight;

import java.util.ArrayList;
import java.util.Optional;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.common.hardware.VisionLEDMode;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;
import org.photonvision.targeting.TargetCorner;

import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.Constants.FieldConstants;
import frc.robot.subsystems.Limelight.structs.XeroFiducial;
import frc.robot.subsystems.Limelight.structs.XeroGamepiece;
import frc.robot.subsystems.Limelight.structs.XeroPoseEstimate;

public class VisionIOPhoton implements VisionIO {

    // Transform from robot to camera.
    protected final Transform3d robotToCamera_;

    protected final PhotonCamera camera_;
    private final PhotonPoseEstimator poseEstimator_;

    public VisionIOPhoton(String name, Transform3d robotToCamera) {
        // Setup camera
        camera_ = new PhotonCamera(name);
        robotToCamera_ = robotToCamera;

        // Setup pose stimator
        poseEstimator_ = new PhotonPoseEstimator(
            FieldConstants.FIELD_LAYOUT,
            PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR,
            camera_,
            robotToCamera_
        );
    }

    @Override
    public void updateInputs(VisionIOInputsAutoLogged inputs) {
        PhotonPipelineResult result = camera_.getLatestResult();
        PhotonTrackedTarget bestTarget = result.getBestTarget();

        // Simple setup
        if (bestTarget != null) {
            inputs.simpleID = bestTarget.getFiducialId();
            inputs.simpleX = bestTarget.getPitch();
            inputs.simpleY = bestTarget.getYaw();
            inputs.simpleArea = bestTarget.getArea();
            inputs.simpleValid = true;
        } else {
            inputs.simpleID = 0;
            inputs.simpleX = 0.0;
            inputs.simpleY = 0.0;
            inputs.simpleArea = 0.0;
            inputs.simpleValid = false;
        }

        inputs.timestampSeconds = result.getTimestampSeconds();

        // Target information to fill
        ArrayList<Translation2d> cornerCoords = new ArrayList<>();
        ArrayList<XeroFiducial> fiducials = new ArrayList<>();
        ArrayList<XeroGamepiece> gamepieces = new ArrayList<>();

        // Get target information
        for (PhotonTrackedTarget target : result.getTargets()) {

            for (TargetCorner corner : target.getDetectedCorners()) {
                cornerCoords.add(new Translation2d(corner.x, corner.y));
            }

            fiducials.add(new XeroFiducial(target));
            gamepieces.add(new XeroGamepiece(target));
        }

        inputs.rawCorners = cornerCoords.toArray(new Translation2d[0]);
        inputs.fiducials = fiducials.toArray(new XeroFiducial[0]);
        inputs.gamepieces = gamepieces.toArray(new XeroGamepiece[0]);

        Optional<EstimatedRobotPose> optionalPhotonEstimate = poseEstimator_.update();

        optionalPhotonEstimate.ifPresentOrElse(photonEstimate -> {
            inputs.poseEstimate = new XeroPoseEstimate(
                photonEstimate.estimatedPose.toPose2d(),
                photonEstimate.timestampSeconds,
                photonEstimate.targetsUsed.size(),
                true
            );
        }, () -> {
            inputs.poseEstimate = new XeroPoseEstimate();
        });
    }

    @Override
    public void forceBlink() {
        camera_.setLED(VisionLEDMode.kBlink);
    }

    @Override
    public void forceOff() {
        camera_.setLED(VisionLEDMode.kOff);
    }

    @Override
    public void forceOn() {
        camera_.setLED(VisionLEDMode.kOn);
    }

    @Override
    public void resetLed() {
        camera_.setLED(VisionLEDMode.kDefault);
    }

}
