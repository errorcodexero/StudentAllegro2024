package frc.robot.subsystems.oi;

import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.oi.type.ActionType;
import frc.robot.subsystems.oi.type.ShootType;

public class OISubsystem extends SubsystemBase {

  private final OIIO io;
  private final OIIOInputsAutoLogged inputs = new OIIOInputsAutoLogged();

  private Supplier<ActionType> actionType;
  private Supplier<ShootType> shootType;

  /**
   * Creates a new OI.
   * @param io The IO layer implementation to use for the subsystem.
   */
  public OISubsystem(OIIO io) {
    this.io = io;
    this.actionType = io.actionTypeSupplier();
    this.shootType = io.shootTypeSupplier();
  }

  /**
   * Creates a new OI at a port number.
   * @param port The usb port the OI is plugged into.
   */
  public OISubsystem(int port) {
    this(new OIIOHardware(port));
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs(getName(), inputs);

    // Logging the current type of things, EXPERIMENTAL, WILL FINALIZE SOON
    Logger.recordOutput("ActionThype", actionType.get());
    Logger.recordOutput("CycleType", shootType.get());
  }

  /**
   * Abort Button Trigger
   * @return {@link Trigger} for the abort button.
   */
  public Trigger abort() {
    return io.abort();
  }

  /**
   * Eject Button Trigger
   * @return {@link Trigger} for the unclimb button.
   */
  public Trigger eject() {
    return io.eject();
  }

  /**
   * Turtle Button Trigger
   * @return {@link Trigger} for the unclimb button.
   */
  public Trigger turtle() {
    return io.turtle();
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
   * Unclumb Button Trigger
   * @return {@link Trigger} for the unclimb button.
   * @deprecated This button isnt currently used on the robot.
   */
  @Deprecated
  public Trigger unclimb() {
    return io.unclimb();
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
   * Cycle Mode Speaker Left Switch Trigger
   * @return {@link Trigger} fired when cycle mode is set to speaker.
   */
  public Trigger cycleSpeaker() {
      return io.actionSpeaker();
  }

  /**
   * Cycle Mode Trap Right Switch Trigger
   * @return {@link Trigger} fired when cycle mode is set to trap.
   */
  public Trigger cycleTrap() {
      return io.actionTrap();
  }

  /**
   * Cycle Mode Amp Default Middle Switch Trigger
   * @return {@link Trigger} fired when cycle mode is set to amp.
   */
  public Trigger cycleAmp() {
      return io.actionAmp();
  }

  /**
   * Shooting Mode Podium Left Switch Trigger
   * @return {@link Trigger} fired when shooting mode is set to podium.
   */
  public Trigger shootPodium() {
      return io.shootPodium();
  }

  /**
   * Shooting Mode Auto Middle Switch Trigger
   * @return {@link Trigger} fired when shooting mode is set to auto.
   */
  public Trigger shootAuto() {
      return io.shootAuto();
  }

  /**
   * Shooting Mode Subwoofer Right Switch Trigger
   * @return  {@link Trigger} fired when shooting mode is set to subwoofer.
   */
  public Trigger shootSubwoofer() {
      return io.shootSubwoofer();
  }

  /**
   * Gets a supplier for the action switch state.
   * @return A supplier that gives the current state of the action type switch. (SPEAKER, AMP, TRAP)
   */
  public Supplier<ActionType> actionTypeSupplier() {
    return actionType;
  }

  /**
   * Gets a supplier for the shooting switch state.
   * @return A supplier that gives the current state of the shooting type switch. (PODIUM, AUTO, SUBWOOFER)
   */
  public Supplier<ShootType> shootTypeSupplier() {
    return shootType;
  }

  /**
   * Sets the state of an indicator light on the top of the OI.
   * @param index The light to set.
   * @param on The state to set it to.
   */
  public void setIndicator(int index, boolean on) {
    io.setIndicator(index, on);
  }
}
