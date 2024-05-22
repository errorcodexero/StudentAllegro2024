package frc.robot.subsystems.oi;

import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants;

/** Add your docs here. */
public class OIIOHardware extends CommandGenericHID implements OIIO {

    public OIIOHardware(int port) {
        super(port);
    }

    @Override
    public void updateInputs(OIIOInputsAutoLogged inputs) {
        inputs.abort = abort().getAsBoolean();
        inputs.shoot = shoot().getAsBoolean();
        inputs.collect = collect().getAsBoolean();
        inputs.unclimb = unclimb().getAsBoolean();
        inputs.climbPrepare = climbPrepare().getAsBoolean();
        inputs.climbExecute = climbExecute().getAsBoolean();
        inputs.eject = eject().getAsBoolean();
        inputs.turtle = eject().getAsBoolean();
        
        inputs.shootPodium = shootPodium().getAsBoolean();
        inputs.shootSubwoofer = shootSubwoofer().getAsBoolean();
        inputs.cycleAmp = cycleAmp().getAsBoolean();
        inputs.cycleSpeaker = cycleSpeaker().getAsBoolean();
    }

    @Override
    public Trigger abort() {
        return button(Constants.OI.Buttons.abort);
    }

    @Override
    public Trigger shoot() {
        return button(Constants.OI.Buttons.shoot);
    }

    @Override
    public Trigger collect() {
        return button(Constants.OI.Buttons.collect);
    }

    @Override
    public Trigger unclimb() {
        return button(Constants.OI.Buttons.unclimb);
    }

    @Override
    public Trigger climbPrepare() {
        return button(Constants.OI.Buttons.climbPrepare);
    }

    @Override
    public Trigger climbExecute() {
        return button(Constants.OI.Buttons.climbExecute);
    }

    @Override
    public Trigger eject() {
        return button(Constants.OI.Buttons.eject);
    }

    @Override
    public Trigger turtle() {
        return button(Constants.OI.Buttons.turtle);
    }

    @Override
    public Trigger cycleSpeaker() {
        return button(Constants.OI.Buttons.cycleSpeaker); // flipped to the left
    }

    @Override
    public Trigger cycleTrap() {
        return button(Constants.OI.Buttons.cycleTrap); // flipped to the right
    }

    @Override
    public Trigger cycleAmp() {
        return new Trigger(() -> !cycleSpeaker().getAsBoolean() && !cycleTrap().getAsBoolean()); // whenever its not flipped to left OR right :3 (in the middle)
    }

    @Override
    public Trigger shootPodium() {
        return button(Constants.OI.Buttons.shootingPodium);
    }

    @Override
    public Trigger shootAuto() {
        return new Trigger(() -> !shootPodium().getAsBoolean() && !shootSubwoofer().getAsBoolean());
    }

    @Override
    public Trigger shootSubwoofer() {
        return button(Constants.OI.Buttons.shootingSubwoofer);
    }

    @Override
    public ShootingType getShootingType() {
        if (shootPodium().getAsBoolean())
            return ShootingType.PODIUM; //  (left)

        if (shootSubwoofer().getAsBoolean())
            return ShootingType.SUBWOOFER; // (right)

        return ShootingType.AUTO;
    }

    @Override
    public CycleType getCycleType() {
        if (cycleSpeaker().getAsBoolean())
            return CycleType.SPEAKER; // speaker position (left)

        if (cycleTrap().getAsBoolean())
            return CycleType.TRAP; // trap position (right)

        return CycleType.AMP; // default middle position
    }

    

}
