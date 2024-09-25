package frc.robot.subsystems.Orchestra;

import com.ctre.phoenix6.Orchestra;
import com.ctre.phoenix6.hardware.TalonFX;

import frc.robot.Constants;

public class OrchestraIOTalonFX implements OrchestraIO {

    private final Orchestra orchestra;

    public OrchestraIOTalonFX() {
        orchestra = new Orchestra();

        for (int id : Constants.OperatorConstants.ORCHESTRA_INSTRUMENTS) {
            orchestra.addInstrument(new TalonFX(id));
        }
    }

    // @Override
    // public void addInstrument(int canID) {
    //     orchestra.addInstrument(new TalonFX(canID));
    // }

    // @Override
    // public void addInstrument(int canID, int trackNum) {
    //     orchestra.addInstrument(new TalonFX(canID), trackNum);
    // }

    @Override
    public void load(String midi) {
        orchestra.loadMusic(midi);
    }

    @Override
    public void pause() {
        orchestra.pause();
    }

    @Override
    public void play() {
        orchestra.play();
    }

    @Override
    public void stop() {
        orchestra.stop();
    }
    
}
