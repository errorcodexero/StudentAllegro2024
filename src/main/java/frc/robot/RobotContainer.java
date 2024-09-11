// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;

import frc.robot.subsystems.IntakeShooter.IntakeShooterSubsystem;
import frc.robot.subsystems.Tramp.TrampSubsystem;
import frc.robot.subsystems.Tramp.TrampSubsystemIO_HW;
import frc.robot.subsystems.IntakeShooter.IntakeShooterIOHardware;

import com.revrobotics.CANSparkFlex;


import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.oi.OIConstants;
import frc.robot.subsystems.oi.OISubsystem;
import frc.robot.subsystems.oi.type.ActionType;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final OISubsystem oiPanel_ = new OISubsystem(2);

  private final IntakeShooterSubsystem intake_shooter_ = new IntakeShooterSubsystem(new IntakeShooterIOHardware());
  private final TrampSubsystem tramp_ = new TrampSubsystem(new TrampSubsystemIO_HW(), oiPanel_.actionTypeSupplier());


  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController gamepad_ =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

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

    oiPanel_.climbPrepare().onTrue(tramp_.PrepClimbCommand());
    oiPanel_.climbExecute().onTrue(tramp_.ClimbCommand());
    oiPanel_.eject().onTrue(tramp_.EjectCommand());
    oiPanel_.turtle().onTrue(tramp_.StowCommand());
    oiPanel_.shoot().onTrue(tramp_.AmpShootCommand());
    gamepad_.a().onTrue(tramp_.AmpShootCommand());
    
  

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
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return Autos.exampleAuto(intake_shooter_);
  }
}
