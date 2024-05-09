// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import MotorFactory.XeroTalon;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeShooterSubsystem extends SubsystemBase {
  /** Creates a new ExampleSubsystem. */
  private XeroTalon shooter1;
  public IntakeShooterSubsystem() {
    shooter1 = new XeroTalon(3, "Shooter1", 0.6);
    shooter1.setPID("s", 0.05);
    shooter1.setPID("p", 0.11);
    shooter1.setPID("v", 0.12);
  }

  @Override
  public void periodic() {
    shooter1.periodic();
  }

  public void spin(double rps){
    shooter1.setVelocity(rps);
  }

  public void stop(){
    shooter1.stop();
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
