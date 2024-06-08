package frc.robot.subsystems.IntakeShooter;

import XeroTalon.XeroTalon;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AsynchronousInterrupt;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.IntakeShooter.IntakeShooterConstants.FeederConstants;
import frc.robot.subsystems.IntakeShooter.IntakeShooterConstants.UpDownConstants;
import frc.robot.subsystems.IntakeShooter.IntakeShooterConstants.ShooterConstants;
import frc.robot.subsystems.IntakeShooter.IntakeShooterConstants.TiltConstants;
import frc.robot.subsystems.TargetTracker.TargetTrackerSubsystem.TTShooter;
import frc.robot.subsystems.oi.type.ActionType;
import frc.robot.subsystems.oi.type.ShootType;

import java.security.InvalidAlgorithmParameterException;
import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.PositionVoltage;

import AKInput.AKInput;
import EncoderMapper.EncoderMapper;

public class IntakeShooterSubsystemAbstraction extends SubsystemBase{

    public enum State{
        Idle,
        Intake,
        PrepForShoot,
        Shoot,
        Transfer,
        Eject,
        Stow,
    }

    private enum IntakeState{
        Start,
        MoveToIntakeStart,
        MoveToIntake,
        WaitingForNote,
        Cancelling,
        HasNoteIdle,
        MoveToShootStart,
        MoveToShoot,
        MoveToTransferStart,
        MoveToTransfer,
        Done
    }

    private enum StowState{
        Start,
        End,
        Done
    }

    private enum TransferState{
        Start,
        WaitingForSensorStart,
        WaitingForSensorEnd,
        End,
        Done
    }

    private XeroTalon feeder_;
    private XeroTalon upDown_;
    private XeroTalon shooter1_;
    private XeroTalon shooter2_;
    private XeroTalon tilt_;

    private AsynchronousInterrupt interrupt_ ;
    private boolean sensorEdgeSeen_ ;

    private DigitalInput noteSensor_; 
    private boolean noteSensorInverted_;
    private AnalogInput absoluteEncoder_;
    private EncoderMapper encoderMapper_;
  
    // This is true if the sensor is currently detecting a note
    private boolean hasNote_;
  
    private double angle_;
    private int timesSeenSensor_ = 0;
  
    private boolean sensorForLogging_ = false;

    private State state_;
    private IntakeState intakeState_;
    private StowState stowState_;
    private TransferState transferState_;

    private Supplier<ActionType> intakeNextAction_;
    private Supplier<ShootType> shootingType_;

    private TTShooter shootingTargets_;
    private double upDownShootTarget_;
    private double tiltShootTarget_;
    private double shooterShootTarget_;
    private Supplier<Boolean> DBReady_;
    private Supplier<Boolean> aprilTagReady_;

    private double intakeFeederStartPosSeenNote_;
    private double transferShooterStartPosSeenNote_;

    private boolean runOnceShootPrep_ = true;
    private boolean runOnceEject_ = true;
    private boolean weAreShooting_ = false;

    private int[] encInputIndicies_;
    private int[] sensorInputIndicies_;
    private int[] hasNoteInputIndicies_;

    public IntakeShooterSubsystemAbstraction (Supplier<ActionType> actionType, Supplier<ShootType> shootType, TTShooter shootingTargets){
        feeder_ = new XeroTalon(FeederConstants.CANID, IntakeShooterConstants.name, "Feeder", FeederConstants.gearRatio, FeederConstants.inverted);
        Slot0Configs feederPIDS = new Slot0Configs();
        feederPIDS.kP = FeederConstants.kP;
        feederPIDS.kD = FeederConstants.kD;
        feederPIDS.kV = FeederConstants.kV;
        feeder_.setPIDs(feederPIDS);

        upDown_ = new XeroTalon(UpDownConstants.CANID, IntakeShooterConstants.name, "Up Down", UpDownConstants.gearRatio, UpDownConstants.inverted);
        Slot0Configs upDownPIDS = new Slot0Configs();
        upDownPIDS.kP = UpDownConstants.kP;
        upDownPIDS.kD = UpDownConstants.kD;
        upDownPIDS.kV = UpDownConstants.kV;
        upDown_.setPIDs(upDownPIDS);

        shooter1_ = new XeroTalon(ShooterConstants.CANID1, IntakeShooterConstants.name, "Shooter 1", ShooterConstants.gearRatio, ShooterConstants.inverted1);
        shooter2_ = new XeroTalon(ShooterConstants.CANID2, IntakeShooterConstants.name, "Shooter 2", ShooterConstants.gearRatio, ShooterConstants.inverted2);
        Slot0Configs shooterPIDS = new Slot0Configs();
        shooterPIDS.kP = ShooterConstants.kP;
        shooterPIDS.kD = ShooterConstants.kD;
        shooterPIDS.kV = ShooterConstants.kV;
        shooter1_.setPIDs(shooterPIDS);
        shooter2_.setPIDs(shooterPIDS);

        tilt_ = new XeroTalon(TiltConstants.CANID, IntakeShooterConstants.name, "Tilt", TiltConstants.gearRatio, TiltConstants.inverted);
        Slot0Configs tiltPIDS = new Slot0Configs();
        tiltPIDS.kP = TiltConstants.kP;
        tiltPIDS.kD = TiltConstants.kD;
        tiltPIDS.kV = TiltConstants.kV;
        tilt_.setPIDs(tiltPIDS);

        intakeNextAction_ = actionType;
        shootingType_ = shootType;
        shootingTargets_ = shootingTargets;

        noteSensor_ = new DigitalInput(1);
        noteSensorInverted_ = true;
        absoluteEncoder_ = new AnalogInput(0);
        encoderMapper_ = new EncoderMapper(5.0, 0, 90, -90);
        encoderMapper_.calibrate(-72, 4.283);

        interrupt_ = new AsynchronousInterrupt(noteSensor_, (rising, falling) -> { interruptHandler(rising, falling); }) ;
        interrupt_.setInterruptEdges(true, true);            
        interrupt_.enable();

        encInputIndicies_ = AKInput.add(IntakeShooterConstants.name, "Encoder Value", encoderMapper_.toRobot(absoluteEncoder_.getVoltage()));
        sensorInputIndicies_ = AKInput.add(IntakeShooterConstants.name, "Sensor Value", sensorForLogging_);
        hasNoteInputIndicies_ = AKInput.add(IntakeShooterConstants.name, "Has Note", hasNote_);
    }
    private void interruptHandler(Boolean rising, Boolean falling) {

        Logger.recordOutput("rising",rising);
        Logger.recordOutput("falling", falling);

        if (falling == noteSensorInverted_) {
        sensorEdgeSeen_ = true;
        sensorForLogging_ = false;
        }

        if(rising == noteSensorInverted_){
        sensorForLogging_ = true;
        }
    }

    public boolean tiltReady(){
        boolean upDownGood = Math.abs(upDown_.getPosition() - upDownShootTarget_) < IntakeShooterConstants.shootOKThresh && Math.abs(upDown_.getVelocity()) < IntakeShooterConstants.otherOKThresh;
        boolean tiltGood = Math.abs(tilt_.getPosition() - tiltShootTarget_) < IntakeShooterConstants.shootOKThresh && Math.abs(tilt_.getVelocity()) < IntakeShooterConstants.otherOKThresh;
        return (state_ == State.Shoot || state_ == State.PrepForShoot) && upDownGood && tiltGood;
    }

    public boolean shooterReady(){
        boolean shooter1Good = Math.abs(shooter1_.getVelocity() - shooterShootTarget_) < IntakeShooterConstants.shootOKThresh;
        boolean shooter2Good = Math.abs(shooter2_.getVelocity() - shooterShootTarget_) < IntakeShooterConstants.shootOKThresh;
        return shooter1Good && shooter2Good;
    }
    
    private void updateMotorPosition(){
        tilt_.getMotor().setPosition(angle_/360.0);
    }

    private void updateInputs(){
        AKInput.update(encInputIndicies_, encoderMapper_.toRobot(absoluteEncoder_.getVoltage()));
        AKInput.update(sensorInputIndicies_, sensorForLogging_);
        AKInput.update(hasNoteInputIndicies_, hasNote_);
    }

    @Override
    public void periodic(){
        timesSeenSensor_ += sensorEdgeSeen_ ? 1 : 0;
        hasNote_ = timesSeenSensor_ % 2 == 1;

        double eval = absoluteEncoder_.getVoltage();
        angle_ = encoderMapper_.toRobot(eval);

        if(tilt_.getPosition() % 1 - angle_ > 1/180){
            updateMotorPosition();
        }

        updateInputs();

        Logger.recordOutput("Intake Shooter State", state_);
        Logger.recordOutput("Intake State", intakeState_);
        Logger.recordOutput("Transfer State", transferState_);
        Logger.recordOutput("Stow State", stowState_);

        switch (state_) {
            case Idle:
                break;
            case Intake:
                try {
                    intake();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }
                break;
            case PrepForShoot:
                try {
                    prepShoot();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }
                break;
            case Shoot:
                try {
                    shoot();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }
                break;
            case Stow:
                stow();
                break;
            case Transfer:
                try {
                    transfer();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }
                break;
            case Eject:
                eject();
                break;
            default:
                state_ = State.Idle;
                break;
        }
    }

    public void forceChangeState(State state){
        state_ = state;
    }

    public void stow(){
        state_ = State.Stow;
        switch (stowState_) {
            case Start:
                abort();
                upDown_.motionMagicGotoDegrees(UpDownConstants.stowTarget);
                stowState_ = StowState.End;
                break;
            case End:
                boolean upDownPastZone = Math.abs(upDown_.getPosition() - UpDownConstants.tiltCanMoveIntakeTarget) < IntakeShooterConstants.otherOKThresh;
                boolean upDownDone = Math.abs(upDown_.getPosition() - UpDownConstants.stowTarget) < IntakeShooterConstants.otherOKThresh && Math.abs(upDown_.getVelocity()) < IntakeShooterConstants.otherOKThresh;
                boolean tiltDone = Math.abs(tilt_.getPosition() - TiltConstants.stowTarget) < IntakeShooterConstants.otherOKThresh && Math.abs(tilt_.getVelocity()) < IntakeShooterConstants.otherOKThresh;
                if(upDownPastZone){
                    tilt_.motionMagicGotoDegrees(TiltConstants.stowTarget);
                }
                if(upDownDone && tiltDone){
                    state_ = State.Idle;
                }
            default:
                stowState_ = StowState.Start;
                break;
        }
    }

    public void cancelIntake(){
        intakeState_ = IntakeState.Cancelling;
    }

    public void intake() throws InvalidAlgorithmParameterException{
        if(state_ != State.Intake && state_ != State.Idle){
            throw new InvalidAlgorithmParameterException("Wrong State!");
        }
        state_ = State.Intake;
        boolean tiltDone;
        boolean upDownDone;
        switch(intakeState_){
            case Start:
                tilt_.motionMagicGotoDegrees(TiltConstants.intakeTarget);
                intakeState_ = IntakeState.MoveToIntakeStart;
            case MoveToIntakeStart:
                if(Math.abs(tilt_.getPosition() - TiltConstants.upDownCanMoveIntakeTarget) < IntakeShooterConstants.otherOKThresh){
                    intakeState_ = IntakeState.MoveToIntake;
                    upDown_.motionMagicGotoDegrees(UpDownConstants.intakeTarget);
                    feeder_.setVelocityDegrees(FeederConstants.intakeTarget);
                }
                break;
            case MoveToIntake:
                tiltDone = Math.abs(tilt_.getPosition() - TiltConstants.intakeTarget) < IntakeShooterConstants.otherOKThresh && Math.abs(tilt_.getVelocity()) < IntakeShooterConstants.otherOKThresh;
                upDownDone = Math.abs(upDown_.getPosition() - UpDownConstants.intakeTarget) < IntakeShooterConstants.otherOKThresh && Math.abs(upDown_.getVelocity()) < IntakeShooterConstants.otherOKThresh;
                if(tiltDone && upDownDone){
                    intakeState_ = IntakeState.WaitingForNote;
                }
                break;
            case WaitingForNote:
                if(hasNote_){
                    intakeFeederStartPosSeenNote_ = feeder_.getPosition();
                    intakeState_ = IntakeState.HasNoteIdle;
                }
                break;
            case Cancelling:
                state_ = State.Stow;
            case HasNoteIdle:
                if(Math.abs(intakeFeederStartPosSeenNote_ + FeederConstants.keepSpinningIntakeTarget - feeder_.getPosition()) < 2.0){
                    feeder_.stop();
                    switch (intakeNextAction_.get()) {
                        case SPEAKER:
                            intakeState_ = IntakeState.MoveToShootStart;
                            upDown_.motionMagicGotoDegrees(UpDownConstants.subShootTarget);
                            break;
                        default:
                            intakeState_ = IntakeState.MoveToTransferStart;
                            upDown_.motionMagicGotoDegrees(UpDownConstants.transferTarget);
                            break;
                    }
                }
                break;
            case MoveToShootStart:
                if(Math.abs(upDown_.getPosition() - UpDownConstants.tiltCanMoveIntakeTarget) < IntakeShooterConstants.otherOKThresh){
                    intakeState_ = IntakeState.MoveToShoot;
                    tilt_.motionMagicGotoDegrees(TiltConstants.subShootTarget);
                    shooter1_.setVelocity(ShooterConstants.subShootTarget);
                    shooter2_.setVelocity(ShooterConstants.subShootTarget);
                }
                break;
            case MoveToShoot:
                tiltDone = Math.abs(tilt_.getPosition() - TiltConstants.subShootTarget) < IntakeShooterConstants.otherOKThresh && Math.abs(tilt_.getVelocity()) < IntakeShooterConstants.otherOKThresh;
                upDownDone = Math.abs(upDown_.getPosition() - UpDownConstants.subShootTarget) < IntakeShooterConstants.otherOKThresh && Math.abs(upDown_.getVelocity()) < IntakeShooterConstants.otherOKThresh;
                if(tiltDone && upDownDone){
                    state_ = State.PrepForShoot;
                }
                break;
            case MoveToTransferStart:
                if(Math.abs(upDown_.getPosition() - UpDownConstants.tiltCanMoveIntakeTarget) < IntakeShooterConstants.otherOKThresh){
                    intakeState_ = IntakeState.MoveToTransfer;
                    tilt_.motionMagicGotoDegrees(TiltConstants.transferTarget);
                }
                break;
            case MoveToTransfer:
                tiltDone = Math.abs(tilt_.getPosition() - TiltConstants.transferTarget) < IntakeShooterConstants.otherOKThresh && Math.abs(tilt_.getVelocity()) < 5;
                upDownDone = Math.abs(upDown_.getPosition() - UpDownConstants.transferTarget) < IntakeShooterConstants.otherOKThresh && Math.abs(upDown_.getVelocity()) < 5;
                if(tiltDone && upDownDone){
                    state_ = State.Transfer;
                    intakeState_ = IntakeState.Done;
                }
                break;
            default:
                intakeState_ = IntakeState.MoveToIntakeStart;
        }
        Logger.recordOutput("Intake State", intakeState_);
    }

    public void prepShoot() throws InvalidAlgorithmParameterException{
        if(state_ != State.PrepForShoot && state_ != State.Intake){
            throw new InvalidAlgorithmParameterException("Wrong State!");
        }
        state_ = State.PrepForShoot;

        switch (shootingType_.get()) {
            case AUTO:
                shooterShootTarget_ = shootingTargets_.getShooterVel();
                tiltShootTarget_ = shootingTargets_.getTiltPos();
                upDownShootTarget_ = shootingTargets_.getUpDownPos();
                break;
            case PODIUM:
                shooterShootTarget_ = ShooterConstants.podShootTarget;
                tiltShootTarget_ = TiltConstants.podShootTarget;
                upDownShootTarget_ = UpDownConstants.podShootTarget;
                break;
            case SUBWOOFER:
                shooterShootTarget_ = ShooterConstants.subShootTarget;
                tiltShootTarget_ = TiltConstants.subShootTarget;
                upDownShootTarget_ = UpDownConstants.subShootTarget;
                break;
        }
        if(runOnceShootPrep_ || shootingType_.get() == ShootType.AUTO){
            shooter1_.setVelocity(shooterShootTarget_);
            shooter2_.setVelocity(shooterShootTarget_);
            tilt_.getMotor().setControl(new PositionVoltage(tiltShootTarget_));
            upDown_.getMotor().setControl(new PositionVoltage(upDownShootTarget_));
            runOnceShootPrep_ = false;
        }
    }

    public void shoot() throws InvalidAlgorithmParameterException{
        if(state_ != State.PrepForShoot && state_ != State.Shoot){
            throw new InvalidAlgorithmParameterException("Wrong State!");
        }
        state_ = State.Shoot;
        runOnceShootPrep_ = false;
        boolean shooter1Good = Math.abs(shooter1_.getVelocity() - shooterShootTarget_) < IntakeShooterConstants.shootOKThresh;
        boolean shooter2Good = Math.abs(shooter2_.getVelocity() - shooterShootTarget_) < IntakeShooterConstants.shootOKThresh;
        boolean upDownGood = Math.abs(upDown_.getPosition() - upDownShootTarget_) < IntakeShooterConstants.shootOKThresh && Math.abs(upDown_.getVelocity()) < IntakeShooterConstants.otherOKThresh;
        boolean tiltGood = Math.abs(tilt_.getPosition() - tiltShootTarget_) < IntakeShooterConstants.shootOKThresh && Math.abs(tilt_.getVelocity()) < IntakeShooterConstants.otherOKThresh;
        Command wait = new Command(){};
        boolean waitSecsDone = false;
        if(aprilTagReady_.get() && DBReady_.get() && shooter1Good && shooter2Good && upDownGood && tiltGood){
            feeder_.setVelocity(FeederConstants.shootTarget);
            wait = Commands.waitSeconds(IntakeShooterConstants.shootSecs);
            weAreShooting_ = true;
        }
        if(weAreShooting_){
            waitSecsDone = wait.isFinished();
            if(waitSecsDone){
                feeder_.stop();
                shooter1_.stop();
                shooter2_.stop();
                weAreShooting_ = false;
                state_ = State.Stow;
            }
        }
    }


    public void transfer() throws InvalidAlgorithmParameterException{
        if(state_ != State.Transfer && state_ != State.Intake){
            throw new InvalidAlgorithmParameterException("Wrong State!");
        }
        state_ = State.Transfer;
        switch(transferState_){
            case Start:
                shooter1_.setVelocity(ShooterConstants.transferTargetVel);
                shooter2_.setVelocity(ShooterConstants.transferTargetVel);
                feeder_.setVelocity(FeederConstants.transferTarget);
                transferState_ = TransferState.WaitingForSensorStart;
                break;
            case WaitingForSensorStart:
                if(sensorForLogging_){
                    if(!hasNote_){
                        transferState_ = TransferState.End;
                        transferShooterStartPosSeenNote_ = shooter2_.getPosition();
                    }else{
                        transferState_ = TransferState.WaitingForSensorEnd;
                    }
                }
                break;
            case WaitingForSensorEnd:
                if(!hasNote_){
                    transferState_ = TransferState.End;
                    transferShooterStartPosSeenNote_ = shooter2_.getPosition();
                }
                break;
            case End:
                boolean shooterDone = Math.abs(shooter2_.getPosition() - ShooterConstants.transferTargetPos - transferShooterStartPosSeenNote_) < IntakeShooterConstants.otherOKThresh;
                if(shooterDone){
                    shooter1_.stop();
                    shooter2_.stop();
                    feeder_.stop();
                    transferState_ = TransferState.Done;
                    state_ = State.Stow;
                }
                break;
            default:
                transferState_ = TransferState.Start;
            
        }
    }

    public void abort(){
        feeder_.stop();
        upDown_.stop();
        shooter1_.stop();
        shooter2_.stop();
        tilt_.stop();
    }

    public void eject(){
        state_ = State.Eject;
        boolean ejectDone = false;
        Command wait = new Command(){};
        if(runOnceEject_){
            abort();
            shooter1_.setVelocity(ShooterConstants.ejectTarget);
            shooter2_.setVelocity(ShooterConstants.ejectTarget);
            feeder_.setVelocity(ShooterConstants.ejectTarget);
            wait = Commands.waitSeconds(IntakeShooterConstants.ejectSecs);
        }
        ejectDone = wait.isFinished();
        if(ejectDone){
            runOnceEject_ = true;
            state_ = State.Stow;
        }
    }

}
