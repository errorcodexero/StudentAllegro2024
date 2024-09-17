// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
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
        COMPETITION,
        PRACTICE
    }
    
    public static enum RobotMode {
        REAL,
        SIMULATED,
        REPLAYED
    }
    
    public static final Robot ROBOT = Robot.COMPETITION; // The robot currently running.
    public static final boolean REPLAYING = false; // Should the simulation start into replay mode when not running on the RIO?
    public static final boolean SAVE_SIMULATED_LOGS = false; // Should the physics simulation mode save a .wpilog?
    
    public static final RobotMode ROBOT_MODE = RobotBase.isReal() ? RobotMode.REAL : (REPLAYING ? RobotMode.REPLAYED : RobotMode.SIMULATED);
    
    public static class FieldConstants {
        private static final AprilTagFields FIELD = AprilTagFields.k2024Crescendo;
        public static final AprilTagFieldLayout FIELD_LAYOUT = AprilTagFieldLayout.loadField(FIELD);
    }

    public static class OperatorConstants {
        public static final int kDriverControllerPort = 0;
    }
    
    public static class IntakeShooterConstants {
        public static class FeederConstants{
            public static final double kP = 0.0;
            public static final double kD = 0.0;
            public static final double kV = 0.0;
            
            public static final double gearRatio = 1.0;
            
            public static final double intakeTarget = 0.0;
            
            public static final double keepSpinningIntakeTarget = 0.0;
        }
        
        public static class UpDownConstants{
            public static final double kP = 0.0;
            public static final double kD = 0.0;
            public static final double kV = 0.0;
            
            public static final double gearRatio = 1.0;
            
            public static final double intakeTarget = 0.0;
        }
        
        public static class Shooter1Constants{
            public static final double kP = 0.0;
            public static final double kD = 0.0;
            public static final double kV = 0.0;
            
            public static final double gearRatio = 1.0;
        }
        
        public static class Shooter2Constants{
            public static final double kP = 0.0;
            public static final double kD = 0.0;
            public static final double kV = 0.0;
            
            public static final double gearRatio = 1.0;
        }
        
        public static class TiltConstants{
            public static final double kP = 0.0;
            public static final double kD = 0.0;
            public static final double kV = 0.0;
            
            public static final double gearRatio = 1.0;
            
            public static final double intakeTarget = 0.0;
            public static final double upDownCanMoveIntakeTarget = 0.0;
        }
    }
}
