package garden.algorithms;

import garden.algorithms.src.fakedalgorithmhelper.mAdapter;
import garden.core.Algorithm;
import garden.model.Robot;

import java.awt.geom.Point2D;
import java.util.List;

public class FakedAlgorithm extends Algorithm {

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
        sec.setAlgorithm(this);
        Point2D.Double point = new Point2D.Double();
        point.setLocation(1, 0);
        getRobot().getGraphicalDisplay().insertBottomLayer(sec);
//        Random random = new Random();
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
    public boolean timeToTerminate(List<Robot> globalRobotList) {
        return globalRobotList.size() == 5;
    }
}
