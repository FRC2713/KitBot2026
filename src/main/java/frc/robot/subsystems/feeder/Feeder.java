package frc.robot.subsystems.feeder;

import com.revrobotics.REVLibError;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class Feeder extends SubsystemBase {
  private double commandedrpm;
  private SparkMax motorController = new SparkMax(2, MotorType.kBrushed);
  // TODO: needs current limits

  @Override
  public void periodic() {
    // Log CAN Bus Health
    REVLibError err = motorController.getLastError();
    Logger.recordOutput("CAN/DeviceStatus/Sparks/" + motorController.getDeviceId(), err);
  }

  public void setRPM(double launcherRPMset) {
    commandedrpm = launcherRPMset;
    motorController.getClosedLoopController().setSetpoint(commandedrpm, ControlType.kVelocity);
  }

  public void setVoltage(double desiredVoltage) {
    motorController.setVoltage(desiredVoltage);
  }

  public Command voltageCmd(double desiredVoltage) {
    return Commands.sequence(
        Commands.print("Feeder set to: " + desiredVoltage),
        Commands.runOnce(() -> this.setVoltage(desiredVoltage), this));
  }
}
