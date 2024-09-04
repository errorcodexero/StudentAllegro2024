package frc.robot.subsystems.Swerve;

import java.util.function.Supplier;

import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.Swerve.ctre.CommandSwerveDrivetrain;

public class SwerveIOCrossTheRoad implements SwerveIO {

    private final CommandSwerveDrivetrain drivetrain = TunerConstants.DriveTrain; // My drivetrain

    @Override
    public void updateInputs(SwerveIOInputsAutoLogged inputs) {
        // TODO Auto-generated method stub
        SwerveIO.super.updateInputs(inputs);
    }

    @Override
    public Command applyRequest(Supplier<SwerveRequest> requestSupplier) {
        // TODO Auto-generated method stub
        return SwerveIO.super.applyRequest(requestSupplier);
    }

    
    
}
