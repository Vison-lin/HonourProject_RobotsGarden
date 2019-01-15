package garden.core.llibrary;

import garden.core.Algorithm;
import garden.model.Robot;

import java.awt.*;
import java.util.List;
import java.util.Random;

/**
 * By applying this algorithm, in each step, the robots will go to an arbitrary point that locates within its vision.
 * <br/>
 * <br/>
 * Note that this algorithm cannot guarantee that the new location is located within the garden scope. This will be handled in the gardenController.
 */
public class RandomAlgorithm extends Algorithm {
    /**
     * Create a new Algorithm class with the associated robot.
     *
     * @param robot The robot instance that this algorithm deal with
     */
    public RandomAlgorithm(Robot robot) {
        super(robot);
    }

    /**
     * Assign robot goes to an arbitrary point that locates within robot's vision
     *
     * @param localRobotList the list that contains all the visible robots for this robots.
     * @return the next position that the robot is going to go.
     */
    @Override
    public Point next(List<Robot> localRobotList) {

        Random random = new Random();
        double distance = Double.POSITIVE_INFINITY;
        double currX = 0;
        double currY = 0;
        while (distance > getRobot().getVision()) {
            currX = random.nextInt((int) (2 * getRobot().getVision() + 1)) - getRobot().getVision();
            currY = random.nextInt((int) (2 * getRobot().getVision() + 1)) - getRobot().getVision();
            distance = Math.sqrt(Math.pow(currX, 2) + Math.pow(currY, 2));
        }
        Point point = new Point();
        point.setLocation(currX, currY);
        return point;

    }
}
