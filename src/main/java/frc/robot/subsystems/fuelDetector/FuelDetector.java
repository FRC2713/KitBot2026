package frc.robot.subsystems.fuelDetector;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
    public ArrayList<FuelCoordinates> filterByHighChance(FuelCoordinates[] inputs) {
        //There's probably an easier and shorter way of doing this, but this is simple. Feel free to change it as long as the output doesn't change.
        ArrayList<FuelCoordinates> output = new ArrayList<>();
        for(int i = 0; i < inputs.length; i++) {
            if(inputs[i].chance >= 80.0) {
                output.add(inputs[i]);
            }
        }
        return output;
    }
    public FuelSquare[][] divideIntoSquares(ArrayList<FuelCoordinates> fuelCoords, int gridWidth, int gridHeight) {
        FuelSquare[][] output = new FuelSquare[gridWidth][gridHeight];
        for(int w = 0; w < output.length; w++) {
            for(int h = 0; h < output[w].length; h++) {
                output[w][h] = new FuelSquare();
            }
        }
        for(int i = 0; i < fuelCoords.size(); i++) {
            fuelCoords.get(i).assignSelfToFuelSquare(gridWidth, gridHeight, output);
        }
        return output;
    }
}
