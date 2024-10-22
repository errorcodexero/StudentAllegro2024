/**
 * Interface for the IntakeShooter subsystem's input/output (I/O) operations.
 * This interface provides methods for updating input data, controlling the motors,
 * and retrieving various sensor and motor data.
 */
package frc.robot.subsystems.IntakeShooter;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.units.Angle;
import edu.wpi.first.units.Measure;

public interface IntakeShooterIO {
    /**
     * Class to hold input data for the IntakeShooter subsystem.
     */
    @AutoLog
    public class IntakeShooterIOInputs{
        public double feederPosition = 0.0;
        public double feederVelocity = 0.0;
        public double feederAcceleration = 0.0;
        public double feederCurrent = 0.0;
        public double feederVoltage = 0.0;

        public double upDownPosition = 0.0;
        public double upDownVelocity = 0.0;
        public double upDownAcceleration = 0.0;
        public double upDownCurrent = 0.0;
        public double upDownVoltage = 0.0;

        public double shooter1Position = 0.0;
        public double shooter1Velocity = 0.0;
        public double shooter1Acceleration = 0.0;
        public double shooter1Current = 0.0;
        public double shooter1Voltage = 0.0;

        public double shooter2Position = 0.0;
        public double shooter2Velocity = 0.0;
        public double shooter2Acceleration = 0.0;
        public double shooter2Current = 0.0;
        public double shooter2Voltage = 0.0;

        public double tiltPosition = 0.0;
        public double tiltVelocity = 0.0;
        public double tiltAcceleration = 0.0;
        public double tiltCurrent = 0.0;
        public double tiltVoltage = 0.0;

        public double encoderPosition = 0.0;

        public boolean hasNote = false;
    }

    /**
     * Update the input data for the IntakeShooter subsystem.
     * @param inputs_ The new input data.
     */
    public default void updateInputs(IntakeShooterIOInputsAutoLogged inputs_) {};

    /**
     * Move the up-down mechanism to the specified revolutions.
     * @param revs The desired revolutions.
     */
    public default void moveUpDown(Measure<Angle> angle) {};

    /**
     * Move the tilt mechanism to the specified revolutions.
     * @param revs The desired revolutions.
     */
    public default void moveTilt(Measure<Angle> angle) {};

    /**
     * Spin the feeder motor at the specified revolutions per second (rps).
     * @param rps The desired revolutions per second.
     */
    public default void spinFeeder(double rps) {};

    /**
     * Spin the shooter motor 1 at the specified revolutions per second (rps).
     * @param rps The desired revolutions per second.
     */
    public default void spinShooter1(double rps) {};

    /**
     * Spin the shooter motor 2 at the specified revolutions per second (rps).
     * @param rps The desired revolutions per second.
     */
    public default void spinShooter2(double rps) {};


    // /**
    //  * Move the IntakeShooter subsystem to the specified tilt and up-down positions.
    //  * @param tilt The desired tilt position.
    //  * @param upDown The desired up-down position.
    //  * @return True if the move was successful, false otherwise.
    //  */
    // public default boolean moveSystem(double tilt, double upDown) {
    //     return false;
    // };

    public default void stopFeeder() {};
    
    public default void stopUpDown() {};

    public default void stopShooter1() {};

    public default void stopShooter2() {};

    public default void stopTilt() {};

    // TODO: REMOVE THIS!
    public default boolean moveSystem(double a, double b) {
        throw new RuntimeException("This is unimplemented! Fix the move system functionality to be inside the subsystem! Then delete this method!");
    };

}