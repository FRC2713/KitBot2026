// Copyright (c) 2021-2026 Littleton Robotics
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by a BSD
// license that can be found in the LICENSE file
// at the root directory of this project.

package frc.robot;

import choreo.auto.AutoChooser;
import choreo.auto.AutoFactory;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import frc.robot.commands.DriveCommands;
import frc.robot.commands.autos.AutoConstants;
import frc.robot.commands.autos.DriveTesting;
import frc.robot.commands.autos.StartCollectShoot;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.DriveConstants;
import frc.robot.subsystems.drive.GyroIO;
import frc.robot.subsystems.drive.GyroIOPigeon2;
import frc.robot.subsystems.drive.ModuleIO;
import frc.robot.subsystems.drive.ModuleIOSim;
import frc.robot.subsystems.drive.ModuleIOTalonFX;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.feeder.FeederConstants;
import frc.robot.subsystems.intakeAndLauncher.IntakeAndLauncher;
import frc.robot.subsystems.intakeAndLauncher.IntakeAndLauncherConstants;
import frc.robot.util.CANHealthLogger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // Subsystems
  private final Drive drive;
  private final IntakeAndLauncher intakeAndShooter;
  private final Feeder feederAndIndexer;

  // Controller
  private final CommandXboxController controller = new CommandXboxController(0);
  private static boolean hasRanAuto = false;

  // Utility
  private final AutoFactory autoFactory;
  public final AutoChooser autoChooser;
  private final CANHealthLogger canLogger;

  // Dashboard inputs

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

    switch (Constants.currentMode) {
      case REAL:
        // Real robot, instantiate hardware IO implementations
        // ModuleIOTalonFX is intended for modules with TalonFX drive, TalonFX turn, and
        // a CANcoder
        drive =
            new Drive(
                new GyroIOPigeon2(),
                new ModuleIOTalonFX(TunerConstants.FrontLeft),
                new ModuleIOTalonFX(TunerConstants.FrontRight),
                new ModuleIOTalonFX(TunerConstants.BackLeft),
                new ModuleIOTalonFX(TunerConstants.BackRight));

        // The ModuleIOTalonFXS implementation provides an example implementation for
        // TalonFXS controller connected to a CANdi with a PWM encoder. The
        // implementations
        // of ModuleIOTalonFX, ModuleIOTalonFXS, and ModuleIOSpark (from the Spark
        // swerve
        // template) can be freely intermixed to support alternative hardware
        // arrangements.
        // Please see the AdvantageKit template documentation for more information:
        // https://docs.advantagekit.org/getting-started/template-projects/talonfx-swerve-template#custom-module-implementations
        //
        // drive =
        // new Drive(
        // new GyroIOPigeon2(),
        // new ModuleIOTalonFXS(TunerConstants.FrontLeft),
        // new ModuleIOTalonFXS(TunerConstants.FrontRight),
        // new ModuleIOTalonFXS(TunerConstants.BackLeft),
        // new ModuleIOTalonFXS(TunerConstants.BackRight));
        this.intakeAndShooter = new IntakeAndLauncher();
        this.feederAndIndexer = new Feeder();
        break;

      case SIM:
        // Sim robot, instantiate physics sim IO implementations
        drive =
            new Drive(
                new GyroIO() {},
                new ModuleIOSim(TunerConstants.FrontLeft),
                new ModuleIOSim(TunerConstants.FrontRight),
                new ModuleIOSim(TunerConstants.BackLeft),
                new ModuleIOSim(TunerConstants.BackRight));
        this.intakeAndShooter = new IntakeAndLauncher();
        this.feederAndIndexer = new Feeder();
        break;

      default:
        // Replayed robot, disable IO implementations
        drive =
            new Drive(
                new GyroIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {});
        this.intakeAndShooter = new IntakeAndLauncher();
        this.feederAndIndexer = new Feeder();
        break;
    }
    this.canLogger = new CANHealthLogger();

    // Initialize ChoreoLib AutoFactory
    autoFactory =
        new AutoFactory(
            drive::getPose, // Function that returns the current robot pose
            drive::setPose, // Function that resets the current robot pose to the provided Pose2d
            drive::followTrajectory, // The drive subsystem trajectory follower
            true, // If alliance flipping should be enabled
            drive // The drive subsystem
            );

    // Set up auto routines
    autoChooser = new AutoChooser();

    autoChooser.addRoutine(
        "Drive Straight Test", () -> DriveTesting.getRoutine(autoFactory, drive));
    autoChooser.addRoutine(
        "Start, Collect, and Shoot",
        () -> StartCollectShoot.getRoutine(autoFactory, drive, intakeAndShooter, feederAndIndexer));
    RobotModeTriggers.autonomous()
        .whileTrue(
            Commands.sequence(
                new InstantCommand(() -> RobotContainer.hasRanAuto = true),
                autoChooser.selectedCommandScheduler()));

    // Set up SysId routines
    // autoChooser.addCmd(
    //     "Drive Wheel Radius Characterization",
    //     () -> DriveCommands.wheelRadiusCharacterization(drive));
    // autoChooser.addCmd(
    //     "Drive Simple FF Characterization",
    //     () -> DriveCommands.feedforwardCharacterization(drive));
    // autoChooser.addCmd(
    //     "Drive SysId (Quasistatic Forward)",
    //     () -> drive.sysIdQuasistatic(SysIdRoutine.Direction.kForward));
    // autoChooser.addCmd(
    //     "Drive SysId (Quasistatic Reverse)",
    //     () -> drive.sysIdQuasistatic(SysIdRoutine.Direction.kReverse));
    // autoChooser.addCmd(
    //     "Drive SysId (Dynamic Forward)",
    //     () -> drive.sysIdDynamic(SysIdRoutine.Direction.kForward));
    // autoChooser.addCmd(
    //     "Drive SysId (Dynamic Reverse)",
    //     () -> drive.sysIdDynamic(SysIdRoutine.Direction.kReverse));

    // Send to elastic
    SmartDashboard.putData("Auto Chooser", autoChooser);

    // Initialize auto tunable numbers so they appear on dashboard
    AutoConstants.initialize();

    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // Default command, normal field-relative drive
    drive.setDefaultCommand(
        DriveCommands.joystickDrive(
            drive,
            () -> DriveConstants.speedScalar.get() * controller.getLeftY(),
            () -> DriveConstants.speedScalar.get() * controller.getLeftX(),
            () -> DriveConstants.speedScalar.get() * -controller.getRightX()));

    // Lock to 0° when A button is held
    controller
        .a()
        .whileTrue(
            DriveCommands.joystickDriveAtAngle(
                drive,
                () -> -controller.getLeftY(),
                () -> -controller.getLeftX(),
                () -> Rotation2d.kZero));

    // Switch to X pattern when X button is pressed
    controller.x().onTrue(Commands.runOnce(drive::stopWithX, drive));

    // Reset gyro to 0° when B button is pressed
    controller
        .start()
        .onTrue(
            Commands.runOnce(
                    () ->
                        drive.setPose(
                            new Pose2d(drive.getPose().getTranslation(), Rotation2d.kZero)),
                    drive)
                .ignoringDisable(true));
    // Start intake while left-Trigger is held
    controller
        .leftBumper()
        .whileTrue(
            Commands.parallel(
                intakeAndShooter.voltageCmd(IntakeAndLauncherConstants.intakeVoltage.get()),
                feederAndIndexer.voltageCmd(FeederConstants.intakeVoltage.get())))
        .onFalse(
            Commands.parallel(intakeAndShooter.voltageCmd(0.0), feederAndIndexer.voltageCmd(0.0)));

    // Start launcher when right-Trigger is held
    controller
        .rightBumper()
        .whileTrue(
            Commands.parallel(
                intakeAndShooter.voltageCmd(IntakeAndLauncherConstants.launchVoltage.get()),
                Commands.sequence(
                    Commands.waitSeconds(IntakeAndLauncherConstants.launchWarmUpTime.get()),
                    feederAndIndexer.voltageCmd(FeederConstants.launchVoltage.get()))))
        .onFalse(
            Commands.parallel(intakeAndShooter.voltageCmd(0.0), feederAndIndexer.voltageCmd(0.0)));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autoChooser.selectedCommand();
  }
}
