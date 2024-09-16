package frc.robot.subsystems.Swerve;

import java.util.function.Supplier;

import org.littletonrobotics.junction.AutoLog;

import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj2.command.Command;

public interface SwerveIO {

    @AutoLog
    public static class SwerveIOInputs {
        public SwerveModuleState[] moduleStates = new SwerveModuleState[4];
        public SwerveModuleState[] moduleTargetStates = new SwerveModuleState[4];
        public Pose2d pose = new Pose2d();
        public ChassisSpeeds speeds = new ChassisSpeeds();
        public double odometryPeriodSeconds = 0.0;
        public int successfulDaqs = 0;
        public int failedDaqs = 0;

        public double gyroRate = 0.0;
        public Rotation3d rotation3d = new Rotation3d();
        public boolean odometryIsValid = false;
    }

    public default void updateInputs(SwerveIOInputsAutoLogged inputs) {}

    public default Command applyRequest(Supplier<SwerveRequest> requestSupplier) { return new Command() {}; }
    
    public default void seedFieldRelative(Pose2d pose2d) {};
    public default void seedFieldRelative() {};

    public default void addVisionMeasurement(Pose2d visionMeasurement, double timestampSeconds) {}
    public default void addVisionMeasurement(Pose2d visionMeasurement, double timestampSeconds, Matrix<N3, N1> visionMeasurementStdDevs) {}

    


}