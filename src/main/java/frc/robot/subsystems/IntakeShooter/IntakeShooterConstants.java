package frc.robot.subsystems.IntakeShooter;

public final class IntakeShooterConstants {
    public static final class FeederConstants{
      public static final int CANID = 1;

      public static final double kP = 0.0;
      public static final double kD = 0.0;
      public static final double kV = 0.0;

      public static final boolean inverted = false;

      public static final double gearRatio = 1.0;

      public static final double intakeTarget = 0.0;
      public static final double keepSpinningIntakeTarget = 0.0;
      public static final double shootTarget = 0.0;
      public static final double transferTarget = 0.0;
      public static final double ejectTarget = 0.0;
    }

    public static class UpDownConstants{
      public static final int CANID = 2;

      public static final double kP = 0.0;
      public static final double kD = 0.0;
      public static final double kV = 0.0;

      public static final boolean inverted = false;

      public static final double gearRatio = 1.0;

      public static final double intakeTarget = 0.0;
      public static final double autoShootTargetClose = 90.0;
      public static final double autoShootTargetSwitchSpot = 0.0;
      public static final double autoShootTargetFar = 0.0;
      public static final double podShootTarget = 0.0;
      public static final double subShootTarget = 0.0;
      public static final double tiltCanMoveIntakeTarget = 0.0;
      public static final double transferTarget = 0.0;
      public static final double stowTarget = 0.0;
    }

    public static class ShooterConstants{
      public static final int CANID1 = 3;
      public static final int CANID2 = 4;

      public static final double kP = 0.0;
      public static final double kD = 0.0;
      public static final double kV = 0.0;
      
      public static final boolean inverted1 = false;
      public static final boolean inverted2 = false;

      public static final double gearRatio = 1.0;

      public static final double podShootTarget = 70.0;
      public static final double subShootTarget = 65.0;
      @Deprecated
      public static final double autoShootTarget = 80.0;
      public static final double transferTargetVel = 58.0;
      public static final double transferTargetPos = 540.0;
      public static final double ejectTarget = 80.0;
    }

    public static class TiltConstants{
      public static final int CANID = 5;

      public static final double kP = 0.0;
      public static final double kD = 0.0;
      public static final double kV = 0.0;

      public static final boolean inverted = false;

      public static final double gearRatio = 1.0;
      
      public static final double intakeTarget = 0.0;
      public static final double upDownCanMoveIntakeTarget = 0.0;
      public static final double podShootTarget = 0.0;
      public static final double subShootTarget = 0.0;
      public static final double transferTarget = 0.0;
      public static final double stowTarget = 0.0;
    }
    public static final String name = "Intake Shooter Subsystem";

    public static final double shootSecs = 1.0;
    public static final double shootOKThresh = 1.0;
    public static final double otherOKThresh = 2.5;
    public static final double ejectSecs = 1.75;
    public static final double speakerHeight = 2.1;
  }
