package frc.robot.subsystems.Swerve;

import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.mechanisms.swerve.SwerveDrivetrain.SwerveDriveState;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.generated.TunerConstants;

public class SwerveSubsystem extends SubsystemBase {
    private final SwerveIO io_;
    private final SwerveIOInputsAutoLogged inputs_;
    private final SwerveDriveState state_;

    private double maxSpeed_ = TunerConstants.kSpeedAt12VoltsMps; // kSpeedAt12VoltsMps desired top speed
    private double maxAngularRate_ = 1.5 * Math.PI; // 3/4 of a rotation per second max angular velocity

    private final SwerveRequest.FieldCentric driveRequest_ = new SwerveRequest.FieldCentric()
        .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // I want field-centric driving in open loop
    
    private final SwerveRequest.SwerveDriveBrake brakeRequest_ = new SwerveRequest.SwerveDriveBrake();

    private final SwerveRequest.PointWheelsAt pointRequest_ = new SwerveRequest.PointWheelsAt();   

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
    }

    /**
     * Eases the velocity input (-1 to 1) to be quadratic. This makes drivebases be affected less by smaller movements of joysticks.
     * @param rawInput
     * @return The eased input.
     */
    private double easeVelocityInput(double rawInput) {
        return Math.signum(MathUtil.applyDeadband(rawInput, 0.02)) * Math.abs(Math.pow(rawInput, Constants.OperatorConstants.DRIVE_EASE_EXPONENT));
    }

    public Command teleopDrive(Supplier<Double> rawLeftX, Supplier<Double> rawLeftY, Supplier<Double> rawRightX, Supplier<Boolean> slowMode) {
        return applyRequest(() ->
            driveRequest_
                .withVelocityX(easeVelocityInput(-rawLeftY.get()) * maxSpeed_)
                .withVelocityY(easeVelocityInput(-rawLeftX.get()) * maxSpeed_)
                .withRotationalRate(easeVelocityInput(-rawRightX.get()) * maxAngularRate_)
        );
    }

    public Command applyRequest(Supplier<SwerveRequest> requestSupplier) {
        Command cmd = io_.applyRequest(requestSupplier);
        cmd.addRequirements(this);

        return cmd;
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
