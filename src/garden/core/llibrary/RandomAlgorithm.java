package garden.core.llibrary;

import garden.core.Algorithm;
import garden.model.Robot;

import java.awt.*;
import java.util.List;

public class RandomAlgorithm extends Algorithm {
    /**
     * Create a new Algorithm class with the associated robot.
     *
     * @param robot The robot instance that this algorithm deal with
     */
    public RandomAlgorithm(Robot robot) {
        super(robot);
    }

    @Override
    public Point next(List<Robot> robotList) {
        //init first one
//        double maxX = (int) getWidth() + 1;
//        double maxY = (int) getHeight() + 1;
//        int ctr = 0;
//        Robot initRobot = gardenController.robotGenerator(" =>" + ctr + "<= ", random.nextInt((int) maxX), random.nextInt((int) maxY));
//
//        //create the rest
//        for (int i = 1; i < 50; i++) {
//            double xBoundUp = validateWithinTheEnclosingSquare(initRobot.getPositionX() + initRobot.getVision(), maxX);
//            double xBoundLow = validateWithinTheEnclosingSquare(initRobot.getPositionX() - initRobot.getVision(), maxX);
//            double yBoundUp = validateWithinTheEnclosingSquare(initRobot.getPositionY() + initRobot.getVision(), maxY);
//            double yBoundLow = validateWithinTheEnclosingSquare(initRobot.getPositionY() - initRobot.getVision(), maxY);
////                    System.out.println(maxX+"|"+maxY+"|"+xBoundLow + "~" + xBoundUp +"|" +yBoundLow+"~"+yBoundUp);
//            //check if is within the circle
//            double distance = Double.POSITIVE_INFINITY;
//            double currX = 0;
//            double currY = 0;
//            while (distance > initRobot.getVision()) {
//                double x = initRobot.getPositionX();
//                double y = initRobot.getPositionY();
//                currX = random.nextInt((int) (xBoundUp - xBoundLow + 1)) + xBoundLow;
//                currY = random.nextInt((int) (yBoundUp - yBoundLow + 1)) + yBoundLow;
//                double differX = currX - x;
//                double differY = currY - y;
//                distance = Math.sqrt(Math.pow(differX, 2) + Math.pow(differY, 2));
//            }
//            initRobot = gardenController.robotGenerator(" =>" + ctr + "<= ", currX, currY);
//        }
        return null;
    }

    /**
     * Validate if the point is within the square that encloses the circle.
     *
     * @param original the point
     * @param bound    the radius
     * @return validated value
     */
    private double validateWithinTheEnclosingSquare(double original, double bound) {
        if (original >= bound) {
            original = bound;
        } else if (original <= 0) {
            original = 0;
        }
        return original;
    }
}
