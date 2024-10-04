// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Inches;

import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedDashboardNumber;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.drive.TeleopSwerveDrive;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.IntakeShooter.IntakeShooterIOHardware;
import frc.robot.subsystems.IntakeShooter.IntakeShooterSubsystem;
import frc.robot.subsystems.Limelight.Limelight;
import frc.robot.subsystems.Swerve.SwerveSubsystem;
import frc.robot.subsystems.TargetTracker.TargetTracker;
import frc.robot.subsystems.oi.OISubsystem;
import frc.robot.util.ComponentVisualizer;


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
    
    // Subsystems
    
    private final SwerveSubsystem drivetrain_ = new SwerveSubsystem(TunerConstants.DriveTrain); 

    private final IntakeShooterSubsystem intake_shooter_ =
        new IntakeShooterSubsystem(new IntakeShooterIOHardware());

    private final OISubsystem oiPanel_ = new OISubsystem(2);

    private final Limelight limelight_ = new Limelight();

    private final TargetTracker targetTracker_ = new TargetTracker(() -> drivetrain_.getState().Pose);

    private final ComponentVisualizer visualizer_ = new ComponentVisualizer("TestingComponents");

    private final LoggedDashboardNumber updownAngle_ = new LoggedDashboardNumber("UpdownAngle");
    private final LoggedDashboardNumber tiltAngle_ = new LoggedDashboardNumber("TiltAngle");
    private final LoggedDashboardNumber elevatorHeight_ = new LoggedDashboardNumber("ElevatorHeight");
    private final LoggedDashboardNumber armAngle_ = new LoggedDashboardNumber("ArmAngle");
    private final LoggedDashboardNumber climberHeight_ = new LoggedDashboardNumber("ClimberHeight");
    
    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        // Configure the trigger bindings
        configureBindings();
        setupDrivetrain();

        // Sets up Megatag2 tracking with the limelight.
        limelight_.trackMegatag2(() -> drivetrain_.getState().Pose, (estimate) -> {
            drivetrain_.addVisionMeasurement(
                estimate.pose,
                estimate.timestamp,
                VecBuilder.fill(0.7, 0.7, 9999999)
            );
        }).schedule();

        Logger.recordOutput("testpose", new Pose2d());

        Pose3d[] poses = {new Pose3d(), new Pose3d(), new Pose3d(), new Pose3d(), new Pose3d()};
        Logger.recordOutput("componentstest", poses);

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
        // Points all the swerve modules inward, resulting in restisting movement
        gamepad_.a().whileTrue(drivetrain_.brake());
        
        // Point all the swerve modules to where the joystick points.
        gamepad_.b().whileTrue(drivetrain_.pointModules(gamepad_::getLeftX, gamepad_::getLeftY)); 

        // reset the field-centric heading on Y and B press simultaneously
        gamepad_.y().and(gamepad_.b()).onTrue(drivetrain_.runOnce(() -> drivetrain_.seedFieldRelative()));
    }
    
    private void setupDrivetrain() {
        drivetrain_.setDefaultCommand(new TeleopSwerveDrive(
            drivetrain_, // 
            gamepad_::getLeftX, // Suppliers for gamepad controls.
            gamepad_::getLeftY,
            gamepad_::getRightX,
            gamepad_.x()::getAsBoolean // Slow mode boolean supplier.
        ));
    }
    
    /**
    * Use this to pass the autonomous command to the main {@link Robot} class.
    *
    * @return the command to run in autonomous
    */
    public Command getAutonomousCommand() {
        // return Commands.print("No autonomous command configured");
        return Commands.run(() -> {
            visualizer_.setUpdownAngle(Degrees.of(updownAngle_.get()));
            visualizer_.setTiltAngle(Degrees.of(tiltAngle_.get()));
            visualizer_.setElevatorHeight(Inches.of(elevatorHeight_.get()));
            visualizer_.setArmAngle(Degrees.of(armAngle_.get()));
            visualizer_.setClimberHeight(Inches.of(climberHeight_.get()));
        });
    }
}