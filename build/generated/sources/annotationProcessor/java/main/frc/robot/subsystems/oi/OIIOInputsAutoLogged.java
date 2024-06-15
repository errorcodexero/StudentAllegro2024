package frc.robot.subsystems.oi;

import java.lang.Cloneable;
import java.lang.Override;
import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class OIIOInputsAutoLogged extends OIIO.OIIOInputs implements LoggableInputs, Cloneable {
  @Override
  public void toLog(LogTable table) {
    table.put("Abort", abort);
    table.put("Eject", eject);
    table.put("Turtle", turtle);
    table.put("ClimbPrepare", climbPrepare);
    table.put("ClimbExecute", climbExecute);
    table.put("Unclimb", unclimb);
    table.put("Shoot", shoot);
    table.put("Collect", collect);
    table.put("ActionSpeaker", actionSpeaker);
    table.put("ActionTrap", actionTrap);
    table.put("ShootPodium", shootPodium);
    table.put("ShootSubwoofer", shootSubwoofer);
  }

  @Override
  public void fromLog(LogTable table) {
    abort = table.get("Abort", abort);
    eject = table.get("Eject", eject);
    turtle = table.get("Turtle", turtle);
    climbPrepare = table.get("ClimbPrepare", climbPrepare);
    climbExecute = table.get("ClimbExecute", climbExecute);
    unclimb = table.get("Unclimb", unclimb);
    shoot = table.get("Shoot", shoot);
    collect = table.get("Collect", collect);
    actionSpeaker = table.get("ActionSpeaker", actionSpeaker);
    actionTrap = table.get("ActionTrap", actionTrap);
    shootPodium = table.get("ShootPodium", shootPodium);
    shootSubwoofer = table.get("ShootSubwoofer", shootSubwoofer);
  }

  public OIIOInputsAutoLogged clone() {
    OIIOInputsAutoLogged copy = new OIIOInputsAutoLogged();
    copy.abort = this.abort;
    copy.eject = this.eject;
    copy.turtle = this.turtle;
    copy.climbPrepare = this.climbPrepare;
    copy.climbExecute = this.climbExecute;
    copy.unclimb = this.unclimb;
    copy.shoot = this.shoot;
    copy.collect = this.collect;
    copy.actionSpeaker = this.actionSpeaker;
    copy.actionTrap = this.actionTrap;
    copy.shootPodium = this.shootPodium;
    copy.shootSubwoofer = this.shootSubwoofer;
    return copy;
  }
}
