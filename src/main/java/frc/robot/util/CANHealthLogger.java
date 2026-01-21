package frc.robot.util;

import com.ctre.phoenix6.CANBus;
import edu.wpi.first.hal.can.CANStatus;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class CANHealthLogger extends SubsystemBase {

  @Override
  public void periodic() {
    CANStatus status = RobotController.getCANStatus();
    Logger.recordOutput("CAN/RIO/recieveErrorCount", status.receiveErrorCount);
    Logger.recordOutput("CAN/RIO/transmitErrorCount", status.transmitErrorCount);
    Logger.recordOutput("CAN/RIO/txFullCount", status.txFullCount);
    Logger.recordOutput("CAN/RIO/busOffCount", status.busOffCount);
    Logger.recordOutput("CAN/RIO/busUtilization", status.percentBusUtilization);
    // Battery voltage can be useful when debugging intermittent dropouts
    Logger.recordOutput("CAN/RIO/batteryVoltage", RobotController.getBatteryVoltage());

    CANBus.CANBusStatus ctreStatus = CANBus.roboRIO().getStatus();
    Logger.recordOutput("CAN/RIOfromCTRE/recieveErrorCount", ctreStatus.REC);
    Logger.recordOutput("CAN/RIOfromCTRE/transmitErrorCount", ctreStatus.TEC);
    Logger.recordOutput("CAN/RIOfromCTRE/txFullCount", ctreStatus.TxFullCount);
    Logger.recordOutput("CAN/RIOfromCTRE/busOffCount", ctreStatus.BusOffCount);
    Logger.recordOutput("CAN/RIOfromCTRE/busUtilization", ctreStatus.BusUtilization);
  }
}
