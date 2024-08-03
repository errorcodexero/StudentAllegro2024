package frc.robot.subsystems.Tramp.Commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Tramp.TrampSubsystem;
import frc.robot.subsystems.Tramp.TrampSubsystem.State;


public class PrepClimbCommand extends Command {

    private TrampSubsystem sub_;
    
    public PrepClimbCommand(TrampSubsystem sub){
        sub_ = sub;
    }

    @Override 
    public void initialize(){
        if(sub_.getState().equals(State.Idle)){
        sub_.setState(State.PrepClimb);
       }
    }

}