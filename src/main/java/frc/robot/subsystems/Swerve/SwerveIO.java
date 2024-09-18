package frc.robot.subsystems.Swerve;

import org.littletonrobotics.junction.AutoLog;

import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;

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

    public void updateInputs(SwerveIOInputsAutoLogged inputs);

    public void setOperatorPerspectiveForward(Rotation2d fieldDirection);

    public void setControl(SwerveRequest request);
    
    public void seedFieldRelative(Pose2d pose2d);
    public void seedFieldRelative();

    public void addVisionMeasurement(Pose2d visionMeasurement, double timestampSeconds);
    public void addVisionMeasurement(Pose2d visionMeasurement, double timestampSeconds, Matrix<N3, N1> visionMeasurementStdDevs);

}
