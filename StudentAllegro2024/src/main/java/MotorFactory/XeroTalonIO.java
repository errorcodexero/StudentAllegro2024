package MotorFactory;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.hardware.TalonFX;

public interface XeroTalonIO {

    public class XeroTalonIOInputs implements LoggableInputs{
        public String name = "";
        public double position = 0.0;
        public double velocity = 0.0;
        public double accel = 0.0;
        public double current = 0.0;
        public double volts = 0.0;

        public XeroTalonIOInputs(String name){
            this.name = name;
        }

        @Override
        public void toLog(LogTable table) {
            table.put(name + " Position", position);
            table.put(name + " Velocity", velocity);
            table.put(name + " Accel", accel);
            table.put(name + " Current", current);
            table.put(name + " Volts", volts);
        }

        @Override
        public void fromLog(LogTable table) {
            position = table.get(name + " Position", position);
            velocity = table.get(name + " Velocity", velocity);
            accel = table.get(name + " Accel", accel);
            current = table.get(name + " Current", current);
            volts = table.get(name + " Volts", volts);
        }

        public XeroTalonIOInputs clone() {
            XeroTalonIOInputs copy = new XeroTalonIOInputs(name);
            copy.position = this.position;
            copy.velocity = this.velocity;
            copy.accel = this.accel;
            copy.current = this.current;
            copy.volts = this.volts;
            return copy;
        }
    }

    public default void updateInputs(XeroTalonIOInputs inputs){}

    public default TalonFX getMotor(){
        return new TalonFX(0);
    }

    public default void setVelocity(double rps){}

    public default void motionMagicGotoMeters(double length){}

    public default void motionMagicGotoRevolutions(double revs){}

    public default void motionMagicGotoDegrees(double degrees){}

    public default void motionMagicGotoRadians(double rads){}

    public default void stop(){}

    public default void setControl(ControlRequest req){}

    public default void setPIDs(Slot0Configs pids){}

    public default void setPIDs(double p, double i, double d){}

    public default boolean setPID(double changed, String whatToSet){
        return false;
    }

    public default Slot0Configs getPIDs(){
        return new Slot0Configs();
    }
}
