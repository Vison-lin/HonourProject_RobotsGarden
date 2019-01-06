package garden.core.llibrary;

import garden.core.model.Algorithm;
import garden.model.Robot;

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
    public void next() {
    }
}
