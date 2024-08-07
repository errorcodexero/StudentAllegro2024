package frc.robot.commands.TestCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Tramp.TrampSubsystem;

/** An example command that uses an example subsystem. */
public class ArmPosCommand extends Command {

    private TrampSubsystem trampSubsystem_; 
    private double deg_; 

  public ArmPosCommand(TrampSubsystem trampSubsystem, double deg) {
    trampSubsystem_ = trampSubsystem;
    deg_ = deg;
   
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(trampSubsystem_);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    trampSubsystem_.setArmPosition(deg_);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
