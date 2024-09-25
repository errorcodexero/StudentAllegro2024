package frc.robot.subsystems.Orchestra;

public interface OrchestraIO {

    public void load(String midi);

    // public void addInstrument(int canID);
    // public void addInstrument(int canID, int trackNum);

    public void play();
    public void pause();
    public void stop();
    
}
