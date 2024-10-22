package frc.robot.subsystems.IntakeShooter;

import static edu.wpi.first.units.Units.Degrees;

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
        Aborted,
        Test
    }

    private enum IntakeState{
        MovingAndWaiting,
        Cancel,
        HasNoteIdle,
        MoveToShoot,
        MoveToTransfer,
        Done
    }

    private enum TransferState{
        Start,
        WaitingForSensor,
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

    private boolean testing_ = false;

    public IntakeShooterSubsystem (IntakeShooterIO io, Supplier<ActionType> actionType, Supplier<ShootType> shootType, Supplier<Double> distFromTarget, Supplier<Boolean> aprilTagReady){
        io_ = io;
        intakeNextAction_ = actionType;
        shootingType_ = shootType;
        distFromTarget_ = distFromTarget;
        state_ = State.Idle;
        intakeState_ = IntakeState.MovingAndWaiting;
        transferState_ = TransferState.Start;
        prepShootState_ = PrepShootState.Prep;
        aprilTagReady_ = aprilTagReady;
    }

    public boolean tiltReady(){
        boolean upDownGood = Math.abs(inputs_.upDownPosition - upDownShootTarget_) < IntakeShooterConstants.shootOKThresh;
        boolean tiltGood = Math.abs(inputs_.tiltPosition - tiltShootTarget_) < IntakeShooterConstants.shootOKThresh;
        return (state_ == State.Shoot || state_ == State.PrepShoot) && upDownGood && tiltGood;
    }

    public boolean shooterReady(){
        boolean shooter1Good = Math.abs(inputs_.shooter1Velocity - shooterShootTarget_) < IntakeShooterConstants.shootOKThresh;
        boolean shooter2Good = Math.abs(inputs_.shooter2Velocity - shooterShootTarget_) < IntakeShooterConstants.shootOKThresh;
        return shooter1Good && shooter2Good;
    }

    @Deprecated
    public State getState(){
        return state_;
    }
    
    @Override
    public void periodic(){
        io_.updateInputs(inputs_);
        Logger.processInputs("IntakeShooter", inputs_);

        Logger.recordOutput("IntakeShooter/State", state_);
        Logger.recordOutput("IntakeShooter/IntakeState", intakeState_);
        Logger.recordOutput("IntakeShooter/TransferState", transferState_);
        Logger.recordOutput("IntakeShooter/ShooterShootTarget", shooterShootTarget_);
        Logger.recordOutput("IntakeShooter/UpdownShootTarget", upDownShootTarget_);
        Logger.recordOutput("IntakeShooter/TiltShootTarget", tiltShootTarget_);

        switch (state_) {
            case Idle, Aborted:
                if(testing_){
                    state_ = State.Test;
                }
                break;
            case Intake:
                intakePeriodic();
                break;
            case PrepShoot:
                prepShootPeriodic();
                break;
            case Shoot:
                shootPeriodic();
                break;
            case Stow:
                if (true) { return; } // Temporary exit TODO: remove eventually
                stowPeriodic();
                break;
            case Transfer:
                transferPeriodic();
                break;
            case Eject:
                ejectPeriodic();
                break;
            case Test:
                io_.moveSystem(TiltConstants.transferTarget, UpDownConstants.transferTarget);
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
        return runOnce(() -> {
            abort().schedule(); // bc abort changes state, i need to change it back. 
            state_ = State.Stow;
        });
    }
    

    private void stowPeriodic(){
        if(io_.moveSystem(TiltConstants.stowTarget, UpDownConstants.stowTarget)){
            state_ = State.Idle;
        }
    }

    public Command intake(){
        return startEnd(() -> {
            if(state_.equals(State.Idle)){
                state_ = State.Intake;
            }
        }, () -> {
            if(state_.equals(State.Intake)){
                intakeState_ = IntakeState.Cancel;
            }
        });
    }

    private void intakePeriodic(){
        boolean doneMoving = false;
        switch(intakeState_){
            case MovingAndWaiting:
                state_ = State.Test;

                if (true) { return; } // Temporary exit TODO: remove eventually

                doneMoving = io_.moveSystem(TiltConstants.intakeTarget, UpDownConstants.intakeTarget);
                if(inputs_.hasNote){
                    intakeFeederStartPosSeenNote_ = inputs_.feederPosition;
                    intakeState_ = IntakeState.HasNoteIdle;
                }
                break;
            case Cancel:
                state_ = State.Stow;
                break;
            case HasNoteIdle:
                if(!doneMoving){
                    doneMoving = io_.moveSystem(TiltConstants.intakeTarget, UpDownConstants.intakeTarget);
                    break;
                }
                if(intakeFeederStartPosSeenNote_ + FeederConstants.keepSpinningIntakeTarget - inputs_.feederPosition < IntakeShooterConstants.otherOKThresh){
                    io_.stopFeeder();
                }else{
                    break;
                }

                switch (intakeNextAction_.get()) {
                        case SPEAKER:
                            intakeState_ = IntakeState.MoveToShoot;
                            break;
                        case AMP, TRAP:
                            intakeState_ = IntakeState.MoveToTransfer;
                            break;
                }

                break;
            
            case MoveToShoot:
                getShootingTargets();
                io_.spinShooter1(shooterShootTarget_);
                io_.spinShooter2(shooterShootTarget_);
                if(io_.moveSystem(tiltShootTarget_, upDownShootTarget_)){
                    state_ = State.PrepShoot;
                    intakeState_ = IntakeState.Done;
                }
                break;

            case MoveToTransfer:
                if(io_.moveSystem(TiltConstants.transferTarget, UpDownConstants.transferTarget)){
                    state_ = State.Transfer;
                    intakeState_ = IntakeState.Done;
                }
                break;
            
            default:
                intakeState_ = IntakeState.MovingAndWaiting;
        }
    }

    private void getShootingTargets(){
        switch (shootingType_.get()) {
            case AUTO:
                //lerp PWLs later when tuning
                shooterShootTarget_ = ShooterConstants.pwls.lerp(distFromTarget_.get());
                upDownShootTarget_ = distFromTarget_.get() < UpDownConstants.autoShootTargetSwitchSpot ? UpDownConstants.autoShootTargetClose : UpDownConstants.autoShootTargetFar;
                tiltShootTarget_ = TiltConstants.pwls.lerp(distFromTarget_.get());
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
    }

    //No setter because this state should not be set by the OI and instead by intake. 
    public void prepShootPeriodic(){
        switch(prepShootState_){
            case Prep:
                getShootingTargets();
                io_.spinShooter1(shooterShootTarget_);
                io_.spinShooter2(shooterShootTarget_);
                io_.moveTilt(Degrees.of(tiltShootTarget_));
                io_.moveUpDown(Degrees.of(upDownShootTarget_));
                if(intakeNextAction_.get() != ActionType.SPEAKER){
                    io_.moveUpDown(Degrees.of(UpDownConstants.transferTarget));
                    io_.moveTilt(Degrees.of(TiltConstants.transferTarget));
                    prepShootState_ = PrepShootState.MovingToTransfer;
                }
                break;
            case MovingToTransfer:
                if(io_.moveSystem(TiltConstants.transferTarget, UpDownConstants.transferTarget)){
                    prepShootState_ = PrepShootState.Done;
                    state_ = State.Transfer;
                }
                break;
            default:
                prepShootState_ = PrepShootState.Prep;
        }
    }

    public Command shoot(){
        return runOnce(() -> {
            if(state_.equals(State.PrepShoot)){
                state_ = State.Shoot;
                prepShootState_ = PrepShootState.Done;
            }
        });
    }

    private void shootPeriodic(){
        boolean shooter1Good = Math.abs(inputs_.shooter1Velocity - shooterShootTarget_) < IntakeShooterConstants.shootOKThresh;
        boolean shooter2Good = Math.abs(inputs_.shooter2Velocity - shooterShootTarget_) < IntakeShooterConstants.shootOKThresh;
        boolean upDownGood = Math.abs(inputs_.upDownPosition - upDownShootTarget_) < IntakeShooterConstants.shootOKThresh && Math.abs(inputs_.upDownVelocity) < IntakeShooterConstants.otherOKThresh;
        boolean tiltGood = Math.abs(inputs_.tiltPosition - tiltShootTarget_) < IntakeShooterConstants.shootOKThresh && Math.abs(inputs_.tiltVelocity) < IntakeShooterConstants.otherOKThresh;
        
        XeroTimer wait = new XeroTimer(IntakeShooterConstants.shootSecs); //So VS code doesnt get mad at me
        
        if(aprilTagReady_.get() && DBReady_.get() && shooter1Good && shooter2Good && upDownGood && tiltGood){
            io_.spinFeeder(FeederConstants.shootTarget);
            wait.start();
            weAreShooting_ = true;
        }
        
        if(weAreShooting_ && wait.isExpired()){
            io_.stopFeeder();
            io_.stopShooter1();
            io_.stopShooter2();
            weAreShooting_ = false;
            state_ = State.Stow;
        }
    }

    //Should not get called by OI, no setter. Should get state changed by intake or PrepShoot. 
    //Should already be at destination
    private void transferPeriodic(){
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
                transferState_ = TransferState.WaitingForSensor;
                break;
            case WaitingForSensor:
                if(!inputs_.hasNote){
                    transferState_ = TransferState.End;
                    transferShooterStartPosSeenNote_ = inputs_.shooter2Position;
                }
                break;
            case End:
                boolean shooterDone = Math.abs(inputs_.shooter2Position - ShooterConstants.transferTargetPos - transferShooterStartPosSeenNote_) < IntakeShooterConstants.otherOKThresh;
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
        return runOnce(() -> {
            io_.stopFeeder();
            io_.stopUpDown();
            io_.stopShooter1();
            io_.stopShooter2();
            io_.stopTilt();
            state_ = State.Aborted;
        });
    }

    public Command turtle() {
        return runOnce(() -> {
            state_ = State.Stow;
        });
    }

    public Command eject(){
        return runOnce(() -> {
            state_ = State.Eject;
        });
    }

    private void ejectPeriodic(){
        XeroTimer ejectTimer = null;
        if(runOnceEject_){
            ejectTimer = new XeroTimer(IntakeShooterConstants.ejectSecs);
            abort().schedule();
            state_ = State.Eject; // bc abort changes state, i need to change it back. 
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
