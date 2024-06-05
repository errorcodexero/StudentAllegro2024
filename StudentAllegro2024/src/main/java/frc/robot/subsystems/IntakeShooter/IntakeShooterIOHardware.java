// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.IntakeShooter;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import EncoderMapper.EncoderMapper;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AsynchronousInterrupt;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Constants.IntakeShooterConstants.FeederConstants;
import frc.robot.Constants.IntakeShooterConstants.ShooterConstants;
import frc.robot.Constants.IntakeShooterConstants.TiltConstants;
import frc.robot.Constants.IntakeShooterConstants.UpDownConstants;

public class IntakeShooterIOHardware implements IntakeShooterIO{
  /** Creates a new ExampleSubsystem. */
  private TalonFX feeder_;
  private TalonFX upDown_;
  private TalonFX shooter1_;
  private TalonFX shooter2_;
  private TalonFX tilt_;

  private AsynchronousInterrupt interrupt_ ;
  private boolean sensor_edge_seen_ ;

  private DigitalInput noteSensor_; 
  private boolean noteSensorInverted_;
  private AnalogInput absoluteEncoder_;
  private EncoderMapper encoderMapper_;

  // This is true if the sensor is currently detecting a note
  private boolean is_note_present_;

  private double angle_;
  private int timesSeenSensor_ = 0;

  private boolean sensor_for_logging = false;

  public IntakeShooterIOHardware() {
    feeder_ = new TalonFX(TiltConstants.CANID);
    upDown_ = new TalonFX(UpDownConstants.CANID);
    shooter1_ = new TalonFX(ShooterConstants.CANID1);
    shooter2_ = new TalonFX(ShooterConstants.CANID2);
    tilt_ = new TalonFX(TiltConstants.CANID);

    Slot0Configs feederPIDS = new Slot0Configs();
    feederPIDS.kP = FeederConstants.kP;
    feederPIDS.kD = FeederConstants.kD;
    feederPIDS.kV = FeederConstants.kV;
    feeder_.getConfigurator().apply(feederPIDS);
    feeder_.setInverted(FeederConstants.inverted);

    Slot0Configs upDownPIDS = new Slot0Configs();
    upDownPIDS.kP = 0.0;
    upDownPIDS.kV = 0.0;
    upDown_.setPosition(117.0/360.0);
    upDown_.getConfigurator().apply(upDownPIDS);
    upDown_.setInverted(UpDownConstants.inverted);

    Slot0Configs shooter1PIDS = new Slot0Configs();
    shooter1PIDS.kP = 0.0;
    shooter1PIDS.kV = 0.0;
    shooter1_.getConfigurator().apply(shooter1PIDS);
    shooter1_.setInverted(ShooterConstants.inverted);

    Slot0Configs shooter2PIDS = new Slot0Configs();
    shooter2PIDS.kP = 0.0;
    shooter2PIDS.kV = 0.0;
    shooter2_.getConfigurator().apply(shooter2PIDS);
    shooter2_.setInverted(!ShooterConstants.inverted);

    Slot0Configs tiltPIDS = new Slot0Configs();
    tiltPIDS.kP = 0.0;
    tiltPIDS.kV = 0.0;
    tilt_.getConfigurator().apply(tiltPIDS);
    tilt_.setInverted(TiltConstants.inverted);

    noteSensor_ = new DigitalInput(1);
    noteSensorInverted_ = true;
    absoluteEncoder_ = new AnalogInput(0);
    encoderMapper_ = new EncoderMapper(5.0, 0, 90, -90);
    encoderMapper_.calibrate(-72, 4.283);

    interrupt_ = new AsynchronousInterrupt(noteSensor_, (rising, falling) -> { interruptHandler(rising, falling); }) ;
    interrupt_.setInterruptEdges(true, true);            
    interrupt_.enable();
  }
  private void interruptHandler(Boolean rising, Boolean falling) {

    Logger.recordOutput("rising",rising);
    Logger.recordOutput("falling", falling);

    if (falling == noteSensorInverted_) {
      sensor_edge_seen_ = true;
      sensor_for_logging = false;
    }

    if(rising == noteSensorInverted_){
      sensor_for_logging = true;
    }
  }

  public TalonFX getFeeder(){
    return feeder_;
  }

  public void stopFeeder(){
    feeder_.stopMotor();
  }

  public void spinFeeder(double rps){
    feeder_.setControl(new VelocityVoltage(rps * FeederConstants.gearRatio));
  }
  
  public TalonFX getUpDown(){
    return upDown_;
  }

  public void stopUpDown(){
    upDown_.stopMotor();
  }

  public void moveUpDown(double revs){
    upDown_.setControl(new MotionMagicVoltage(revs * UpDownConstants.gearRatio));
  }

  public void moveUpDownPurePID(double degs){
    upDown_.setControl(new PositionVoltage(degs / 360));
  }

  public void moveUpDownRevs(double revs){
    moveUpDown(revs);
  }

  public void moveUpDownDegrees(double degs){
    moveUpDown(degs / 360.0);
  }

  public void moveUpDownRadians(double rads){
    moveUpDown(rads/(2 * Math.PI));
  }

  public TalonFX getShooter1(){
    return shooter1_;
  }

  public void stopShooter1(){
    shooter1_.stopMotor();
  }

  public void spinShooter1(double rps){
    shooter1_.setControl(new VelocityVoltage(rps * ShooterConstants.gearRatio));
  }

  public TalonFX getShooter2(){
    return shooter2_;
  }

  public void stopShooter2(){
    shooter2_.stopMotor();
  }

  public void spinShooter2(double rps){
    shooter2_.setControl(new VelocityVoltage(rps * ShooterConstants.gearRatio));
  }

  public TalonFX getTilt(){
    return tilt_;
  }

  public void stopTilt(){
    tilt_.stopMotor();
  }

  public void moveTilt(double revs){
    tilt_.setControl(new MotionMagicVoltage(revs * TiltConstants.gearRatio));
  }

  public void moveTiltPurePID(double degs){
    tilt_.setControl(new PositionVoltage(degs / 360));
  }

  public void moveTiltRevs(double revs){
    moveTilt(revs);
  }

  public void moveTiltDegrees(double degs){
    moveTilt(degs / 360.0);
  }

  public void moveTiltRadians(double rads){
    moveTilt(rads/(2 * Math.PI));
  }

  public boolean hasNote(){
    return is_note_present_;
  }

  public boolean sensorVal(){
    return sensor_for_logging;
  }

  public void update(IntakeShooterIOInputsAutoLogged inputs) {
    timesSeenSensor_ += sensor_edge_seen_ ? 1 : 0;
    is_note_present_ = timesSeenSensor_ % 2 == 1;

    double eval = absoluteEncoder_.getVoltage();
    angle_ = encoderMapper_.toRobot(eval);

    if(tilt_.getPosition().getValueAsDouble() % 1 - angle_ > 1/180){
      updateMotorPosition();
    }

    updateInputs(inputs);
  }

  private void updateInputs (IntakeShooterIOInputsAutoLogged inputs){
    inputs.feederPosition = feeder_.getPosition().getValueAsDouble();
    inputs.feederCurrent = feeder_.getSupplyCurrent().getValueAsDouble();
    inputs.feederAcceleration = feeder_.getAcceleration().getValueAsDouble();
    inputs.feederVelocity = feeder_.getVelocity().getValueAsDouble();
    inputs.feederVoltage = feeder_.getMotorVoltage().getValueAsDouble();

    inputs.upDownPosition = upDown_.getPosition().getValueAsDouble();
    inputs.upDownCurrent = upDown_.getSupplyCurrent().getValueAsDouble();
    inputs.upDownAcceleration = upDown_.getAcceleration().getValueAsDouble();
    inputs.upDownVelocity = upDown_.getVelocity().getValueAsDouble();
    inputs.upDownVoltage = upDown_.getMotorVoltage().getValueAsDouble();

    inputs.shooter1Position = shooter1_.getPosition().getValueAsDouble();
    inputs.shooter1Current = shooter1_.getSupplyCurrent().getValueAsDouble();
    inputs.shooter1Acceleration = shooter1_.getAcceleration().getValueAsDouble();
    inputs.shooter1Velocity = shooter1_.getVelocity().getValueAsDouble();
    inputs.shooter1Voltage = shooter1_.getMotorVoltage().getValueAsDouble();

    inputs.shooter2Position = shooter2_.getPosition().getValueAsDouble();
    inputs.shooter2Current = shooter2_.getSupplyCurrent().getValueAsDouble();
    inputs.shooter2Acceleration = shooter2_.getAcceleration().getValueAsDouble();
    inputs.shooter2Velocity = shooter2_.getVelocity().getValueAsDouble();
    inputs.shooter2Voltage = shooter2_.getMotorVoltage().getValueAsDouble();

    inputs.tiltPosition = tilt_.getPosition().getValueAsDouble();
    inputs.tiltCurrent = tilt_.getSupplyCurrent().getValueAsDouble();
    inputs.tiltAcceleration = tilt_.getAcceleration().getValueAsDouble();
    inputs.tiltVelocity = tilt_.getVelocity().getValueAsDouble();
    inputs.tiltVoltage = tilt_.getMotorVoltage().getValueAsDouble();

    inputs.encoderPosition = encoderMapper_.toRobot(absoluteEncoder_.getVoltage());

    inputs.sensorVal = sensor_for_logging;
    inputs.hasNote = is_note_present_;
  }

  private void updateMotorPosition(){
    tilt_.setPosition(angle_/360.0);
  }
}
