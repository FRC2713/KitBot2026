package frc.robot.subsystems.fuelDetector;

import java.util.ArrayList;

public class FuelCluster {
    public ArrayList<FuelSquare> fuelSquares = new ArrayList<>(0);
    public double clusterDepth;

    public FuelCluster() {

    }

    public FuelCluster(ArrayList<FuelSquare> squares) {
        fuelSquares = squares;
    }

    public FuelCluster(FuelSquare square) {
        addFuelSquare(square);
    }

    public void addFuelSquare(FuelSquare square) {
        fuelSquares.add(square);
        square.addToCluster(this);
    }

    public double averageSquareDepth() {
        int size = fuelSquares.size();
        double sum = 0;
        for(int i = 0; i < size; i++) {
            sum += fuelSquares.get(i).averageDepth;
        }
        sum /= size;
        return sum;
    }
}
