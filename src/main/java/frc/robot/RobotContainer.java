// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.IntakeShooter.IntakeShooterIOHardware;
import frc.robot.subsystems.IntakeShooter.IntakeShooterSubsystem;
import frc.robot.subsystems.Swerve.SwerveIOCrossTheRoad;
import frc.robot.subsystems.Swerve.SwerveSubsystem;
import frc.robot.subsystems.oi.OISubsystem;


/**
* This class is where the bulk of the robot should be declared. Since Command-based is a
* "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
* periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
* subsystems, commands, and trigger mappings) should be declared here.
*/
public class RobotContainer {
    
    // Gamepad and generic initialization
    
    private final CommandXboxController gamepad_ =
        new CommandXboxController(OperatorConstants.kDriverControllerPort);
    
    private double maxSpeed_ = TunerConstants.kSpeedAt12VoltsMps; // kSpeedAt12VoltsMps desired top speed
    private double maxAngularRate_ = 1.5 * Math.PI; // 3/4 of a rotation per second max angular velocity
    
    private final SwerveRequest.FieldCentric drive_ = new SwerveRequest.FieldCentric()
        .withDeadband(maxSpeed_ * 0.1).withRotationalDeadband(maxAngularRate_ * 0.1) // Add a 10% deadband
        .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // I want field-centric driving in open loop
    
    private final SwerveRequest.SwerveDriveBrake brake_ = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point_ = new SwerveRequest.PointWheelsAt();   
    
    // Subsystems
    
    private final SwerveSubsystem drivetrain_ = new SwerveSubsystem(new SwerveIOCrossTheRoad(TunerConstants.DriveTrain)); 

    private final IntakeShooterSubsystem intake_shooter_ =
        new IntakeShooterSubsystem(new IntakeShooterIOHardware());  

    private final OISubsystem oiPanel_ = new OISubsystem(2);   
    
    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        // Configure the trigger bindings
        configureBindings();
        setupDrivetrain();
    }
    
    /**
    * Use this method to define your trigger->command mappings. Triggers can be created via the
    * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
    * predicate, or via the named factories in {@link
    * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
    * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
    * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
    * joysticks}.
    */
    private void configureBindings() {
        // Points all the swerve modules inward, resulting in restisting movement.
        gamepad_.a().whileTrue(drivetrain_.applyRequest(() -> brake_));
        
        // Point all the swerve modules to where the joystick points.
        gamepad_.b().whileTrue(drivetrain_.applyRequest(
            () -> point_.withModuleDirection(new Rotation2d(-gamepad_.getLeftY(), -gamepad_.getLeftX()))
        )); 

        // reset the field-centric heading on Y and B press simultaneously
        gamepad_.y().and(gamepad_.b()).onTrue(drivetrain_.runOnce(() -> drivetrain_.seedFieldRelative()));
    }
    
    private void setupDrivetrain() {
        drivetrain_.setDefaultCommand( // Drivetrain will execute this command periodically
            drivetrain_.applyRequest(() -> drive_
                .withVelocityX(Math.pow(MathUtil.applyDeadband(-gamepad_.getLeftY(), 0.04), 2) * maxSpeed_) // Drive forward with negative Y (forward)
                .withVelocityY(Math.pow(MathUtil.applyDeadband(-gamepad_.getLeftX(), 0.04), 2) * maxSpeed_) // Drive left with negative X (left)
                .withRotationalRate(Math.pow(MathUtil.applyDeadband(-gamepad_.getRightX(), 0.04), 2) * maxAngularRate_) // Drive counterclockwise with negative X (left)
            )
        );
    }
    
    /**
    * Use this to pass the autonomous command to the main {@link Robot} class.
    *
    * @return the command to run in autonomous
    */
    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }
}