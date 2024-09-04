package frc.robot.subsystems.Swerve;

import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SwerveSubsystem extends SubsystemBase {
    private final SwerveIO io_;
    private final SwerveIOInputsAutoLogged inputs_;

    public SwerveSubsystem(SwerveIO io) {
        io_ = io;
        inputs_ = new SwerveIOInputsAutoLogged();
    }

    @Override
    public void periodic() {
        io_.updateInputs(inputs_);
        Logger.processInputs("Swerve", inputs_);
    }

    public Command applyRequest(Supplier<SwerveRequest> requestSupplier) {
        return io_.applyRequest(requestSupplier);
    }

    public void seedFieldRelative(Pose2d pose2d) {
        io_.seedFieldRelative(pose2d);
    }

    public void seedFieldRelative() {
        io_.seedFieldRelative();
    }
}
