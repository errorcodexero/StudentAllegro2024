package frc.robot.subsystems.IntakeShooter;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeShooterConstants.FeederConstants;
import frc.robot.Constants.IntakeShooterConstants.TiltConstants;
import frc.robot.Constants.IntakeShooterConstants.UpDownConstants;

public class IntakeShooterSubsystem extends SubsystemBase{
    private IntakeShooterIOInputsAutoLogged inputs_ = new IntakeShooterIOInputsAutoLogged();
    private IntakeShooterIO io_;
    private double intakeFeederStartPosSeenNote_;

    enum IntakeState{
        MoveToIntakeStart,
        MoveToIntake,
        CancellingFromMoveToIntake,
        WaitingForNote,
        CancellingFromWaiting,
        HasNoteIdle,
        MoveToShoot,
        MoveToTransfer
    }

    IntakeState intakeState_;

    public IntakeShooterSubsystem (IntakeShooterIO io){
        io_ = io;
    }
    
    @Override
    public void periodic(){
        io_.update(inputs_);
    }

    public void intake(){
        switch(intakeState_){
            case MoveToIntakeStart:
                io_.spinFeeder(FeederConstants.intakeTarget);
                io_.moveTiltDegrees(TiltConstants.intakeTarget);
                if(Math.abs(io_.getTilt().getPosition().getValueAsDouble() * 360 - TiltConstants.upDownCanMoveIntakeTarget) < 2){
                    intakeState_ = IntakeState.MoveToIntake;
                }
            break;
            case MoveToIntake:
                io_.moveUpDownDegrees(UpDownConstants.intakeTarget);
                if(Math.abs(io_.getTilt().getPosition().getValueAsDouble() * 360 - TiltConstants.intakeTarget) < 3 && Math.abs(io_.getUpDown().getPosition().getValueAsDouble() - UpDownConstants.intakeTarget) < 3){
                    intakeState_ = IntakeState.WaitingForNote;
                }
            break;
            case CancellingFromMoveToIntake:
            break;
            case WaitingForNote:
                if(io_.hasNote()){
                    intakeState_ = IntakeState.HasNoteIdle;
                }
                intakeFeederStartPosSeenNote_ = io_.getFeeder().getPosition().getValueAsDouble() * 360;
            break;
            case CancellingFromWaiting:
            break;
            case HasNoteIdle:
                if(Math.abs(intakeFeederStartPosSeenNote_ + FeederConstants.keepSpinningIntakeTarget - io_.getFeeder().getPosition().getValueAsDouble() * 360) < 2.0){
                    io_.stopFeeder();
                }
            break;
            case MoveToShoot:
            break;
            case MoveToTransfer:
        }
    }
    

}
