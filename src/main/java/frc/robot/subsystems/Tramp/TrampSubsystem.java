package frc.robot.subsystems.Tramp;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

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
        ArmElevatorPositioning, 
        PrepTrap,
        MovingNote,
        WaitingAfterTransfer,
        Stowing,
        Ejecting
    }

    private enum ArmElevatorPositionState {
        Idle,                           // Doing nothing
        CrossMinToMax,                  // The arm needs to cross the keepout zone from min to max
        CrossMaxToMin,                  // The arm needs to cross the keepout zone from max to min
        CrossMinToMaxWaitClear,         // The arm is actively crossing from min to max, waiting for the arm to clear the keepout zone
        CrossMaxToMinWaitClear,         // The arm is actively crossing from max to min, waiting for the arm to clear the keepout zone
        DirectToTarget,                 // Everything is clear, just waiting for the actions to complete
        PositioningDone                 // Actions completed
    }

    private final TrampSubsystemIO io_; 
    private final TrampSubsystemIOInputsAutoLogged inputs_;
    public double target_position_; 

    private State state_;
    private ArmElevatorPositionState armElevatorPositionState_; 

 /** Creates a new Tramp. */
    public TrampSubsystem(TrampSubsystemIO io) {
      io_ = io; 
      inputs_ = new TrampSubsystemIOInputsAutoLogged(); 
      state_ = State.Idle; 
      armElevatorPositionState_ = armElevatorPositionState_.Idle; 
  }

  @Override
  public void periodic() {
      io_.updateInputs(inputs_);
      Logger.processInputs("TrampSubsystem", inputs_);

      Logger.recordOutput("TrampSubsystem/arm_angle", io_.getArmPosition());
      Logger.recordOutput("TrampSubsystem/elevator_height", io_.getElevatorPosition()); 
      Logger.recordOutput("TrampSubsystem/manipulator_revolutions", io_.getManipulatorPosition()); 
      Logger.recordOutput("TrampSubsystem/target_position", target_position_); 

      switch (state_) {
        case ArmElevatorPositioning:
            ArmElevatorPosition(0.05, 5);
            break;

        default:
            state_ = State.Idle;
            break;
    }
    }

    // sets position in degrees: 
    public void setArmPosition(double deg){
        io_.setArmPosition(deg);
    }

    // sets position in meters: 
    public void setElevatorPosition(double m){
        io_.setElevatorPosition(m);
        target_position_ = m; 
    }    

    // sets position in motor revolutions 
    public void setManipulatorPosition(double rev){
        io_.setManipulatorPosition(rev);
    }

    // sets velocity in revolutions per second 
    public void runManipulator(double rps){
        io_.runManipulator(rps);
    }    

    // stops motor
    public void stopManipulator(){
        io_.stopManipulator();
    }

    public Command armElevatorPositionCommand(){
        return new Command() {
            @Override
            public void initialize() {
                super.initialize();
                if(state_.equals(State.Idle)){
                    state_ = State.ArmElevatorPositioning;
                }
            }
        };
    }
    public void ArmElevatorPosition(double height, double angle){
        
        double currentArmPosition = io_.getArmPosition(); 
        double target_angle_ ;
        double target_height_ ;
        double keep_out_min_ ;
        double keep_out_max_ ;
        double keep_out_height_ ;
        
        target_height_ = height; 
        target_angle_ = angle; 
        
        keep_out_height_ = TrampConstants.KeepoutZone.kElevatorHeight; 
        keep_out_min_ = TrampConstants.KeepoutZone.kMinArm; 
        keep_out_max_ = TrampConstants.KeepoutZone.kMaxArm; 

        if (currentArmPosition < keep_out_min_ && target_angle_ > keep_out_max_) {
            //
            // We need to cross the keep out zone from min to max
            armElevatorPositionState_ = ArmElevatorPositionState.CrossMinToMax; 
            //
            if (target_height_ > keep_out_height_) {
                //
                // The eventual height is above the keep out zone, so we can go directly to the target height
                // and just monitor the elevator height and start the arm when we get above the keepout height
                //
                // move elevator to target height
                setElevatorPosition(target_height_); 
                // when elevator is as keepout height, start moving arm to target angle (moving up)
                if (io_.getElevatorPosition() - keep_out_height_ < 2) {
                    setArmPosition(target_angle_);
                    armElevatorPositionState_ = ArmElevatorPositionState.CrossMinToMaxWaitClear; 
                }
            } else {
                //
                // The eventual height is below the keepout zone, so we need to go to the keepout height,
                // move the arm, and then go to the target height
                //
                // move elevator to keepout height 
                setElevatorPosition(keep_out_height_);
                if (io_.getElevatorPosition() - keep_out_height_ < 2) {
                    // move arm to target angle 
                    setArmPosition(target_angle_);
                    armElevatorPositionState_ = ArmElevatorPositionState.CrossMinToMaxWaitClear; 
                }
                if (io_.getArmPosition() - target_angle_ < 2){
                    // move elevator to target height 
                    setElevatorPosition(target_height_);
                }
            }
            //
            // In either case we are going to move the arm to the max position and wait for the elevator
            // to be above the keepout height before moving the arm to the target position
            // 
            setArmPosition(keep_out_min_);
        }

        else if (currentArmPosition >= keep_out_max_ && target_angle_ <= keep_out_min_) {
            //
            // We need to cross the keep out zone from max to min
            armElevatorPositionState_ = ArmElevatorPositionState.CrossMaxToMin; 
            //
            if (target_height_ > keep_out_height_) {
                //
                // The eventual height is above the keep out zone, so we can go directly to the target height
                // and just monitor the elevator height and start the arm when we are above the keepout height
                //
                // move elevator to target height 
                setElevatorPosition(target_height_);
                armElevatorPositionState_ = ArmElevatorPositionState.CrossMaxToMinWaitClear; 
                // when elevator is at keepout height, start moving arm to target angle (moving down)
                if (io_.getElevatorPosition() - keep_out_height_ < 2) {
                    setArmPosition(target_height_);
                }
            } else {
                //
                // The eventual height is below the keepout zone, so we need to go to the keepout height,
                // move the arm, and then go to the target height
                //
                setElevatorPosition(keep_out_height_);
                if (io_.getElevatorPosition() - keep_out_height_ < 2) {
                    // move arm to target angle (moving down)
                    setArmPosition(target_angle_);
                armElevatorPositionState_ = ArmElevatorPositionState.CrossMaxToMinWaitClear; 
                }
                if (io_.getArmPosition() - target_angle_ < 2) {
                    // move elevator to target height
                    setElevatorPosition(target_height_);
                }
            }
            //
            // In either case we are going to move the arm to the max position and wait for the elevator
            // to be above the keepout height before moving the arm to the target position
            //            
            // move arm to keep out min
            setArmPosition(keep_out_min_);
        }
        else {
            //
            // There is no interference with the keepout zone, both elevator and arm can go directly to the target
            //
            setElevatorPosition(target_height_); 
            setArmPosition(target_angle_); 
            armElevatorPositionState_ = ArmElevatorPositionState.DirectToTarget; 

        }
        if((io_.getElevatorPosition() - target_height_ < 2) & (io_.getArmPosition() - target_angle_ < 2)){
            armElevatorPositionState_ = ArmElevatorPositionState.PositioningDone; 
        }
    }

}

