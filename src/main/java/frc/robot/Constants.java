// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;

/**
* The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
* constants. This class should not be used for any other purpose. All constants should be declared
* globally (i.e. public static). Do not put anything functional in this class.
*
* <p>It is advised to statically import this class (or one of its inner classes) wherever the
* constants are needed, to reduce verbosity.
*/
public final class Constants {
    
    public static enum Robot {
        COMPBOT,
        PRACTICEBOT,
        SIMBOT
    }
    
    public static enum RobotEnvironment {
        REAL,
        SIMULATED,
        REPLAYED
    }
    
    // The robot currently running. Change to simbot to use simulation.
    public static final Robot ROBOT = Robot.COMPBOT;

    // Check to see if configured robot is configured incorrectly for the runtime.
    static {
        if (RobotBase.isReal() && ROBOT == Robot.SIMBOT) {
            throw new RuntimeException("Invalid Configuration! Robot set to simulation on real bot!");
        }
    }

    public static final RobotEnvironment ENVIRONMENT = RobotBase.isReal() ? RobotEnvironment.REAL : (ROBOT == Robot.SIMBOT ? RobotEnvironment.SIMULATED : RobotEnvironment.REPLAYED);

    public static final boolean SAVE_SIMULATED_LOGS = false; // Should the physics simulation mode save a .wpilog?
    
    public static class OperatorConstants {
        public static final int GAMEPAD_PORT = 0;
        public static final int OI_PORT = 2;

        public static final double SLOW_DRIVE_MULTIPLIER = 0.25;
        public static final int DRIVE_EASE_EXPONENT = 3;

        public static final double CONTROLLER_DEADBAND = 0.02;
    }
    
}
