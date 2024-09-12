/**
 * Constants for the IntakeShooter subsystem.
 */
package frc.robot.subsystems.IntakeShooter;

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

      public static final double kP = 7.0;
      public static final double kD = 0.0;
      public static final double kV = 0.4;

      public static final double maxv = 200;
      public static final double maxa = 700;
      public static final double jerk = 0.0;

      public static final boolean inverted = false;

      public static final double gearRatio = 66.0;

      public static final double intakeTarget = -11.0;
      public static final double autoShootTargetClose = 90.0;
      public static final double autoShootTargetFar = 118.0;
      public static final double autoShootTargetSwitchSpot = 2.5;
      public static final double podiumShootTarget = 118.0;
      public static final double subwooferShootTarget = 100.0;
      public static final double tiltCanMoveIntakeTarget = 30.0;
      public static final double transferTarget = 90.0;
      public static final double stowTarget = 118.0;
    }

    /**
     * Constants for the shooter mechanism.
     */
    public static class ShooterConstants{
      public static final int CANID1 = 3;
      public static final int CANID2 = 4;

      public static final double kP = 0.3;
      public static final double kD = 0.0;
      public static final double kV = 0.08;
      
      public static final boolean inverted1 = false;
      public static final boolean inverted2 = false;

      public static final double gearRatio = 0.6;

      public static final double podiumShootTarget = 70.0;
      public static final double subwooferShootTarget = 65.0;
      public static final double autoShootTarget = 80.0;
      public static final double transferTargetVel = 58.0;
      public static final double transferTargetPos = 540.0;
      public static final double ejectTarget = 80.0;
    }

    /**
     * Constants for the tilt mechanism.
     */
    public static class TiltConstants{
      public static final int CANID = 5;

      public static final double kP = 2.8;
      public static final double kD = 0.0;
      public static final double kV = 0.1;

      public static final double maxv = 200;
      public static final double maxa = 700;
      public static final double jerk = 0.0;

      public static final boolean inverted = false;

      public static final double gearRatio = 21.33;
      
      public static final double intakeTarget = 50.0;
      public static final double upDownCanMoveIntakeTarget = -25.0;
      public static final double podiumShootTarget = -67.0;
      public static final double subwooferShootTarget = -65.0;
      public static final double transferTarget = 0.0;
      public static final double stowTarget = -71.0;
      public static final double diffFromUpDown = 71.0 - 118.0;
    }

    /**
     * General constants for the IntakeShooter subsystem.
     */
    public static final String name = "Intake Shooter Subsystem";

    public static final double shootSecs = 0.5;
    public static final double shootOKThresh = 1.0;
    public static final double otherOKThresh = 2.5;
    public static final double ejectSecs = 1.0;

    public static final double speakerHeight = 2.1;
    public static final double tiltUpDownSpeed = 0.5;
  }
