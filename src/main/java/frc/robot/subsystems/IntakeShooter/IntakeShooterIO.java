package frc.robot.subsystems.IntakeShooter;

import org.littletonrobotics.junction.AutoLog;
import frc.robot.subsystems.IntakeShooter.IntakeShooterIOHardware.MoveState;

import com.ctre.phoenix6.hardware.TalonFX;

public abstract interface IntakeShooterIO {
    @AutoLog
    public class IntakeShooterIOInputs{
        public double feederPosition;
        public double feederVelocity;
        public double feederAcceleration;
        public double feederCurrent;
        public double feederVoltage;

        public double upDownPosition;
        public double upDownVelocity;
        public double upDownAcceleration;
        public double upDownCurrent;
        public double upDownVoltage;

        public double shooter1Position;
        public double shooter1Velocity;
        public double shooter1Acceleration;
        public double shooter1Current;
        public double shooter1Voltage;

        public double shooter2Position;
        public double shooter2Velocity;
        public double shooter2Acceleration;
        public double shooter2Current;
        public double shooter2Voltage;

        public double tiltPosition;
        public double tiltVelocity;
        public double tiltAcceleration;
        public double tiltCurrent;
        public double tiltVoltage;

        public double encoderPosition;

        public boolean sensorVal;
        public boolean hasNote;
    }

    abstract void update(IntakeShooterIOInputsAutoLogged inputs_);

    abstract TalonFX getFeeder();

    abstract TalonFX getUpDown();

    abstract TalonFX getShooter1();

    abstract TalonFX getShooter2();

    abstract TalonFX getTilt();

    abstract void spinFeeder(double rps);

    abstract void moveUpDown(double revs);

    abstract void moveUpDownPurePID(double degs);

    abstract void moveUpDownRevs(double revs);

    abstract void moveUpDownDegrees(double degs);

    abstract void moveUpDownRadians(double rads);

    abstract void spinShooter1(double rps);

    abstract void spinShooter2(double rps);

    abstract void moveTilt(double revs);

    abstract void moveTiltPurePID(double degs);

    abstract void moveTiltRevs(double revs);

    abstract void moveTiltDegrees(double degs);

    abstract void moveTiltRadians(double rads);

    abstract MoveState getMoveState();

    abstract boolean moveSystem(double tilt, double upDown);

    abstract void stopFeeder();
    
    abstract void stopUpDown();

    abstract void stopShooter1();

    abstract void stopShooter2();

    abstract void stopTilt();

    abstract boolean hasNote();

    abstract boolean sensorVal();

    abstract double getFeederPosition();
    
    abstract double getFeederVelocity();

    abstract double getUpDownPosition();

    abstract double getUpDownVelocity();

    abstract double getShooter1Position();

    abstract double getShooter1Velocity();

    abstract double getShooter2Position();

    abstract double getShooter2Velocity();

    abstract double getTiltPosition();
    
    abstract double getTiltVelocity();
}
