// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
import frc.robot.Constants.RobotEnvironment;
import frc.robot.commands.drive.TeleopSwerveDrive;
import frc.robot.generated.CompSwerveConstants;
import frc.robot.subsystems.Swerve.SwerveIO;
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
    
    // Subsystems

    // Init with default implementations in case robot is configured wrongly or is in replay. This will be overitten later.
    private SwerveSubsystem drivetrain_ = new SwerveSubsystem(new SwerveIO() {});

    private final OISubsystem oiPanel_ = new OISubsystem(OperatorConstants.kOperatorInterfacePort);
    
    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {

        // Set up real subsystems based on the runtime environment.
        if (Constants.ENVIRONMENT != RobotEnvironment.REPLAYED) {
            switch (Constants.ROBOT) {
                case COMPETITION -> {
                    drivetrain_ = new SwerveSubsystem(TunerConstants.DriveTrain);
                }
                case PRACTICE -> {
                    drivetrain_ = new SwerveSubsystem(TunerConstants.DriveTrain); // Change for other tuner constants when they are generated.
                }
                case SIMBOT -> {
                    drivetrain_ = new SwerveSubsystem(TunerConstants.DriveTrain); // Should auto simulate.
                }
            }
        }

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
        return Commands.print("No autonomous command configured");
    }
}