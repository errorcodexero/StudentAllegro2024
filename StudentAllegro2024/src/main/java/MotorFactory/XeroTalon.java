package MotorFactory;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.hardware.TalonFX;

import MotorFactory.XeroTalonIO.XeroTalonIOInputs;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class XeroTalon extends SubsystemBase {
    private XeroTalonIO io_;
    private XeroTalonIOInputs inputs_;
    private String motor_name;
    private String sub_name;

    public XeroTalon(int CANID, String motor_name, String sub_name, double gearRatio, double gearRadius){
        this.io_ = new XeroTalonIOMotor(CANID, motor_name, gearRatio, gearRadius);
        this.inputs_ = new XeroTalonIOInputs(motor_name);
        this.sub_name = sub_name;
        this.motor_name = motor_name;
    }

    public XeroTalon(int CANID, String name, String sub_name, double gearRatio){
        this(CANID, name, sub_name, gearRatio, 1);
    }

    public XeroTalon(int CANID, String motor_name, String sub_name){
        this(CANID, motor_name, sub_name, 1, 1);
    }

    public XeroTalon(int CANID, String name){
        this(CANID, name, name);
    }

    public XeroTalon(int CANID){
        this(CANID, "motor" + CANID);
    }

    @Override
    public void periodic(){
        io_.updateInputs(inputs_);
        Logger.processInputs(sub_name, inputs_);
        Logger.recordOutput(motor_name + " temperature", getMotor().getDeviceTemp().getValueAsDouble());
    }

    public void setVelocity(double rps){
        io_.setVelocity(rps);
    }

    public void motionMagicGotoMeters(double meters){
        io_.motionMagicGotoMeters(meters);
    }

    public void motionMagicGotoDegrees(double degs){
        io_.motionMagicGotoDegrees(degs);
    }

    public void motionMagicGotoRadians(double rads){
        io_.motionMagicGotoRadians(rads);
    }

    public void motionMagicGotoRevolutions(double revs){
        io_.motionMagicGotoRevolutions(revs);
    }

    public void setControl(ControlRequest req){
        io_.setControl(req);
    }

    public void stop(){
        io_.stop();
    }

    public TalonFX getMotor(){
        return io_.getMotor();
    }

    public void setPIDs(Slot0Configs config){
        io_.setPIDs(config);
    }

    public void setPIDs(double p, double i, double d){
        io_.setPIDs(p, i, d);
    }

    public boolean setPID(String whatToSet, double changed){
        return io_.setPID(changed, whatToSet);
    }

    public Slot0Configs getPIDs(){
        return io_.getPIDs();
    }
}
