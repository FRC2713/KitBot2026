package frc.robot.commands.autos;

import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.feeder.FeederConstants;
import frc.robot.subsystems.intakeAndLauncher.IntakeAndLauncher;
import frc.robot.subsystems.intakeAndLauncher.IntakeAndLauncherConstants;

public class StartCollectShoot {
  public static AutoRoutine getRoutine(
      AutoFactory factory,
      Drive driveSubsystem,
      IntakeAndLauncher intakeAndShooter,
      Feeder feederAndIndexer) {
    AutoRoutine routine = factory.newRoutine("Start Collect Shoot");

    AutoTrajectory faceFuel = routine.trajectory("FaceFuel");
    AutoTrajectory collectFuel = routine.trajectory("CollectFuel");
    AutoTrajectory fuelToShot = routine.trajectory("FuelToShot");

    routine
        .active()
        .onTrue(
            Commands.sequence(
                new InstantCommand(() -> Commands.print("Going to fuel")),
                faceFuel.resetOdometry(),
                faceFuel.cmd()));

    faceFuel
        .done()
        .onTrue(
            Commands.sequence(
                new InstantCommand(() -> Commands.print("Starting intake and collecting fuel")),
                Commands.parallel(
                    intakeAndShooter.voltageCmd(IntakeAndLauncherConstants.intakeVoltage.get()),
                    collectFuel.cmd())));
    collectFuel
        .done()
        .onTrue(
            Commands.sequence(
                new InstantCommand(() -> Commands.print("Moving to shooting position")),
                fuelToShot.cmd()));
    fuelToShot
        .done()
        .onTrue(
            Commands.sequence(
                new InstantCommand(() -> Commands.print("Starting launch sequence")),
                Commands.race(
                    Commands.parallel(
                        intakeAndShooter.voltageCmd(IntakeAndLauncherConstants.launchVoltage.get()),
                        Commands.sequence(
                            Commands.waitSeconds(IntakeAndLauncherConstants.launchWarmUpTime.get()),
                            Commands.sequence(
                                new InstantCommand(
                                    () -> Commands.print("Feeding fuel to launcher")),
                                feederAndIndexer.voltageCmd(FeederConstants.launchVoltage.get())))),
                    Commands.waitSeconds(AutoConstants.launchDuration.get()))));

    return routine;
  }
}
