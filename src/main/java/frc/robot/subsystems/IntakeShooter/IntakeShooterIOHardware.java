// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.IntakeShooter;

import static edu.wpi.first.units.Units.Rotations;

import org.xero1425.misc.EncoderMapper;

import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.units.Angle;
import edu.wpi.first.units.Measure;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AsynchronousInterrupt;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.subsystems.IntakeShooter.IntakeShooterConstants.FeederConstants;
import frc.robot.subsystems.IntakeShooter.IntakeShooterConstants.ShooterConstants;
import frc.robot.subsystems.IntakeShooter.IntakeShooterConstants.TiltConstants;
import frc.robot.subsystems.IntakeShooter.IntakeShooterConstants.UpDownConstants;

public class IntakeShooterIOHardware implements IntakeShooterIO{
    /** Creates a new ExampleSubsystem. */
    private TalonFX feeder_;
    private TalonFX upDown_;
    private TalonFX shooter1_;
    private TalonFX shooter2_;
    private TalonFX tilt_;
    
    private AsynchronousInterrupt interrupt_ ;
    private boolean sensorVal_;
    // private boolean sensor_edge_seen_ ;
    
    private DigitalInput noteSensor_; 
    // private boolean noteSensorInverted_;
    private AnalogInput absoluteEncoder_;
    private EncoderMapper encoderMapper_;
    
    // This is true if the sensor is currently detecting a note
    // private boolean isNotePresent_;
    
    // private double angle_;
    // private int timesSeenSensor_ = 0;
    
    // private boolean sensor_for_logging_ = false;
    
    public IntakeShooterIOHardware() {
        feeder_ = new TalonFX(FeederConstants.CANID);
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
        
        TalonFXConfiguration upDownConfig = new TalonFXConfiguration();
        
        Slot0Configs upDownPIDS = upDownConfig.Slot0;
        upDownPIDS.kP = UpDownConstants.kP;
        upDownPIDS.kD = UpDownConstants.kD;
        upDownPIDS.kV = UpDownConstants.kV;
        
        MotionMagicConfigs upDownMagicConfigs = upDownConfig.MotionMagic;
        upDownMagicConfigs.MotionMagicCruiseVelocity = UpDownConstants.maxv;
        upDownMagicConfigs.MotionMagicAcceleration = UpDownConstants.maxa;
        upDownMagicConfigs.MotionMagicJerk = UpDownConstants.jerk;
        upDown_.setPosition((UpDownConstants.stowTarget/360.0) * UpDownConstants.gearRatio);
        upDown_.getConfigurator().apply(upDownConfig);
        upDown_.setInverted(UpDownConstants.inverted);
        
        Slot0Configs shooterPIDS = new Slot0Configs();
        shooterPIDS.kP = ShooterConstants.kP;
        shooterPIDS.kD = ShooterConstants.kD;
        shooterPIDS.kV = ShooterConstants.kV;
        shooter1_.getConfigurator().apply(shooterPIDS);
        shooter1_.setInverted(ShooterConstants.inverted1);
        shooter2_.getConfigurator().apply(shooterPIDS);
        shooter2_.setInverted(ShooterConstants.inverted2);
        
        TalonFXConfiguration tiltConfig = new TalonFXConfiguration();
        
        Slot0Configs tiltPIDS = tiltConfig.Slot0;
        tiltPIDS.kP = TiltConstants.kP;
        tiltPIDS.kD = TiltConstants.kD;
        tiltPIDS.kV = TiltConstants.kV;
        
        MotionMagicConfigs tiltMagicConfigs = tiltConfig.MotionMagic;
        tiltMagicConfigs.MotionMagicCruiseVelocity = TiltConstants.maxv;
        tiltMagicConfigs.MotionMagicAcceleration = TiltConstants.maxa;
        tiltMagicConfigs.MotionMagicJerk = TiltConstants.jerk;
        tilt_.getConfigurator().apply(tiltConfig);
        //tilt_.setInverted(TiltConstants.inverted); TODO: this is not hardware specific, and why would we ever change this?
        //tilt_.setPosition((TiltConstants.stowTarget/360) * TiltConstants.gearRatio); TODO: what is this??
        
        noteSensor_ = new DigitalInput(1);
        // noteSensorInverted_ = true;
        absoluteEncoder_ = new AnalogInput(0);
        encoderMapper_ = new EncoderMapper(90, -90, 5.0, 0);
        encoderMapper_.calibrate(-72, 4.283);
        
        interrupt_ = new AsynchronousInterrupt(noteSensor_, (rising, falling) -> { interruptHandler(rising, falling); }) ;
        interrupt_.setInterruptEdges(true, true);            
        interrupt_.enable();
        
        // TODO: are these really needed?
        // double encVal = absoluteEncoder_.getVoltage();
        // angle_ = encoderMapper_.toRobot(encVal);
        // tilt_.setPosition((angle_/360.0) * TiltConstants.gearRatio); TODO: what does this do?
        
        // moveState_ = MoveState.Starting;
    }
    
    // TODO: move this functionality into the subsystem layer instead of the io layer or go on without it
    // public boolean moveSystem(double tilt, double updown){
    //     double forTiltOnPath = IntakeShooterConstants.tiltDiffFromUpDown - getUpDownPosition();
    //     switch (moveState_) {
    //         case Starting:
    //             if(Math.abs(forTiltOnPath - getTiltPosition()) > IntakeShooterConstants.otherOKThresh){
    //                 tilt_.setControl(new MotionMagicVoltage((forTiltOnPath / 360) * TiltConstants.gearRatio));
    //             }
    //             moveState_ = MoveState.Start;
    //             break;
    //         case Start:
    //             if(Math.abs(forTiltOnPath - getTiltPosition()) < IntakeShooterConstants.otherOKThresh){
    //                 int sign = updown - getUpDownPosition() < 0 ? -1 : 1;
    //                 tilt_.setControl(new VelocityVoltage(-IntakeShooterConstants.tiltUpDownSpeed * TiltConstants.gearRatio * sign));
    //                 upDown_.setControl(new VelocityVoltage(IntakeShooterConstants.tiltUpDownSpeed * UpDownConstants.gearRatio * sign));
    //             }
    //             moveState_ = MoveState.Path;
    //             break;
    //         case Path:
    //             if(Math.abs(getUpDownPosition() - updown) < IntakeShooterConstants.otherOKThresh){
    //                 tilt_.stopMotor();
    //                 upDown_.stopMotor();
    //                 tilt_.setControl(new MotionMagicVoltage((tilt / 360) * TiltConstants.gearRatio));
    //                 moveState_ = MoveState.End;
    //             }
    //         case End:
    //             if(Math.abs(getTiltPosition() - tilt) < IntakeShooterConstants.otherOKThresh){
    //                 moveState_ = MoveState.Done;
    //             }
    //             break;
    //         case Done:
    //             moveState_ = MoveState.Starting;
    //             return true;
    //     }
    //     return false;
    // }
    
    @Override
    public void updateInputs(IntakeShooterIOInputsAutoLogged inputs) {
        // TODO: what is the point of these if you can just do !sensorVal?
        // timesSeenSensor_ += sensor_edge_seen_ ? 1 : 0;
        // isNotePresent_ = timesSeenSensor_ % 2 == 1;
        
        // double encVal = absoluteEncoder_.getVoltage();
        // angle_ = encoderMapper_.toRobot(encVal);
        
        // TODO: what is this?
        // if(Math.abs(getTiltPosition() - angle_) > 2){
        //   tilt_.setPosition((angle_/360.0) * TiltConstants.gearRatio);
        // }
        
        inputs.feederPosition = feeder_.getPosition().getValueAsDouble() * 360 / FeederConstants.gearRatio;
        inputs.feederCurrent = feeder_.getSupplyCurrent().getValueAsDouble();
        inputs.feederAcceleration = feeder_.getAcceleration().getValueAsDouble() / FeederConstants.gearRatio;
        inputs.feederVelocity = feeder_.getVelocity().getValueAsDouble() / FeederConstants.gearRatio;
        inputs.feederVoltage = feeder_.getMotorVoltage().getValueAsDouble();
        
        inputs.upDownPosition = upDown_.getPosition().getValueAsDouble() * 360 / UpDownConstants.gearRatio;
        inputs.upDownCurrent = upDown_.getSupplyCurrent().getValueAsDouble();
        inputs.upDownAcceleration = upDown_.getAcceleration().getValueAsDouble() * 360 / UpDownConstants.gearRatio;
        inputs.upDownVelocity = upDown_.getVelocity().getValueAsDouble() * 360 / UpDownConstants.gearRatio;
        inputs.upDownVoltage = upDown_.getMotorVoltage().getValueAsDouble();
        
        inputs.shooter1Position = shooter1_.getPosition().getValueAsDouble() * 360 / ShooterConstants.gearRatio;
        inputs.shooter1Current = shooter1_.getSupplyCurrent().getValueAsDouble();
        inputs.shooter1Acceleration = shooter1_.getAcceleration().getValueAsDouble() / ShooterConstants.gearRatio;
        inputs.shooter1Velocity = shooter1_.getVelocity().getValueAsDouble() / ShooterConstants.gearRatio;
        inputs.shooter1Voltage = shooter1_.getMotorVoltage().getValueAsDouble();
        
        inputs.shooter2Position = shooter2_.getPosition().getValueAsDouble() * 360 / ShooterConstants.gearRatio;
        inputs.shooter2Current = shooter2_.getSupplyCurrent().getValueAsDouble();
        inputs.shooter2Acceleration = shooter2_.getAcceleration().getValueAsDouble() / ShooterConstants.gearRatio;
        inputs.shooter2Velocity = shooter2_.getVelocity().getValueAsDouble() / ShooterConstants.gearRatio;
        inputs.shooter2Voltage = shooter2_.getMotorVoltage().getValueAsDouble();
        
        inputs.tiltPosition = tilt_.getPosition().getValueAsDouble() * 360 / TiltConstants.gearRatio;
        inputs.tiltCurrent = tilt_.getSupplyCurrent().getValueAsDouble();
        inputs.tiltAcceleration = tilt_.getAcceleration().getValueAsDouble() * 360 / TiltConstants.gearRatio;
        inputs.tiltVelocity = tilt_.getVelocity().getValueAsDouble() * 360 / TiltConstants.gearRatio;
        inputs.tiltVoltage = tilt_.getMotorVoltage().getValueAsDouble();
        
        inputs.encoderPosition = encoderMapper_.toRobot(absoluteEncoder_.getVoltage());
        
        inputs.hasNote = !sensorVal_;
    }
    
    @Override
    public void moveUpDown(Measure<Angle> angle){
        upDown_.setControl(new MotionMagicVoltage(angle.in(Rotations)).withSlot(0));
    }
    
    @Override
    public void moveTilt(Measure<Angle> angle){
        tilt_.setControl(new MotionMagicVoltage(angle.in(Rotations) * TiltConstants.gearRatio).withSlot(0));
    }
    
    @Override
    public void spinFeeder(double rps){
        feeder_.setControl(new VelocityVoltage(rps).withSlot(0));
    }
    
    @Override
    public void spinShooter1(double rps){
        shooter1_.setControl(new VelocityVoltage(rps / ShooterConstants.gearRatio).withSlot(0));
    }
    
    @Override
    public void spinShooter2(double rps){
        shooter2_.setControl(new VelocityVoltage(rps * ShooterConstants.gearRatio).withSlot(0));
    }

    @Override
    public void stopUpDown() {
        upDown_.stopMotor();
    }

    @Override
    public void stopTilt() {
        tilt_.stopMotor();
    }

    @Override
    public void stopFeeder() {
        feeder_.stopMotor();
    }

    @Override
    public void stopShooter1() {
        shooter1_.stopMotor();
    }

    @Override
    public void stopShooter2() {
        shooter2_.stopMotor();
    }
    
    // TODO: why use an interrupt if we are only going to get the data every robot loop anyways?
    private void interruptHandler(Boolean rising, Boolean falling) {
        if (falling) {
            sensorVal_ = false;
        }
        
        if (rising) {
            sensorVal_ = true;
        }
    }
    
}