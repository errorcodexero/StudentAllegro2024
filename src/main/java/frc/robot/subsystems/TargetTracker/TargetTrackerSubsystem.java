package frc.robot.subsystems.TargetTracker;

public class TargetTrackerSubsystem {
    public class TTShooter{
        private double shooterVel_ = 0.0;
        private double tiltPos_ = 0.0;
        private double upDownPos_ = 0.0;

        public double getShooterVel_(){
            return shooterVel_;
        }
        public void setShooterVel_(double shooterVel){
            shooterVel_ = shooterVel;
        }

        public double getTiltPos(){
            return tiltPos_;
        }
        public void setTiltPos(double tiltPos){
            tiltPos_ = tiltPos;
        }

        public double getUpDownPos(){
            return upDownPos_;
        }
        public void setUpDownPos(double upDownPos){
            upDownPos_ = upDownPos;
        }
    }

    public TTShooter getTTShooter(){
        return new TTShooter();
    }
}
