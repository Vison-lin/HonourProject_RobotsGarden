package garden.algorithms;

import garden.core.Algorithm;
import garden.model.Robot;

import java.awt.*;
import java.util.List;

public class FakedAlgorithm_UpdateColor extends Algorithm {

    public FakedAlgorithm_UpdateColor() {

    }

    @Override
    public Point next(List<Robot> robots) {
        Point point = new Point();
        point.setLocation(0, 0);
        getRobot().getSensor().getAllVisibleRobotsInLocalScale();//In real algorithm, keep obtaining local watchable robots in each step.
        return point;
    }

    @Override
    public String algorithmName() {
        return "FAKED";
    }

    @Override
    public String algorithmDescription() {
        return "???!!!___";
    }
}
