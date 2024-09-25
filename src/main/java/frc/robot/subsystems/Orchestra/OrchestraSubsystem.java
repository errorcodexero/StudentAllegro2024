package frc.robot.subsystems.Orchestra;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class OrchestraSubsystem extends SubsystemBase {

    private final OrchestraIO io_;

    public OrchestraSubsystem(OrchestraIO io) {
        io_ = io;
    }

    // Methods

    public void load(String midi) {
        io_.load(midi);
    }

    // public void addInstruments(int[] canIDs) {
    //     for (int id : canIDs) {
    //         addInstrument(id);
    //     }
    // }

    // public void addInstrument(int canID) {
    //     io_.addInstrument(canID);
    // }

    // public void addInstrument(int canID, int trackNum) {
    //     io_.addInstrument(canID, trackNum);
    // }

    public void play() {
        io_.play();
    }

    public void pause() {
        io_.pause();
    }

    public void stop() {
        io_.stop();
    }

    // Commands

    public Command playChirp(String midi) {
        return startEnd(() -> {
            load(midi);
            play();
        }, this::stop);
    }
    
}