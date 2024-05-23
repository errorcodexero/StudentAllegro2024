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

    // Logging the current type of things, EXPERIMENTAL, WILL FINALIZE SOON
    Logger.recordOutput("ShootingType", io.getShootingType());
    Logger.recordOutput("CycleType", io.getCycleType());
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
  @Deprecated
  public Trigger unclimb() {
    return io.unclimb();
  }

  /**
   * Turtle Button Trigger
   * @return {@link Trigger} for the unclimb button.
   */
  public Trigger turtle() {
    return io.turtle();
  }

  /**
   * Eject Button Trigger
   * @return {@link Trigger} for the unclimb button.
   */
  public Trigger eject() {
    return io.eject();
  }

    /**
   * Abort Button Trigger
   * @return {@link Trigger} for the abort button.
   */
  public Trigger abort() {
    return io.abort();
  }

  /**
   * Climbing Prepare Button Trigger
   * @return {@link Trigger} for the Prepare Button
   */
  public Trigger climbPrepare() {
    return io.climbPrepare();
  }

  /**
   * Climbing Execute Button Trigger
   * @return {@link Trigger} for the Execute Button
   */
  public Trigger climbExecute() {
    return io.climbExecute();
  }

  /**
   * Cycle Mode Speaker Left Switch Trigger
   * @return {@link Trigger} fired when cycle mode is set to speaker.
   * @deprecated Must coordinate with the team as to how this should be implemened.
   */
  @Deprecated
  public Trigger cycleSpeaker() {
      return io.cycleSpeaker();
  }

  /**
   * Cycle Mode Trap Right Switch Trigger
   * @return {@link Trigger} fired when cycle mode is set to trap.
   * @deprecated Must coordinate with the team as to how this should be implemened.
   */
  @Deprecated
  public Trigger cycleTrap() {
      return io.cycleTrap();
  }

  /**
   * Cycle Mode Amp Default Middle Switch Trigger
   * @return {@link Trigger} fired when cycle mode is set to amp.
   * @deprecated Must coordinate with the team as to how this should be implemened.
   */
  @Deprecated
  public Trigger cycleAmp() {
      return io.cycleAmp();
  }

  /**
   * Shooting Mode Podium Left Switch Trigger
   * @return {@link Trigger} fired when shooting mode is set to podium.
   * @deprecated Must coordinate with the team as to how this should be implemened.
   */
  @Deprecated
  public Trigger shootPodium() {
      return io.shootPodium();
  }

  /**
   * Shooting Mode Auto Middle Switch Trigger
   * @return {@link Trigger} fired when shooting mode is set to auto.
   * @deprecated Must coordinate with the team as to how this should be implemened.
   */
  @Deprecated
  public Trigger shootAuto() {
      return io.shootAuto();
  }

  /**
   * Shooting Mode Subwoofer Right Switch Trigger
   * @return  {@link Trigger} fired when shooting mode is set to subwoofer.
   * @deprecated Must coordinate with the team as to how this should be implemened.
   */
  @Deprecated
  public Trigger shootSubwoofer() {
      return io.shootSubwoofer();
  }
}
