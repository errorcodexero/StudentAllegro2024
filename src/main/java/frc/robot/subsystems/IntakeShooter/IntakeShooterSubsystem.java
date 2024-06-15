package frc.robot.subsystems.IntakeShooter;

import java.security.InvalidAlgorithmParameterException;
import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;
import org.xero1425.util.XeroTimer;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.IntakeShooter.IntakeShooterConstants.FeederConstants;
import frc.robot.subsystems.IntakeShooter.IntakeShooterConstants.UpDownConstants;
import frc.robot.subsystems.IntakeShooter.IntakeShooterConstants.ShooterConstants;
import frc.robot.subsystems.IntakeShooter.IntakeShooterConstants.TiltConstants;
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
        Done, WaitingForTrampReady
    }

    private enum PrepShootState{
        Prep,
        MovingToTransfer,
        Done 
    }

    private IntakeShooterIOInputsAutoLogged inputs_ = new IntakeShooterIOInputsAutoLogged();
    private IntakeShooterIO io_;
    private State state_;
    private IntakeState intakeState_;
    private StowState stowState_;
    private TransferState transferState_;
    private PrepShootState prepShootState_;
    private Supplier<ActionType> intakeNextAction_;
    private Supplier<ShootType> shootingType_;
    private Supplier<Double> distFromTarget_;
    private double upDownShootTarget_;
    private double tiltShootTarget_;
    private double shooterShootTarget_;
    private Supplier<Boolean> DBReady_;
    private Supplier<Boolean> aprilTagReady_;
    private Supplier<Boolean> trampTransferReady_;
    private double intakeFeederStartPosSeenNote_;
    private double transferShooterStartPosSeenNote_;
    private boolean runOnceEject_ = true;
    private boolean weAreShooting_ = false;

    public IntakeShooterSubsystem (IntakeShooterIO io, Supplier<ActionType> actionType, Supplier<ShootType> shootType, Supplier<Double> distFromTarget, Supplier<Boolean> aprilTagReady){
        io_ = io;
        intakeNextAction_ = actionType;
        shootingType_ = shootType;
        distFromTarget_ = distFromTarget;
        state_ = State.Idle;
        aprilTagReady_ = aprilTagReady;
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
                intakeState_ = IntakeState.Start;
                stowState_ = StowState.Start;
                transferState_ = TransferState.WaitingForTrampReady;
                prepShootState_ = PrepShootState.Prep;
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

    public void setState(State state){
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
        switch(prepShootState_){
            case Prep:
                switch (shootingType_.get()) {
                    case AUTO:
                        shooterShootTarget_ = (2.0 * distFromTarget_.get()) + 65.0;
                        upDownShootTarget_ = distFromTarget_.get() < UpDownConstants.autoShootTargetSwitchSpot ? UpDownConstants.autoShootTargetClose : UpDownConstants.autoShootTargetFar;
                        tiltShootTarget_ = Math.atan2(IntakeShooterConstants.speakerHeight, distFromTarget_.get());
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
                io_.spinShooter1(shooterShootTarget_);
                io_.spinShooter2(shooterShootTarget_);
                io_.moveTiltDegrees(tiltShootTarget_);
                io_.moveUpDownDegrees(upDownShootTarget_);
                if(intakeNextAction_.get() != ActionType.SPEAKER){
                    io_.moveUpDownDegrees(UpDownConstants.transferTarget);
                    io_.moveTiltDegrees(TiltConstants.transferTarget);
                    prepShootState_ = PrepShootState.MovingToTransfer;
                }
                break;
            case MovingToTransfer:
                boolean upDownDone = Math.abs(io_.getUpDownPosition() - UpDownConstants.transferTarget) < IntakeShooterConstants.otherOKThresh && Math.abs(io_.getUpDownVelocity()) < IntakeShooterConstants.otherOKThresh;
                boolean tiltDone = Math.abs(io_.getTiltPosition() - TiltConstants.transferTarget) < IntakeShooterConstants.otherOKThresh && Math.abs(io_.getTiltVelocity()) < IntakeShooterConstants.otherOKThresh;
                if(upDownDone && tiltDone){
                    prepShootState_ = PrepShootState.Done;
                    state_ = State.Transfer;
                }
                break;
            default:
                prepShootState_ = PrepShootState.Prep;
        }
    }

    public void shoot() throws InvalidAlgorithmParameterException{
        if(state_ != State.PrepForShoot && state_ != State.Shoot){
            throw new InvalidAlgorithmParameterException("Wrong State!");
        }
        state_ = State.Shoot;
        boolean shooter1Good = Math.abs(io_.getShooter1Velocity() - shooterShootTarget_) < IntakeShooterConstants.shootOKThresh;
        boolean shooter2Good = Math.abs(io_.getShooter2Velocity() - shooterShootTarget_) < IntakeShooterConstants.shootOKThresh;
        boolean upDownGood = Math.abs(io_.getUpDownPosition() - upDownShootTarget_) < IntakeShooterConstants.shootOKThresh && Math.abs(io_.getUpDownVelocity()) < IntakeShooterConstants.otherOKThresh;
        boolean tiltGood = Math.abs(io_.getTiltPosition() - tiltShootTarget_) < IntakeShooterConstants.shootOKThresh && Math.abs(io_.getTiltVelocity()) < IntakeShooterConstants.otherOKThresh;
        XeroTimer wait = new XeroTimer(IntakeShooterConstants.shootSecs);
        
        if(aprilTagReady_.get() && DBReady_.get() && shooter1Good && shooter2Good && upDownGood && tiltGood){
            io_.spinFeeder(FeederConstants.shootTarget);
            wait.start();
            weAreShooting_ = true;
        }
        if(weAreShooting_){
            if(wait.isExpired()){
                io_.stopFeeder();
                io_.stopShooter1();
                io_.stopShooter2();
                weAreShooting_ = false;
                state_ = State.Stow;
            }
        }
    }


    public void transfer() throws InvalidAlgorithmParameterException{
        if(state_ != State.Transfer){
            throw new InvalidAlgorithmParameterException("Wrong State!");
        }
        switch(transferState_){
            case WaitingForTrampReady:
                if(trampTransferReady_.get()){
                    transferState_ = TransferState.Start;
                }
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
        XeroTimer wait = new XeroTimer(IntakeShooterConstants.ejectSecs);
        if(runOnceEject_){
            abort();
            io_.spinShooter1(ShooterConstants.ejectTarget);
            io_.spinShooter2(ShooterConstants.ejectTarget);
            io_.spinFeeder(ShooterConstants.ejectTarget);
            wait.start();
        }
        if(wait.isExpired()){
            runOnceEject_ = true;
            state_ = State.Stow;
        }
    }
    

}
