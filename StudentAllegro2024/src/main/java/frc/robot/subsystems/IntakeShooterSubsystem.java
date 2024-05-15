// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.hardware.TalonFX;

import AKInput.AKInput;
import MotorFactory.XeroTalon;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AsynchronousInterrupt;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeShooterSubsystem extends SubsystemBase {
  /** Creates a new ExampleSubsystem. */
  private XeroTalon feeder;
  private XeroTalon up_down;
  private XeroTalon shooter1;
  private XeroTalon shooter2;
  private XeroTalon tilt;

  private AsynchronousInterrupt interrupt_ ;
  private boolean sensor_edge_seen_ ;

  private DigitalInput noteSensor_; 
  private boolean noteSensorInverted_;
  private AnalogInput absoluteEncoder_;
  private Encoder encoderMapper_;

  // This is true if the sensor is currently detecting a note
  private boolean is_note_present_;

  private double angle_;

  private int[] inputIndicies;

  private static final String NAME = "IntakeShooterSubsystem";

  public IntakeShooterSubsystem() {
    feeder = new XeroTalon(1, NAME, "Feeder", 3.16);
    up_down = new XeroTalon(2, NAME, "Up Down", 66);
    shooter1 = new XeroTalon(3, NAME, "Shooter1", 0.6);
    shooter2 = new XeroTalon(4, NAME, "Shooter2", 0.6);
    tilt = new XeroTalon(5, NAME, "Tilt", 21.33);

    feeder.setPID("p", 0.0035);
    feeder.setPID("d", 0.004);
    feeder.setPID("v", 0.00312);

    up_down.setPID("p", 7);
    up_down.setPID("v", 0.4);

    shooter1.setPID("p", 0.3);
    shooter1.setPID("v", 0.08);

    shooter2.setPID("p", 0.3);
    shooter2.setPID("v", 0.08);

    tilt.setPID("p", 2.8);
    tilt.setPID("v", 0.1);

    //I have no clue what I am doing with the sensors and absolute encoders
    noteSensor_ = new DigitalInput(1);
    absoluteEncoder_ = new AnalogInput(0);

    interrupt_ = new AsynchronousInterrupt(noteSensor_, (rising, falling) -> { interruptHandler(rising, falling); }) ;
    interrupt_.setInterruptEdges(true, true);            
    interrupt_.enable();
    
    inputIndicies = AKInput.add(NAME, "Sensor", false);
  }

  private void interruptHandler(Boolean rising, Boolean falling) {

    Logger.recordOutput("rising",rising) ;
    Logger.recordOutput("falling", falling) ;

    if (falling) {
        sensor_edge_seen_ = true ;
    }
  }

  public XeroTalon getFeeder(){
    return feeder;
  }
  
  public XeroTalon getUpDown(){
    return up_down;
  }

  public XeroTalon getShooter1(){
    return shooter1;
  }

  public XeroTalon getShooter2(){
    return shooter2;
  }

  public XeroTalon getTilt(){
    return tilt;
  }

  public TalonFX getRawFeeder(){
    return feeder.getMotor();
  }

  public TalonFX getRawUpDown(){
    return up_down.getMotor();
  }

  public TalonFX getRawShooter1(){
    return shooter1.getMotor();
  }

  public TalonFX getRawShooter2(){
    return shooter2.getMotor();
  }

  public TalonFX getRawTilt(){
    return tilt.getMotor();
  }

  @Override
  public void periodic() {
    is_note_present_ = sensor_edge_seen_ ;
    sensor_edge_seen_ = false ;

    double eval = absoluteEncoder_.getVoltage();
    angle_ = eval;

    updateInputs();
  }

  private void updateInputs(){
    AKInput.update(inputIndicies, Boolean.valueOf(is_note_present_));
  }

  public void spin(double rps){
    shooter1.setVelocity(rps);
  }

  public void stop(){
    shooter1.stop();
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
