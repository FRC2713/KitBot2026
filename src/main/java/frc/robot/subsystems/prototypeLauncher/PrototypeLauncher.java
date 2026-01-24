package frc.robot.subsystems.prototypeLauncher;

import com.ctre.phoenix6.configs.ClosedLoopGeneralConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Velocity;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static edu.wpi.first.units.Units.Rotations;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import org.littletonrobotics.junction.Logger;

public class PrototypeLauncher extends SubsystemBase {
  private TalonFX leftMotor = new TalonFX(2);
  private TalonFX rightMotor = new TalonFX(1);
  private double trackedvoltage = 0;
  private AngularVelocity velocity = RotationsPerSecond.of(0);
  private TalonFXConfiguration leftMotorConfig = new TalonFXConfiguration();

  @Override
  public void periodic() {
    Logger.recordOutput("Prototype/voltage", trackedvoltage);
    Logger.recordOutput("Prototype/desiredVelocity", velocity);
    Logger.recordOutput("Prototype/velocity", leftMotor.getVelocity().getValue());
  }

  public PrototypeLauncher() {
    rightMotor.setControl(new Follower(leftMotor.getDeviceID(), MotorAlignmentValue.Opposed));
    leftMotorConfig.Slot0.kP = 0.01;
    leftMotorConfig.Slot0.kV = 500;
    leftMotor.getConfigurator().apply(leftMotorConfig);
  }

  public void setVoltagePrototype(double commandedVoltage) {
    leftMotor.setVoltage(commandedVoltage);
    trackedvoltage = commandedVoltage;
  }

  public void setVelocityPrototype(AngularVelocity desiredVelocity) {
    this.velocity = desiredVelocity;
    leftMotor.setControl(new VelocityVoltage(desiredVelocity));
  }

  public Command voltageCmdPrototype(double desiredVoltage) {
    return Commands.sequence(
        Commands.print("LaunchPrototype set to: " + desiredVoltage),
        Commands.runOnce(() -> this.setVoltagePrototype(desiredVoltage), this));
  }

  public Command velocityCmdPrototype(AngularVelocity velocity) {
    return Commands.sequence(
        Commands.print("LaunchPrototypeVelocity set to: " + velocity),
        Commands.runOnce(() -> this.setVelocityPrototype(velocity), this));
  }
}
