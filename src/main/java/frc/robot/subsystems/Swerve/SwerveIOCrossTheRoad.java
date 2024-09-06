package frc.robot.subsystems.Swerve;

import java.util.function.Supplier;

import com.ctre.phoenix6.mechanisms.swerve.SwerveDrivetrain.SwerveDriveState;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.Swerve.ctre.CommandSwerveDrivetrain;

public class SwerveIOCrossTheRoad implements SwerveIO {

    private final CommandSwerveDrivetrain drivetrain = TunerConstants.DriveTrain; // My drivetrain

    @Override
    public void updateInputs(SwerveIOInputsAutoLogged inputs) {
        SwerveDriveState swerveDriveState = drivetrain.getState();
        
        inputs.moduleStates = swerveDriveState.ModuleStates;
        inputs.moduleTargetStates = swerveDriveState.ModuleTargets;
        inputs.pose = swerveDriveState.Pose;
        inputs.speeds = swerveDriveState.speeds;
        inputs.odometryPeriodSeconds = swerveDriveState.OdometryPeriod;
        inputs.successfulDaqs = swerveDriveState.SuccessfulDaqs;
        inputs.failedDaqs = swerveDriveState.FailedDaqs;

        inputs.rotation3d = drivetrain.getRotation3d();
        inputs.odometryIsValid = drivetrain.odometryIsValid();

        // Add more as needed.

    }

    @Override
    public Command applyRequest(Supplier<SwerveRequest> requestSupplier) {
        return drivetrain.applyRequest(requestSupplier);
    }

    @Override
    public void seedFieldRelative(Pose2d pose2d) {
        drivetrain.seedFieldRelative(pose2d);
    }

    @Override
    public void seedFieldRelative() {
        drivetrain.seedFieldRelative();
    }

    @Override
    public void addVisionMeasurement(Pose2d visionMeasurement, double timestampSeconds) {
        drivetrain.addVisionMeasurement(visionMeasurement, timestampSeconds);
    }

    @Override
    public void addVisionMeasurement(Pose2d visionMeasurement, double timestampSeconds, Matrix<N3, N1> visionMeasurementStdDevs) {
        drivetrain.addVisionMeasurement(visionMeasurement, timestampSeconds, visionMeasurementStdDevs);
    }
    
}
