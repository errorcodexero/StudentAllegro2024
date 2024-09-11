// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
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
import frc.robot.subsystems.oi.OIConstants;
import frc.robot.subsystems.oi.OISubsystem;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

  // Gamepad
  private final CommandXboxController gamepad_ =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

  // Swerve
  private double MaxSpeed = TunerConstants.kSpeedAt12VoltsMps; // kSpeedAt12VoltsMps desired top speed
  private double MaxAngularRate = 1.5 * Math.PI; // 3/4 of a rotation per second max angular velocity

  private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
    .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
    .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // I want field-centric driving in open loop

  private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
  private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

  private final SwerveSubsystem drivetrain = new SwerveSubsystem(new SwerveIOCrossTheRoad());

  // private final Telemetry logger = new Telemetry(MaxSpeed);

  // Intake Shooter
  private final IntakeShooterSubsystem intake_shooter_ = new IntakeShooterSubsystem(new IntakeShooterIOHardware());

  // OI
  private final OISubsystem oiPanel_ = new OISubsystem(2);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
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

    oiPanel_.setIndicator(2, false);
    oiPanel_.setIndicator(3, true);
    oiPanel_.setIndicator(4, false);

    // Button Interactivity Testing
    oiPanel_.climbPrepare().onTrue(Commands.runOnce(() -> {
      oiPanel_.setIndicator(OIConstants.Indicators.climbPrepareEnabled, true);
      oiPanel_.setIndicator(OIConstants.Indicators.climbExecuteEnabled, false);
    }, oiPanel_));

    oiPanel_.climbExecute().onTrue(Commands.runOnce(() -> {
      oiPanel_.setIndicator(OIConstants.Indicators.climbPrepareEnabled, false);
      oiPanel_.setIndicator(OIConstants.Indicators.climbExecuteEnabled, true);
    }, oiPanel_));

    oiPanel_.unclimb().whileTrue(Commands.runOnce(() -> {
      oiPanel_.setIndicator(OIConstants.Indicators.unclimbEnabled, true);
    }, oiPanel_));

    oiPanel_.unclimb().whileFalse(Commands.runOnce(() -> {
      oiPanel_.setIndicator(OIConstants.Indicators.unclimbEnabled, false);
    }, oiPanel_));

        drivetrain.setDefaultCommand( // Drivetrain will execute this command periodically
        drivetrain.applyRequest(() -> drive.withVelocityX(-gamepad_.getLeftY() * MaxSpeed) // Drive forward with
                                                                                           // negative Y (forward)
            .withVelocityY(-gamepad_.getLeftX() * MaxSpeed) // Drive left with negative X (left)
            .withRotationalRate(-gamepad_.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
        ));

    gamepad_.a().whileTrue(drivetrain.applyRequest(() -> brake));
    gamepad_.b().whileTrue(drivetrain
        .applyRequest(() -> point.withModuleDirection(new Rotation2d(-gamepad_.getLeftY(), -gamepad_.getLeftX()))));

    // reset the field-centric heading on left bumper press
    gamepad_.leftBumper().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldRelative()));

    if (Utils.isSimulation()) {
      drivetrain.seedFieldRelative(new Pose2d(new Translation2d(), Rotation2d.fromDegrees(90)));
    }
    // drivetrain.registerTelemetry(logger::telemeterize);
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