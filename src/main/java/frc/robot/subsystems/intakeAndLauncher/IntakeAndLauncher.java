package frc.robot.subsystems.intakeAndLauncher;

import com.revrobotics.REVLibError;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class IntakeAndLauncher extends SubsystemBase {
  private SparkMax motorController = new SparkMax(1, MotorType.kBrushed);
  private double rotationsperminute = 1000.0;
  private boolean state = false;
  private double trackedvoltage = 0;

  @Override
  public void periodic() {
    // Log CAN Bus Health
    REVLibError err = motorController.getLastError();
    Logger.recordOutput("CAN/DeviceStatus/Sparks/" + motorController.getDeviceId(), err);
  }

  public void turnon() {
    state = true;
  }

  public void turnoff() {
    state = false;
  }

  public void setRPM(double commandedrpm) {
    motorController.getClosedLoopController().setSetpoint(commandedrpm, ControlType.kVelocity);
    rotationsperminute = commandedrpm;
  }

  public void setVoltage(double commandedVoltage) {
    motorController.setVoltage(commandedVoltage);
    trackedvoltage = commandedVoltage;
  }

  public Command voltageCmd(double desiredVoltage) {
    return Commands.sequence(
        Commands.print("IntakeAndLaunch set to: " + desiredVoltage),
        Commands.runOnce(() -> this.setVoltage(desiredVoltage), this));
  }
}
