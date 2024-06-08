package XeroTalon;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.Slot1Configs;
import com.ctre.phoenix6.configs.Slot2Configs;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import AKInput.AKInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class XeroTalon extends SubsystemBase {
    private String motor_name;
    private TalonFX motor_;
    private Slot0Configs pids_;
    private double gearRatio;
    private double gearRadius;
    private int[][] inputIndicies = new int[5][2];

    public XeroTalon(int CANID, String sub_name, String motor_name, double gearRatio, double gearRadius, boolean inverted){
        motor_ = new TalonFX(CANID);
        pids_ = new Slot0Configs();
        pids_.kS = 0.05; // Add 0.05 V output to overcome static friction
        pids_.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
        pids_.kP = 0.11; // An error of 1 rps results in 0.11 V output
        pids_.kI = 0; // no output for integrated error
        pids_.kD = 0; // no output for error derivative
        motor_.getConfigurator().apply(pids_);
        motor_.setInverted(inverted);
        this.gearRatio = gearRatio;
        this.gearRadius = gearRadius;
        this.motor_name = motor_name;
        inputIndicies[0] = AKInput.add(sub_name, motor_name + " Position", 0.0);
        inputIndicies[1] = AKInput.add(sub_name, motor_name + " Velocity", 0.0);
        inputIndicies[2] = AKInput.add(sub_name, motor_name + " Acceleration", 0.0);
        inputIndicies[3] = AKInput.add(sub_name, motor_name + " Current", 0.0);
        inputIndicies[4] = AKInput.add(sub_name, motor_name + " Voltage", 0.0);
    }

    public XeroTalon(int CANID, String sub_name, String motor_name, double gearRatio, double gearRadius){
        this(CANID, motor_name, sub_name, gearRatio, gearRadius, false);
    }

    public XeroTalon (int CANID, String sub_name, String motor_name, double gearRatio, boolean inverted){
        this(CANID, motor_name, sub_name, gearRatio, 1, inverted);
    }

    public XeroTalon(int CANID, String sub_name, String motor_name, double gearRatio){
        this(CANID, motor_name, sub_name, gearRatio, 1, false);
    }

    public XeroTalon(int CANID, String sub_name, String motor_name){
        this(CANID, motor_name, sub_name, 1, 1, false);
    }

    public XeroTalon(int CANID, String name){
        this(CANID, name, name);
    }

    public XeroTalon(int CANID){
        this(CANID, "motor" + CANID);
    }

    @Override
    public void periodic(){
        updateInputs();
        Logger.recordOutput(motor_name + " temperature", getMotor().getDeviceTemp().getValueAsDouble());
    }

    private void updateInputs() {
        AKInput.update(inputIndicies[0], motor_.getPosition().getValueAsDouble());
        AKInput.update(inputIndicies[1], motor_.getVelocity().getValueAsDouble());
        AKInput.update(inputIndicies[2], motor_.getAcceleration().getValueAsDouble());
        AKInput.update(inputIndicies[3], motor_.getSupplyCurrent().getValueAsDouble());
        AKInput.update(inputIndicies[4], motor_.getMotorVoltage().getValueAsDouble());
    }

    public TalonFX getMotor(){
        return motor_;
    }

    public void setVelocity(double rps){
        setControl(new VelocityVoltage(rps/gearRatio));
    }

    public void setVelocityDegrees(double degs){
        setVelocity(degs/360);
    }

    public void stop(){
        motor_.stopMotor();
    }

    public void motionMagicGotoMeters(double length){
        setControl(new MotionMagicVoltage((length * gearRadius)/(Math.PI * 2 * gearRatio)));
    }

    public void motionMagicGotoRevolutions(double revs){
        setControl(new MotionMagicVoltage(revs));
    }

    public void motionMagicGotoDegrees(double degrees){
        setControl(new MotionMagicVoltage(degrees/360));
    }

    public void motionMagicGotoRadians(double rads){
        setControl(new MotionMagicVoltage(rads/(Math.PI * 2)));
    }

    public void setControl(ControlRequest req){
        motor_.setControl(req);
    }

    public double getPosition(){
        return motor_.getPosition().getValueAsDouble();
    }

    public double getVelocity(){
        return motor_.getVelocity().getValueAsDouble();
    }

    public double getAcceleration(){
        return motor_.getAcceleration().getValueAsDouble();
    }

    public void setPIDs(Slot0Configs PIDs){
        this.pids_ = PIDs;
        motor_.getConfigurator().apply(PIDs);
    }

    public void setPIDs(Slot1Configs PIDs){
        motor_.getConfigurator().apply(PIDs);
    }

    public void setPIDs(Slot2Configs PIDs){
        motor_.getConfigurator().apply(PIDs);
    }

    public void setPIDs(double p, double i, double d){
        if(p != 0){
            pids_.kP = p;
        }
        if(i != 0){
            pids_.kI = i;
        }
        if(d != 0){
            pids_.kD = d;
        }

        motor_.getConfigurator().apply(pids_);
    }

    public boolean setPID(String whatToSet, double changed){
        if(whatToSet.toLowerCase().contains("k")){
            whatToSet = whatToSet.substring(1);
        }
        switch(whatToSet.toLowerCase()){
            case "s": pids_.kS = changed; break;
            case "v": pids_.kV = changed; break;
            case "p": pids_.kP = changed; break;
            case "i": pids_.kI = changed; break;
            case "d": pids_.kD = changed; break;
            case "a": pids_.kA = changed; break;
            case "g": pids_.kG = changed; break;
            default: return false;
        }
        motor_.getConfigurator().apply(pids_);
        return true;
    }

    public Slot0Configs getPIDs(){
        return pids_;
    }
}
