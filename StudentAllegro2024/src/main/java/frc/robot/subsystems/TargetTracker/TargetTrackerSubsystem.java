package frc.robot.subsystems.TargetTracker;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.shuffleboard.SuppliedValueWidget;

public class TargetTrackerSubsystem {
    public class TTShooter{
        public double getShooterVel(){
            return 0.0;
        }
        public double getUpDownPos(){
            return 0.0;
        }
        public double getTiltPos(){
            return 0.0;
        }
    }
    public class TTDriveBase{

    }

    public TTShooter getTTShooter(){
        return new TTShooter();
    }

    public TTDriveBase getTTDriveBase(){
        return new TTDriveBase();
    }
}
