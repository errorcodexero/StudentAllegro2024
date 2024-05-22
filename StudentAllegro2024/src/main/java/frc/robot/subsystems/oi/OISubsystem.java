package frc.robot.subsystems.oi;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class OISubsystem extends SubsystemBase {

  private final OIIO io;
  private final OIIOInputsAutoLogged inputs = new OIIOInputsAutoLogged();

  /** Creates a new OISubsystem. */
  public OISubsystem(OIIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs(getName(), inputs);
  }

  public Trigger abort() {
    return io.abort();
  }

  public Trigger shoot() {
    return io.shoot();
  }

  public Trigger collect() {
    return io.collect();
  }

  public Trigger unclimb() {
    return io.unclimb();
  }

  public Trigger prepare() {
    return io.climbPrepare();
  }

  public Trigger execute() {
    return io.climbExecute();
  }
}
