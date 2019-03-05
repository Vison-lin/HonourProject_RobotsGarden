package statistics;

import algorithms.GatheringAlgorithm;
import algorithms.GatheringGoToSec;
import algorithms.src.gatheringalgorithm.TotalDistance;
import core.Statistic;
import core.StatisticData;
import model.Robot;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SumOfDistance extends Statistic<Double> {
    @Override
    public boolean showingCondition(List<Robot> robots) {
        for (Robot robot : robots) {
            if ((!(robot.getAlgorithm() instanceof GatheringAlgorithm)) && (!(robot.getAlgorithm() instanceof GatheringGoToSec))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public double result(List<Robot> robots, List<Map<String, StatisticData<Double>>> data) {
        double cumulativeDistance = 0;
        Iterator<Map<String, StatisticData<Double>>> iteratorIterator = data.iterator();
        while (iteratorIterator.hasNext()) {
            Map<String, StatisticData<Double>> curr = iteratorIterator.next();
            StatisticData<Double> currRobotDistance = curr.get(TotalDistance.TAG);
            if (currRobotDistance != null) {
                cumulativeDistance = cumulativeDistance + currRobotDistance.getValue();
            }

        }
        return cumulativeDistance;
    }

    @Override
    public String tag() {
        return "Sum of distance";
    }

    @Override
    public void clean() {

    }
}
