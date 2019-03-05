package statistics;

import algorithms.GatheringAlgorithm;
import algorithms.GatheringGoToSec;
import algorithms.src.gatheringalgorithm.NumOfRun;
import core.Statistic;
import core.StatisticData;
import model.Robot;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LongestRun extends Statistic<Integer> {
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
    public double result(List<Robot> robots, List<Map<String, StatisticData<Integer>>> data) {
        int longestRun = 0;
        Iterator<Map<String, StatisticData<Integer>>> iteratorIterator = data.iterator();
        while (iteratorIterator.hasNext()) {
            Map<String, StatisticData<Integer>> curr = iteratorIterator.next();
            StatisticData<Integer> numOfRun = curr.get(NumOfRun.TAG);
            if (numOfRun != null) {
                if (longestRun < numOfRun.getValue()) {
                    longestRun = numOfRun.getValue();
                }
            }

        }
        return longestRun;
    }

    @Override
    public String tag() {
        return "Longest Run";
    }

    @Override
    public void clean() {

    }
}
