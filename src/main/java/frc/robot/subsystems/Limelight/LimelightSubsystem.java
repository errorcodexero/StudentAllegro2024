package frc.robot.subsystems.Limelight;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LimelightSubsystem extends SubsystemBase {

    private final LimelightIO io_;
    private final LimelightIOInputsAutoLogged inputs_;

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
        inputs_ = new LimelightIOInputsAutoLogged();
    }

    @Override
    public void periodic() {
        io_.updateState();
        io_.updateInputs(inputs_);

        Logger.processInputs(getName(), inputs_);
    }
    
    /**
     * Sets the Limelight indicator LED back to the default state, controlled by the limelight software itself.
     */
    public void resetLed() {
        io_.resetLed();
    }

    /**
     * Forces the Limelight indicator LED to be off.
     */
    public void forceLedOff() {
        io_.forceOff();;
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
     * Figures out if its currently seeing a specific april tag.
     * @param id The id of the april tag.
     * @return Whether or not the Limelight can currently see the specified april tag.
     */
    public boolean hasAprilTag(int id) {
        return io_.hasAprilTag(id);
    }
    
}
