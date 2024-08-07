package frc.robot.subsystems.Tramp;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TrampSubsystem extends SubsystemBase {
  
    private final TrampSubsystemIO io_; 
    private final TrampSubsystemIOInputsAutoLogged inputs_;

 /** Creates a new Tramp. */
    public TrampSubsystem(TrampSubsystemIO io) {
      io_ = io; 
      inputs_ = new TrampSubsystemIOInputsAutoLogged(); 
  }

  @Override
  public void periodic() {
      io_.updateInputs(inputs_);
      Logger.processInputs("TrampSubsystem", inputs_);
      Logger.recordOutput("TrampSybsystem/arm_angle", io_.getArmPosition());
      Logger.recordOutput("TrampSubsystem/elevator_height", io_.getElevatorPosition()); 
      Logger.recordOutput("TrampSubsystem/manipulator_revolutions", io_.getManipulatorPosition()); 

    }


    // sets position in degrees: 
    public void setArmPosition(double deg){
        io_.setArmPosition(deg);
    }

    // sets position in meters: 
    public void setElevatorPosition(double m){
        io_.setElevatorPosition(m);
    }    

    // sets position in motor revolutions 
    public void setManipulatorPosition(double rev){
        io_.setManipulatorPosition(rev);; 
    }

    // sets velocity in revolutions per second 
    public void runManipulator(double rps){
        io_.runManipulator(rps);
    }    

    // sets position in motor revolutions 
    public void stopManipulator(){
        io_.stopManipulator();
    }

}

