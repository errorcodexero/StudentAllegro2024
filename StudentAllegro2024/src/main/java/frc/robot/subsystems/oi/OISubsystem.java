package frc.robot.subsystems.oi;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class OISubsystem extends SubsystemBase {

  private final OIIO io;
  private final OIIOInputsAutoLogged inputs = new OIIOInputsAutoLogged();

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs(getName(), inputs);
  }

  /** Creates a new OISubsystem. */
  /**
   * Creates a new OI.
   * @param io The IO layer implementation to use for the subsystem.
   */
  public OISubsystem(OIIO io) {
    this.io = io;
  }

  /**
   * Creates a new OI at a port number.
   * @param port The usb port the OI is plugged into.
   */
  public OISubsystem(int port) {
    this(new OIIOHardware(port));
  }

  /**
   * Abort Button Trigger
   * @return {@link Trigger} for the abort button.
   */
  public Trigger abort() {
    return io.abort();
  }

  /**
   * Shoot Button Trigger
   * @return {@link Trigger} for the shoot button.
   */
  public Trigger shoot() {
    return io.shoot();
  }

  /**
   * Collect Button Trigger
   * @return {@link Trigger} for the intake collect button.
   */
  public Trigger collect() {
    return io.collect();
  }

  /**
   * Unclumb Button Trigger
   * @return {@link Trigger} for the unclimb button.
   * @deprecated This button isnt currently used on the robot.
   */
  public Trigger unclimb() {
    return io.unclimb();
  }

  /**
   * Climbing Prepare Button Trigger
   * @return {@link Trigger} for the Prepare Button
   */
  public Trigger prepare() {
    return io.climbPrepare();
  }

  /**
   * Climbing Execute Button Trigger
   * @return {@link Trigger} for the Execute Button
   */
  public Trigger execute() {
    return io.climbExecute();
  }
}
