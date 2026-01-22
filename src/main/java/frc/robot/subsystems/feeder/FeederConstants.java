package frc.robot.subsystems.feeder;

import frc.robot.util.LoggedTunableNumber;

public class FeederConstants {
  public static final LoggedTunableNumber intakeVoltage =
      new LoggedTunableNumber("Feeder/Intake Voltage", -12);
  public static final LoggedTunableNumber launchVoltage =
      new LoggedTunableNumber("Feeder/Launch Voltage", 8);
}
