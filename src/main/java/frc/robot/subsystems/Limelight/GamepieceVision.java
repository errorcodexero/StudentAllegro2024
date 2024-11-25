package frc.robot.subsystems.Limelight;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Limelight.structs.XeroGamepiece;

public class GamepieceVision extends SubsystemBase {
    private final VisionIO io_;
    private final VisionIOInputsAutoLogged inputs_;

    public GamepieceVision(VisionIO io) {
        io_ = io;
        inputs_ = new VisionIOInputsAutoLogged();
    }

    @Override
    public void periodic() {
        io_.updateInputs(inputs_);
        Logger.processInputs(getName(), inputs_);
    }

    public int numGamepieces() {
        return inputs_.gamepieces.length;
    }

    public XeroGamepiece[] getGamepieces() {
        return inputs_.gamepieces;
    }
}
