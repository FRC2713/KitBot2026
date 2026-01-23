package frc.robot.subsystems.prototype;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class Prototype extends SubsystemBase {
  private TalonFX motorController = new TalonFX(1);
  private double trackedvoltage = 0;

  @Override
  public void periodic() {
    Logger.recordOutput("Prototype/voltage", trackedvoltage);
  }

  public void setVoltagePrototype(double commandedVoltage) {
    motorController.setVoltage(commandedVoltage);
    trackedvoltage = commandedVoltage;
  }

  public Command voltageCmdPrototype(double desiredVoltage) {
    return Commands.sequence(
        Commands.print("LaunchPrototype set to: " + desiredVoltage),
        Commands.runOnce(() -> this.setVoltagePrototype(desiredVoltage), this));
  }
}
