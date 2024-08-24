package frc.robot.subsystems.Tramp;

import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.oi.type.ActionType;

public class TrampSubsystem extends SubsystemBase {
    public enum State {
        Idle,
        PrepClimb,
        Climbing,
        StartShoot,
        Shooting,
        AfterShootingInTrap,
        MovingToTransfer,
        Tranferring,
        PrepAmp,

        // Place Holder for goTo like Action
        KeepOutZoneCode,
        
        PrepTrap,
        MovingNote,
        WaitingAfterTransfer,
        Stowing,
        Ejecting
    }


    private enum ClimbingState {
        Idle,
        IsClimbReady,
        Climbing
    }

    private enum TransferState {
        Idle,
        WaitingForNote, 
        ExitTransfer
    }

    private enum AfterTrapShootState {
        Idle,
        Trap1,
        Trap2,
    } 
  
    private final TrampSubsystemIO io;
    private State state_; 
    private ClimbingState climbingState_;
    private TransferState transferState_;
    private AfterTrapShootState afterTrapShootState_;  
    private Supplier<ActionType> actionType_;
    private boolean climbReady_;
    private boolean climbDone_; 
    private boolean hasNote_;
    private final TrampSubsystemIOInputsAutoLogged inputs = new TrampSubsystemIOInputsAutoLogged();

 /** Creates a new Tramp. */
    public TrampSubsystem(TrampSubsystemIO io, Supplier<ActionType> ActionType) {
        this.io = io;
        state_ = State.Idle;
        climbingState_ = ClimbingState.IsClimbReady;
        transferState_ = TransferState.WaitingForNote;
        actionType_ = ActionType;
        climbReady_ = false;
        climbDone_ = false;
        hasNote_ = false; 
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("TrampSubsystem", inputs);

        switch (state_) {
            case Idle:
                break;

            case KeepOutZoneCode:
                break;

            case Climbing:
                climbPeriodic();
                break;
            
            case PrepClimb:
                prepClimbPeriodic();
                break;

            case Shooting:
                shootingPeriodic(climbDone_);
                if (actionType_.get() == ActionType.AMP){
                    state_ = State.Stowing;
                }
                break;

            case WaitingAfterTransfer:
                waitingPeriodic();
                break;

            case Tranferring:
                transferringPeriodic();
                if (actionType_.get() == ActionType.AMP){
                    state_ = State.PrepAmp;
                }
                else if (actionType_.get() == ActionType.TRAP){
                    state_ = State.PrepTrap;
                }
                break;

            case PrepAmp:
                prepAmpPeriodic();
                state_ = State.WaitingAfterTransfer;
                break;

            case PrepTrap:
                prepTrapPeriodic();
                state_ = State.KeepOutZoneCode;
                break;

            case Stowing:
                stowingPeriodic();
                break;

            case Ejecting:
                ejectingPeriodic();
                state_ = State.Stowing;
                break;

            case AfterShootingInTrap:
                afterShootingInTrapPeriodic();
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

    // Might Change
    public boolean climbPeriodic(){
        climbReady_ = false;
        switch (climbingState_){
            case Idle:
                break;
            
            case IsClimbReady:
                if (io.getElevatorPosition() == TrampConstants.Elevator.Positions.kTrap &
                    io.getArmPosition() == TrampConstants.Arm.Positions.kTrap &
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
                    climbDone_ = true;
                }
                break;
            
        }
        return climbDone_;
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


    public void prepClimbPeriodic(){
        if (io.getElevatorPosition() == TrampConstants.Elevator.Positions.kTrap&
            io.getArmPosition() == TrampConstants.Arm.Positions.kTrap &
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
                if (state_.equals(State.Idle)){
                    state_ = State.PrepClimb;
                }
            }
        };
    }
    
    public void shootingPeriodic(boolean climbDone_){
        switch(actionType_.get()){
            case SPEAKER:
                break;

            case AMP:
                if (io.getElevatorPosition() == TrampConstants.Elevator.Positions.kAmp &
                    io.getArmPosition() == TrampConstants.Arm.Positions.kAmp){
                        io.runManipulator(35);
                        Commands.waitSeconds(1);
                        io.stopManipulator();
                }
                break;

            case TRAP:
                if (io.getClimberPosition() == TrampConstants.Climber.ClimbTarget &
                    io.getElevatorPosition() == TrampConstants.Elevator.Positions.kTrap &
                    io.getArmPosition() == TrampConstants.Arm.Positions.kTrap &
                    climbDone_ == true){
                        Commands.waitSeconds(2);
                        io.runManipulator(35);
                        Commands.waitSeconds(1);
                        io.stopManipulator();
                }
                break;
        }
    }


    // Might need keepout zone code for switching to trap
    public void waitingPeriodic(){
        switch(actionType_.get()){
            case SPEAKER:
                break;
            
            case AMP: 
                if (io.getElevatorPosition() == TrampConstants.Elevator.Positions.kTrap &
                    io.getArmPosition() == TrampConstants.Arm.Positions.kTrap){
                        io.setElevatorPosition(TrampConstants.Elevator.Positions.kAmp);
                        io.setArmPosition(TrampConstants.Arm.Positions.kAmp);
                }
                break;
            
            case TRAP:
                if (io.getElevatorPosition() == TrampConstants.Elevator.Positions.kAmp &
                    io.getArmPosition() == TrampConstants.Arm.Positions.kAmp){
                        io.setElevatorPosition(TrampConstants.Elevator.Positions.kTrap);
                        io.setArmPosition(TrampConstants.Arm.Positions.kTrap); 
                }
                break;
        }
    }

    // Might need to use go to action to stow
    public void stowingPeriodic(){
        if (io.getElevatorPosition() != TrampConstants.Elevator.Positions.kStowed &
            io.getArmPosition() != TrampConstants.Arm.Positions.kStowed &
            io.getClimberPosition() != TrampConstants.Climber.ClimbTarget &
            inputs.armVelocity < 0.01 &
            inputs.elevatorVelocity < 0.01 &
            inputs.climberVelocity < 0.01){
                io.setClimberPosition(TrampConstants.Climber.ClimbTarget);
                io.setElevatorPosition(TrampConstants.Elevator.Positions.kStowed);
                io.setArmPosition(TrampConstants.Arm.Positions.kStowed);
            }
    }

    // Will Change
    public void ejectingPeriodic(){
        io.runManipulator(35);
        Commands.waitSeconds(2);
        io.stopManipulator();
        Commands.waitSeconds(1);
        io.runManipulator(35);
        Commands.waitSeconds(2);
        io.stopManipulator();
    }


    // Not Done
    public void transferringPeriodic(){
        switch(transferState_){
            case Idle:
                break;

            case WaitingForNote:
                if (hasNote_ == false){
                    io.runManipulator(5);
                }
                else {
                    io.stopManipulator();
                }
                transferState_ = TransferState.ExitTransfer;
                break;

            case ExitTransfer:
                break;
        }
    }

    public void prepAmpPeriodic(){
        io.setElevatorPosition(TrampConstants.Elevator.Positions.kAmp);
        io.setArmPosition(TrampConstants.Arm.Positions.kAmp);
    }

    // Not Done?
    public void prepTrapPeriodic(){
        if (io.getElevatorPosition() == TrampConstants.Elevator.Positions.kTransfer &
            io.getArmPosition() == TrampConstants.Arm.Positions.kTransfer){

        }
    }

    // Not Done
    public void afterShootingInTrapPeriodic(){
        switch(afterTrapShootState_) {
            case Idle:
                break;

            case Trap1:
                if (hasNote_ == false){
                    Commands.waitSeconds(1);
                    io.setArmPosition(2);
                    afterTrapShootState_ = AfterTrapShootState.Trap2;
                }
                break;

            case Trap2:
                io.setArmPosition(1);
                break;
        }
    }
    
    
}

