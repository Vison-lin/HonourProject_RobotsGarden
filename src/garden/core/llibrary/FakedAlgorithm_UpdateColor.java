package garden.core.llibrary;

import garden.core.Algorithm;
import garden.model.Robot;

import java.awt.*;

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
    public Point next() {
        Point point = new Point();
        point.setLocation(getRobot().getPositionX() - 1, getRobot().getPositionY() - 1);
        return point;
    }
}
