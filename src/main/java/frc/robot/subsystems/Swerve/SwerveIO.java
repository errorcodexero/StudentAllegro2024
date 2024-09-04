package frc.robot.subsystems.Swerve;

import java.util.function.Supplier;

import org.littletonrobotics.junction.AutoLog;

import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public interface SwerveIO {

    @AutoLog
    public static class SwerveIOInputs {

    }

    public default void updateInputs(SwerveIOInputsAutoLogged inputs) {}

    public default Command applyRequest(Supplier<SwerveRequest> requestSupplier) { return new Command() {}; }
    
    public default void seedFieldRelative(Pose2d pose2d) {};
    public default void seedFieldRelative() {};


}
