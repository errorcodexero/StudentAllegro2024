package frc.robot.subsystems.Limelight;

import java.util.Optional;
import java.util.stream.Stream;

import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Limelight.LimelightIO.LimelightIOInputs;
import frc.robot.subsystems.Limelight.structs.Fiducial;
import frc.robot.subsystems.Limelight.structs.VisionPoseEstimate;
import frc.robot.subsystems.Swerve.CommandSwerveDrivetrain;

public class LimelightSubsystem extends SubsystemBase {

    private final LimelightIO io_;

    // TODO: move to swerve codebase later!!!
    private final CommandSwerveDrivetrain drivetrain_;

    private final LimelightIOInputs inputs_;

    /**
     * Creates a Limelight Subsystem,
     * Uses default limelight name of "limelight" and default IO implementation.
     */
    public LimelightSubsystem(CommandSwerveDrivetrain drivetrain) {
        this(new LimelightHardware(), drivetrain);
    }

    /**
     * Creates a Limelight Subsystem,
     * Uses a specified Limelight name and the default IO implementation.
     * @param name The name of the limelight, ex. "limelight-side"
     */
    public LimelightSubsystem(String name, CommandSwerveDrivetrain drivetrain) {
        this(new LimelightHardware(name), drivetrain);
    }

    /**
     * Creates a Limelight Subsystem,
     * Uses specified IO implementation, specifying name possibly in its constructor.
     * @param io The IO Implementation to use.
     */
    public LimelightSubsystem(LimelightIO io, CommandSwerveDrivetrain drivetrain) {
        io_ = io;
        inputs_ = new LimelightIOInputs();
        drivetrain_ = drivetrain;
    }

    @Override
    public void periodic() {
        io_.updateInputs(inputs_);
        Logger.processInputs(getName(), inputs_);

        giveRobotOrientation(drivetrain_.getState().Pose.getRotation().getDegrees());
        drivetrain_.addVisionMeasurement(inputs_.megatag2PoseEstimate.pose, inputs_.megatag2PoseEstimate.timestamp);

        // Creates an array of coordinates to view in AdvantageScope
        Translation2d[] points = Stream.of(inputs_.fiducials).map((f) -> new Translation2d(f.xPixels, f.yPixels)).toArray(Translation2d[]::new);
        Logger.recordOutput(getName() + "/PointsPixels", points);

    }

    /**
     * Forces the Limelight indicator LED to be off.
     */
    public void forceLedOff() {
        io_.forceOff();
    }

    /**
     * Forces the Limelight indicator LED to blink.
     */
    public void forceLedBlink() {
        io_.forceBlink();
    }

    /**
     * Forces the Limelight indicator LED to be on.
     */
    public void forceLedOn() {
        io_.forceOn();
    }

    /**
     * Sets the Limelight indicator LED back to the default state, controlled by the limelight software itself.
     */
    public void resetLed() {
        io_.resetLed();
    }

    /**
     * Sets the priority tag to search for.
     * @param id
     */
    public void setPriorityTagID(int id) {
        io_.setPriorityTagID(id);
    }

    /**
     * Sets the filter list of valid april tags.
     * ex. {3, 4} would filter to only apriltags 3 and 4.
     * @param validIds The filter list
     */
    public void setTagFilters(int[] validIds) {
        io_.setValidTags(validIds);
    }

    /**
     * Gives the robot orientation to the limelight for Megatag2 odometry.
     * Blue origin, CCW-positive, 0 degrees facing red alliance wall
     * @param yaw
     */
    public void giveRobotOrientation(double yaw) {
        io_.giveRobotOrientation(yaw, 0, 0, 0, 0, 0);
    }

    /**
     * Gives the robot orientation to the limelight for Megatag2 odometry.
     * Blue origin, CCW-positive, 0 degrees facing red alliance wall
     * @param yaw
     * @param yawRate
     * @param pitch
     * @param pitchRate
     * @param roll
     * @param rollRate
     */
    public void giveRobotOrientation(double yaw, double yawRate, double pitch, double pitchRate, double roll, double rollRate) {
        io_.giveRobotOrientation(yaw, yawRate, pitch, pitchRate, roll, rollRate);
    }

    /**
     * Gets estimated information from the Megatag2 pose estimation.
     * For this value to be relevant, you must provide the current robot orientation for every loop.
     * @see <a href="https://docs.limelightvision.io/docs/docs-limelight/pipeline-apriltag/apriltag-robot-localization-megatag2">Megatag2 Docs</a>
     * @return Estimated Pose from Megatag2 to be used in pose estimation.
     */
    public VisionPoseEstimate getMegatag2PoseEstimate() {
        return inputs_.megatag2PoseEstimate;
    }

    /**
     * Gets estimated information from the regular pose estimation.
     * @return Estimated pose.
     */
    public VisionPoseEstimate getPoseEstimate() {
        return inputs_.basicPoseEstimate;
    }

    /**
     * Finds if a valid target exists.
     * @return Whether or not a valid target exists.
     */
    public boolean hasValidTarget() {
        return inputs_.fiducials.length > 0;
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
     * Gets the id of the primary in-view Fiducial/Apriltag
     * @return
     */
    public int getFiducialID() {
        return inputs_.fiducialID;
    }

    /**
     * Gets the vision targets X offset. Make sure to check if the robot is seeing a tag, and its the one you want first. Otherwise this data will be innaccurate.
     * @return Its X offset in degrees from the center of the camera. +X Right +Y Down
     */
    public double getTX() {
        return inputs_.tX;
    }

    /**
     * Gets the vision targets Y offset. Make sure to check if the robot is seeing a tag, and its the one you want first. Otherwise this data will be innaccurate.
     * @return Its Y offset in degrees from the center of the camera. +X Right +Y Down
     */
    public double getTY() {
        return inputs_.tY;
    }

    /**
     * Gets the vision targets area on the camera. Make sure to check if the robot is seeing a tag, and its the one you want first. Otherwise this data will be innaccurate.
     * @return How much of the camera the target covers. This range is configured in the limelight tuning.
     */
    public double getTArea() {
        return inputs_.tArea;
    }

    /**
     * Gets a specific apriltag's X offset. Make sure to check if the robot can see the tag first. Otherwise this data will be innaccurate.
     * @param id The id of the apriltag.
     * @return Its X offset in degrees from the center of the camera. +X Right +Y Down
     */
    public Optional<Double> getTX(int id) {
        Optional<Fiducial> fid = findFid(id);

        if (fid.isEmpty())
            return Optional.empty();

        return Optional.of(fid.get().x);
    }

    /**
     * Gets a specific apriltag's Y offset. Make sure to check if the robot can see the tag first. Otherwise this data will be innaccurate.
     * @param id The id of the apriltag.
     * @return Its Y offset in degrees from the center of the camera. +X Right +Y Down
     */
    public Optional<Double> getTY(int id) {
        Optional<Fiducial> fid = findFid(id);

        if (fid.isEmpty())
            return Optional.empty();

        return Optional.of(fid.get().y);
    }

    /**
     * Gets a specific apriltag's area on the camera. Make sure to check if the robot can see the tag first. Otherwise this data will be innaccurate.
     * @param id The id of the apriltag.
     * @return How much of the camera the tag covers. This range is configured in the limelight tuning.
     */
    public Optional<Double> getTArea(int id) {
        Optional<Fiducial> fid = findFid(id);

        if (fid.isEmpty())
            return Optional.empty();

        return Optional.of(fid.get().area);
    }

    /**
     * Finds a fidicial object in the array.
     * @return The fidicial, null if not found.
     */
    private Optional<Fiducial> findFid(int id) {
        for (Fiducial fid : inputs_.fiducials) {
            if (fid.id == (double) id) {
                return Optional.of(fid);
            }
        }

        return Optional.empty();
    }

}
