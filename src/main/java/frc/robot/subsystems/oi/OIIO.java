package frc.robot.subsystems.oi;

import java.util.function.Supplier;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.oi.type.ShootType;
import frc.robot.subsystems.oi.type.ActionType;

/**
 * OI layer interface for different hardware implementations to be used.
 * When extending this class, simply override all abstract methods to return a trigger to the desired button mapping.
 */
public interface OIIO {

    /**
     * The object with all of the logged inputs to AdvantageKit
     */
    @AutoLog // Automatically loads to and from logs during running, and replaying.
    public class OIIOInputs {
        public boolean abort = false;
        public boolean eject = false;
        public boolean turtle = false;
        public boolean climbPrepare = false;
        public boolean climbExecute = false;
        public boolean unclimb = false;
        public boolean shoot = false;
        public boolean collect = false;

        public boolean actionSpeaker = false;
        public boolean actionTrap = false;

        public boolean shootPodium = false;
        public boolean shootSubwoofer = false;
    }

    /**
     * The method to actually update the inputs to the correct values, must be called in the subsystem periodic method for correct logging.
     * @param inputs The inputs object to update.
     */
    public default void updateInputs(OIIOInputsAutoLogged inputs) {
        inputs.abort = abort().getAsBoolean();
        inputs.eject = eject().getAsBoolean();
        inputs.turtle = turtle().getAsBoolean();
        inputs.climbPrepare = climbPrepare().getAsBoolean();
        inputs.climbExecute = climbExecute().getAsBoolean();
        inputs.unclimb = unclimb().getAsBoolean();
        inputs.shoot = shoot().getAsBoolean();
        inputs.collect = collect().getAsBoolean();

        inputs.actionSpeaker = actionSpeaker().getAsBoolean();
        inputs.actionTrap = actionTrap().getAsBoolean();
        
        inputs.shootPodium = shootPodium().getAsBoolean();
        inputs.shootSubwoofer = shootSubwoofer().getAsBoolean();

        // Other two not included here because they technically dont count as 'inputs' but 'outputs' in the current model.
    }

    public abstract Trigger abort();
    public abstract Trigger eject();
    public abstract Trigger turtle();
    public abstract Trigger climbPrepare();
    public abstract Trigger climbExecute();
    public abstract Trigger unclimb();
    public abstract Trigger shoot();
    public abstract Trigger collect();

    public abstract Trigger actionSpeaker();
    public abstract Trigger actionTrap();
    public abstract Trigger actionAmp();

    public abstract Trigger shootPodium();
    public abstract Trigger shootAuto();
    public abstract Trigger shootSubwoofer();

    /**
     * Gets a supplier for the action switch state.
     * @return A supplier that gives the current state of the action type switch. (PODIUM, AUTO, SUBWOOFER)
     */
    public abstract Supplier<ActionType> actionTypeSupplier();

    /**
     * Gets a supplier for the shooting switch state.
     * @return A supplier that gives the current state of the shooting type switch. (SPEAKER, AMP, TRAP)
     */
    public abstract Supplier<ShootType> shootTypeSupplier();

    public abstract void setIndicator(int index, boolean on);

}