package frc.robot.subsystems.prototype;

import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class Prototype extends SubsystemBase {
  private TalonFX leftMotor = new TalonFX(2);
  private TalonFX rightMotor = new TalonFX(1);
  private double trackedvoltage = 0;

  @Override
  public void periodic() {
    Logger.recordOutput("Prototype/voltage", trackedvoltage);
  }

  public Prototype() {
    rightMotor.setControl(new Follower(leftMotor.getDeviceID(), MotorAlignmentValue.Opposed));
  }

  public void setVoltagePrototype(double commandedVoltage) {
    leftMotor.setVoltage(commandedVoltage);
    trackedvoltage = commandedVoltage;
  }

  public Command voltageCmdPrototype(double desiredVoltage) {
    return Commands.sequence(
        Commands.print("LaunchPrototype set to: " + desiredVoltage),
        Commands.runOnce(() -> this.setVoltagePrototype(desiredVoltage), this));
  }
}
