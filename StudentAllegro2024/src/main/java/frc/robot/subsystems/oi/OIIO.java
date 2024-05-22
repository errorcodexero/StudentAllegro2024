// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.oi;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.wpilibj2.command.button.Trigger;

/** Add your docs here. */
public interface OIIO {

    @AutoLog
    public class OIIOInputs {
        public boolean shoot = false;
        public boolean collect = false;
        public boolean unclimb = false;
        public boolean climbExecute = false;
        public boolean climbPrepare = false;
        public boolean abort = false;
        public boolean eject = false;
        public boolean turtle = false;
    }

    public abstract void updateInputs(OIIOInputsAutoLogged inputs);

    public abstract Trigger shoot();
    public abstract Trigger collect();
    public abstract Trigger unclimb();
    public abstract Trigger climbPrepare();
    public abstract Trigger climbExecute();
    public abstract Trigger abort();
    public abstract Trigger eject();
    public abstract Trigger turtle();

}
