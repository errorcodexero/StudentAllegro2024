package frc.robot.subsystems.Tramp;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class TrampSubsystemIO_HW implements TrampSubsystemIO {    
    private TalonFX arm_motor_; 
    private TalonFX climber_motor_; 
    private TalonFX elevator_motor_; 
    private CANSparkFlex manipulator_motor_; 
    private SparkPIDController manipulator_PIDController; 

    public TrampSubsystemIO_HW() {
    arm_motor_ = new TalonFX(TrampConstants.Arm.kMotorCANID);
    climber_motor_ = new TalonFX(TrampConstants.Climber.kMotorCANID);
    elevator_motor_ = new TalonFX(TrampConstants.Elevator.kMotorCANID);
    manipulator_motor_ = new CANSparkFlex(TrampConstants.Manipulator.kMotorCANID, MotorType.kBrushless);

    // Setting PID values for arm and elevator: 
    var arm_pids = new Slot0Configs();
    arm_pids.kP = TrampConstants.Arm.PID.kP; 
    arm_pids.kI = TrampConstants.Arm.PID.kI; 
    arm_pids.kD = TrampConstants.Arm.PID.kD; 
    arm_pids.kV = TrampConstants.Arm.PID.kV; 
    arm_pids.kA = TrampConstants.Arm.PID.kA; 
    arm_pids.kG = TrampConstants.Arm.PID.kG; 
    arm_pids.kS = TrampConstants.Arm.PID.kS; 

    var elevator_pids = new Slot0Configs();
    elevator_pids.kP = TrampConstants.Elevator.PID.kP; 
    elevator_pids.kI = TrampConstants.Elevator.PID.kI; 
    elevator_pids.kD = TrampConstants.Elevator.PID.kD; 
    elevator_pids.kV = TrampConstants.Elevator.PID.kV;
    elevator_pids.kA = TrampConstants.Elevator.PID.kA; 
    elevator_pids.kG = TrampConstants.Elevator.PID.kG; 
    elevator_pids.kS = TrampConstants.Elevator.PID.kS; 

    elevator_motor_.getConfigurator().apply(elevator_pids);
    arm_motor_.getConfigurator().apply(arm_pids);

    // setting PID values for manipulator: 
    manipulator_PIDController.setP(TrampConstants.Manipulator.PID.kP, 0); 
    manipulator_PIDController.setI(TrampConstants.Manipulator.PID.kI, 0); 
    manipulator_PIDController.setD(TrampConstants.Manipulator.PID.kD, 0); 
    manipulator_PIDController.setFF(TrampConstants.Manipulator.PID.kV, 0); 

    // setting kInverted values for all the motors: 
    elevator_motor_.setInverted(TrampConstants.Elevator.kInverted);
    arm_motor_.setInverted(TrampConstants.Elevator.kInverted);
    climber_motor_.setInverted(TrampConstants.Elevator.kInverted);
    manipulator_motor_.setInverted(TrampConstants.Elevator.kInverted);
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

        inputs.manipulatorPosition = manipulator_motor_.getEncoder().getPosition(); 
        inputs.manipulatorVelocity = manipulator_motor_.getEncoder().getVelocity();  
        inputs.manipulatorCurrent = manipulator_motor_.getOutputCurrent(); 
  }

    // ARM METHODS: 
    public TalonFX getArm(){
        return arm_motor_;
    }       

    public void setArmPosition(double rps){
        arm_motor_.setControl(new MotionMagicVoltage(rps * TrampConstants.Arm.kDegreesPerRev)); 
    }

     public double getArmPosition(){
        return arm_motor_.getPosition().getValueAsDouble() * 360 / TrampConstants.Arm.kDegreesPerRev; 
     }

    // CLIMBER METHODS: 
    public TalonFX getClimber(){
        return climber_motor_;
    }    

    public void setClimberPosition(double pos){
        climber_motor_.setControl(new MotionMagicVoltage(pos)); 
    }

    public double getClimberPosition(){
        return climber_motor_.getPosition().getValueAsDouble() * 360; 
    }

    // ELEVATOR METHODS: 
    public TalonFX getElevator(){
        return elevator_motor_;
    }    

    public void setElevatorPosition(double pos){
        elevator_motor_.setControl(new MotionMagicVoltage(pos * TrampConstants.Elevator.kMetersPerRev)); 
    } 

    public double getElevatorPosition(){
        return elevator_motor_.getPosition().getValueAsDouble() * 360 / TrampConstants.Elevator.kMetersPerRev; 
    }

    //MANIPULATOR METHODS: 
    public CANSparkFlex getManipulator(){
        return manipulator_motor_;
    }  

    public void runManipulator(double rps){
        manipulator_PIDController.setReference(rps * 60, ControlType.kVelocity); 
    }

    public void stopManipulator(){
        manipulator_motor_.stopMotor();
    }

    public double getManipulatorPosition(){
        return manipulator_motor_.getEncoder().getPosition() * 360; 
    }    

}

// need to add: 
// some more motor initialization things (current limit, min/max pos, max accel/vel)
// withSlot(0) to end of certain methods 