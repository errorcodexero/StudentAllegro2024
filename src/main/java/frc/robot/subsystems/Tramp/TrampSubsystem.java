

package frc.robot.subsystems.Tramp;

import org.littletonrobotics.junction.Logger;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TrampSubsystem extends SubsystemBase {
  
    private final TrampSubsystemIO io; 
    private final TrampSubsystemIOInputsAutoLogged inputs = new TrampSubsystemIOInputsAutoLogged();

 /** Creates a new Tramp. */
    public TrampSubsystem(TrampSubsystemIO io) {
      this.io = io;
  }

  @Override
  public void periodic() {
      io.updateInputs(inputs);
      Logger.processInputs("TrampSubsystem", inputs);
}
}

