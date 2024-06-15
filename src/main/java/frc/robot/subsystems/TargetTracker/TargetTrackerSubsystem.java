package frc.robot.subsystems.TargetTracker;

import java.util.function.Supplier;

public class TargetTrackerSubsystem {
    public Supplier<Double> getDistanceFromTarget(){
        return new Supplier<Double>() {
            public Double get(){
                return Double.valueOf(0.0);
            }
        };
    }

    public Supplier<Boolean> getATSeen(){
        return new Supplier<Boolean>() {
            public Boolean get(){
                return Boolean.valueOf(true);
            }
        };
    }
}
