// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
* The VM is configured to automatically run this class, and to call the functions corresponding to
* each mode, as described in the TimedRobot documentation. If you change the name of this class or
* the package after creating this project, you must also update the build.gradle file in the
* project.
*/
public class Robot extends LoggedRobot {
    
    private Command m_autonomousCommand;
    
    private RobotContainer m_robotContainer;
    
    /**
    * This function is run when the robot is first started up and should be used for any
    * initialization code.
    */
    @Override
    public void robotInit() {
        
        // Record gversion metadata.

        Logger.recordMetadata("Robot", Constants.ROBOT.toString());
        Logger.recordMetadata("ProjectName", BuildConstants.MAVEN_NAME);
        Logger.recordMetadata("BuildDate", BuildConstants.BUILD_DATE);
        Logger.recordMetadata("GitSHA", BuildConstants.GIT_SHA);
        Logger.recordMetadata("GitDate", BuildConstants.GIT_DATE);
        Logger.recordMetadata("GitBranch", BuildConstants.GIT_BRANCH);
        Logger.recordMetadata("RuntimeEnvironment", getRuntimeType().toString());

        switch (BuildConstants.DIRTY) {
            case 0:
                Logger.recordMetadata("GitDirty", "All changes committed");
                break;
            case 1:
                Logger.recordMetadata("GitDirty", "Uncomitted changes");
                break;
            default:
                Logger.recordMetadata("GitDirty", "Unknown");
                break;
        }
        
        // Set up data flow
        switch (Constants.ROBOT_MODE) {
            case REAL:
                Logger.addDataReceiver(new WPILOGWriter());
                Logger.addDataReceiver(new NT4Publisher());
                
                // new PowerDistribution(0, ModuleType.kCTRE); // Enables power distribution logging
            
                break;
            case SIMULATED:
                if (Constants.SAVE_SIMULATED_LOGS) {
                    Logger.addDataReceiver(new WPILOGWriter());
                }
                
                Logger.addDataReceiver(new NT4Publisher());
            
                break;
            case REPLAYED:
                setUseTiming(false);
                
                String logPath = LogFileUtil.findReplayLog(); // Finds the log to replay from, whatever is currently open in AdvantageScope
                String replayLogPath = LogFileUtil.addPathSuffix(logPath, "_replay"); // Adds _replay to differentiate between the original file
                
                Logger.setReplaySource(new WPILOGReader(logPath));
                Logger.addDataReceiver(new WPILOGWriter(replayLogPath));
                
                break;
        }
        
        Logger.recordMetadata("Environment", Constants.ROBOT_MODE.toString());
        
        // Logger.disableDeterministicTimestamps() // See "Deterministic Timestamps" in the "Understanding Data Flow" page
        Logger.start(); // Start logging! No more data receivers, replay sources, or metadata values may be added.
        
        // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
        // autonomous chooser on the dashboard.
        m_robotContainer = new RobotContainer();
    }
    
    /**
    * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
    * that you want ran during disabled, autonomous, teleoperated and test.
    *
    * <p>This runs after the mode specific periodic functions, but before LiveWindow and
    * SmartDashboard integrated updating.
    */
    @Override
    public void robotPeriodic() {
        // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
        // commands, running already-scheduled commands, removing finished or interrupted commands,
        // and running subsystem periodic() methods.  This must be called from the robot's periodic
        // block in order for anything in the Command-based framework to work.
        CommandScheduler.getInstance().run();
    }
    
    /** This function is called once each time the robot enters Disabled mode. */
    @Override
    public void disabledInit() {}
    
    @Override
    public void disabledPeriodic() {}
    
    @Override
    public void disabledExit() {}
    
    @Override
    public void autonomousInit() {
        m_autonomousCommand = m_robotContainer.getAutonomousCommand();
        
        // schedule the autonomous command (example)
        if (m_autonomousCommand != null) {
            m_autonomousCommand.schedule();
        }
    }
    
    /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic() {}
    
    @Override
    public void autonomousExit() {}
    
    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (m_autonomousCommand != null) {
            m_autonomousCommand.cancel();
        }
    }
    
    /** This function is called periodically during operator control. */
    @Override
    public void teleopPeriodic() {}
    
    @Override
    public void teleopExit() {}
    
    @Override
    public void testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
    }
    
    /** This function is called periodically during test mode. */
    @Override
    public void testPeriodic() {}
    
    /** This function is called once when the robot is first started up. */
    @Override
    public void simulationInit() {}
    public void testExit() {}
    /** This function is called periodically whilst in simulation. */
    @Override
    public void simulationPeriodic() {}
}
