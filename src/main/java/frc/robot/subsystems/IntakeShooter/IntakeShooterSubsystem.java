package frc.robot.subsystems.IntakeShooter;

import java.security.InvalidAlgorithmParameterException;
import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;
import org.xero1425.util.XeroTimer;

import edu.wpi.first.wpilibj2.command.Command;
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
        PrepShoot,
        Shoot,
        Transfer,
        Eject,
        Stow,
        Aborted
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
        MovingStart,
        MovingEnd,
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
        return (state_ == State.Shoot || state_ == State.PrepShoot) && upDownGood && tiltGood;
    }

    public boolean shooterReady(){
        boolean shooter1Good = Math.abs(io_.getShooter1Velocity() - shooterShootTarget_) < IntakeShooterConstants.shootOKThresh;
        boolean shooter2Good = Math.abs(io_.getShooter2Velocity() - shooterShootTarget_) < IntakeShooterConstants.shootOKThresh;
        return shooter1Good && shooter2Good;
    }

    @Deprecated
    public State getState(){
        return state_;
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
                    intakePeriodic();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }
                break;
            case PrepShoot:
                try {
                    prepShootPeriodic();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }
                break;
            case Shoot:
                try {
                    shootPeriodic();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }
                break;
            case Stow:
                stowPeriodic();
                break;
            case Transfer:
                try {
                    transferPeriodic();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }
                break;
            case Eject:
                ejectPeriodic();
                break;
            case Aborted:
                break;
            default:
                state_ = State.Idle;
                break;
        }
    }

    @Deprecated
    public void setState(State state){
        state_ = state;
    }

    public Command stow(){
        return new Command() {
            @Override
            public void initialize() {
                super.initialize();
                state_ = State.Stow;
            }
        };
    }

    private void stowPeriodic(){
        switch (stowState_) {
            case Start:
                abort(); // bc abort chnages state, i need to change it back. 
                state_ = State.Stow;
                io_.moveUpDownDegrees(UpDownConstants.stowTarget);
                stowState_ = StowState.MovingEnd;
                break;
            case MovingStart:
                boolean upDownPastZone = io_.getUpDownPosition() > UpDownConstants.tiltCanMoveIntakeTarget;
                if(upDownPastZone){
                    io_.moveTiltDegrees(TiltConstants.stowTarget);
                }
                break;
            case MovingEnd:
                boolean upDownDone = Math.abs(io_.getUpDownPosition() - UpDownConstants.stowTarget) < IntakeShooterConstants.otherOKThresh && Math.abs(io_.getUpDownVelocity()) < IntakeShooterConstants.otherOKThresh;
                boolean tiltDone = Math.abs(io_.getTiltPosition() - TiltConstants.stowTarget) < IntakeShooterConstants.otherOKThresh && Math.abs(io_.getTiltVelocity()) < IntakeShooterConstants.otherOKThresh;
                if(upDownDone && tiltDone){
                    state_ = State.Idle;
                }
                break;
            default:
                stowState_ = StowState.Start;
                break;
        }
    }

    public Command intake(){
        return new Command() {
            @Override
            public void initialize() {
                super.initialize();
                if(state_.equals(State.Idle)){
                    state_ = State.Intake;
                }
            }
        };
    }

    public Command cancelIntake(){
        return new Command() {
            @Override
            public void initialize() {
                super.initialize();
                if(state_.equals(State.Intake)){
                    intakeState_ = IntakeState.Cancelling;
                }
            }
        };
    }

    private void intakePeriodic() throws InvalidAlgorithmParameterException{
        if(state_ != State.Intake){
            throw new InvalidAlgorithmParameterException("Wrong State!");
        }
        boolean tiltDone;
        boolean upDownDone;
        switch(intakeState_){
            case Start:
                io_.moveTiltDegrees(TiltConstants.intakeTarget);
                intakeState_ = IntakeState.MoveToIntakeStart;
                break;
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
                //Should fall through so I can check for a note while going down. 
            case WaitingForNote:
                if(io_.hasNote()){
                    intakeFeederStartPosSeenNote_ = io_.getFeederPosition();
                    intakeState_ = IntakeState.HasNoteIdle;
                }
                break;
            case Cancelling:
                state_ = State.Stow;
                break;
            case HasNoteIdle:
                if(intakeFeederStartPosSeenNote_ + FeederConstants.keepSpinningIntakeTarget - io_.getFeederPosition() < IntakeShooterConstants.otherOKThresh){
                    io_.stopFeeder();
                    switch (intakeNextAction_.get()) {
                        case SPEAKER:
                            intakeState_ = IntakeState.MoveToShootStart;
                            io_.moveUpDownDegrees(UpDownConstants.subwooferShootTarget);
                            break;
                        case AMP, TRAP:
                            intakeState_ = IntakeState.MoveToTransferStart;
                            io_.moveUpDownDegrees(UpDownConstants.transferTarget);
                            break;
                    }
                }
                break;
            case MoveToShootStart:
                if(io_.getUpDownPosition() > UpDownConstants.tiltCanMoveIntakeTarget){
                    intakeState_ = IntakeState.MoveToShoot;
                    io_.moveTiltDegrees(TiltConstants.subwooferShootTarget);
                    io_.spinShooter1(ShooterConstants.subwooferShootTarget);
                    io_.spinShooter2(ShooterConstants.subwooferShootTarget);
                }
                break;
            case MoveToShoot:
                tiltDone = Math.abs(io_.getTiltPosition() - TiltConstants.subwooferShootTarget) < IntakeShooterConstants.otherOKThresh && Math.abs(io_.getTiltVelocity()) < 5;
                upDownDone = Math.abs(io_.getUpDownPosition() - UpDownConstants.subwooferShootTarget) < IntakeShooterConstants.otherOKThresh && Math.abs(io_.getUpDownVelocity()) < 5;
                if(tiltDone && upDownDone){
                    state_ = State.PrepShoot;
                    intakeState_ = IntakeState.Done;
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

    //No setter because this state should not be set by the OI and instead by intake. 
    public void prepShootPeriodic() throws InvalidAlgorithmParameterException{
        if(state_ != State.PrepShoot && state_ != State.Intake){
            throw new InvalidAlgorithmParameterException("Wrong State!");
        }
        state_ = State.PrepShoot;
        switch(prepShootState_){
            case Prep:
                switch (shootingType_.get()) {
                    case AUTO:
                        shooterShootTarget_ = (2.0 * distFromTarget_.get()) + 65.0;
                        upDownShootTarget_ = distFromTarget_.get() < UpDownConstants.autoShootTargetSwitchSpot ? UpDownConstants.autoShootTargetClose : UpDownConstants.autoShootTargetFar;
                        tiltShootTarget_ = Math.atan2(IntakeShooterConstants.speakerHeight, distFromTarget_.get()) + upDownShootTarget_ - 90.0;
                        break;
                    case PODIUM:
                        shooterShootTarget_ = ShooterConstants.podiumShootTarget;
                        tiltShootTarget_ = TiltConstants.podiumShootTarget;
                        upDownShootTarget_ = UpDownConstants.podiumShootTarget;
                        break;
                    case SUBWOOFER:
                        shooterShootTarget_ = ShooterConstants.subwooferShootTarget;
                        tiltShootTarget_ = TiltConstants.subwooferShootTarget;
                        upDownShootTarget_ = UpDownConstants.subwooferShootTarget;
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

    public Command shoot(){
        return new Command() {
            @Override
            public void initialize() {
                super.initialize();
                if(state_.equals(State.PrepShoot)){
                    state_ = State.Shoot;
                }
            }
        };
    }

    private void shootPeriodic() throws InvalidAlgorithmParameterException{
        if(state_ != State.Shoot){
            throw new InvalidAlgorithmParameterException("Wrong State!");
        }
        boolean shooter1Good = Math.abs(io_.getShooter1Velocity() - shooterShootTarget_) < IntakeShooterConstants.shootOKThresh;
        boolean shooter2Good = Math.abs(io_.getShooter2Velocity() - shooterShootTarget_) < IntakeShooterConstants.shootOKThresh;
        boolean upDownGood = Math.abs(io_.getUpDownPosition() - upDownShootTarget_) < IntakeShooterConstants.shootOKThresh && Math.abs(io_.getUpDownVelocity()) < IntakeShooterConstants.otherOKThresh;
        boolean tiltGood = Math.abs(io_.getTiltPosition() - tiltShootTarget_) < IntakeShooterConstants.shootOKThresh && Math.abs(io_.getTiltVelocity()) < IntakeShooterConstants.otherOKThresh;
        XeroTimer wait = null; //So VS code doesnt get amd at me
        
        if(aprilTagReady_.get() && DBReady_.get() && shooter1Good && shooter2Good && upDownGood && tiltGood){
            wait = new XeroTimer(IntakeShooterConstants.shootSecs);
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

    //Should not get called by OI, no setter. Should get state changed by intake. 
    private void transferPeriodic() throws InvalidAlgorithmParameterException{
        if(state_ != State.Transfer){
            throw new InvalidAlgorithmParameterException("Wrong State!");
        }
        switch(transferState_){
            case WaitingForTrampReady:
                if(trampTransferReady_.get()){
                    transferState_ = TransferState.Start;
                }
                break;
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

    public Command abort(){
        return new Command() {
            @Override
            public void initialize() {
                super.initialize();
                io_.stopFeeder();
                io_.stopUpDown();
                io_.stopShooter1();
                io_.stopShooter2();
                io_.stopTilt();
                state_ = State.Aborted;
            }
        };
    }

    public Command eject(){
        return new Command() {
            @Override
            public void initialize() {
                super.initialize();
                state_ = State.Eject;
            }
        };
    }

    private void ejectPeriodic(){
        XeroTimer ejectTimer = null;
        if(runOnceEject_){
            ejectTimer = new XeroTimer(IntakeShooterConstants.ejectSecs);
            abort(); 
            state_ = State.Eject; // bc abort chnages state, i need to change it back. 
            io_.spinShooter1(ShooterConstants.ejectTarget);
            io_.spinShooter2(ShooterConstants.ejectTarget);
            io_.spinFeeder(ShooterConstants.ejectTarget);
            ejectTimer.start();
        }
        if(ejectTimer.isExpired()){
            runOnceEject_ = true;
            state_ = State.Stow;
        }
    }
    

}
