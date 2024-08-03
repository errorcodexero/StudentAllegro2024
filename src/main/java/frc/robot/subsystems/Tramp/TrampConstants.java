package frc.robot.subsystems.Tramp;

// things that need to be added:
//
// settings/heights for certain commands (this is needed for commands)
// motor initialization things (inverted, current limit, min/max pos, max accel/vel)


public class TrampConstants {
    public class KeepoutZone {
        public static final double elevatorHeight = 0.21; 
        public static final double armMin = 5.0; 
        public static final double armMax = 75.0; 
    }

    public class Arm {
        public static final int motorCANID = 7; 

        public static final double kP = 2.3;
        public static final double kI = 0.0;
        public static final double kD = 0.0;
        public static final double kV = 0.2;
        public static final double kA = 0.0;
        public static final double kG = 0.0;
        public static final double kS = 0.0;

        public static final double outmax = 12.0;
        public static final double maxv = 360;
        public static final double maxa = 300;
        public static final double jerk = 0.0;

        public static final boolean inverted = false;

        public static final double gearRatio = 14.81;

        public static final double trapTarget = 180;
        public static final double ampTarget = 225;
        public static final double stowTarget = 0.0;
    }

    public class Climber {
        public static final int motorCANID = 10; 

        public static final boolean inverted = true;       
        
        public static final double metersPerRev = 0.00056420512;

        public static final double ClimbTarget = 0.0;
        public static final double PrepClimbTarget = 1.0;
    }

    public class Elevator {
        public static final int motorCANID = 6; 
        
        public static final double kP = 600.0;
        public static final double kI = 0.0;
        public static final double kD = 0.0;
        public static final double kV = 50.0;
        public static final double kA = 0.0;
        public static final double kG = 0.0;
        public static final double kS = 0.0;
        
        public static final double outmax = 12.0;
        public static final double maxv = 0.4;
        public static final double maxa = 1.0;
        public static final double jerk = 0.0;

        public static final boolean inverted = false;

        public static final double metersPerRev = 0.00393313925;

        public static final double trapTarget = 0.2411;
        public static final double ampTarget = 0.2 ;
        public static final double stowTarget = 0.0;

    }

    public class Manipulator {
        public static final int motorCANID = 9; 

        public static final double kP = 0.0015;
        public static final double kI = 0.0;
        public static final double kD = 0.0;
        public static final double kV = 0.000312;
        public static final double kA = 0.0;
        public static final double kG = 0.0;
        public static final double kS = 0.0;
        
        public static final double outmax = 12.0;
        public static final double maxv = 5.0;
        public static final double maxa = 7.5;
        public static final double jerk = 0.0;

        public static final boolean inverted = false;
    }
}
