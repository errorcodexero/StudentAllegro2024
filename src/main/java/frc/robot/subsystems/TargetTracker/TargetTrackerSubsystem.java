package frc.robot.subsystems.TargetTracker;

import java.util.function.Supplier;

public class TargetTrackerSubsystem {
    public Supplier<Double> getDistanceFromTarget(){
        return new Supplier<Double>() {
            public Double get(){
                return new Double(0.0);
            }
        };
    }

}
