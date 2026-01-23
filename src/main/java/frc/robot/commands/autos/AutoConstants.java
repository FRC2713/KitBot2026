package frc.robot.commands.autos;

import frc.robot.util.LoggedTunableNumber;

public class AutoConstants {
  public static final LoggedTunableNumber launchDuration =
      new LoggedTunableNumber("Auto/Launch Length", 10);
  public static final LoggedTunableNumber autoSpeed =
      new LoggedTunableNumber("Auto/Auto Speed", 1.0);

  /**
   * Initialize all auto constants by accessing them once so they appear on dashboard
   */
  public static void initialize() {
    autoSpeed.get();
    launchDuration.get();
  }
}
