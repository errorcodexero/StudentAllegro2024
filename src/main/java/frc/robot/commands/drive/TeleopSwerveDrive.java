package frc.robot.commands.drive;

import java.util.function.Supplier;

import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.generated.CompSwerveConstants;
import frc.robot.subsystems.Swerve.SwerveSubsystem;

public class TeleopSwerveDrive extends Command {

    private final SwerveSubsystem drivebase_;

    private final Supplier<Double> rawLeftX_;
    private final Supplier<Double> rawLeftY_;
    private final Supplier<Double> rawRightX_;
    private final Supplier<Boolean> slowMode_;

    private final double MAX_SPEED = CompSwerveConstants.kSpeedAt12VoltsMps; // kSpeedAt12VoltsMps desired top speed
    private final double MAX_ANGULAR_RATE = 1.5 * Math.PI; // 3/4 of a rotation per second max angular velocity

    private final SwerveRequest.FieldCentric driveRequest_ = new SwerveRequest.FieldCentric()
        .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // I want field-centric driving in open loop

    public TeleopSwerveDrive(SwerveSubsystem drivebase, Supplier<Double> rawLeftX, Supplier<Double> rawLeftY, Supplier<Double> rawRightX, Supplier<Boolean> slowMode) {
        drivebase_ = drivebase;

        rawLeftX_ = rawLeftX;
        rawLeftY_ = rawLeftY;
        rawRightX_ = rawRightX;
        slowMode_ = slowMode;

        addRequirements(drivebase);
    }

    @Override
    public void execute() {

        double speedMultiplier = slowMode_.get() ? Constants.OperatorConstants.SLOW_DRIVE_MULTIPLIER : 1;

        drivebase_.setControl(
            driveRequest_
                .withVelocityX(easeVelocityInput(-rawLeftY_.get()) * MAX_SPEED * speedMultiplier)
                .withVelocityY(easeVelocityInput(-rawLeftX_.get()) * MAX_SPEED * speedMultiplier)
                .withRotationalRate(easeVelocityInput(-rawRightX_.get()) * MAX_ANGULAR_RATE * speedMultiplier)
        );
    }

    /**
     * Eases the velocity input (-1 to 1) to be quadratic. This makes drivebases be affected less by smaller movements of joysticks.
     * @param rawInput
     * @return The eased input.
     */
    private double easeVelocityInput(double rawInput) {
        return Math.signum(MathUtil.applyDeadband(rawInput, Constants.OperatorConstants.CONTROLLER_DEADBAND)) * Math.abs(Math.pow(rawInput, Constants.OperatorConstants.DRIVE_EASE_EXPONENT));
    }

}
