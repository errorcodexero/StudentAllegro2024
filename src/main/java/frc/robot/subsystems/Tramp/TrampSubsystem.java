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
        Shooting,
        AfterShootingInTrap,
        MovingToTransfer,
        Transferring,
        PrepAmp,

        // Place Holder for goTo like Action
        KeepOutZoneCode,
        
        PrepTrap,
        MovingNote,
        WaitingAfterTransfer,
        Stowing,
        Ejecting
    }

    // These are states which are used within the "Climbing" State.
    private enum ClimbingState {
        Idle,
        IsClimbReady,
        Climbing
    }

    // These are states which are used within the "Transferring" State.
    private enum TransferState {
        Idle,
        WaitingForNote, 
        ExitTransfer
    }

    // These are states whoich are used within the "AfterShootingInTrap" State.
    private enum AfterTrapShootState {
        Idle,
        Trap1,
        Trap2
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
        afterTrapShootState_ = AfterTrapShootState.Trap1;
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

            /*  default State which the Tramp Subsystem is in when a note is not within the
             *    subsystem.
             */
            case Idle:
                break;

            /*  This is a state where the subsystem goes to the Trap position legally.
             *  After the subsystem has reached position it switches to the "MovingNote" State.
             */
            case KeepOutZoneCode:
                state_ = State.MovingNote;
                break;

            /* This state is called when the "climbExecute" button on the OI is pressed. This
             * state lowers the climbing hook to the ideal 'climbed' position during/after a
             * climb. Switches to the "Shooting" state afterwards which starts shooting the 
             * note into the trap after a climb. 
             */
            case Climbing:
                climbPeriodic();
                state_ = State.Shooting;
                break;
            
            /* This state is called when the "climbPrepare" button on the OI is pressed. This
             * state is raises the climbing hooks to the ideal 'up' position before a climb.
             * Waits within this state until the robot exits the "PrepClimb" state
             * (Eject, Stow, Climb).
             */
            case PrepClimb:
                prepClimbPeriodic();
                break;

            /* This state is called after the robot finished climbing or when the "AmpShootCommand"
             * is scheduled. Within this state the robot 'shoots' a note out of the Tramp rollers.
             * Depending on the ActionType of the robot, the subsystem either goes to the "Stowing"
             * or "AfterShootingInTrap" state after the note is shot. 
             */
            case Shooting:
                shootingPeriodic(climbDone_);
                if (actionType_.get() == ActionType.AMP){
                    state_ = State.Stowing;
                }
                else if (actionType_.get() == ActionType.TRAP){
                    state_ = State.AfterShootingInTrap;
                }
                break;
            
            /* This is a state where it is periodically monitored whether there is a necessary
             * change from 'Amp' to 'Trap' and vice versa. If the change is to 'Trap', then
             * this switches to the "PrepTrap" State. Mainly a waiting state.
             */
            case WaitingAfterTransfer:
                waitingPeriodic();
                break;
            
            /* This state is called when the Tramp Subsystem is required to Transfer. It moves
             * the subsystem to the 'Transfer' position. Afterwards it goes to the "Transferring"
             * State. 
             */
            case MovingToTransfer:
                movingToTransfer();
                state_ = State.Transferring;
                break;
            
            /* This state waits for the note to be transferred from the IntakeShooter and, 
             * once the Tramp has the note, switches to either the "PrepAmp" or "PrepTrap"
             * State depending on the actionType.
             */
            case Transferring:
                transferringPeriodic();
                if (actionType_.get() == ActionType.AMP){
                    state_ = State.PrepAmp;
                }
                else if (actionType_.get() == ActionType.TRAP){
                    state_ = State.PrepTrap;
                }
                break;

            /* This state moves the Tramp Subsystem to the 'Amp' position. Afterwards it changes
             * the State to the "WaitingAfterTransfer" State. 
             */
            case PrepAmp:
                prepAmpPeriodic();
                state_ = State.WaitingAfterTransfer;
                break;
            
            /* This state is called either when directly transferring to 'Trap' position or when
             * the ActionType is changed to "TRAP". This is an inbetween state which immediatly 
             * uses the "KeepOutZoneCode" to go to the 'Trap' position legally. Immediatly called
             * after transfer.
             */
            case PrepTrap:
                prepTrapPeriodic();
                state_ = State.KeepOutZoneCode;
                break;

            /* This state moves the note in the ideal position for 'Trapping' after the
             * robot is in the Trap position. Goes to the "WaitingAfterTransfer" State
             * afterwards.
             */
            case MovingNote:
                movingNotePeriodic();
                state_ = State.WaitingAfterTransfer;
                break;

            /* This state puts every mechanism within the Tramp Subsystem to its 'Stow' position.
             * It acts as almost a reset for the subsystem, as everything goes back to normal and 
             * the State is set as "Idle".
             */
            case Stowing:
                stowingPeriodic();
                state_ = State.Idle;
                break;
            
            /* This state acts as a general reset state and ejects any note within the Tramp 
             * Subsystem. After the note is ejected, the subsystem go to the "Stowing" State
             * and eventually the "Idle" State.
             */
            case Ejecting:
                ejectingPeriodic();
                state_ = State.Stowing;
                break;

            /* This state moves and pulls back the manipulator arm to make sure the note falls
             * within the Trap slip and that the robot is does not look like it is relying on the
             * stage to support itself.
             */
            case AfterShootingInTrap:
                afterShootingInTrapPeriodic();
                break;
            
            // Default case is Idle.
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



    // Method called during the "Climbing" State
    public boolean climbPeriodic(){
        // if climb is ready
        climbReady_ = false;
        switch (climbingState_){
            case Idle:
                climbDone_ = true;
                break;
            
            case IsClimbReady:
                // if in 'Trapping' position, start climbing.
                if (io.getElevatorPosition() == TrampConstants.Elevator.Positions.kTrap &
                    io.getArmPosition() == TrampConstants.Arm.Positions.kTrap &
                    inputs.armVelocity < 0.01 & inputs.armVelocity > -0.01 &
                    inputs.elevatorVelocity < 0.01 & inputs.elevatorVelocity > -0.01 &
                    io.getClimberPosition() == TrampConstants.Climber.kClimberUpPosition){

                    climbReady_ = true;
                    climbingState_ = ClimbingState.Climbing;
                }
                break;

            case Climbing:
                // if climb is ready, climb and finish.
                if (climbReady_ == true){
                    io.setClimberPosition(TrampConstants.Climber.kClimberDownPosition);
                    Commands.waitSeconds(1);
                    climbingState_ = ClimbingState.Idle;
                }
                break;
            
        }
        return climbDone_;
    }

    // Switches State to "Climbing" if climb is prepped. Scheduled by pressing "Climb" on OI.
    public Command ClimbCommand(){
        return new Command() {
            @Override
            public void initialize() {
                super.initialize();
                if(state_.equals(State.PrepClimb) && climbDone_ == false){
                    state_ = State.Climbing;
                }
            }
        };   
    }



    // Method called during the "PrepClimb" State.
    public void prepClimbPeriodic(){
        // if the Tramp is in 'Trapping' position and not moving, move the climbers up.
        if (io.getElevatorPosition() == TrampConstants.Elevator.Positions.kTrap &
            io.getArmPosition() == TrampConstants.Arm.Positions.kTrap &
            inputs.armVelocity < 0.01 & inputs.armVelocity > -0.01 &
            inputs.elevatorVelocity < 0.01 & inputs.armVelocity > -0.01){
                io.setClimberPosition(TrampConstants.Climber.kClimberUpPosition);
        }
    }

    // This command is scheduled after the "PrepClimb" button on OI is pressed. Switches to the 
    // "PrepClimb" State.
    public Command PrepClimbCommand(){
        return new Command() {
            @Override
            public void initialize() {
                super.initialize();
                if (state_.equals(State.WaitingAfterTransfer)){
                    state_ = State.PrepClimb;
                }
            }
        };
    }
    

    // Method called in the "Shooting" State.
    public void shootingPeriodic(boolean climbDone_){
        switch(actionType_.get()){
            case SPEAKER:
                break;

            case AMP:
                // if in 'Amp' position, "shoot" the note.
                if (io.getElevatorPosition() == TrampConstants.Elevator.Positions.kAmp &
                    io.getArmPosition() == TrampConstants.Arm.Positions.kAmp){
                        io.runManipulator(70);
                        Commands.waitSeconds(TrampConstants.Manipulator.kShootTime);
                        io.stopManipulator();
                }
                break;

            case TRAP:
                // if in 'Trap' position, wait and "shoot" the note.
                if (io.getClimberPosition() == TrampConstants.Climber.kClimberDownPosition &
                    io.getElevatorPosition() == TrampConstants.Elevator.Positions.kTrap &
                    io.getArmPosition() == TrampConstants.Arm.Positions.kTrap &
                    climbDone_ == true){
                        Commands.waitSeconds(2);
                        io.runManipulator(70);
                        Commands.waitSeconds(TrampConstants.Manipulator.kDepositTime);
                        io.stopManipulator();
                }
                break;
        }
    }

    // Switches State to "Shooting" when Command is scheduled after "shoot" button is pressed on OI.
    public Command AmpShootCommand(){
        return new Command() {
            @Override
            public void initialize() {
                super.initialize();
                if (state_.equals(State.WaitingAfterTransfer)){
                    state_ = State.Shooting;
                }
            }
        };
    }



    // Method that is called in the "WaitingAfterTransfer" State.
    public void waitingPeriodic(){
        switch(actionType_.get()){
            case SPEAKER:
                break;
            
            case AMP: 
                // if in 'Trap' position, move to 'Amp' position. 
                if (io.getElevatorPosition() != TrampConstants.Elevator.Positions.kAmp &
                    io.getArmPosition() != TrampConstants.Arm.Positions.kAmp){
                    state_ = State.PrepAmp;
                }
                break;
            
            case TRAP:
                // if in 'Amp' position, move to 'Trap' position.
                if (io.getElevatorPosition() != TrampConstants.Elevator.Positions.kTrap &
                    io.getArmPosition() != TrampConstants.Arm.Positions.kTrap){
                    state_ = State.PrepTrap;
                }
                break;
        }
    }



    // Method that is called during in the "Stowing" State.
    public void stowingPeriodic(){
        // if Tramp not moving, stow the whole subsystem.
        if (inputs.armVelocity < 0.01 &
            inputs.elevatorVelocity < 0.01 &
            inputs.climberVelocity < 0.01){
                io.setElevatorPosition(TrampConstants.Elevator.Positions.kStowed);
                io.setArmPosition(TrampConstants.Arm.Positions.kStowed);
                // Stow Climber?
            }
    }

    // Switches to the "Stowing" State. Command is scheduled when "stow" is pressed on OI.
    public Command StowCommand(){
        return new Command(){
            @Override
            public void initialize() {
                super.initialize();
                    state_ = State.Stowing;      
            }
        };   
    }


    // Method called in the "Ejecting" State.
    public void ejectingPeriodic(){
        io.runManipulator(35);
        Commands.waitSeconds(1);
        io.stopManipulator();
        Commands.waitSeconds(0.5);
        io.runManipulator(35);
        Commands.waitSeconds(1);
        io.stopManipulator();
    }

    // Switches the State to "Ejecting". Command is scheduled when the "eject" is pressed on OI.
    public Command EjectCommand(){
        return new Command(){
            @Override
            public void initialize() {
                super.initialize();
                state_ = State.Ejecting;
            }
        };   
    }



    // Method called during the "MovingToTransfer" State.
    public void movingToTransfer(){
        io.setElevatorPosition(TrampConstants.Elevator.Positions.kTransfer);
        io.setArmPosition(TrampConstants.Arm.Positions.kTransfer);
    }

    // Method called during the "Transferring" State.
    public void transferringPeriodic(){
        switch(transferState_){
            case Idle:
                break;

            case WaitingForNote:
                // This depends on the use of a sensor between the manipulator rollers.
                if (hasNote_ == false){

                }
                else {
                    io.stopManipulator();
                    transferState_ = TransferState.ExitTransfer;
                }
                break;

            case ExitTransfer:
                break;
        }
    }

    // Switches to the "MovingToTransfer" State and starts the process of transferring
    // Command is scheduled when IntakeShooter is ready. 
    public Command TransferCommand(){
        return new Command(){
            @Override
            public void initialize() {
                super.initialize();
                if (state_.equals(State.Idle)){
                    state_ = State.MovingToTransfer;
                }
            }
        };   
    }


    
    // Method called in the "PrepAmp" State. 
    public void prepAmpPeriodic(){
        io.setElevatorPosition(TrampConstants.Elevator.Positions.kAmp);
        io.setArmPosition(TrampConstants.Arm.Positions.kAmp);
    }

    // Method called in the "PrepTrap" State.
    public void prepTrapPeriodic(){
        // switches state to KeepOutZoneCode which will be added later. 
        if (io.getElevatorPosition() == TrampConstants.Elevator.Positions.kTransfer &
            io.getArmPosition() == TrampConstants.Arm.Positions.kTransfer){
                state_ = State.KeepOutZoneCode;
        }
    }



    // Method that is called during the "MovingNote" State.
    public void movingNotePeriodic(){
        double noteDistance = io.getManipulatorPosition() + TrampConstants.Manipulator.kTrapMoveNoteDistance;
        io.setManipulatorPosition(noteDistance);
    }



    // Method called during the "AfterShootingInTrap" State. 
    public void afterShootingInTrapPeriodic(){
        switch(afterTrapShootState_) {
            case Idle:
                break;

            case Trap1:
                // if we don't have the note, move the arm forward to make sure it gets in.
                // This assumes sensor
                if (hasNote_ == false){
                    Commands.waitSeconds(1);
                    io.setArmPosition(TrampConstants.Arm.Positions.kTrap3);
                    afterTrapShootState_ = AfterTrapShootState.Trap2;
                }
                break;

            case Trap2:
                // Moving the arm back to be in a legal position.
                Commands.waitSeconds(1);
                io.setArmPosition(TrampConstants.Arm.Positions.kTrap4);
                break;
        }
    } 
}

