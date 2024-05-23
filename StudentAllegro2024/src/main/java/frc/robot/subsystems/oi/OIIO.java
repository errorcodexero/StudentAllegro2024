// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.oi;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.oi.type.CycleType;
import frc.robot.subsystems.oi.type.ShootingType;

/**
 * OI layer interface for different hardware implementations to be used.
 * When extending this class, simply override all abstract methods to return a trigger to the desired button mapping.
 */
public interface OIIO {

    @AutoLog
    public class OIIOInputs {
        public boolean shoot = false;
        public boolean collect = false;
        public boolean unclimb = false;
        public boolean climbExecute = false;
        public boolean climbPrepare = false;
        public boolean abort = false;
        public boolean eject = false;
        public boolean turtle = false;

        public boolean cycleSpeaker = false;
        public boolean cycleAmp = false;

        public boolean shootPodium = false;
        public boolean shootSubwoofer = false;
    }

    public default void updateInputs(OIIOInputsAutoLogged inputs) {
        inputs.abort = abort().getAsBoolean();
        inputs.shoot = shoot().getAsBoolean();
        inputs.collect = collect().getAsBoolean();
        inputs.unclimb = unclimb().getAsBoolean();
        inputs.climbPrepare = climbPrepare().getAsBoolean();
        inputs.climbExecute = climbExecute().getAsBoolean();
        inputs.eject = eject().getAsBoolean();
        inputs.turtle = turtle().getAsBoolean();
        
        inputs.shootPodium = shootPodium().getAsBoolean();
        inputs.shootSubwoofer = shootSubwoofer().getAsBoolean();
        inputs.cycleAmp = cycleAmp().getAsBoolean();
        inputs.cycleSpeaker = cycleSpeaker().getAsBoolean();
        // Other two not included here because they technically dont count as 'inputs' but 'outputs' in the current model.
    }

    public abstract Trigger shoot();
    public abstract Trigger collect();
    public abstract Trigger unclimb();
    public abstract Trigger climbPrepare();
    public abstract Trigger climbExecute();
    public abstract Trigger abort();
    public abstract Trigger eject();
    public abstract Trigger turtle();

    public abstract Trigger cycleSpeaker();
    public abstract Trigger cycleTrap();
    public abstract Trigger cycleAmp();

    public abstract Trigger shootPodium();
    public abstract Trigger shootAuto();
    public abstract Trigger shootSubwoofer();

    /**
     * Gets the current type of shooting from the bottom switch.
     * @return PODIUM if set to left, AUTO if set to middle, SUBWOOFER if set to the right.
     * @deprecated Must coordinate with the team as to how this should be implemened.
     */
    @Deprecated
    public default ShootingType getShootingType() {
        if (shootPodium().getAsBoolean())
            return ShootingType.PODIUM; //  (left)

        if (shootSubwoofer().getAsBoolean())
            return ShootingType.SUBWOOFER; // (right)

        return ShootingType.AUTO;
    }

    /**
     * Gets the current set cycle type from the top switch.
     * @return SPEAKER if set to left, AMP if set to middle, TRAP if set to the right.
     * @deprecated Must coordinate with the team as to how this should be implemened.
     */
    @Deprecated
    public default CycleType getCycleType() {
        if (cycleSpeaker().getAsBoolean())
            return CycleType.SPEAKER; // speaker position (left)

        if (cycleTrap().getAsBoolean())
            return CycleType.TRAP; // trap position (right)

        return CycleType.AMP; // default middle position
    }

}
