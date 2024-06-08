// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }
  
  @Deprecated
  public static class IntakeShooterConstants {

    @Deprecated
    public static class FeederConstants{
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

    @Deprecated
    public static class UpDownConstants{
      public static final int CANID = 2;

      public static final double kP = 0.0;
      public static final double kD = 0.0;
      public static final double kV = 0.0;

      public static final boolean inverted = false;

      public static final double gearRatio = 1.0;

      public static final double intakeTarget = 0.0;
      public static final double podShootTarget = 0.0;
      public static final double subShootTarget = 0.0;
      public static final double tiltCanMoveIntakeTarget = 0.0;
      public static final double transferTarget = 0.0;
      public static final double stowTarget = 0.0;
    }

    @Deprecated
    public static class ShooterConstants{
      public static final int CANID1 = 3;
      public static final int CANID2 = 4;

      public static final double kP = 0.0;
      public static final double kD = 0.0;
      public static final double kV = 0.0;
      
      public static final boolean inverted = false;

      public static final double gearRatio = 1.0;

      public static final double podShootTarget = 0.0;
      public static final double subShootTarget = 0.0;
      public static final double transferTargetVel = 0.0;
      public static final double transferTargetPos = 0.0;
      public static final double ejectTarget = 0.0;
    }

    @Deprecated
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

    public static final double shootSecs = 1.0;
    public static final double shootOKThresh = 1.0;
    public static final double otherOKThresh = 2.5;
    public static final double ejectSecs = 1.75;
  }
}
