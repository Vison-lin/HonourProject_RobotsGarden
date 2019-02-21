package algorithms;


import core.Algorithm;
import model.Robot;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;

public class RandomJumpAlgorithm extends Algorithm {
    @Override
    public Point2D.Double next(List<Robot> localRobotList) {
        Random random = new Random();
        Robot targetRobot = localRobotList.get(random.nextInt(localRobotList.size()));
        System.out.println(targetRobot.getPositionX() + ", " + targetRobot.getPositionY());
        return new Point2D.Double(targetRobot.getPositionX(), targetRobot.getPositionY());
    }

    @Override
    public String algorithmName() {
        return "Random Jump Algorithm";
    }

    @Override
    public String algorithmDescription() {
        return "Random jump to another random robot";
    }
}
