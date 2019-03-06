package algorithms;


import model.Algorithm;
import model.Robot;

import java.awt.geom.Point2D;
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
     * Assign robot goes to an arbitrary point that locates within robot's vision
     *
     * @param localRobotList the list that contains all the visible robots for this robots.
     * @return the next position that the robot is going to go.
     */
    @Override
    public Point2D.Double next(List<Robot> localRobotList) {

        Random random = new Random();
        double distance = Double.POSITIVE_INFINITY;
        double currX = 0;
        double currY = 0;
        while (distance > getRobot().getVision()) {
            currX = random.nextInt((int) (2 * getRobot().getVision() + 1)) - getRobot().getVision();
            currY = random.nextInt((int) (2 * getRobot().getVision() + 1)) - getRobot().getVision();
            distance = Math.sqrt(Math.pow(currX, 2) + Math.pow(currY, 2));
        }
        Point2D.Double point = new Point2D.Double();
        point.setLocation(currX, currY);
        return point;

    }

    @Override
    public String algorithmName() {
        return "Random Algorithm";
    }

    @Override
    public String algorithmDescription() {
        return "Robot will go to a random position that is within its vision";
    }
}
