package frc.robot.subsystems.Tramp;

import org.littletonrobotics.junction.AutoLog;

import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.CANSparkFlex;

public interface TrampSubsystemIO {
   
    @AutoLog
    public static class TrampSubsystemIOInputs {

        // all of the inputs: 
        public double elevatorPosition = 0.0;
        public double elevatorVelocity = 0.0;
        public double elevatorCurrent = 0.0;
        public double elevatorVoltage = 0.0;
        public double elevatorAcceleration = 0.0;

        public double armPosition = 0.0; 
        public double armVelocity = 0.0; 
        public double armCurrent = 0.0;
        public double armVoltage = 0.0;
        public double armAcceleration = 0.0; 

        public double climberPosition = 0.0; 
        public double climberVelocity = 0.0; 
        public double climberCurrent = 0.0;
        public double climberVoltage = 0.0;
        public double climberAcceleration = 0.0; 

        public double manipulatorPosition = 0.0; 
        public double manipulatorVelocity = 0.0; 
        public double manipulatorCurrent = 0.0;
        public double manipulatorVoltage = 0.0;
        public double manipulatorAcceleration = 0.0; 
    }
    // updating inputs
    public default void updateInputs(TrampSubsystemIOInputs inputs) {}

    // ARM METHODS: 
    public TalonFX getArm(); 
    
    public void setArmPosition(double rps); 

    public double getArmPosition(); 

    // CLIMBER METHODS: 
    public TalonFX getClimber(); 

    public void setClimberPosition(double pos); 

    public double getClimberPosition(); 

    // ELEVATOR METHODS: 
    public TalonFX getElevator(); 

    public void setElevatorPosition(double pos); 

    public double getElevatorPosition(); 

    //MANIPULATOR METHODS: 
    public CANSparkFlex getManipulator(); 

    public void runManipulator(double speed); 

    public double getManipulatorPosition(); 

}