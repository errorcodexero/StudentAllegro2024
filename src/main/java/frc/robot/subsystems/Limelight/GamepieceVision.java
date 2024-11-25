package frc.robot.subsystems.Limelight;

import static edu.wpi.first.units.Units.Degrees;

import java.util.ArrayList;
import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Limelight.structs.XeroGamepiece;

public class GamepieceVision extends SubsystemBase {
    private final VisionIO io_;
    private final VisionIOInputsAutoLogged inputs_;

    private final Transform3d robotToCamera_;
    private final Supplier<Pose2d> robotPoseSupplier_;

    public GamepieceVision(VisionIO io, Transform3d robotToCamera, Supplier<Pose2d> robotPoseSupplier) {
        io_ = io;
        inputs_ = new VisionIOInputsAutoLogged();
        robotToCamera_ = robotToCamera;
        robotPoseSupplier_ = robotPoseSupplier;
    }

    @Override
    public void periodic() {
        io_.updateInputs(inputs_);
        Logger.processInputs(getName(), inputs_);

        ArrayList<Pose3d> notePoses = new ArrayList<>();

        for (XeroGamepiece gamepiece : inputs_.gamepieces) {
            double distance = calcDistance(gamepiece) + robotToCamera_.getX();
            Logger.recordOutput("Distance", distance);

            double yAdjustment = calcYAdjustment(distance, gamepiece.angleX);

            Pose2d notePose = robotPoseSupplier_.get().transformBy(new Transform2d(
                new Translation2d(distance, -yAdjustment),
                new Rotation2d()
            ));

            notePoses.add(new Pose3d(notePose));
        }

        Logger.recordOutput("NotePoses", notePoses.toArray(new Pose3d[0]));
    }

    private double calcYAdjustment(double distance, double angleX) {
        return Math.tan(Units.degreesToRadians(angleX)) * distance;
    }

    private double calcDistance(XeroGamepiece piece) {
        double camHeight = robotToCamera_.getZ();
        double angle = 90 - Units.radiansToDegrees(robotToCamera_.getRotation().getY()) + piece.angleY;
        return camHeight * Math.tan(Units.degreesToRadians(angle));
    }

    public int numGamepieces() {
        return inputs_.gamepieces.length;
    }

    public XeroGamepiece[] getGamepieces() {
        return inputs_.gamepieces;
    }
}
