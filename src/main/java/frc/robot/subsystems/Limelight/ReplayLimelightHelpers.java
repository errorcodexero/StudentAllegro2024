package frc.robot.subsystems.Limelight;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import frc.robot.subsystems.Limelight.LimelightHelpers.LimelightResults;

public class ReplayLimelightHelpers {

    private static ObjectMapper mapper_;

    public static LimelightResults parseJson(String json) {

        if (mapper_ == null) {
            mapper_ = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }

        try {
            return mapper_.readValue(json, LimelightResults.class);
        } catch (JsonProcessingException e) {
            LimelightResults results = new LimelightResults();
            results.error = "Error while parsing logged limelight json: " + e.getMessage();

            return results;
        }
        
    }
}

