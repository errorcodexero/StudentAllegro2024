/**
 * Interface for the IntakeShooter subsystem's input/output (I/O) operations.
 * This interface provides methods for updating input data, controlling the motors,
 * and retrieving various sensor and motor data.
 */
package frc.robot.subsystems.IntakeShooter;

import org.littletonrobotics.junction.AutoLog;
import frc.robot.subsystems.IntakeShooter.IntakeShooterIOHardware.MoveState;

import com.ctre.phoenix6.hardware.TalonFX;

public abstract interface IntakeShooterIO {
    /**
     * Class to hold input data for the IntakeShooter subsystem.
     */
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

    /**
     * Update the input data for the IntakeShooter subsystem.
     * @param inputs_ The new input data.
     */
    abstract void update(IntakeShooterIOInputsAutoLogged inputs_);

    abstract TalonFX getFeeder();

    abstract TalonFX getUpDown();

    abstract TalonFX getShooter1();

    abstract TalonFX getShooter2();

    abstract TalonFX getTilt();

    /**
     * Spin the feeder motor at the specified revolutions per second (rps).
     * @param rps The desired revolutions per second.
     */
    abstract void spinFeeder(double rps);

    /**
     * Move the up-down mechanism to the specified revolutions.
     * @param revs The desired revolutions.
     */
    abstract void moveUpDown(double revs);

    /**
     * Move the up-down mechanism using pure PID control to the specified degrees.
     * @param degs The desired degrees.
     */
    abstract void moveUpDownPurePID(double degs);

    /**
     * Move the up-down mechanism to the specified revolutions.
     * @param revs The desired revolutions.
     */
    abstract void moveUpDownRevs(double revs);

    /**
     * Move the up-down mechanism to the specified degrees.
     * @param degs The desired degrees.
     */
    abstract void moveUpDownDegrees(double degs);

    /**
     * Move the up-down mechanism to the specified radians.
     * @param rads The desired radians.
     */
    abstract void moveUpDownRadians(double rads);

    /**
     * Spin the shooter motor 1 at the specified revolutions per second (rps).
     * @param rps The desired revolutions per second.
     */
    abstract void spinShooter1(double rps);

    /**
     * Spin the shooter motor 2 at the specified revolutions per second (rps).
     * @param rps The desired revolutions per second.
     */
    abstract void spinShooter2(double rps);

    /**
     * Move the tilt mechanism to the specified revolutions.
     * @param revs The desired revolutions.
     */
    abstract void moveTilt(double revs);

    /**
     * Move the tilt mechanism using pure PID control to the specified degrees.
     * @param degs The desired degrees.
     */
    abstract void moveTiltPurePID(double degs);

    /**
     * Move the tilt mechanism to the specified revolutions.
     * @param revs The desired revolutions.
     */
    abstract void moveTiltRevs(double revs);

    /**
     * Move the tilt mechanism to the specified degrees.
     * @param degs The desired degrees.
     */
    abstract void moveTiltDegrees(double degs);

    /**
     * Move the tilt mechanism to the specified radians.
     * @param rads The desired radians.
     */
    abstract void moveTiltRadians(double rads);

    /**
     * Get the current move state of the IntakeShooter subsystem.
     * @return The current move state.
     */
    abstract MoveState getMoveState();

    /**
     * Move the IntakeShooter subsystem to the specified tilt and up-down positions.
     * @param tilt The desired tilt position.
     * @param upDown The desired up-down position.
     * @return True if the move was successful, false otherwise.
     */
    abstract boolean moveSystem(double tilt, double upDown);

    abstract void stopFeeder();
    
    abstract void stopUpDown();

    abstract void stopShooter1();

    abstract void stopShooter2();

    abstract void stopTilt();

    /**
     * Check if the IntakeShooter subsystem has a note.
     * @return True if a note is present, false otherwise.
     */
    abstract boolean hasNote();

    /**
     * Check the sensor value for the IntakeShooter subsystem.
     * @return True if the sensor value is true, false otherwise.
     */
    abstract boolean sensorVal();

    /**
     * Get the current position of the feeder motor.
     * @return The feeder position in revolutions.
     */
    abstract double getFeederPosition();
    
    /**
     * Get the current velocity of the feeder motor.
     * @return The feeder velocity in revolutions per second.
     */
    abstract double getFeederVelocity();

    /**
     * Get the current position of the up-down mechanism.
     * @return The up-down position in revolutions.
     */
    abstract double getUpDownPosition();

    /**
     * Get the current velocity of the up-down mechanism.
     * @return The up-down velocity in revolutions per second.
     */
    abstract double getUpDownVelocity();

    /**
     * Get the current position of the shooter motor 1.
     * @return The shooter 1 position in revolutions.
     */
    abstract double getShooter1Position();

    /**
     * Get the current velocity of the shooter motor 1.
     * @return The shooter 1 velocity in revolutions per second.
     */
    abstract double getShooter1Velocity();

    /**
     * Get the current position of the shooter motor 2.
     * @return The shooter 2 position in revolutions.
     */
    abstract double getShooter2Position();

    /**
     * Get the current velocity of the shooter motor 2.
     * @return The shooter 2 velocity in revolutions per second.
     */
    abstract double getShooter2Velocity();

    /**
     * Get the current position of the tilt mechanism.
     * @return The tilt position in revolutions.
     */
    abstract double getTiltPosition();
    
    /**
     * Get the current velocity of the tilt mechanism.
     * @return The tilt velocity in revolutions per second.
     */
    abstract double getTiltVelocity();
}