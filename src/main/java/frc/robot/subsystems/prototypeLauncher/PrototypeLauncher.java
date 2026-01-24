package frc.robot.subsystems.prototypeLauncher;

import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.LoggedTunableNumber;
import org.littletonrobotics.junction.Logger;

public class PrototypeLauncher extends SubsystemBase {
  private TalonFX leftMotor = new TalonFX(2);
  private TalonFX rightMotor = new TalonFX(1);
  private double trackedvoltage = 0;
  private AngularVelocity velocity = RotationsPerSecond.of(0);
  private TalonFXConfiguration leftMotorConfig = new TalonFXConfiguration();
  private LoggedTunableNumber prototypeP = new LoggedTunableNumber("prototypeP", 0);

  @Override
  public void periodic() {
    if (prototypeP.hasChanged(hashCode())) {
      this.setPID(prototypeP.get());
    }
    Logger.recordOutput("Prototype/voltage", trackedvoltage);
    Logger.recordOutput("Prototype/desiredVelocity", velocity);
    Logger.recordOutput("Prototype/velocity", leftMotor.getVelocity().getValue());
  }

  public PrototypeLauncher() {
    rightMotor.setControl(new Follower(leftMotor.getDeviceID(), MotorAlignmentValue.Opposed));
    this.setPID(prototypeP.get());
  }

  public void setPID(double P) {
    leftMotorConfig.Slot0.kP = P;
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
