package frc.robot.util;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * Hardware Implementation Mapping for The Physical OI Panel
 */
public class OperatorInterface extends CommandGenericHID {

    public static class ButtonMap {
        public static final int cycleSpeaker = 1;
        public static final int cycleTrap = 2;
        public static final int climbPrepare = 3;
        public static final int climbExecute = 4;
        public static final int unclimb = 6;
        public static final int shoot = 7;
        public static final int turtle = 8;
        public static final int abort = 9;
        public static final int eject = 10;
        public static final int collect = 11;
        public static final int shootingPodium = 12;
        public static final int shootingSubwoofer = 13;
    }

    public static class Indicators {
        public static final int driveBaseReady = 1;
        public static final int shooterReady = 2;
        public static final int tiltReady = 3;
        public static final int aprilTagReady = 4;
        public static final int climbPrepareEnabled = 5;
        public static final int climbExecuteEnabled = 6;
        public static final int unclimbEnabled = 8;
    }

    public static enum CycleType {
        SPEAKER,
        AMP,
        TRAP
    }

    public static enum ShotType {
        PODIUM,
        AUTO,
        SUBWOOFER
    }
    
    public OperatorInterface(int port) {
        super(port);
    }
    
    /**
     * Abort Button Trigger
     * @return {@link Trigger} for the abort button.
     */
    public Trigger abort() {
        return button(ButtonMap.abort);
    }

    /**
     * Eject Button Trigger
     * @return {@link Trigger} for the unclimb button.
     */
    public Trigger eject() {
        return button(ButtonMap.eject);
    }

    /**
     * Turtle Button Trigger
     * @return {@link Trigger} for the unclimb button.
     */
    public Trigger turtle() {
        return button(ButtonMap.turtle);
    }

    /**
     * Climb Basic Button Trigger
     * @return {@link Trigger} for the Climb Basic Button
     */
    public Trigger climbBasic() {
        return button(ButtonMap.climbPrepare);
    }

    /**
     * Climbing Execute Button Trigger
     * @return {@link Trigger} for the Execute Button
     */
    public Trigger climbExecute() {
        return button(ButtonMap.climbExecute);
    }

    /**
     * Auto Amp/Trap Trigger
     * @return {@link Trigger} for the Auto Amp/Trap button.
     */
    public Trigger autoTramp() {
        return button(ButtonMap.unclimb);
    }

    /**
     * Shoot Button Trigger
     * @return {@link Trigger} for the shoot button.
     */
    public Trigger shoot() {
        return button(ButtonMap.shoot);
    }

    /**
     * Collect Button Trigger
     * @return {@link Trigger} for the intake collect button.
     */
    public Trigger collect() {
        return button(ButtonMap.collect);
    }

    /**
     * Cycle Mode Speaker Left Switch Trigger
     * @return {@link Trigger} fired when cycle mode is set to speaker.
     */
    public Trigger cycleSpeaker() {
        return button(ButtonMap.cycleSpeaker); // flipped to the left
    }

    /**
     * Cycle Mode Trap Right Switch Trigger
     * @return {@link Trigger} fired when cycle mode is set to trap.
     */
    public Trigger cycleTrap() {
        return button(ButtonMap.cycleTrap); // flipped to the right
    }

    /**
     * Cycle Mode Amp Default Middle Switch Trigger
     * @return {@link Trigger} fired when cycle mode is set to amp.
     */
    public Trigger cycleAmp() {
        return new Trigger(() -> !cycleSpeaker().getAsBoolean() && !cycleTrap().getAsBoolean()); // whenever its not flipped to left OR right :3 (in the middle)
    }

    /**
     * Shooting Mode Podium Left Switch Trigger
     * @return {@link Trigger} fired when shooting mode is set to podium.
     */
    public Trigger shotPodium() {
        return button(ButtonMap.shootingPodium);
    }

    /**
     * Shooting Mode Auto Middle Switch Trigger
     * @return {@link Trigger} fired when shooting mode is set to auto.
     */
    public Trigger shotAuto() {
        return new Trigger(() -> !shotPodium().getAsBoolean() && !shotSubwoofer().getAsBoolean());
    }

    /**
     * Shooting Mode Subwoofer Right Switch Trigger
     * @return  {@link Trigger} fired when shooting mode is set to subwoofer.
     */
    public Trigger shotSubwoofer() {
        return button(ButtonMap.shootingSubwoofer);
    }

    /**
     * Gets a supplier for the cycle switch state.
     * @return A supplier that gives the current state of the cycle type switch. (SPEAKER, AMP, TRAP)
     */
    public Supplier<CycleType> cycleTypeSupplier() {
        return () -> {
            if (cycleSpeaker().getAsBoolean())
                return CycleType.SPEAKER;

            if (cycleTrap().getAsBoolean())
                return CycleType.TRAP;

            return CycleType.AMP;
        };
    }

    /**
     * Gets a supplier for the shooting switch state.
     * @return A supplier that gives the current state of the shooting type switch. (PODIUM, AUTO, SUBWOOFER)
     */
    public Supplier<ShotType> shotTypeSupplier() {
        return () -> {
            if (shotPodium().getAsBoolean())
                return ShotType.PODIUM;

            if (shotSubwoofer().getAsBoolean())
                return ShotType.SUBWOOFER;

            return ShotType.AUTO;
        };
    }

    /**
     * Sets the state of an indicator light on the OI (buttons or top indicators).
     * @param index The light to set.
     * @param on The state to set it to.
     */
    public void setIndicator(int index, boolean on) {
        getHID().setOutput(index, on);
    }

}
