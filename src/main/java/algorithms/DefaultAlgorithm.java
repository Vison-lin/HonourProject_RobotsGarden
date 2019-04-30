package algorithms;


import model.Algorithm;
import model.Robot;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * This represent the default behavior of each robot -- Do nothing: robot will stay where they are in each step. All the robots will be statisticInit with this algorithm.
 */
public class DefaultAlgorithm extends Algorithm {

    /**
     * This is a default behavior for each robot. So do nothing.
     *
     * @param localRobotList the list that contains all the visible robots for this robots.
     * @return the next position that the robot is going to go.
     */
    @Override
    public Point2D.Double next(List<Robot> localRobotList) {
        Point2D.Double point = new Point2D.Double();
        point.setLocation(0, 0);//by default, not move, so move to 0, 0 in its local coordinate
        return point;
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
