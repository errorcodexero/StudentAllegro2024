package frc.robot.subsystems.Swerve;

import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.mechanisms.swerve.SwerveDrivetrain.SwerveDriveState;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SwerveSubsystem extends SubsystemBase {
    private final SwerveIO io_;
    private final SwerveIOInputsAutoLogged inputs_;
    private final SwerveDriveState state_;
    
    private final SwerveRequest.SwerveDriveBrake brakeRequest_ = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt pointRequest_ = new SwerveRequest.PointWheelsAt();

    private boolean hasAppliedOperatorPerspective_;

    /* Blue alliance sees forward as 0 degrees (toward red alliance wall) */
    private final Rotation2d BLUE_PERSPECTIVE = Rotation2d.fromDegrees(0);
    
    /* Red alliance sees forward as 180 degrees (toward blue alliance wall) */
    private final Rotation2d RED_PERSPECTIVE = Rotation2d.fromDegrees(180);

    public SwerveSubsystem(SwerveIO io) {
        io_ = io;
        inputs_ = new SwerveIOInputsAutoLogged();
        state_ = new SwerveDriveState();
    }

    @Override
    public void periodic() {
        io_.updateInputs(inputs_);
        Logger.processInputs("Swerve", inputs_);

        state_.FailedDaqs = inputs_.failedDaqs;
        state_.ModuleStates = inputs_.moduleStates;
        state_.ModuleTargets = inputs_.moduleTargetStates;
        state_.OdometryPeriod = inputs_.odometryPeriodSeconds;
        state_.Pose = inputs_.pose;
        state_.SuccessfulDaqs = inputs_.successfulDaqs;
        state_.speeds = inputs_.speeds;

        if (!hasAppliedOperatorPerspective_ || DriverStation.isDisabled()) {
            DriverStation.getAlliance().ifPresent((allianceColor) -> {
                io_.setOperatorPerspectiveForward(allianceColor == Alliance.Red ? RED_PERSPECTIVE : BLUE_PERSPECTIVE);
                hasAppliedOperatorPerspective_ = true;
            });
        }
    }

    public Command applyRequest(Supplier<SwerveRequest> requestSupplier) {
        return run(() -> io_.setControl(requestSupplier.get()));
    }

    public Command brake() {
        return applyRequest(() -> brakeRequest_);
    }

    public Command pointModules(Supplier<Double> rawLeftX, Supplier<Double> rawLeftY) {
        return applyRequest(() -> pointRequest_.withModuleDirection(new Rotation2d(-rawLeftY.get(), -rawLeftX.get())));
    }

    public void setControl(SwerveRequest request) {
        io_.setControl(request);
    }

    public void seedFieldRelative(Pose2d pose2d) {
        io_.seedFieldRelative(pose2d);
    }

    public void seedFieldRelative() {
        io_.seedFieldRelative();
    }

    public void addVisionMeasurement(Pose2d visionMeasurement, double timestampSeconds) {
        io_.addVisionMeasurement(visionMeasurement, timestampSeconds);
    }

    public void addVisionMeasurement(Pose2d visionMeasurement, double timestampSeconds, Matrix<N3, N1> visionMeasurementStdDevs) {
        io_.addVisionMeasurement(visionMeasurement, timestampSeconds, visionMeasurementStdDevs);
    }

    public SwerveDriveState getState() {
        return state_;
    }

    public double getGyroRate() {
        return inputs_.gyroRate;
    }

    public boolean odometryIsValid() {
        return inputs_.odometryIsValid;
    }
}
