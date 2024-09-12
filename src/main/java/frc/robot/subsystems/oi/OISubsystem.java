package frc.robot.subsystems.oi;

import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.oi.type.ActionType;
import frc.robot.subsystems.oi.type.ShootType;

/**
 * Represents the Operator Interface subsystem, which handles input from
 * the operator interface and manages corresponding actions and indicators.
 */
public class OISubsystem extends SubsystemBase {

    private final OIIO io_;
    private final OIIOInputsAutoLogged inputs_ = new OIIOInputsAutoLogged();

    private final Supplier<ActionType> actionType_;
    private final Supplier<ShootType> shootType_;

    // Stores the current note destination
    private NoteDestination currentNoteDestination;

    /**
     * Creates a new OISubsystem.
     * @param io The IO layer implementation to use for the subsystem.
     */
    public OISubsystem(OIIO io) {
        io_ = io;
        actionType_ = io.actionTypeSupplier();
        shootType_ = io.shootTypeSupplier();
        currentNoteDestination = NoteDestination.ManualSpeaker; // Default destination
    }

    /**
     * Creates a new OISubsystem at a port number.
     * @param port The USB port the OI is plugged into.
     */
    public OISubsystem(int port) {
        this(new OIIOHardware(port));
    }

    @Override
    public void periodic() {
        io_.updateInputs(inputs_);
        Logger.processInputs(getName(), inputs_);

        // Logging the current type of things, EXPERIMENTAL, WILL FINALIZE SOON
        Logger.recordOutput("ActionType", actionType_.get());
        Logger.recordOutput("CycleType", shootType_.get());
    }

    /**
     * Abort Button Trigger.
     * @return {@link Trigger} for the abort button.
     */
    public Trigger abort() {
        return io_.abort();
    }

    /**
     * Eject Button Trigger.
     * @return {@link Trigger} for the eject button.
     */
    public Trigger eject() {
        return io_.eject();
    }

    /**
     * Turtle Button Trigger.
     * @return {@link Trigger} for the turtle button.
     */
    public Trigger turtle() {
        return io_.turtle();
    }

    /**
     * Climbing Prepare Button Trigger.
     * @return {@link Trigger} for the prepare button.
     */
    public Trigger climbPrepare() {
        return io_.climbPrepare();
    }

    /**
     * Climbing Execute Button Trigger.
     * @return {@link Trigger} for the execute button.
     */
    public Trigger climbExecute() {
        return io_.climbExecute();
    }

    /**
     * Unclimb Button Trigger.
     * @return {@link Trigger} for the unclimb button.
     * @deprecated This button isn't currently used on the robot.
     */
    @Deprecated
    public Trigger unclimb() {
        return io_.unclimb();
    }

    /**
     * Shoot Button Trigger.
     * @return {@link Trigger} for the shoot button.
     */
    public Trigger shoot() {
        return io_.shoot();
    }

    /**
     * Collect Button Trigger.
     * @return {@link Trigger} for the intake collect button.
     */
    public Trigger collect() {
        return io_.collect();
    }

    /**
     * Cycle Mode Speaker Left Switch Trigger.
     * @return {@link Trigger} fired when cycle mode is set to speaker.
     * @deprecated Might break in the future!
     */
    @Deprecated
    public Trigger cycleSpeaker() {
        return io_.actionSpeaker();
    }

    /**
     * Cycle Mode Trap Right Switch Trigger.
     * @return {@link Trigger} fired when cycle mode is set to trap.
     * @deprecated Might break in the future!
     */
    @Deprecated
    public Trigger cycleTrap() {
        return io_.actionTrap();
    }

    /**
     * Cycle Mode Amp Default Middle Switch Trigger.
     * @return {@link Trigger} fired when cycle mode is set to amp.
     * @deprecated Might break in the future!
     */
    @Deprecated
    public Trigger cycleAmp() {
        return io_.actionAmp();
    }

    /**
     * Shooting Mode Podium Left Switch Trigger.
     * @return {@link Trigger} fired when shooting mode is set to podium.
     * @deprecated Might break in the future!
     */
    @Deprecated
    public Trigger shootPodium() {
        return io_.shootPodium();
    }

    /**
     * Shooting Mode Auto Middle Switch Trigger.
     * @return {@link Trigger} fired when shooting mode is set to auto.
     * @deprecated Might break in the future!
     */
    @Deprecated
    public Trigger shootAuto() {
        return io_.shootAuto();
    }

    /**
     * Shooting Mode Subwoofer Right Switch Trigger.
     * @return {@link Trigger} fired when shooting mode is set to subwoofer.
     * @deprecated Might break in the future!
     */
    @Deprecated
    public Trigger shootSubwoofer() {
        return io_.shootSubwoofer();
    }

    /**
     * Gets a supplier for the action switch state.
     * @return A supplier that gives the current state of the action type switch.
     */
    public Supplier<ActionType> actionTypeSupplier() {
        return actionType_;
    }

    /**
     * Gets a supplier for the shooting switch state.
     * @return A supplier that gives the current state of the shooting type switch.
     */
    public Supplier<ShootType> shootTypeSupplier() {
        return shootType_;
    }

    /**
     * Sets the state of an indicator light on the OI (buttons or top indicators).
     * @param index The light to set.
     * @param on The state to set it to.
     */
    public void setIndicator(int index, boolean on) {
        io_.setIndicator(index, on);
    }

    /**
     * Sets the note destination for the OI subsystem.
     * @param destination The destination to set.
     */
    public void setNoteDestination(NoteDestination destination) {
        this.currentNoteDestination = destination;
        System.out.println("Note destination set to: " + destination);
    }
}
