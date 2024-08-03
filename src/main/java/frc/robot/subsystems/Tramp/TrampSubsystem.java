

package frc.robot.subsystems.Tramp;



import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TrampSubsystem extends SubsystemBase {
    public enum State {
        Idle,
        PrepClimb,
        Climbing,
    }


    private enum ClimbingState {
        Idle,
        IsClimbReady,
        Climbing
    }
  
    private final TrampSubsystemIO io;
    private State state_; 
    private ClimbingState climbingState_;
    private final TrampSubsystemIOInputsAutoLogged inputs = new TrampSubsystemIOInputsAutoLogged();

 /** Creates a new Tramp. */
    public TrampSubsystem(TrampSubsystemIO io) {
        this.io = io;
        state_ = State.Idle;
        climbingState_ = ClimbingState.IsClimbReady;
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("TrampSubsystem", inputs);

        switch (state_) {
            case Idle:
                break;

            case Climbing:
                climbPeriodic();
                break;
            
            case PrepClimb:
                prepClimbPeriodic();
                break;

            default:
                state_ = State.Idle;
                break;
        }
    }

    
    public State getState(){
        return state_;
    }

    
    public void setState(State state){
        state_ = state;
    }

    public void climbPeriodic(){
        boolean climbReady_ = false;
        switch (climbingState_){
            case Idle:
                break;
            
            case IsClimbReady:
                if (io.getElevatorPosition() == TrampConstants.Elevator.trapTarget &
                    io.getArmPosition() == TrampConstants.Arm.trapTarget &
                    inputs.armVelocity < 0.01 &
                    inputs.elevatorVelocity < 0.01 &
                    io.getClimberPosition() == 1.0){

                    climbReady_ = true;
                    climbingState_ = ClimbingState.Climbing;
                }
                break;

            case Climbing:
                if (climbReady_ == true){
                    io.setClimberPosition(TrampConstants.Climber.ClimbTarget);
                    Commands.waitSeconds(1);
                    climbingState_ = ClimbingState.Idle;
                }
                break;
            
        }
    }

    public void prepClimbPeriodic(){
        if (io.getElevatorPosition() == TrampConstants.Elevator.trapTarget &
            io.getArmPosition() == TrampConstants.Arm.trapTarget &
            inputs.armVelocity < 0.01 &
            inputs.elevatorVelocity < 0.01){
                io.setClimberPosition(TrampConstants.Climber.PrepClimbTarget);
        }
    }


    public Command PrepClimbCommand(){
        return new Command() {
            @Override
            public void initialize() {
                super.initialize();
                if(state_.equals(State.Idle)){
                    state_ = State.PrepClimb;
                }
            }
        };
    }
    
    public Command ClimbCommand(){
        return new Command() {
            @Override
            public void initialize() {
                super.initialize();
                if(state_.equals(State.PrepClimb)){
                    state_ = State.Climbing;
                }
            }
        };   
    }

}

