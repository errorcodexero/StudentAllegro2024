package frc.robot.subsystems.IntakeShooter.Commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeShooter.IntakeShooterSubsystem;
import frc.robot.subsystems.IntakeShooter.IntakeShooterSubsystem.State;

@Deprecated
public class EjectCommand extends Command {

    private IntakeShooterSubsystem sub_;
    
    public EjectCommand(IntakeShooterSubsystem sub){
        sub_ = sub;
    }

    @Override
    public void initialize(){
        sub_.setState(State.Eject);
    }
}
