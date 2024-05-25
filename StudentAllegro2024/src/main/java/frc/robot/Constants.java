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

  public static class OI {
    public class Buttons {
      public static int cycleSpeaker = 2; // these are flipped for the new OI (flipped for some reason)
      public static int cycleTrap = 1;
      public static int climbPrepare = 3;
      public static int climbExecute = 4;
      public static int collect = 5; // on 5 for the new OI, on 11 for the old one.
      public static int unclimb = 6;
      public static int shoot = 7;
      public static int turtle = 8;
      public static int abort = 9;
      public static int eject = 10;
      public static int shootingPodium = 13; // same for these :(
      public static int shootingSubwoofer = 12;
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
