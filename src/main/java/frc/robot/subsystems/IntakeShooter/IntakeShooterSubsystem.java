package frc.robot.subsystems.IntakeShooter;

import java.security.InvalidAlgorithmParameterException;
import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;

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

public class IntakeShooterSubsystem extends SubsystemBase{
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

    private IntakeShooterIOInputsAutoLogged inputs_ = new IntakeShooterIOInputsAutoLogged();
    private IntakeShooterIO io_;
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

    public IntakeShooterSubsystem (IntakeShooterIO io, Supplier<ActionType> actionType, Supplier<ShootType> shootType, TTShooter shootingTargets){
        io_ = io;
        intakeNextAction_ = actionType;
        shootingType_ = shootType;
        shootingTargets_ = shootingTargets;
    }

    public boolean tiltReady(){
        boolean upDownGood = Math.abs(io_.getUpDownPosition() - upDownShootTarget_) < IntakeShooterConstants.shootOKThresh;
        boolean tiltGood = Math.abs(io_.getTiltPosition() - tiltShootTarget_) < IntakeShooterConstants.shootOKThresh;
        return (state_ == State.Shoot || state_ == State.PrepForShoot) && upDownGood && tiltGood;
    }

    public boolean shooterReady(){
        boolean shooter1Good = Math.abs(io_.getShooter1Velocity() - shooterShootTarget_) < IntakeShooterConstants.shootOKThresh;
        boolean shooter2Good = Math.abs(io_.getShooter2Velocity() - shooterShootTarget_) < IntakeShooterConstants.shootOKThresh;
        return shooter1Good && shooter2Good;
    }
    
    @Override
    public void periodic(){
        io_.update(inputs_);
        Logger.processInputs("IntakeShooterSubsystem", inputs_);
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
                io_.moveUpDownDegrees(UpDownConstants.stowTarget);
                stowState_ = StowState.End;
                break;
            case End:
                boolean upDownPastZone = Math.abs(io_.getUpDownPosition() - UpDownConstants.tiltCanMoveIntakeTarget) < IntakeShooterConstants.otherOKThresh;
                boolean upDownDone = Math.abs(io_.getUpDownPosition() - UpDownConstants.stowTarget) < IntakeShooterConstants.otherOKThresh && Math.abs(io_.getUpDownVelocity()) < IntakeShooterConstants.otherOKThresh;
                boolean tiltDone = Math.abs(io_.getTiltPosition() - TiltConstants.stowTarget) < IntakeShooterConstants.otherOKThresh && Math.abs(io_.getTiltVelocity()) < IntakeShooterConstants.otherOKThresh;
                if(upDownPastZone){
                    io_.moveTiltDegrees(TiltConstants.stowTarget);
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
                io_.moveTiltDegrees(TiltConstants.intakeTarget);
                intakeState_ = IntakeState.MoveToIntakeStart;
            case MoveToIntakeStart:
                if(Math.abs(io_.getTiltPosition() - TiltConstants.upDownCanMoveIntakeTarget) < IntakeShooterConstants.otherOKThresh){
                    intakeState_ = IntakeState.MoveToIntake;
                    io_.moveUpDownDegrees(UpDownConstants.intakeTarget);
                    io_.spinFeeder(FeederConstants.intakeTarget);
                }
                break;
            case MoveToIntake:
                tiltDone = Math.abs(io_.getTiltPosition() - TiltConstants.intakeTarget) < IntakeShooterConstants.otherOKThresh && Math.abs(io_.getTiltVelocity()) < 5;
                upDownDone = Math.abs(io_.getUpDownPosition() - UpDownConstants.intakeTarget) < IntakeShooterConstants.otherOKThresh && Math.abs(io_.getUpDownVelocity()) < 5;
                if(tiltDone && upDownDone){
                    intakeState_ = IntakeState.WaitingForNote;
                }
                break;
            case WaitingForNote:
                if(io_.hasNote()){
                    intakeFeederStartPosSeenNote_ = io_.getFeederPosition();
                    intakeState_ = IntakeState.HasNoteIdle;
                }
                break;
            case Cancelling:
                state_ = State.Stow;
            case HasNoteIdle:
                if(Math.abs(intakeFeederStartPosSeenNote_ + FeederConstants.keepSpinningIntakeTarget - io_.getFeederPosition()) < 2.0){
                    io_.stopFeeder();
                    switch (intakeNextAction_.get()) {
                        case SPEAKER:
                            intakeState_ = IntakeState.MoveToShootStart;
                            io_.moveUpDownDegrees(UpDownConstants.subShootTarget);
                            break;
                        default:
                            intakeState_ = IntakeState.MoveToTransferStart;
                            io_.moveUpDownDegrees(UpDownConstants.transferTarget);
                            break;
                    }
                }
                break;
            case MoveToShootStart:
                if(Math.abs(io_.getUpDownPosition() - UpDownConstants.tiltCanMoveIntakeTarget) < IntakeShooterConstants.otherOKThresh){
                    intakeState_ = IntakeState.MoveToShoot;
                    io_.moveTiltDegrees(TiltConstants.subShootTarget);
                    io_.spinShooter1(ShooterConstants.subShootTarget);
                    io_.spinShooter2(ShooterConstants.subShootTarget);
                }
                break;
            case MoveToShoot:
                tiltDone = Math.abs(io_.getTiltPosition() - TiltConstants.subShootTarget) < IntakeShooterConstants.otherOKThresh && Math.abs(io_.getTiltVelocity()) < 5;
                upDownDone = Math.abs(io_.getUpDownPosition() - UpDownConstants.subShootTarget) < IntakeShooterConstants.otherOKThresh && Math.abs(io_.getUpDownVelocity()) < 5;
                if(tiltDone && upDownDone){
                    state_ = State.PrepForShoot;
                }
                break;
            case MoveToTransferStart:
                if(Math.abs(io_.getUpDownPosition() - UpDownConstants.tiltCanMoveIntakeTarget) < IntakeShooterConstants.otherOKThresh){
                    intakeState_ = IntakeState.MoveToTransfer;
                    io_.moveTiltDegrees(TiltConstants.transferTarget);
                }
                break;
            case MoveToTransfer:
                tiltDone = Math.abs(io_.getTiltPosition() - TiltConstants.transferTarget) < IntakeShooterConstants.otherOKThresh && Math.abs(io_.getTiltVelocity()) < 5;
                upDownDone = Math.abs(io_.getUpDownPosition() - UpDownConstants.transferTarget) < IntakeShooterConstants.otherOKThresh && Math.abs(io_.getUpDownVelocity()) < 5;
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
            io_.spinShooter1(shooterShootTarget_);
            io_.spinShooter2(shooterShootTarget_);
            io_.moveTiltDegrees(tiltShootTarget_);
            io_.moveUpDownDegrees(upDownShootTarget_);
            runOnceShootPrep_ = false;
        }
    }

    public void shoot() throws InvalidAlgorithmParameterException{
        if(state_ != State.PrepForShoot && state_ != State.Shoot){
            throw new InvalidAlgorithmParameterException("Wrong State!");
        }
        state_ = State.Shoot;
        runOnceShootPrep_ = false;
        boolean shooter1Good = Math.abs(io_.getShooter1Velocity() - shooterShootTarget_) < IntakeShooterConstants.shootOKThresh;
        boolean shooter2Good = Math.abs(io_.getShooter2Velocity() - shooterShootTarget_) < IntakeShooterConstants.shootOKThresh;
        boolean upDownGood = Math.abs(io_.getUpDownPosition() - upDownShootTarget_) < IntakeShooterConstants.shootOKThresh && Math.abs(io_.getUpDownVelocity()) < IntakeShooterConstants.otherOKThresh;
        boolean tiltGood = Math.abs(io_.getTiltPosition() - tiltShootTarget_) < IntakeShooterConstants.shootOKThresh && Math.abs(io_.getTiltVelocity()) < IntakeShooterConstants.otherOKThresh;
        Command wait = new Command(){};
        boolean waitSecsDone = false;
        if(aprilTagReady_.get() && DBReady_.get() && shooter1Good && shooter2Good && upDownGood && tiltGood){
            io_.spinFeeder(FeederConstants.shootTarget);
            wait = Commands.waitSeconds(IntakeShooterConstants.shootSecs);
            weAreShooting_ = true;
        }
        if(weAreShooting_){
            waitSecsDone = wait.isFinished();
            if(waitSecsDone){
                io_.stopFeeder();
                io_.stopShooter1();
                io_.stopShooter2();
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
                io_.spinShooter1(ShooterConstants.transferTargetVel);
                io_.spinShooter2(ShooterConstants.transferTargetVel);
                io_.spinFeeder(FeederConstants.transferTarget);
                transferState_ = TransferState.WaitingForSensorStart;
                break;
            case WaitingForSensorStart:
                if(io_.sensorVal()){
                    if(!io_.hasNote()){
                        transferState_ = TransferState.End;
                        transferShooterStartPosSeenNote_ = io_.getShooter2Position();
                    }else{
                        transferState_ = TransferState.WaitingForSensorEnd;
                    }
                }
                break;
            case WaitingForSensorEnd:
                if(!io_.hasNote()){
                    transferState_ = TransferState.End;
                    transferShooterStartPosSeenNote_ = io_.getShooter2Position();
                }
                break;
            case End:
                boolean shooterDone = Math.abs(io_.getShooter2Position() - ShooterConstants.transferTargetPos - transferShooterStartPosSeenNote_) < IntakeShooterConstants.otherOKThresh;
                if(shooterDone){
                    io_.stopShooter1();
                    io_.stopShooter2();
                    io_.stopFeeder();
                    transferState_ = TransferState.Done;
                    state_ = State.Stow;
                }
                break;
            default:
                transferState_ = TransferState.Start;
            
        }
    }

    public void abort(){
        io_.stopFeeder();
        io_.stopUpDown();
        io_.stopShooter1();
        io_.stopShooter2();
        io_.stopTilt();
    }

    public void eject(){
        state_ = State.Eject;
        boolean ejectDone = false;
        Command wait = new Command(){};
        if(runOnceEject_){
            abort();
            io_.spinShooter1(ShooterConstants.ejectTarget);
            io_.spinShooter2(ShooterConstants.ejectTarget);
            io_.spinFeeder(ShooterConstants.ejectTarget);
            wait = Commands.waitSeconds(IntakeShooterConstants.ejectSecs);
        }
        ejectDone = wait.isFinished();
        if(ejectDone){
            runOnceEject_ = true;
            state_ = State.Stow;
        }
    }
    

}