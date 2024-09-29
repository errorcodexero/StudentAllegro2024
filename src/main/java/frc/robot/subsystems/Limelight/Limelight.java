package frc.robot.subsystems.Limelight;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Limelight.structs.XeroFiducial;
import frc.robot.subsystems.Limelight.structs.XeroPoseEstimate;
import frc.robot.util.AprilTags;

public class Limelight extends SubsystemBase {

    private final LimelightIO io_;
    private final LimelightIOInputsAutoLogged inputs_;

    private Supplier<Pose2d> poseSupplier_;
    private Consumer<XeroPoseEstimate> megatagConsumer_;

    @AutoLogOutput(key = "Limelight/LastValidTargetTimestamp")
    private double lastValidTargetTimestamp_;

    @AutoLogOutput(key = "Limelight/PoseEstimationMode")
    private LimelightPoseMode poseMode_;

    public static enum LimelightPoseMode {
        MEGATAG,
        MEGATAG2,
        NONE
    }

    /**
     * Creates a Limelight.
     * @param name The limelight name.
     */
    public Limelight(String name) {
        io_ = new LimelightHardware(name);
        inputs_ = new LimelightIOInputsAutoLogged();

        poseMode_ = LimelightPoseMode.NONE;

        poseSupplier_ = null;
        megatagConsumer_ = null;
        lastValidTargetTimestamp_ = 0;
    }

    /**
     * Creates a Limelight using default name "limelight"
     */
    public Limelight() {
        this("limelight");
    }

    /**
     * Sets up Megatag1 tracking.
     * @param estimateConsumer The consumer to consume pose estimates.
     */
    public Command trackMegatag(Consumer<XeroPoseEstimate> megatagConsumer) {
        return runOnce(() -> {
            megatagConsumer_ = megatagConsumer;

            poseMode_ = LimelightPoseMode.MEGATAG;
        });
    }

    /**
     * Sets up Megatag2 tracking. A much better tracking system.
     * @see <a href="https://docs.limelightvision.io/docs/docs-limelight/pipeline-apriltag/apriltag-robot-localization-megatag2">Megatag2 Docs</a>
     * @param poseSupplier Supplier for robot pose.
     * @param megatagConsumer The consumer to consume pose estimates.
     */
    public Command trackMegatag2(Supplier<Pose2d> poseSupplier, Consumer<XeroPoseEstimate> megatagConsumer) {
        return runOnce(() -> {
            poseSupplier_ = poseSupplier;
            megatagConsumer_ = megatagConsumer;
            
            poseMode_ = LimelightPoseMode.MEGATAG2;
        });
    }

    @Override
    public void periodic() {
        io_.updateInputs(inputs_);
        Logger.processInputs(getName(), inputs_);

        switch (poseMode_) {
            case MEGATAG:

                megatagConsumer_.accept(inputs_.poseEstimateBasic); // give megatag1 estimate to consumers

                break;
            case MEGATAG2:

                // give the limelight an orientation
                io_.giveRobotOrientation(poseSupplier_.get().getRotation().getDegrees(), 0, 0, 0, 0, 0);
                
                // give the megatag2 estimate to consumers (ex. drivebase vision measurement)
                megatagConsumer_.accept(inputs_.poseEstimateMegatag2);

                break;
            case NONE:
                break;
        }

        if (hasValidTarget()) {
            lastValidTargetTimestamp_ = Timer.getFPGATimestamp();
        }

        // Creates an array of coordinates to view in AdvantageScope
        Translation2d[] points = Stream.of(inputs_.fiducials).map((f) -> new Translation2d(f.xPixels, f.yPixels)).toArray(Translation2d[]::new);
        Logger.recordOutput(getName() + "/PointsPixels", points);

        // Creates an array of poses of the currently visible tags. This is useful for seeing which tags the robot can see visually.
        ArrayList<Pose3d> validTargetPoses = new ArrayList<Pose3d>();

        for (XeroFiducial fiducial : inputs_.fiducials) {
            AprilTags.byID((int) fiducial.id).ifPresent((pose) -> {
                validTargetPoses.add(pose);
            });
        }

        Logger.recordOutput(getName() + "/ValidTargetPoses", validTargetPoses.toArray(new Pose3d[0]));

    }

    /**
     * Forces the Limelight indicator LED to be off.
     */
    public Command forceLedOff() {
        return runOnce(io_::forceOff);
    }

    /**
     * Forces the Limelight indicator LED to blink.
     */
    public Command forceLedBlink() {
        return runOnce(io_::forceBlink);
    }

    /**
     * Forces the Limelight indicator LED to be on.
     */
    public Command forceLedOn() {
        return runOnce(io_::forceOn);
    }

    /**
     * Sets the Limelight indicator LED back to the default state, controlled by the limelight software itself.
     */
    public Command resetLed() {
        return runOnce(io_::resetLed);
    }

    /**
     * Sets the priority tag to search for.
     * @param id
     */
    public Command setPriorityTagID(int id) {
        return runOnce(() -> {
            io_.setPriorityTagID(id);
        });
    }

    /**
     * Sets the filter list of valid april tags.
     * ex. {3, 4} would filter to only apriltags 3 and 4.
     * @param validIds The filter list
     */
    public Command setTagFilters(int[] validIds) {
        return runOnce(() -> {
            io_.setValidTags(validIds);
        });
    }

    /**
     * Gets estimated information from the Megatag2 pose estimation.
     * For this value to be relevant, you must provide the current robot orientation for every loop, by calling and setting suppliers with setupMegatag2()
     * @see <a href="https://docs.limelightvision.io/docs/docs-limelight/pipeline-apriltag/apriltag-robot-localization-megatag2">Megatag2 Docs</a>
     * @return Estimated Pose from Megatag2 to be used in pose estimation.
     */
    public XeroPoseEstimate getMegatag2PoseEstimate() {
        return inputs_.poseEstimateMegatag2;
    }

    /**
     * Gets estimated information from the regular pose estimation.
     * @return Estimated pose.
     */
    public XeroPoseEstimate getMegatagPoseEstimate() {
        return inputs_.poseEstimateBasic;
    }

    /**
     * Figures out if its currently seeing a specific april tag.
     * @param id The id of the april tag.
     * @return Whether or not the Limelight can currently see the specified april tag.
     */
    public boolean hasAprilTag(int id) {
        return findFid(id).isPresent();
    }

    /**
     * Finds if a valid target exists.
     * @return Whether or not a valid target exists.
     */
    public boolean hasValidTarget() {
        return inputs_.simpleValid;
    }

    /**
     * Gets the id of the primary in-view Fiducial/Apriltag
     * @return
     */
    public int getSimpleID() {
        return inputs_.simpleID;
    }

    /**
     * Gets the vision targets X offset. Make sure to check if the robot is seeing a tag, and its the one you want first. Otherwise this data will be innaccurate.
     * @return Its X offset in degrees from the center of the camera. +X Right +Y Down
     */
    public double getSimpleX() {
        return inputs_.simpleX;
    }

    /**
     * Gets the vision targets Y offset. Make sure to check if the robot is seeing a tag, and its the one you want first. Otherwise this data will be innaccurate.
     * @return Its Y offset in degrees from the center of the camera. +X Right +Y Down
     */
    public double getSimpleY() {
        return inputs_.simpleY;
    }

    /**
     * Gets the vision targets area on the camera. Make sure to check if the robot is seeing a tag, and its the one you want first. Otherwise this data will be innaccurate.
     * @return How much of the camera the target covers. This range is configured in the limelight tuning.
     */
    public double getSimpleArea() {
        return inputs_.simpleArea;
    }

    /**
     * Gets a specific apriltag's X offset. Make sure to check if the robot can see the tag first. Otherwise this data will be innaccurate.
     * @param id The id of the apriltag.
     * @return Its X offset in degrees from the center of the camera. +X Right +Y Down
     */
    public Optional<Double> getSpecificX(int id) {
        Optional<XeroFiducial> fid = findFid(id);

        if (fid.isEmpty())
            return Optional.empty();

        return Optional.of(fid.get().x);
    }

    /**
     * Gets a specific apriltag's Y offset. Make sure to check if the robot can see the tag first. Otherwise this data will be innaccurate.
     * @param id The id of the apriltag.
     * @return Its Y offset in degrees from the center of the camera. +X Right +Y Down
     */
    public Optional<Double> getSpecificY(int id) {
        Optional<XeroFiducial> fid = findFid(id);

        if (fid.isEmpty())
            return Optional.empty();

        return Optional.of(fid.get().y);
    }

    /**
     * Gets a specific apriltag's area on the camera. Make sure to check if the robot can see the tag first. Otherwise this data will be innaccurate.
     * @param id The id of the apriltag.
     * @return How much of the camera the tag covers. This range is configured in the limelight tuning.
     */
    public Optional<Double> getSpecificArea(int id) {
        Optional<XeroFiducial> fid = findFid(id);

        if (fid.isEmpty())
            return Optional.empty();

        return Optional.of(fid.get().area);
    }

    /**
     * Gets the time since theres been a valid target.
     * @return The time elapsed, in seconds.
     */
    public double getTimeSinceValidTarget() {
        return Timer.getFPGATimestamp() - lastValidTargetTimestamp_;
    }

    /**
     * Finds a fidicial object in the array.
     * @return The fidicial, null if not found.
     */
    private Optional<XeroFiducial> findFid(int id) {
        for (XeroFiducial fid : inputs_.fiducials) {
            if (fid.id == (double) id) {
                return Optional.of(fid);
            }
        }

        return Optional.empty();
    }

}