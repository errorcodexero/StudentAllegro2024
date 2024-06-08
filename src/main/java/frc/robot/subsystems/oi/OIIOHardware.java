package frc.robot.subsystems.oi;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants;
import frc.robot.subsystems.oi.type.ActionType;
import frc.robot.subsystems.oi.type.ShootType;

/**
 * Hardware Implementation Mapping for The Physical OI Panel
 */
public class OIIOHardware extends CommandGenericHID implements OIIO {
    
    public OIIOHardware(int port) {
        super(port);
    }
    
    @Override
    public Trigger abort() {
        return button(OIConstants.Buttons.abort);
    }
    
    @Override
    public Trigger eject() {
        return button(OIConstants.Buttons.eject);
    }
    
    @Override
    public Trigger turtle() {
        return button(OIConstants.Buttons.turtle);
    }
    
    @Override
    public Trigger climbPrepare() {
        return button(OIConstants.Buttons.climbPrepare);
    }
    
    @Override
    public Trigger climbExecute() {
        return button(OIConstants.Buttons.climbExecute);
    }

    @Override
    public Trigger unclimb() {
        return button(OIConstants.Buttons.unclimb);
    }

    @Override
    public Trigger shoot() {
        return button(OIConstants.Buttons.shoot);
    }

    @Override
    public Trigger collect() {
        return button(OIConstants.Buttons.collect);
    }

    @Override
    public Trigger actionSpeaker() {
        return button(OIConstants.Buttons.cycleSpeaker); // flipped to the left
    }

    @Override
    public Trigger actionTrap() {
        return button(OIConstants.Buttons.cycleTrap); // flipped to the right
    }

    @Override
    public Trigger actionAmp() {
        return new Trigger(() -> !actionSpeaker().getAsBoolean() && !actionTrap().getAsBoolean()); // whenever its not flipped to left OR right :3 (in the middle)
    }

    @Override
    public Trigger shootPodium() {
        return button(OIConstants.Buttons.shootingPodium);
    }

    @Override
    public Trigger shootAuto() {
        return new Trigger(() -> !shootPodium().getAsBoolean() && !shootSubwoofer().getAsBoolean());
    }

    @Override
    public Trigger shootSubwoofer() {
        return button(OIConstants.Buttons.shootingSubwoofer);
    }

    @Override
    public Supplier<ActionType> actionTypeSupplier() {
        return () -> {
            if (actionSpeaker().getAsBoolean())
                return ActionType.SPEAKER;

            if (actionTrap().getAsBoolean())
                return ActionType.TRAP;

            return ActionType.AMP;
        };
    }

    @Override
    public Supplier<ShootType> shootTypeSupplier() {
        return () -> {
            if (shootPodium().getAsBoolean())
                return ShootType.PODIUM;

            if (shootSubwoofer().getAsBoolean())
                return ShootType.SUBWOOFER;

            return ShootType.AUTO;
        };
    }

    @Override
    public void setIndicator(int index, boolean on) {
        getHID().setOutput(index, on);
    }

}
