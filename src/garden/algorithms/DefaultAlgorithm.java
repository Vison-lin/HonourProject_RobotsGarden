package garden.algorithms;

import garden.core.Algorithm;
import garden.model.Robot;

import java.awt.*;
import java.util.List;

/**
 * This represent the default behavior of each robot -- Do nothing: robot will stay where they are in each step. All the robots will be init with this algorithm.
 */
public class DefaultAlgorithm extends Algorithm {
    /**
     * Create a new Algorithm class with the associated robot.
     *
     * @param robot The robot instance that this algorithm deal with
     */
    public DefaultAlgorithm(Robot robot) {
        super(robot);
    }

    /**
     * This is a default behavior for each robot. So do nothing.
     *
     * @param localRobotList the list that contains all the visible robots for this robots.
     * @return the next position that the robot is going to go.
     */
    @Override
    public Point next(List<Robot> localRobotList) {
        Point point = new Point();
        point.setLocation(0, 0);//by default, not move, so move to 0, 0 in its local coordinate
        return  point;
    }

    @Override
    public String algorithmName() {
        return "Default Algorithm";
    }

    @Override
    public String algorithmDescription() {
        return "The Default Algorithm: Robot do nothing.";
    }
}
