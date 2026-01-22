package frc.robot.subsystems.intakeAndLauncher;

import frc.robot.util.LoggedTunableNumber;

public class IntakeAndLauncherConstants {
  public static final LoggedTunableNumber intakeVoltage =
      new LoggedTunableNumber("IntakeAndLauncher/Intake Voltage", -6);
  public static final LoggedTunableNumber launchVoltage =
      new LoggedTunableNumber("IntakeAndLauncher/Launch Voltage", -11);
  public static final LoggedTunableNumber launchWarmUpTime =
      new LoggedTunableNumber("IntakeAndLauncher/Launch Warm Up Time", .5);
}
