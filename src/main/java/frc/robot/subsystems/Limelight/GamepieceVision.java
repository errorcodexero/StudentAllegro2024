package frc.robot.subsystems.Limelight;

import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Meters;

import java.util.ArrayList;
import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.TimeInterpolatableBuffer;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.Distance;
import edu.wpi.first.units.Measure;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Limelight.structs.XeroGamepiece;

public class GamepieceVision extends SubsystemBase {
    
    private final VisionIO io_;
    private final VisionIOInputsAutoLogged inputs_;

    private final Transform3d robotToCamera_;
    private final Supplier<Pose2d> robotPoseSupplier_;

    private final TimeInterpolatableBuffer<Pose2d> poseBuffer_;

    public GamepieceVision(VisionIO io, Transform3d robotToCamera, Supplier<Pose2d> robotPoseSupplier) {
        io_ = io;
        inputs_ = new VisionIOInputsAutoLogged();
        robotToCamera_ = robotToCamera;
        robotPoseSupplier_ = robotPoseSupplier;

        poseBuffer_ = TimeInterpolatableBuffer.createBuffer(15);
    }

    @Override
    public void periodic() {
        io_.updateInputs(inputs_);
        Logger.processInputs(getName(), inputs_);

        poseBuffer_.addSample(Timer.getFPGATimestamp(), robotPoseSupplier_.get());

        Pose2d interpolatedPose = poseBuffer_.getSample(inputs_.timestampSeconds).orElseGet(() -> {
            Logger.recordOutput(getName() + "/TimeInterpolation/LastEmptyInterpolatedPose", inputs_.timestampSeconds);
            return new Pose2d();
        });

        Logger.recordOutput(getName() + "/TimeInterpolation/Pose", interpolatedPose);

        ArrayList<Pose3d> notePoses = new ArrayList<>();

        for (XeroGamepiece gamepiece : inputs_.gamepieces) {
            double distance = calcDistance(gamepiece) + robotToCamera_.getX();
            double yAdjustment = calcYAdjustment(distance, gamepiece.angleX);

            Pose2d notePose = interpolatedPose.transformBy(new Transform2d(
                new Translation2d(distance, -yAdjustment),
                new Rotation2d()
            ));

            Measure<Distance> noteWidth = Inches.of(2);

            notePoses.add(new Pose3d(notePose).transformBy(
                new Transform3d(0.0, 0.0, noteWidth.in(Meters) / 2, new Rotation3d())
            ));
        }

        Logger.recordOutput(getName() + "/NotePoses", notePoses.toArray(new Pose3d[0]));
    }

    private double calcDistance(XeroGamepiece piece) {
        double camHeight = robotToCamera_.getZ();
        double angle = 90 - Units.radiansToDegrees(robotToCamera_.getRotation().getY()) + piece.angleY;
        return camHeight * Math.tan(Units.degreesToRadians(angle));
    }

    private double calcYAdjustment(double distance, double angleX) {
        return Math.tan(Units.degreesToRadians(angleX)) * distance;
    }

    public int numGamepieces() {
        return inputs_.gamepieces.length;
    }

    public XeroGamepiece[] getGamepieces() {
        return inputs_.gamepieces;
    }
}
