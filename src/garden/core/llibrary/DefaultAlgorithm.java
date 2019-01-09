package garden.core.llibrary;

import garden.core.Algorithm;
import garden.model.Robot;

import java.awt.*;
import java.util.List;

/**
 * This represent the default behavior of each robot -- Do nothing. All the robots should be init with this algorithm.
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
     * This is a default behavior for each robot. So do nothing
     */
    @Override
    public Point next(List<Robot> robots) {
        Point point = new Point();
        point.setLocation(0, 0);//by default, not move, so move to 0, 0 in its local coordinate
        return  point;
    }
}
