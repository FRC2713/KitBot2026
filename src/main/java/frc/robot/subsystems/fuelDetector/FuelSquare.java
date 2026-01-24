package frc.robot.subsystems.fuelDetector;

import java.util.ArrayList;

public class FuelSquare {
    public ArrayList<FuelCoordinates> fuelList = new ArrayList<>();

    public int gridX;
    public int gridY;
    public double squareWidth;
    public double squareHeight;

    public FuelSquare(int x, int y, double width, double height) {
        gridX = x;
        gridY = y;
        squareWidth = width;
        squareHeight = height;
    }
    public FuelSquare() {
        
    }

    public void addFuel(FuelCoordinates fuel) {
        fuelList.add(fuel);
    }
}
