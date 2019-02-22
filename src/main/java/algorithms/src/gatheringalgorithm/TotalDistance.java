package algorithms.src.gatheringalgorithm;

import core.StatisticData;

public class TotalDistance implements StatisticData<Double> {

    public static final String TAG = "TotalDistance";

    double distance = 0;

    @Override
    public void update(Double increment) {
        this.distance += increment;
    }

    @Override
    public String display() {
        return distance + "";
    }

    @Override
    public StatisticData<Double> deepCopy() {
        TotalDistance totalDistance = new TotalDistance();
        totalDistance.setDistance(this.distance);
        return totalDistance;
    }

    @Override
    public Double getValue() {
        return distance;
    }

    @Override
    public String getTag() {
        return null;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

}
