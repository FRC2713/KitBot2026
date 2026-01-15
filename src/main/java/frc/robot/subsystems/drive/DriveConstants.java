package frc.robot.subsystems.drive;

import frc.robot.util.ControlGains;
import frc.robot.util.LoggedTunableGains;

public class DriveConstants {
  public final class AutoConstants {
    public static final LoggedTunableGains xTrajectoryController =
        new LoggedTunableGains("xTraj", new ControlGains().p(2));
    public static final LoggedTunableGains yTrajectoryController =
        new LoggedTunableGains("yTraj", new ControlGains().p(2));
    public static final LoggedTunableGains headingTrajectoryController =
        new LoggedTunableGains("headingTraj", new ControlGains().p(3).d(0.0));
  }
}
