package frc.robot.subsystems.IntakeShooter;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeShooterConstants;
import frc.robot.Constants.IntakeShooterConstants.FeederConstants;
import frc.robot.Constants.IntakeShooterConstants.TiltConstants;
import frc.robot.Constants.IntakeShooterConstants.UpDownConstants;

public class IntakeShooterSubsystem extends SubsystemBase{
    private IntakeShooterIOInputsAutoLogged inputs_ = new IntakeShooterIOInputsAutoLogged();
    private IntakeShooterIO io_;

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
                if(Math.abs(io_.getTilt().getPosition().getValueAsDouble() - TiltConstants.upDownCanMoveIntakeTarget) < 2){
                    intakeState_ = IntakeState.MoveToIntake;
                }
            break;
            case MoveToIntake:
                io_.moveUpDownDegrees(UpDownConstants.intakeTarget);
            break;
            case CancellingFromMoveToIntake:
            break;
            case WaitingForNote:
                io_.spinFeeder(0);
            break;
            case CancellingFromWaiting:
            break;
            case HasNoteIdle:
            break;
            case MoveToShoot:
            break;
            case MoveToTransfer:
        }
    }
    

}
