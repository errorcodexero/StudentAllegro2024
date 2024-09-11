package frc.robot.automodes;

import java.util.Optional;

import org.littletonrobotics.junction.Logger;
import frc.org.xero1425.HolonomicPathFollower;
import frc.org.xero1425.Pose2dWithRotation;
import frc.org.xero1425.XeroAutoCommand;
import frc.org.xero1425.XeroRobot;
import frc.org.xero1425.XeroTimer;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.RobotContainer;
import frc.robot.subsystems.IntakeShooter.IntakeShooterSubsystem;
import frc.robot.subsystems.Swerve.CommandSwerveDrivetrain;
import frc.robot.subsystems.oi.NoteDestination;

public class FourNoteCommand extends XeroAutoCommand {

    private enum State {
        Start,
        Shoot1,
        SlowDownForIntake,
        DrivetoNote2,
        Shoot2,
        DoneNote2,
        DrivetoNote3,
        Shoot3,
        DoneNote3,
        DrivetoNote4,
        Shoot4,
        DoneNote4,
        Done,
        Error
    }

    private State currentState;
    private RobotContainer robotContainer;
    private XeroTimer moveToNote;
    private Pose2dWithRotation shootnotepose_;
    private Pose2dWithRotation collectnote1pose_;
    private Pose2dWithRotation collectnote2pose_;
    private Pose2dWithRotation collectnote3pose_;

    public FourNoteCommand(XeroRobot robot, RobotContainer container) {
        super(robot, "four-note", "Description for Four Note Dynamic Command");
    
        this.robotContainer = container;
        this.currentState = State.Start;
    
        addRequirements(container.getDriveTrain(), container.getIntakeShooter());
        
        this.moveToNote = new XeroTimer(getRobot(), "four-note-intake-slow-down", FourNoteDynamicConstants.INTAKE_DOWN_DELAY);
    }

    @Override
    public void initialize() {
        try {
            shootnotepose_ = FourNoteDynamicConstants.getShootPose(getRobot().getFieldLayout().getFieldWidth());
            collectnote1pose_ = FourNoteDynamicConstants.getCollect1Pose(getRobot().getFieldLayout().getFieldWidth());
            collectnote2pose_ = FourNoteDynamicConstants.getCollect2Pose(getRobot().getFieldLayout().getFieldWidth());
            collectnote3pose_ = FourNoteDynamicConstants.getCollect3Pose(getRobot().getFieldLayout().getFieldWidth());
        } catch (Exception ex) {
            currentState = State.Error;
            return;
        }

        robotContainer.getDriveTrain().seedFieldRelative(new Pose2d(shootnotepose_.getTranslation(), shootnotepose_.getRobotRotation()));
        robotContainer.getIntakeShooter().setHasNote(true);
        robotContainer.getIntakeShooter().manualShoot(
            FourNoteDynamicConstants.LOW_MANUAL_UP_DOWN, FourNoteDynamicConstants.LOW_MANUAL_UP_DOWN_POS_TOL, FourNoteDynamicConstants.LOW_MANUAL_SHOOTER_VEL_TOL,
            FourNoteDynamicConstants.LOW_MANUAL_TILT, FourNoteDynamicConstants.LOW_MANUAL_TILT_POS_TOL, FourNoteDynamicConstants.LOW_MANUAL_TILT_VEL_TOL,
            FourNoteDynamicConstants.LOW_MANUAL_SHOOTER, FourNoteDynamicConstants.LOW_MANUAL_SHOOTER_VEL_TOL, true
        );
        currentState = State.Shoot1;
    }

    @Override
    public void execute() {
        super.execute();

        switch (currentState) {
            case Start:
                break;
                
            case Error:
                break;
                
            case Shoot1:
                if (!robotContainer.getIntakeShooter().HasNoteIdle()) {
                    robotContainer.getOI().setNoteDestination(NoteDestination.ManualSpeaker);
                    robotContainer.getIntakeShooter().setManualShootParameters(FourNoteDynamicConstants.LOW_MANUAL_UP_DOWN, FourNoteDynamicConstants.LOW_MANUAL_TILT);
                    robotContainer.getIntakeShooter().collect();
                    moveToNote.start();
                    currentState = State.SlowDownForIntake;
                }
                break;
                
            case SlowDownForIntake:
                if (moveToNote.isExpired()) {
                    getFollower().driveTo(null, null, collectnote1pose_, null, null, null, null, null);
                    currentState = State.DrivetoNote2;
                }
                break;
                
            case DrivetoNote2:
                if (!getFollower().isDriving()) {
                    getFollower().driveTo(null, null, shootnotepose_, null, null, null, null, null);
                    currentState = State.Shoot2;
                } else {
                    getFollower().driveTo(null, null, collectnote2pose_, null, null, null, null, null);
                    currentState = State.DrivetoNote3;
                }
                break;
                
            case Shoot2:
                if (getFollower().getDistance() > FourNoteDynamicConstants.DISTANCE_SHOOT_2) {
                    robotContainer.getIntakeShooter().manualShoot(
                        FourNoteDynamicConstants.LOW_MANUAL_UP_DOWN, FourNoteDynamicConstants.LOW_MANUAL_UP_DOWN_POS_TOL, FourNoteDynamicConstants.LOW_MANUAL_UP_DOWN_VEL_TOL,
                        FourNoteDynamicConstants.LOW_MANUAL_TILT, FourNoteDynamicConstants.LOW_MANUAL_TILT_POS_TOL, FourNoteDynamicConstants.LOW_MANUAL_TILT_VEL_TOL,
                        FourNoteDynamicConstants.LOW_MANUAL_SHOOTER, FourNoteDynamicConstants.LOW_MANUAL_SHOOTER_VEL_TOL, true
                    );
                }
                currentState = State.DoneNote2;
                break;
                
            case DoneNote2:
                if (!getFollower().isDriving() && !robotContainer.getIntakeShooter().HasNoteIdle()) {
                    robotContainer.getOI().setNoteDestination(NoteDestination.ManualSpeaker);
                    robotContainer.getIntakeShooter().setManualShootParameters(FourNoteDynamicConstants.LOW_MANUAL_UP_DOWN, FourNoteDynamicConstants.LOW_MANUAL_TILT);
                    robotContainer.getIntakeShooter().collect();
                    getFollower().driveTo(null, null, collectnote2pose_, null, null, null, null, null);
                    currentState = State.DrivetoNote3;
                }
                break;
                
            case DrivetoNote3:
                if (!getFollower().isDriving()) {
                    if (robotContainer.getIntakeShooter().HasNoteIdle()) {
                        getFollower().driveTo(null, null, shootnotepose_, null, null, null, null, null);
                        robotContainer.getIntakeShooter().manualShoot(
                            FourNoteDynamicConstants.LOW_MANUAL_UP_DOWN, FourNoteDynamicConstants.LOW_MANUAL_UP_DOWN_POS_TOL, FourNoteDynamicConstants.LOW_MANUAL_UP_DOWN_VEL_TOL,
                            FourNoteDynamicConstants.LOW_MANUAL_TILT, FourNoteDynamicConstants.LOW_MANUAL_TILT_POS_TOL, FourNoteDynamicConstants.LOW_MANUAL_TILT_VEL_TOL,
                            FourNoteDynamicConstants.LOW_MANUAL_SHOOTER, FourNoteDynamicConstants.LOW_MANUAL_SHOOTER_VEL_TOL, true
                        );
                        currentState = State.Shoot3;
                    } else {
                        getFollower().driveTo(null, null, collectnote3pose_, null, null, null, null, null);
                        currentState = State.DrivetoNote3;
                    }
                }
                break;
                
            case Shoot3:
                if (getFollower().getDistance() > FourNoteDynamicConstants.DISTANCE_SHOOT_2) {
                    robotContainer.getIntakeShooter().manualShoot(
                        FourNoteDynamicConstants.LOW_MANUAL_UP_DOWN, FourNoteDynamicConstants.LOW_MANUAL_UP_DOWN_POS_TOL, FourNoteDynamicConstants.LOW_MANUAL_UP_DOWN_VEL_TOL,
                        FourNoteDynamicConstants.LOW_MANUAL_TILT, FourNoteDynamicConstants.LOW_MANUAL_TILT_POS_TOL, FourNoteDynamicConstants.LOW_MANUAL_TILT_VEL_TOL,
                        FourNoteDynamicConstants.LOW_MANUAL_SHOOTER, FourNoteDynamicConstants.LOW_MANUAL_SHOOTER_VEL_TOL, true
                    );
                    currentState = State.DoneNote3;
                }
                break;
                
            case DoneNote3:
                if (!getFollower().isDriving() && robotContainer.getIntakeShooter().HasNoteIdle()) {
                    robotContainer.getOI().setNoteDestination(NoteDestination.ManualSpeaker);
                    robotContainer.getIntakeShooter().setManualShootParameters(FourNoteDynamicConstants.LOW_MANUAL_UP_DOWN, FourNoteDynamicConstants.LOW_MANUAL_TILT);
                    robotContainer.getIntakeShooter().collect();
                    getFollower().driveTo(null, null, collectnote3pose_, null, null, null, null, null);
                    currentState = State.DrivetoNote4;
                }
                break;
            
            case DrivetoNote4:
                if (!getFollower().isDriving()) {
                    if (robotContainer.getIntakeShooter().HasNoteIdle()) {
                        getFollower().driveTo(null, null, shootnotepose_, null, null, null, null, null);
                        robotContainer.getIntakeShooter().manualShoot(
                            FourNoteDynamicConstants.LOW_MANUAL_UP_DOWN, FourNoteDynamicConstants.LOW_MANUAL_UP_DOWN_POS_TOL, FourNoteDynamicConstants.LOW_MANUAL_UP_DOWN_VEL_TOL,
                            FourNoteDynamicConstants.LOW_MANUAL_TILT, FourNoteDynamicConstants.LOW_MANUAL_TILT_POS_TOL, FourNoteDynamicConstants.LOW_MANUAL_TILT_VEL_TOL,
                            FourNoteDynamicConstants.LOW_MANUAL_SHOOTER, FourNoteDynamicConstants.LOW_MANUAL_SHOOTER_VEL_TOL, true
                        );
                        currentState = State.Shoot4;
                    } else {
                        currentState = State.Done;
                    }
                }
                break;
                
            case Shoot4:
                if (getFollower().getDistance() > FourNoteDynamicConstants.DISTANCE_SHOOT_4) {
                    robotContainer.getIntakeShooter().manualShoot(
                        FourNoteDynamicConstants.LOW_MANUAL_UP_DOWN, FourNoteDynamicConstants.LOW_MANUAL_UP_DOWN_POS_TOL, FourNoteDynamicConstants.LOW_MANUAL_UP_DOWN_VEL_TOL,
                        FourNoteDynamicConstants.LOW_MANUAL_TILT, FourNoteDynamicConstants.LOW_MANUAL_TILT_POS_TOL, FourNoteDynamicConstants.LOW_MANUAL_TILT_VEL_TOL,
                        FourNoteDynamicConstants.LOW_MANUAL_SHOOTER, FourNoteDynamicConstants.LOW_MANUAL_SHOOTER_VEL_TOL, true
                    );
                    currentState = State.DoneNote4;
                }
                break;
                
            case DoneNote4:
                if (getFollower().isDriving() && !robotContainer.getIntakeShooter().HasNoteIdle()) {
                    currentState = State.Done;
                }
                break;
                
            case Done:
                break;
        }
    }

    @Override
    public void end(boolean interrupted) {
        getFollower().cancel();
    }

    @Override
    public boolean isFinished() {
        return currentState == State.Done;
    }

    private HolonomicPathFollower getFollower() {
        return robotContainer.getFollower();
    }
}
