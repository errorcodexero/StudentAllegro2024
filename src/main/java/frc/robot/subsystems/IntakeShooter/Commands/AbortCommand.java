package frc.robot.subsystems.IntakeShooter.Commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeShooter.IntakeShooterSubsystem;

@Deprecated
public class AbortCommand extends Command {

    private IntakeShooterSubsystem sub_;
    
    public AbortCommand(IntakeShooterSubsystem sub){
        sub_ = sub;
    }

    @Override
    public void initialize(){
        sub_.abort();
    }
}
