package frc.robot.subsystems.Limelight;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public interface LimelightIO {

    public class LimelightIOInputs implements LoggableInputs {
        public double tX = 0.0;
        public double tY = 0.0;
        public double tArea = 0.0;
        public boolean tValid = false;
        
        public double[] fiducialID = {};
        public String[] fiducialFamily = {};
        public double[] fiducialTX = {};
        public double[] fiducialTY = {};
        public double[] fiducialTXPixels = {};
        public double[] fiducialTYPixels = {};
        public double[] fiducialTArea = {};

        // CONSTANTS FOR LOGGED KEYS

        private final String keyFidRoot = "Fiducials";
        private final String keyFidID = fidEntry("ID");
        private final String keyFidFamily = fidEntry("Family");
        private final String keyFidTX = fidEntry("TX");
        private final String keyFidTY = fidEntry("TY");
        private final String keyFidTXPixels = fidEntry("TXPixels");
        private final String keyFidTYPixels = fidEntry("TYPixels");
        private final String keyFidTArea = fidEntry("TArea");
        
        @Override
        public void toLog(LogTable table) {
            table.put("TX", tX);
            table.put("TY", tY);
            table.put("TArea", tArea);
            table.put("TValid", tValid);
            
            // Fiducial Objects
            table.put(keyFidID, fiducialID);
            table.put(keyFidFamily, fiducialFamily);
            table.put(keyFidTX, fiducialTX);
            table.put(keyFidTY, fiducialTY);
            table.put(keyFidTXPixels, fiducialTXPixels);
            table.put(keyFidTYPixels, fiducialTYPixels);
            table.put(keyFidTArea, fiducialTArea);

        }
        @Override
        public void fromLog(LogTable table) {
            tX = table.get("TX", tX);
            tY = table.get("TY", tY);
            tArea = table.get("TArea", tArea);
            tValid =table.get("TValid", tValid);

            // Fiducial Objects
            fiducialID = table.get(keyFidID, fiducialID);
            fiducialFamily = table.get(keyFidFamily, fiducialFamily);
            fiducialTX = table.get(keyFidTX, fiducialTX);
            fiducialTY = table.get(keyFidTY, fiducialTY);
            fiducialTXPixels = table.get(keyFidTXPixels, fiducialTXPixels);
            fiducialTYPixels = table.get(keyFidTYPixels, fiducialTYPixels);
            fiducialTArea = table.get(keyFidTArea, fiducialTArea);
        }

        private String fidEntry(String name) {
            return keyFidRoot + "/" + name;
        }
    }

    public abstract void forceOff();
    public abstract void forceBlink();
    public abstract void forceOn();
    public abstract void resetLed();
    
    public abstract void updateInputs(LimelightIOInputs inputs);

}
