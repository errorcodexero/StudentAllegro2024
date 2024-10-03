package frc.robot.util;

import static edu.wpi.first.units.Units.*;

import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedDashboardNumber;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.units.Angle;
import edu.wpi.first.units.Distance;
import edu.wpi.first.units.Measure;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class ComponentVisualizer {

    
    // The position of the updown.
    private static final Translation3d kUpdownOrigin = new Translation3d(0.203, 0.0, 0.212);
    
    // The length of the updown to the pivot of the tilt.
    private static Measure<Distance> kUpdownLength = Inches.of(8);
    
    private static Measure<Distance> kElevatorLength = Inches.of(35.8);
    
    // For testing only! Delete after values have been figured out
    private static final LoggedDashboardNumber updownLength = new LoggedDashboardNumber("UpdownLength");
    private static final LoggedDashboardNumber elevatorLength = new LoggedDashboardNumber("ElevatorLength");
    private static final LoggedDashboardNumber elevatorX = new LoggedDashboardNumber("ElevatorX");
    private static final LoggedDashboardNumber elevatorY = new LoggedDashboardNumber("ElevatorY");
    private static final LoggedDashboardNumber elevatorZ = new LoggedDashboardNumber("ElevatorZ");
    
    static {
        updownLength.set(8);
        elevatorLength.set(35.8);

        elevatorX.set(-0.260350);
        elevatorY.set(0);
        elevatorZ.set(-0.097958);

        Commands.run(() -> {
            kUpdownLength = Inches.of(updownLength.get());
            kElevatorLength = Inches.of(elevatorLength.get());
            kClimberBottom = new Pose3d(
                elevatorX.get(),
                elevatorY.get(),
                elevatorZ.get(),
                new Rotation3d(0, Degrees.of(-10).in(Radians), 0)
            );
        }).schedule();
    }

    private static Pose3d kClimberBottom = new Pose3d(
        -0.260350, 0, -0.097958,
        new Rotation3d(0, Degrees.of(-10).in(Radians), 0)
    );

    private final String logkey_;

    private Measure<Angle> updownAngle_; // The angle of the updown
    private Measure<Angle> tiltAngle_; // The angle of the tilt relative to the angle of the updown

    private Measure<Distance> elevatorHeight_; // How far the elevator has gone up from the bottom.
    private Measure<Angle> armAngle_; // The angle of the arm

    private Measure<Distance> climberHeight_; // The height of the climber

    public ComponentVisualizer(String logkey) {
        logkey_ = logkey;

        updownAngle_ = Degrees.of(-123.392);
        tiltAngle_ = Degrees.of(160);
 
        elevatorHeight_ = Meters.of(0);
        armAngle_ = Degrees.of(0);

        climberHeight_ = Meters.of(0);

        update();
    }
    
    public void update() {
        Pose3d updownPose = new Pose3d(
            kUpdownOrigin,
            new Rotation3d(0, updownAngle_.in(Radians), 0)
        );

        Pose3d tiltPose = updownPose.transformBy(
            new Transform3d(
                new Translation3d(kUpdownLength.in(Meters), 0, 0),
                new Rotation3d(0.0, tiltAngle_.in(Radians), 0.0)
            )
        );

        Pose3d elevatorPose = kClimberBottom.transformBy(
            new Transform3d(
                new Translation3d(0, 0, elevatorHeight_.in(Meters)),
                new Rotation3d()
            )
        );

        Pose3d armPose = new Pose3d(
            elevatorPose.transformBy(
                new Transform3d(
                    new Translation3d(0, 0, kElevatorLength.in(Meters)),
                    new Rotation3d()
                )
            ).getTranslation(), new Rotation3d(0, armAngle_.in(Radians), 0)
        );
        
        Pose3d climberPose = kClimberBottom.transformBy(
            new Transform3d(
                new Translation3d(0, 0, climberHeight_.in(Meters)),
                new Rotation3d()
            )
        );

        Logger.recordOutput(logkey_, updownPose, tiltPose, elevatorPose, armPose, climberPose);
    }

    public void setUpdownAngle(Measure<Angle> updownAngle) {
        updownAngle_ = updownAngle;
        update();
    }

    public void setTiltAngle(Measure<Angle> tiltAngle) {
        tiltAngle_ = tiltAngle;
        update();
    }

    public void setElevatorHeight(Measure<Distance> elevatorHeight) {
        elevatorHeight_ = elevatorHeight;
        update();
    }

    public void setArmAngle(Measure<Angle> armAngle) {
        armAngle_ = armAngle;
        update();
    }

    public void setClimberHeight(Measure<Distance> climberHeight) {
        climberHeight_ = climberHeight;
        update();
    }

}
