package frc.robot.subsystems.Limelight;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.subsystems.Limelight.structs.XeroFiducial;
import frc.robot.subsystems.Limelight.structs.XeroPoseEstimate;

public interface LimelightIO {

    @AutoLog
    public class LimelightIOInputs {
        // General Values That Will be Replayed
        public int simpleID = 0;
        public double simpleX = 0.0;
        public double simpleY = 0.0;
        public double simpleArea = 0.0;
        public boolean simpleValid = false;
        
        public Translation2d[] rawCorners = new Translation2d[] {};
        public XeroFiducial[] fiducials = new XeroFiducial[] {};

        public XeroPoseEstimate poseEstimate = new XeroPoseEstimate();

    }

    /**
     * Updates the inputs object with values from the hardware.
     * @param inputs The inputs to update
     */
    public default void updateInputs(LimelightIOInputsAutoLogged inputs) {};

    /**
     * Forces the indicator light on the limelight to be off.
     */
    public default void forceOff() {};

    /**
     * Forces the indicator light on the limelight to blink.
     */
    public default void forceBlink() {};

    /**
     * Forces the indicator light on the limelight to be on.
     */
    public default void forceOn() {};

    /**
     * Resets the indicator light on the limelight to be controlled by its own software/pipelines.
     */
    public default void resetLed() {};

    /**
     * Gives the robot orientation to the limelight for Megatag2 odometry.
     * Blue origion, CCW-positive, 0 degrees facing red alliance wall
     */
    public default void giveRobotOrientation(double yaw, double yawRate, double pitch, double pitchRate, double roll, double rollRate) {};

}
