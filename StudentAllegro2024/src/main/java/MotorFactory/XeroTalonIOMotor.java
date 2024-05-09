package MotorFactory;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

public class XeroTalonIOMotor implements XeroTalonIO{

    private TalonFX motor_;
    private Slot0Configs pids_;
    private double gearRatio;
    private double gearRadius;

    public XeroTalonIOMotor(int CANID, String name, double gearRatio, double gearRadius){
        motor_ = new TalonFX(CANID);
        pids_ = new Slot0Configs();
        pids_.kS = 0.05; // Add 0.05 V output to overcome static friction
        pids_.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
        pids_.kP = 0.11; // An error of 1 rps results in 0.11 V output
        pids_.kI = 0; // no output for integrated error
        pids_.kD = 0; // no output for error derivative
        motor_.getConfigurator().apply(pids_);
        this.gearRatio = gearRatio;
        this.gearRadius = gearRadius;
    }

    @Override
    public void updateInputs(XeroTalonIOInputs inputs) {
        inputs.position = motor_.getPosition().getValueAsDouble();
        inputs.velocity = motor_.getVelocity().getValueAsDouble();
        inputs.accel = motor_.getAcceleration().getValueAsDouble();
        inputs.current = motor_.getSupplyCurrent().getValueAsDouble();
        inputs.volts = motor_.getMotorVoltage().getValueAsDouble();
    }

    public TalonFX getMotor(){
        return motor_;
    }

    public void setVelocity(double rps){
        setControl(new VelocityVoltage(rps/gearRatio));
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

    public void setPIDs(Slot0Configs PIDs){
        this.pids_ = PIDs;
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

    //@Return
    //If the PID value was found
    public boolean setPID(String whatToSet, double changed){
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
