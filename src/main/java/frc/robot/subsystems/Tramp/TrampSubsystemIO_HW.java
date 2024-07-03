package frc.robot.subsystems.Tramp;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.CANSparkFlex;

public class TrampSubsystemIO_HW implements TrampSubsystemIO {    
    private TalonFX arm_motor_; 
    private TalonFX climber_motor_; 
    private TalonFX elevator_motor_; 
    private CANSparkFlex manipulator_motor_; 

    public TrampSubsystemIO_HW() {
    arm_motor_ = new TalonFX(7);
    climber_motor_ = new TalonFX(10);
    elevator_motor_ = new TalonFX(6);
    manipulator_motor_ = new CANSparkFlex(9, null);

// Setting all the PID values: 
    var arm_pids = new Slot0Configs();
    arm_pids.kS = 0.0; // Add 0.00 V output to overcome static friction
    arm_pids.kV = 0.0; // A velocity target of 0 rps results in 0.00 V output
    arm_pids.kP = 0.0; // An error of 0 rps results in 0.0 V output
    arm_pids.kI = 0.0; // no output for integrated error
    arm_pids.kD = 0.0; // no output for error derivative

    var climber_pids = new Slot0Configs();
    climber_pids.kS = 0.0; // Add 0.00 V output to overcome static friction
    climber_pids.kV = 0.0; // A velocity target of 0 rps results in 0.00 V output
    climber_pids.kP = 0.0; // An error of 0 rps results in 0.0 V output
    climber_pids.kI = 0.0; // no output for integrated error
    climber_pids.kD = 0.0; // no output for error derivative

    var elevator_pids = new Slot0Configs();
    elevator_pids.kS = 0.0; // Add 0.00 V output to overcome static friction
    elevator_pids.kV = 0.0; // A velocity target of 0 rps results in 0.00 V output
    elevator_pids.kP = 0.0; // An error of 0 rps results in 0.0 V output
    elevator_pids.kI = 0.0; // no output for integrated error
    elevator_pids.kD = 0.0; // no output for error derivative

    var manipulator_pids = new Slot0Configs();
    manipulator_pids.kS = 0.0; // Add 0.00 V output to overcome static friction
    manipulator_pids.kV = 0.0; // A velocity target of 0 rps results in 0.00 V output
    manipulator_pids.kP = 0.0; // An error of 0 rps results in 0.0 V output
    manipulator_pids.kI = 0.0; // no output for integrated error
    manipulator_pids.kD = 0.0; // no output for error derivative

    elevator_motor_.getConfigurator().apply(elevator_pids);
    arm_motor_.getConfigurator().apply(arm_pids);
    climber_motor_.getConfigurator().apply(climber_pids);
    // PIDS for the manipulator motor (SparkFlex) don't work the same way
    // manipulator_motor_.getConfigurator().apply(manipulator_pids);
    }

    // updateInputs function; updates all of the inputs from TrampSubsystemIO 
    @Override
    public void updateInputs(TrampSubsystemIOInputs inputs) {
        inputs.elevatorPosition = elevator_motor_.getPosition().getValueAsDouble(); 
        inputs.elevatorVelocity = elevator_motor_.getVelocity().getValueAsDouble(); 
        inputs.elevatorCurrent = elevator_motor_.getSupplyCurrent().getValueAsDouble(); 
        inputs.elevatorVoltage = elevator_motor_.getMotorVoltage().getValueAsDouble(); 
        inputs.elevatorAcceleration = elevator_motor_.getAcceleration().getValueAsDouble(); 

        inputs.armPosition = arm_motor_.getPosition().getValueAsDouble(); 
        inputs.armVelocity = arm_motor_.getVelocity().getValueAsDouble(); 
        inputs.armCurrent = arm_motor_.getSupplyCurrent().getValueAsDouble(); 
        inputs.armVoltage = arm_motor_.getMotorVoltage().getValueAsDouble(); 
        inputs.armAcceleration = arm_motor_.getAcceleration().getValueAsDouble(); 

        inputs.climberPosition = climber_motor_.getPosition().getValueAsDouble(); 
        inputs.climberVelocity = climber_motor_.getVelocity().getValueAsDouble(); 
        inputs.climberCurrent = climber_motor_.getSupplyCurrent().getValueAsDouble(); 
        inputs.climberVoltage = climber_motor_.getMotorVoltage().getValueAsDouble(); 
        inputs.climberAcceleration = climber_motor_.getAcceleration().getValueAsDouble(); 

        // Inputs for the manipulator motor (SparkFlex) also work differently: 
        //
        // inputs.manipulatorPosition = manipulator_motor_.getPosition().getValueAsDouble(); 
        // inputs.manipulatorVelocity = manipulator_motor_.getVelocity().getValueAsDouble(); 
        // inputs.manipulatorCurrent = manipulator_motor_.getSupplyCurrent().getValueAsDouble(); 
        // inputs.manipulatorVoltage = manipulator_motor_.getMotorVoltage().getValueAsDouble(); 
        // inputs.manipulatorAcceleration = manipulator_motor_.getAcceleration().getValueAsDouble(); 
  }

    public TalonFX getArm(){
        return arm_motor_;
    }   
   
    public TalonFX getClimber(){
        return climber_motor_;
    }    
   
    public TalonFX getElevator(){
        return elevator_motor_;
    }    
   
    public CANSparkFlex getManipulator(){
        return manipulator_motor_;
    }     

     public double getArmPosition(){
        return arm_motor_.getPosition().getValueAsDouble(); 
     }

    public double getClimberPosition(){
        return climber_motor_.getPosition().getValueAsDouble(); 
    }

    public double getElevatorPosition(){
        return elevator_motor_.getPosition().getValueAsDouble(); 
    }

    public void moveClimber(double m){}

    public void moveElevator(double m){} 

    public void runManipulatorRevolutions(double rps){}

    public void runManipulator() {} 

    public void stopManipulator(){
        manipulator_motor_.stopMotor();
    }

    public void moveArmDegrees(double degs){
    }

    public void moveArmRadians(double rad){
        arm_motor_.setControl(new MotionMagicVoltage(rad)); 
    }

    public void moveArmRevolutions(double rps){
        arm_motor_.setControl(new VelocityVoltage(rps)); 
    }
   
    public void trampPositionAction(double target_height_, double target_angle_){
        double currentArmPosition = getArmPosition(); 
        double targetHeight = target_height_

        if (currentArmPosition < keep_out_min_ && target_angle_ > keep_out_max_) {
            //
            // We need to cross the keep out zone from min to max
            //
            if (target_height_ > keep_out_height_) {
                //
                // The eventual height is above the keep out zone, so we can go directly to the target height
                // and just monitor the elevator height and start the arm when we get above the keepout height
                //
                sub_.getElevator().setAction(elevator_goto_target_action_, true) ;
            } else {
                //
                // The eventual height is below the keepout zone, so we need to go to the keepout height,
                // move the arm, and then go to the target height
                //
                sub_.getElevator().setAction(keepout_height_action_, true) ;
            }
    }
    }
}
