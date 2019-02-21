package algorithms;


import algorithms.src.fakedalgorithmhelper.mAdapter;
import core.Algorithm;
import model.Robot;

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
//        sec.setAlgorithm(this);
        Point2D.Double point = new Point2D.Double();
        point.setLocation(1, 0);
        System.out.println("in faked" + getRobot().toString());
        System.out.println(getRobot().getGraphicalDisplay().getBottomLayers().size());
//        getRobot().getGraphicalDisplay().insertBottomLayer(sec);
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
        boolean terminate = true;
        for (Robot robot : globalRobotList) {
            boolean t = robot.getAlgorithm().timeToTerminate(globalRobotList);
            terminate = terminate && t;
        }
        return terminate;
    }
}
