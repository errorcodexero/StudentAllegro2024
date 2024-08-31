/**
 * Constants for the IntakeShooter subsystem.
 */
package frc.robot.subsystems.IntakeShooter;

import org.xero1425.util.PWLs;

public final class IntakeShooterConstants {
    /**
     * Constants for the feeder mechanism.
     */
    public static final class FeederConstants{
      public static final int CANID = 1;
    
      public static final double kP = 0.0035;
      public static final double kD = 0.004;
      public static final double kV = 0.000312;
      public static final double maxv = 80;
      public static final double maxa = 80;
      public static final double jerk = 0.0;

      public static final double currentLimit = 60.0;

      public static final boolean inverted = true;

      public static final double gearRatio = 5.0;

      public static final double intakeTarget = 10.0;
      public static final double keepSpinningIntakeTarget = 0.4;
      public static final double shootTarget = 40.0;
      public static final double transferTarget = 6.0;
      public static final double ejectTarget = 40.0;
    }

    /**
     * Constants for the up-down mechanism.
     */
    public static class UpDownConstants{
      public static final int CANID = 2;

      public static final double kP = 10.0;
      public static final double kD = 0.0;
      public static final double kV = 0.18;

      public static final double maxv = 60.0;
      public static final double maxa = 80.0;
      public static final double jerk = 500.0;

      public static final boolean inverted = false;

      public static final double gearRatio = 66.0;

      public static final double intakeTarget = -10.0;
      public static final double autoShootTargetClose = 99.0;
      public static final double autoShootTargetFar = 116.0;
      public static final double autoShootTargetSwitchSpot = 2.5;
      public static final double podiumShootTarget = 116.0;
      public static final double subwooferShootTarget = 99.0;

      @Deprecated
      public static final double tiltCanMoveIntakeTarget = 30.0;

      public static final double transferTarget = 90.0;
      public static final double stowTarget = 116.0;
    }

    /**
     * Constants for the shooter mechanism.
     */
    public static class ShooterConstants{
      public static final int CANID1 = 3;
      public static final int CANID2 = 4;

      public static final double kP = 0.7;
      public static final double kD = 0.0;
      public static final double kV = 0.135;

      public static final double currentLimit = 80;
      
      public static final boolean inverted1 = false;
      public static final boolean inverted2 = false;

      public static final double gearRatio = 0.6;

      public static final double podiumShootTarget = 70.0;
      public static final double subwooferShootTarget = 65.0;
      public static final double autoShootTarget = 80.0;
      public static final double transferTargetVel = 15.0;
      public static final double transferTargetPos = 540.0;
      public static final double ejectTarget = 80.0;

      public static final double[] distances = new double[] {
        1.24,   
        1.52,  
        1.82,  
        2.119, 
        2.12,  
        2.43,  
        2.73,  
        3.04,  
        3.34,  
        3.6399,
        3.64,  
        4.0,   
      };

      public static final double[] positions = new double[] {
        65, 
        65,
        65,
        65,
        80,
        80,
        80,
        80,
        80,
        80,
        84,
        84
      };

      public static final PWLs pwls = new PWLs(distances, positions);

    }

    /**
     * Constants for the tilt mechanism.
     */
    public static class TiltConstants{
      public static final int CANID = 5;

      public static final double currentLimit = 60.0;

      public static final double kP = 5.0;
      public static final double kD = 0.35;
      public static final double kV = 0.1;

      public static final double maxv = 60 * 7.2727273 / 20.0;
      public static final double maxa = 80 * 7.2727273 / 20.0;
      public static final double jerk = 500 * 7.2727273 / 20.0;

      private static final double[] distances = new double[]{1.24,  -64, 
            1.52,  
            1.82,  
            2.119, 
            2.12,  
            2.43,  
            2.73,  
            3.04,  
            3.34,  
            3.6399,
            3.64,  
            4.0,   };
    
      private static final double[] positions = new double[]{
        -64, 
        -60,
        -55,
        -55,
        -74,
        -66,
        -64,
        -62,
        -61,
        -59,
        -59,
        -57
      };

      public static final PWLs pwls = new PWLs(distances, positions);

      public static final boolean inverted = false;

      public static final double gearRatio = 18.0;
      
      public static final double intakeTarget = 50.0;
      public static final double upDownCanMoveIntakeTarget = -25.0;
      public static final double podiumShootTarget = -67.0;
      public static final double subwooferShootTarget = -65.0;
      public static final double transferTarget = 0.0;
      public static final double stowTarget = -74.0;

    }

    /**
     * General constants for the IntakeShooter subsystem.
     */
    public static final String name = "Intake Shooter Subsystem";

    public static final double shootSecs = 0.2;
    public static final double shootOKThresh = 2.0;
    public static final double otherOKThresh = 5.0;
    public static final double ejectSecs = 0.3;

    public static final double speakerHeight = 2.1;
    public static final double tiltUpDownSpeed = 0.1;

    public static final double tiltDiffFromUpDown = 41; //(avg of diff from updown and tilt) (116 + -74, -10 + 50)
  }