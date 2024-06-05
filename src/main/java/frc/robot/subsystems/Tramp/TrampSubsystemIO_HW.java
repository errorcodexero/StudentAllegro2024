package frc.robot.subsystems.Tramp;

import com.ctre.phoenix6.configs.Slot0Configs;
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
}

// irrelevant code, from the shooter roller assignment: 
    // @Override
     // start shooter and set to certain rps 
    //   public void startShooter(double rps){
    //     shootermotor1_.setControl(new VelocityVoltage(rps)) ;
//   }

    // @Override
     // stop shooter by setting voltage out to 0
    //   public void stopShooter(){
    //     shootermotor1_.setControl(new VoltageOut(0.0)) ;
//   }


