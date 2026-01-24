package frc.robot.subsystems.fuelDetector;

public class FuelCoordinates {
    public double chance;
    public double centerX;
    public double centerY;
    public double boxX;
    public double boxY;
    public double boxX2;
    public double boxY2;
    public double width;
    public double height;
    public double depth;

    public FuelCoordinates(double x, double y, double boxWidth, double boxHeight, double c) {
        boxX = x;
        boxY = y;
        boxX2 = FuelCoordinates.pointFromDistance(x, boxWidth);
        boxY2 = FuelCoordinates.pointFromDistance(y, boxHeight);
        computeCenterPoint(boxX, boxY, boxX2, boxY2);
        width = boxWidth;
        height = boxHeight;
        computeDepth(45); //TODO: replace 45 with constant for camera FOV
        chance = c;
    }
    private static double pointFromDistance(double point, double length) {
        //Get a second point from one point and a distance
        return point + length;
    }
    public void computeCenterPoint(double x, double y, double x2, double y2) {
        centerX = (x2 - x) / 2;
        centerY = (y2 - y) / 2;
    }
    public void assignSelfToFuelSquare(int gridWidth, int gridHeight, FuelSquare[][] squareArray) {
        int squareX = (int) Math.round(centerX / gridWidth);
        int squareY = (int) Math.round(centerY / gridHeight);
        squareArray[squareX][squareY].addFuel(this);
    }
    //TODO: implement depth function
    public double computeDepth(double cameraFOV) {
        return 0.0;
    }
}
