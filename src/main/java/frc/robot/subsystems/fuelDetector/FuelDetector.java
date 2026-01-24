package frc.robot.subsystems.fuelDetector;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class FuelDetector extends SubsystemBase {
    public void periodic() {
        //get fuel information, call algorithm
    }
    public double[][] getFuelClusters() {
        double[][] output = {
            {0,0,0},
            {0,0,0},
            {0,0,0}
        };
        return output;
    }
}
