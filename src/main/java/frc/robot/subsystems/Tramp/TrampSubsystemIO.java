package frc.robot.subsystems.Tramp;

import org.littletonrobotics.junction.AutoLog;

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
    // updating all of the inputs
    public default void updateInputs(TrampSubsystemIOInputs inputs) {}
  
//     // starting shooter
//     public default void startShooter(double rps) {}

//     // stopping shooter (by setting voltage to 0)
//     public default void stopShooter() {}

}