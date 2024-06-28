package frc.robot.subsystems.IntakeShooter.Commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeShooter.IntakeShooterSubsystem;
import frc.robot.subsystems.IntakeShooter.IntakeShooterSubsystem.State;

@Deprecated
public class IntakeCommand extends Command {

    private IntakeShooterSubsystem sub_;
    
    public IntakeCommand(IntakeShooterSubsystem sub){
        sub_ = sub;
    }

    @Override
    public void initialize(){
        if(sub_.getState().equals(State.Idle)){
            sub_.setState(State.Intake);
        }
    }
}
