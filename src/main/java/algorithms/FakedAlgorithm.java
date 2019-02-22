package algorithms;


import algorithms.src.fakedalgorithmhelper.mAdapter;
import algorithms.src.fakedalgorithmhelper.mStatisticData;
import core.Algorithm;
import core.StatisticData;
import core.Statisticable;
import model.Robot;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;

public class FakedAlgorithm extends Algorithm implements Statisticable {

    mAdapter sec;

    private int ctr = 0;

    private static List<Robot> try1;


    public FakedAlgorithm() {
        sec = new mAdapter();
        sec.setAlgorithm(this);
        registerDisplayAdapter(sec);
    }

    @Override
    public Point2D.Double next(List<Robot> robots) {
//        sec.setAlgorithm(this);
        Point2D.Double point = getRobot().getSensor().convertToLocal(getRobot().getPosition());
        point.setLocation(point.getX() + 1, point.getY());
        return point;
    }

    @Override
    public String algorithmName() {
        return "FAKED";
    }

    @Override
    public String algorithmDescription() {
        return "used for developing propose only";
    }


    @Override
    public HashMap<String, StatisticData> update(HashMap<String, StatisticData> currentStatisticData) {
        StatisticData<Integer> statisticData = currentStatisticData.get("");
        statisticData.update(1);
        return currentStatisticData;
    }

    @Override
    public HashMap<String, StatisticData> init() {
        HashMap a = new HashMap();
        a.put("", new mStatisticData());
        return a;
    }
}
