package garden.core;

import garden.model.Robot;

import java.awt.*;
import java.util.List;


public abstract class Algorithm {

    /**
     * The robot instance that this algorithm deal with
     */
    private Robot robot;

    /**
     * Create a new Algorithm class with the associated robot.
     *
     * @param robot The robot instance that this algorithm deal with
     */
    public Algorithm(Robot robot) {
        this.robot = robot;
    }


    /**
     * CORE METHOD:
     * <br/>
     * <br/>
     * Return a point object that represents the next position of the robot in LOCAL COORDINATE SYSTEM.
     * <br/>
     * <br/>
     * Put the computed new location in LOCAL COORDINATE SYSTEM as the return object. Do not convert the final location into the global coordinate system, which will be done by robot.
     * <br/>
     * <br/>
     * This method will be called in the robot's next() method, which will be called in each iteration (each time user click next button).
     *
     * @param localRobotList the list that contains all the visible robots for this robots.
     * @return the next position that the robot is going to go.
     */
    public abstract Point next(List<Robot> localRobotList);

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }
}
