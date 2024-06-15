package frc.robot.subsystems.IntakeShooter;

import java.lang.Cloneable;
import java.lang.Override;
import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class IntakeShooterIOInputsAutoLogged extends IntakeShooterIO.IntakeShooterIOInputs implements LoggableInputs, Cloneable {
  @Override
  public void toLog(LogTable table) {
    table.put("FeederPosition", feederPosition);
    table.put("FeederVelocity", feederVelocity);
    table.put("FeederAcceleration", feederAcceleration);
    table.put("FeederCurrent", feederCurrent);
    table.put("FeederVoltage", feederVoltage);
    table.put("UpDownPosition", upDownPosition);
    table.put("UpDownVelocity", upDownVelocity);
    table.put("UpDownAcceleration", upDownAcceleration);
    table.put("UpDownCurrent", upDownCurrent);
    table.put("UpDownVoltage", upDownVoltage);
    table.put("Shooter1Position", shooter1Position);
    table.put("Shooter1Velocity", shooter1Velocity);
    table.put("Shooter1Acceleration", shooter1Acceleration);
    table.put("Shooter1Current", shooter1Current);
    table.put("Shooter1Voltage", shooter1Voltage);
    table.put("Shooter2Position", shooter2Position);
    table.put("Shooter2Velocity", shooter2Velocity);
    table.put("Shooter2Acceleration", shooter2Acceleration);
    table.put("Shooter2Current", shooter2Current);
    table.put("Shooter2Voltage", shooter2Voltage);
    table.put("TiltPosition", tiltPosition);
    table.put("TiltVelocity", tiltVelocity);
    table.put("TiltAcceleration", tiltAcceleration);
    table.put("TiltCurrent", tiltCurrent);
    table.put("TiltVoltage", tiltVoltage);
    table.put("EncoderPosition", encoderPosition);
    table.put("SensorVal", sensorVal);
    table.put("HasNote", hasNote);
  }

  @Override
  public void fromLog(LogTable table) {
    feederPosition = table.get("FeederPosition", feederPosition);
    feederVelocity = table.get("FeederVelocity", feederVelocity);
    feederAcceleration = table.get("FeederAcceleration", feederAcceleration);
    feederCurrent = table.get("FeederCurrent", feederCurrent);
    feederVoltage = table.get("FeederVoltage", feederVoltage);
    upDownPosition = table.get("UpDownPosition", upDownPosition);
    upDownVelocity = table.get("UpDownVelocity", upDownVelocity);
    upDownAcceleration = table.get("UpDownAcceleration", upDownAcceleration);
    upDownCurrent = table.get("UpDownCurrent", upDownCurrent);
    upDownVoltage = table.get("UpDownVoltage", upDownVoltage);
    shooter1Position = table.get("Shooter1Position", shooter1Position);
    shooter1Velocity = table.get("Shooter1Velocity", shooter1Velocity);
    shooter1Acceleration = table.get("Shooter1Acceleration", shooter1Acceleration);
    shooter1Current = table.get("Shooter1Current", shooter1Current);
    shooter1Voltage = table.get("Shooter1Voltage", shooter1Voltage);
    shooter2Position = table.get("Shooter2Position", shooter2Position);
    shooter2Velocity = table.get("Shooter2Velocity", shooter2Velocity);
    shooter2Acceleration = table.get("Shooter2Acceleration", shooter2Acceleration);
    shooter2Current = table.get("Shooter2Current", shooter2Current);
    shooter2Voltage = table.get("Shooter2Voltage", shooter2Voltage);
    tiltPosition = table.get("TiltPosition", tiltPosition);
    tiltVelocity = table.get("TiltVelocity", tiltVelocity);
    tiltAcceleration = table.get("TiltAcceleration", tiltAcceleration);
    tiltCurrent = table.get("TiltCurrent", tiltCurrent);
    tiltVoltage = table.get("TiltVoltage", tiltVoltage);
    encoderPosition = table.get("EncoderPosition", encoderPosition);
    sensorVal = table.get("SensorVal", sensorVal);
    hasNote = table.get("HasNote", hasNote);
  }

  public IntakeShooterIOInputsAutoLogged clone() {
    IntakeShooterIOInputsAutoLogged copy = new IntakeShooterIOInputsAutoLogged();
    copy.feederPosition = this.feederPosition;
    copy.feederVelocity = this.feederVelocity;
    copy.feederAcceleration = this.feederAcceleration;
    copy.feederCurrent = this.feederCurrent;
    copy.feederVoltage = this.feederVoltage;
    copy.upDownPosition = this.upDownPosition;
    copy.upDownVelocity = this.upDownVelocity;
    copy.upDownAcceleration = this.upDownAcceleration;
    copy.upDownCurrent = this.upDownCurrent;
    copy.upDownVoltage = this.upDownVoltage;
    copy.shooter1Position = this.shooter1Position;
    copy.shooter1Velocity = this.shooter1Velocity;
    copy.shooter1Acceleration = this.shooter1Acceleration;
    copy.shooter1Current = this.shooter1Current;
    copy.shooter1Voltage = this.shooter1Voltage;
    copy.shooter2Position = this.shooter2Position;
    copy.shooter2Velocity = this.shooter2Velocity;
    copy.shooter2Acceleration = this.shooter2Acceleration;
    copy.shooter2Current = this.shooter2Current;
    copy.shooter2Voltage = this.shooter2Voltage;
    copy.tiltPosition = this.tiltPosition;
    copy.tiltVelocity = this.tiltVelocity;
    copy.tiltAcceleration = this.tiltAcceleration;
    copy.tiltCurrent = this.tiltCurrent;
    copy.tiltVoltage = this.tiltVoltage;
    copy.encoderPosition = this.encoderPosition;
    copy.sensorVal = this.sensorVal;
    copy.hasNote = this.hasNote;
    return copy;
  }
}
