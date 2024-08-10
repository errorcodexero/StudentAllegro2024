package frc.robot.subsystems.Limelight;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.subsystems.Limelight.LimelightHelpers.LimelightResults;
import frc.robot.subsystems.Limelight.LimelightHelpers.LimelightTarget_Fiducial;

public interface LimelightIO {

    public class LimelightIOInputs implements LoggableInputs {
        // General Values That Will be Replayed
        public double tX = 0.0;
        public double tY = 0.0;
        public double tXPixels = 0.0;
        public double tYPixels = 0.0;
        public double tArea = 0.0;
        public boolean tValid = false;
        public int fiducialID = 0;
        public Pose2d predictedBotPose2d = new Pose2d();
        public String jsonDump = "";

        // Values for easier usage within code, and for graph visualization.
        public LimelightResults parsedResults = new LimelightResults();
        public LimelightTarget_Fiducial[] fiducials = {};
        
        private final String fiducialListRoot = "Tags"; // The root for list of fiducials in logging.
        
        @Override
        public void toLog(LogTable table) {

            // Creates arrays to be graphed in advantagescope.
            Translation2d[] graphablePoints = new Translation2d[fiducials.length];
            Translation2d[] graphablePointsPixels = new Translation2d[fiducials.length];
            
            for (int i = 0; i < fiducials.length; i++) {
                LimelightTarget_Fiducial fid = fiducials[i];

                // Logs all the values about the certain fiducial.
                table.put(fidEntry("ID", i), fid.fiducialID);
                table.put(fidEntry("Family", i), fid.fiducialFamily);
                table.put(fidEntry("TX", i), fid.tx);
                table.put(fidEntry("TY", i), fid.ty);
                table.put(fidEntry("TXPixels", i), fid.tx_pixels);
                table.put(fidEntry("TYPixels", i), fid.ty_pixels);
                table.put(fidEntry("TArea", i), fid.ta);
                table.put(fidEntry("TS", i), fid.ts);

                // Adds the point to the graphable array.
                graphablePoints[i] = new Translation2d(fid.tx, fid.ty);
                graphablePointsPixels[i] = new Translation2d(fid.tx_pixels, fid.ty_pixels);

                // Logs the calculated poses from the limelight.
                table.put(fidEntry("CameraPoseTargetSpace", i), fid.getCameraPose_TargetSpace());
                table.put(fidEntry("RobotPoseFieldSpace", i), fid.getRobotPose_FieldSpace());
                table.put(fidEntry("RobotPoseTargetSpace", i), fid.getRobotPose_TargetSpace());
                table.put(fidEntry("TargetPoseCameraSpace", i), fid.getTargetPose_CameraSpace());
                table.put(fidEntry("TargetPoseRobotSpace", i), fid.getTargetPose_RobotSpace());
                
            }

            // Logged values for graph visualization.
            table.put(fiducialListRoot + "/GraphablePointsPixels", graphablePointsPixels);

            // Logs values that will be replayed.
            table.put("TX", tX);
            table.put("TY", tY);
            table.put("TXPixels", tXPixels);
            table.put("TYPixels", tYPixels);
            table.put("TCombinedPixels", new double[] {tXPixels, tYPixels});
            table.put("TArea", tArea);
            table.put("TValid", tValid);
            table.put("FiducialID", fiducialID);
            table.put("PredictedBotPoseWpiRed", predictedBotPose2d);
            table.put("JsonDump", jsonDump);

        }
        @Override
        public void fromLog(LogTable table) {

            // Loads values from the log.
            tX = table.get("TX", tX);
            tY = table.get("TY", tY);
            tXPixels = table.get("TXPixels", tXPixels);
            tYPixels = table.get("TYPixels", tYPixels);
            tArea = table.get("TArea", tArea);
            tValid = table.get("TValid", tValid);
            fiducialID = table.get("FiducialID", fiducialID);
            predictedBotPose2d = table.get("PredictedBotPose", predictedBotPose2d);
            jsonDump = table.get("JsonDump", jsonDump);

            // Parses the json dump from the log, this is only to be run during replay, meaning it has no effect on robot performance.
            parsedResults = ReplayLimelightHelpers.parseJson(jsonDump);
            fiducials = parsedResults.targets_Fiducials;
        }

        /**
         * Creates a string key for saving a fiducial at a specific index in the root.
         * @param name The key to save.
         * @param index The index to save it to.
         * @return The formatted key.
         */
        private String fidEntry(String name, int index) {
            return fiducialListRoot + "/" + index + "/" + name;
        }

    }

    /**
     * Forces the indicator light on the limelight to be off.
     */
    public abstract void forceOff();

    /**
     * Forces the indicator light on the limelight to blink.
     */
    public abstract void forceBlink();

    /**
     * Forces the indicator light on the limelight to be on.
     */
    public abstract void forceOn();

    /**
     * Resets the indicator light on the limelight to be controlled by its own software/pipelines.
     */
    public abstract void resetLed();

    /**
     * Sets the id for tx/ty targeting. Ignore other targets.
     */
    public abstract void setPriorityTagID(int id);
    
    /**
     * Updates the inputs object with values from the hardware.
     * @param inputs The inputs to update
     */
    public abstract void updateInputs(LimelightIOInputs inputs);

}
