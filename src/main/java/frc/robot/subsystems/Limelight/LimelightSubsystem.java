package frc.robot.subsystems.Limelight;

import java.util.Optional;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Limelight.LimelightHelpers.LimelightTarget_Fiducial;
import frc.robot.subsystems.Limelight.LimelightIO.LimelightIOInputs;

public class LimelightSubsystem extends SubsystemBase {

    private final LimelightIO io_;
    private final LimelightIOInputs inputs_;

    /**
     * Creates a Limelight Subsystem,
     * Uses default limelight name of "limelight" and default IO implementation.
     */
    public LimelightSubsystem() {
        this(new LimelightHardware());
    }

    /**
     * Creates a Limelight Subsystem,
     * Uses a specified Limelight name and the default IO implementation.
     * @param name The name of the limelight, ex. "limelight-side"
     */
    public LimelightSubsystem(String name) {
        this(new LimelightHardware(name));
    }

    /**
     * Creates a Limelight Subsystem,
     * Uses specified IO implementation, specifying name possibly in its constructor.
     * @param io The IO Implementation to use.
     */
    public LimelightSubsystem(LimelightIO io) {
        io_ = io;
        inputs_ = new LimelightIOInputs();
    }

    @Override
    public void periodic() {
        io_.updateInputs(inputs_);
        Logger.processInputs(getName(), inputs_);
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
     * Finds if a valid target exists.
     * @return Whether or not a valid target exists.
     */
    public boolean isValidTarget() {
        return inputs_.tValid;
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
     * Gets the id of the primary in-view Fiducial/Apriltag
     * @return
     */
    public int getFiducialID() {
        return inputs_.fiducialID;
    }

    /**
     * Gets a specific apriltag's X offset. Make sure to check if the robot can see the tag first. Otherwise this data will be innaccurate.
     * @param id The id of the apriltag.
     * @return Its X offset in degrees from the center of the camera. +X Right +Y Down
     */
    public Optional<Double> getAprilTX(int id) {
        Optional<LimelightTarget_Fiducial> fid = findFid(id);

        if (fid.isEmpty())
            return Optional.empty();

        return Optional.of(fid.get().tx);
    }

    /**
     * Gets a specific apriltag's Y offset. Make sure to check if the robot can see the tag first. Otherwise this data will be innaccurate.
     * @param id The id of the apriltag.
     * @return Its Y offset in degrees from the center of the camera. +X Right +Y Down
     */
    public Optional<Double> getAprilTY(int id) {
        Optional<LimelightTarget_Fiducial> fid = findFid(id);

        if (fid.isEmpty())
            return Optional.empty();

        return Optional.of(fid.get().ty);
    }

    /**
     * Gets a specific apriltag's area on the camera. Make sure to check if the robot can see the tag first. Otherwise this data will be innaccurate.
     * @param id The id of the apriltag.
     * @return How much of the camera the tag covers. This range is configured in the limelight tuning.
     */
    public Optional<Double> getAprilTArea(int id) {
        Optional<LimelightTarget_Fiducial> fid = findFid(id);

        if (fid.isEmpty())
            return Optional.empty();

        return Optional.of(fid.get().ta);
    }

    /**
     * Finds a fidicial object in the array.
     * @return The fidicial, null if not found.
     */
    private Optional<LimelightTarget_Fiducial> findFid(int id) {
        for (LimelightTarget_Fiducial fid : inputs_.fiducials) {
            if (fid.fiducialID == (double) id) {
                return Optional.of(fid);
            }
        }

        return Optional.empty();
    }

}
