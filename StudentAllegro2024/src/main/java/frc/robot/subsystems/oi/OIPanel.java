package frc.robot.subsystems.oi;

import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants;

/** Add your docs here. */
public class OIPanel extends CommandGenericHID implements OIIO {

    public OIPanel(int port) {
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

    

}
