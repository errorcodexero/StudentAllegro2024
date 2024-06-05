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

  public static class OI {
    public class Buttons {
      public static int cycleSpeaker = 1;
      public static int cycleTrap = 2;
      public static int climbPrepare = 3;
      public static int climbExecute = 4;
      public static int unclimb = 6;
      public static int shoot = 7;
      public static int turtle = 8;
      public static int abort = 9;
      public static int eject = 10;
      public static int collect = 11;
      public static int shootingPodium = 12;
      public static int shootingSubwoofer = 13;
    }

    public class Indicators {
      public static int driveBaseReady = 1;
      public static int shooterReady = 2;
      public static int tiltReady = 3;
      public static int aprilTagReady = 4;
      public static int climbPrepareEnabled = 5;
      public static int climbExecuteEnabled = 6;
      public static int unclimbEnabled = 8;
    }
  }
}
