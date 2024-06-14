package frc.robot.subsystems.Limelight;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.subsystems.Limelight.LimelightHelpers.LimelightTarget_Fiducial;

public interface LimelightIO {

    public class LimelightIOInputs implements LoggableInputs {
        public double tX = 0.0;
        public double tY = 0.0;
        public double tArea = 0.0;
        public boolean tValid = false;

        public LimelightTarget_Fiducial[] fiducials = {};
        
        private final String fiducialListRoot = "Fiducials";
        
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

                // Adds the opint to the graphable array.
                graphablePoints[i] = new Translation2d(fid.tx, fid.ty);
                graphablePointsPixels[i] = new Translation2d(fid.tx_pixels, fid.ty_pixels);

                // Poses
                table.put(fidEntry("CameraPoseTargetSpace", i), fid.getCameraPose_TargetSpace());
                table.put(fidEntry("RobotPoseFieldSpace", i), fid.getRobotPose_FieldSpace());
                table.put(fidEntry("RobotPoseTargetSpace", i), fid.getRobotPose_TargetSpace());
                table.put(fidEntry("TargetPoseCameraSpace", i), fid.getTargetPose_CameraSpace());
                table.put(fidEntry("TargetPoseRobotSpace", i), fid.getTargetPose_RobotSpace());
                
            }

            // Fiducial Information
            table.put(fiducialListRoot + "/NumFids", fiducials.length);
            table.put(fiducialListRoot + "/GraphablePoints", graphablePoints);
            table.put(fiducialListRoot + "/GraphablePointsPixels", graphablePointsPixels);

            // General Information
            table.put("TX", tX);
            table.put("TY", tY);
            table.put("TArea", tArea);
            table.put("TValid", tValid);

        }
        @Override
        public void fromLog(LogTable table) {
            tX = table.get("TX", tX);
            tY = table.get("TY", tY);
            tArea = table.get("TArea", tArea);
            tValid = table.get("TValid", tValid);

            int numFids = table.get(fiducialListRoot + "/NumFids", 0);

            for(int i = 0; i < numFids; i++) {
                
                // Creates and saves Fiducial objects into the list based on the logged values.
                LimelightTarget_Fiducial fid = new LimelightTarget_Fiducial();
                fid.fiducialID     = table.get(fidEntry("ID", i), fid.fiducialID);
                fid.fiducialFamily = table.get(fidEntry("Family", i), fid.fiducialFamily);
                fid.tx             = table.get(fidEntry("TX", i), fid.tx);
                fid.ty             = table.get(fidEntry("TY", i), fid.ty);
                fid.tx_pixels      = table.get(fidEntry("TXPixels", i), fid.tx_pixels);
                fid.ty_pixels      = table.get(fidEntry("TYPixels", i), fid.ty_pixels);
                fid.ta             = table.get(fidEntry("TArea", i), fid.ta);
                fid.ts             = table.get(fidEntry("TS", i), fid.ts);

                // Poses
                fid.setCameraPose_TargetSpace(table.get(fidEntry("CameraPoseTargetSpace", i), fid.getCameraPose_TargetSpace()));
                fid.setRobotPose_FieldSpace(table.get(fidEntry("RobotPoseFieldSpace", i), fid.getRobotPose_FieldSpace()));
                fid.setRobotPose_TargetSpace(table.get(fidEntry("RobotPoseTargetSpace", i), fid.getRobotPose_TargetSpace()));
                fid.setTargetPose_CameraSpace(table.get(fidEntry("TargetPoseCameraSpace", i), fid.getTargetPose_CameraSpace()));
                fid.setTargetPose_RobotSpace(table.get(fidEntry("TargetPoseRobotSpace", i), fid.getTargetPose_RobotSpace()));
                
            }

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

    public abstract void forceOff();
    public abstract void forceBlink();
    public abstract void forceOn();
    public abstract void resetLed();
    
    public abstract void updateInputs(LimelightIOInputs inputs);

}
