package garden.core.llibrary;

import garden.core.Algorithm;
import garden.model.Robot;

import java.awt.*;
import java.util.List;

public class FakedAlgorithm_UpdateColor extends Algorithm {
    /**
     * Create a new Algorithm class with the associated robot.
     *
     * @param robot The robot instance that this algorithm deal with
     */
    public FakedAlgorithm_UpdateColor(Robot robot) {
        super(robot);
    }

    @Override
    public Point next(List<Robot> robots) {
        Point point = new Point();
        point.setLocation(0, 0);
        getRobot().getSensor().getAllVisibleRobotsInLocalScale();//In real algorithm, keep obtaining local watchable robots in each step.
        return point;
    }
}
