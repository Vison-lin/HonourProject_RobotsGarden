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

public class LongestDistance extends Statistic<Double> {
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
    public String show(List<Robot> robots, List<Map<String, StatisticData<Double>>> data) {
        double longestDistance = 0;
        Iterator<Map<String, StatisticData<Double>>> iteratorIterator = data.iterator();
        while (iteratorIterator.hasNext()) {
            Map<String, StatisticData<Double>> curr = iteratorIterator.next();
            StatisticData<Double> currRobotDistance = curr.get(TotalDistance.TAG);
            if (currRobotDistance != null) {
                if (currRobotDistance != null) {
                    if (longestDistance < currRobotDistance.getValue()) {
                        longestDistance = currRobotDistance.getValue();
                    }
                }
            }

        }
        return "The longest distance is: " + longestDistance + "";
    }

    @Override
    public void clean() {

    }
}
