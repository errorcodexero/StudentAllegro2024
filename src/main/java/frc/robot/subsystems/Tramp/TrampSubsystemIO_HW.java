package frc.robot.subsystems.Tramp;

import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class TrampSubsystemIO_HW implements TrampSubsystemIO {    
    private TalonFX arm_motor_; 
    private TalonFX climber_motor_; 
    private TalonFX elevator_motor_; 
    private CANSparkFlex manipulator_motor_; 
    private SparkPIDController manipulator_PIDController; 
    private RelativeEncoder manipulator_Encoder; 

    public TrampSubsystemIO_HW() {
    arm_motor_ = new TalonFX(TrampConstants.Arm.kMotorCANID);
    climber_motor_ = new TalonFX(TrampConstants.Climber.kMotorCANID);
    elevator_motor_ = new TalonFX(TrampConstants.Elevator.kMotorCANID);
    manipulator_motor_ = new CANSparkFlex(TrampConstants.Manipulator.kMotorCANID, MotorType.kBrushless);

    // ARM CONFIGS:     
    Slot0Configs arm_pids = new Slot0Configs();
    arm_pids.kP = TrampConstants.Arm.PID.kP; 
    arm_pids.kI = TrampConstants.Arm.PID.kI; 
    arm_pids.kD = TrampConstants.Arm.PID.kD; 
    arm_pids.kV = TrampConstants.Arm.PID.kV; 
    arm_pids.kA = TrampConstants.Arm.PID.kA; 
    arm_pids.kG = TrampConstants.Arm.PID.kG; 
    arm_pids.kS = TrampConstants.Arm.PID.kS; 
    arm_motor_.getConfigurator().apply(arm_pids); 

    MotionMagicConfigs armMotionMagicConfigs = new MotionMagicConfigs(); 
    armMotionMagicConfigs.MotionMagicCruiseVelocity = TrampConstants.Arm.MotionMagic.kMaxVelocity;
    armMotionMagicConfigs.MotionMagicAcceleration = TrampConstants.Arm.MotionMagic.kMaxAcceleration;
    armMotionMagicConfigs.MotionMagicJerk = TrampConstants.Arm.MotionMagic.kJerk;
    arm_motor_.getConfigurator().apply(armMotionMagicConfigs);

    arm_motor_.setInverted(TrampConstants.Arm.kInverted);
    arm_motor_.setPosition(TrampConstants.Arm.Positions.kStowed); 

    // limit configs: 
    SoftwareLimitSwitchConfigs armLimitConfigs = new SoftwareLimitSwitchConfigs(); 
    armLimitConfigs.ForwardSoftLimitEnable = true; 
    armLimitConfigs.ForwardSoftLimitThreshold = TrampConstants.Arm.kMaxPosition; 
    armLimitConfigs.ReverseSoftLimitEnable = true; 
    armLimitConfigs.ReverseSoftLimitThreshold = TrampConstants.Arm.kMinPosition; 
    arm_motor_.getConfigurator().apply(armLimitConfigs); 

    // ELEVATOR CONFIGS: 
    Slot0Configs elevator_pids = new Slot0Configs();
    elevator_pids.kP = TrampConstants.Elevator.PID.kP; 
    elevator_pids.kI = TrampConstants.Elevator.PID.kI; 
    elevator_pids.kD = TrampConstants.Elevator.PID.kD; 
    elevator_pids.kV = TrampConstants.Elevator.PID.kV; 
    elevator_pids.kA = TrampConstants.Elevator.PID.kA; 
    elevator_pids.kG = TrampConstants.Elevator.PID.kG; 
    elevator_pids.kS = TrampConstants.Elevator.PID.kS; 
    elevator_motor_.getConfigurator().apply(elevator_pids); 

    MotionMagicConfigs elevatorMotionMagicConfigs = new MotionMagicConfigs(); 
    elevatorMotionMagicConfigs.MotionMagicCruiseVelocity = TrampConstants.Elevator.MotionMagic.kMaxVelocity;
    elevatorMotionMagicConfigs.MotionMagicAcceleration = TrampConstants.Elevator.MotionMagic.kMaxAcceleration;
    elevatorMotionMagicConfigs.MotionMagicJerk = TrampConstants.Elevator.MotionMagic.kJerk;
    elevator_motor_.getConfigurator().apply(elevatorMotionMagicConfigs);

    elevator_motor_.setInverted(TrampConstants.Elevator.kInverted);
    elevator_motor_.setPosition(TrampConstants.Elevator.Positions.kStowed); 

    // limit configs: 
    SoftwareLimitSwitchConfigs elevatorLimitConfigs = new SoftwareLimitSwitchConfigs(); 
    elevatorLimitConfigs.ForwardSoftLimitEnable = true; 
    elevatorLimitConfigs.ForwardSoftLimitThreshold = TrampConstants.Elevator.kMaxPosition / TrampConstants.Elevator.kMetersPerRev; 
    elevatorLimitConfigs.ReverseSoftLimitEnable = true; 
    elevatorLimitConfigs.ReverseSoftLimitThreshold = TrampConstants.Elevator.kMinPosition / TrampConstants.Elevator.kMetersPerRev; 
    elevator_motor_.getConfigurator().apply(elevatorLimitConfigs); 

    // MANIPULATOR CONFIGS: 
    manipulator_PIDController = manipulator_motor_.getPIDController(); 
    manipulator_Encoder = manipulator_motor_.getEncoder(); 
 
    manipulator_PIDController.setP(TrampConstants.Manipulator.PID.kP, 0); 
    manipulator_PIDController.setI(TrampConstants.Manipulator.PID.kI, 0); 
    manipulator_PIDController.setD(TrampConstants.Manipulator.PID.kD, 0); 
    manipulator_PIDController.setFF(TrampConstants.Manipulator.PID.kV, 0); 

    manipulator_motor_.setInverted(TrampConstants.Manipulator.kInverted);

    manipulator_Encoder.setPositionConversionFactor(manipulator_Encoder.getCountsPerRevolution()) ;
    manipulator_Encoder.setVelocityConversionFactor(manipulator_Encoder.getCountsPerRevolution()) ;  

    // CLIMBER CONFIGS: 
    climber_motor_.setInverted(TrampConstants.Climber.kInverted);
    
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

    // sets position in degrees: 
    public void setArmPosition(double deg){
        arm_motor_.setControl(new MotionMagicVoltage(deg / TrampConstants.Arm.kDegreesPerRev).withSlot(0)); 
    }

    // returns arm position in degrees: 
     public double getArmPosition(){
        return arm_motor_.getPosition().getValueAsDouble() * TrampConstants.Arm.kDegreesPerRev; 
     }

// CLIMBER METHODS: 
    public TalonFX getClimber(){
        return climber_motor_;
    }    

    // sets position in motor rotations: 
    public void setClimberPosition(double pos){
        climber_motor_.setControl(new MotionMagicVoltage(pos)); 
    }

    // gets climber position in motor rotations: 
    public double getClimberPosition(){
        return climber_motor_.getPosition().getValueAsDouble(); 
    }

// ELEVATOR METHODS: 
    public TalonFX getElevator(){
        return elevator_motor_;
    }    

    // sets position in meters: 
    public void setElevatorPosition(double m){
        elevator_motor_.setControl(new MotionMagicVoltage(m / TrampConstants.Elevator.kMetersPerRev).withSlot(0)); 
    } 

    // returns elevator position in meters: 
    public double getElevatorPosition(){
        return elevator_motor_.getPosition().getValueAsDouble() * TrampConstants.Elevator.kMetersPerRev; 
    }

//MANIPULATOR METHODS: 
    public CANSparkFlex getManipulator(){
        return manipulator_motor_;
    }  

    // sets velocity in revolutions per second: 
    public void runManipulator(double rps){
        manipulator_PIDController.setReference(rps * 60, ControlType.kVelocity, 0); 
    }

    // sets position in revolutions of manipulator rollers: 
    public void setManipulatorPosition(double rev){
        manipulator_PIDController.setReference(rev, ControlType.kPosition, 0); 
    }

    public void stopManipulator(){
        manipulator_motor_.stopMotor();
    }

    // returns the number of revolutions that the manipulator rollers have turned 
    public double getManipulatorPosition(){
        return manipulator_Encoder.getPosition(); 
    }    

}