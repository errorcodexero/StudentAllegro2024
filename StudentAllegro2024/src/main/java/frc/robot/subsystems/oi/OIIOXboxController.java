// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.oi;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/** Add your docs here. */
public class OIIOXboxController extends CommandXboxController implements OIIO {

    /**
     * Creates an OI implementation for an xbox controller in the case that we cant use the OI.
     * @param port The port the controller is plugged into.
     */
    public OIIOXboxController(int port) {
        super(port);
        //TODO Auto-generated constructor stub
    }

    @Override
    public Trigger shoot() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'shoot'");
    }

    @Override
    public Trigger collect() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'collect'");
    }

    @Override
    public Trigger unclimb() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unclimb'");
    }

    @Override
    public Trigger climbPrepare() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'climbPrepare'");
    }

    @Override
    public Trigger climbExecute() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'climbExecute'");
    }

    @Override
    public Trigger abort() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'abort'");
    }

    @Override
    public Trigger eject() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eject'");
    }

    @Override
    public Trigger turtle() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'turtle'");
    }

    @Override
    public Trigger cycleSpeaker() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cycleSpeaker'");
    }

    @Override
    public Trigger cycleTrap() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cycleTrap'");
    }

    @Override
    public Trigger cycleAmp() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cycleAmp'");
    }

    @Override
    public Trigger shootPodium() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'shootPodium'");
    }

    @Override
    public Trigger shootAuto() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'shootAuto'");
    }

    @Override
    public Trigger shootSubwoofer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'shootSubwoofer'");
    }

}
