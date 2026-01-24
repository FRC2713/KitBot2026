package frc.robot.subsystems.fuelDetector;

import java.util.ArrayList;

public class FuelSquare {
    public ArrayList<FuelCoordinates> fuelList = new ArrayList<>(0);

    public int gridX;
    public int gridY;
    public double squareWidth;
    public double squareHeight;

    public boolean isInFuelCluster = false;
    public FuelCluster cluster;

    private int fuelCount = 0;

    public FuelSquare(int x, int y, double width, double height) {
        gridX = x;
        gridY = y;
        squareWidth = width;
        squareHeight = height;
    }
    public FuelSquare(int x, int y) {
        gridX = x;
        gridY = y;
    }
    public FuelSquare() {
        
    }
    public void addFuel(FuelCoordinates fuel) {
        fuelList.add(fuel);
        setFuelCount();
    }
    public int getFuelCount() {
        return fuelCount;
    }
    private void setFuelCount() {
        fuelCount = fuelList.size();
    }
    public void addToCluster(FuelCluster c) {
        isInFuelCluster = true;
        cluster = c;
    }
}
