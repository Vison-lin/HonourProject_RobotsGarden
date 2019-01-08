package garden.core;

import garden.model.Robot;

import java.awt.*;

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
     *
     * Return a point object that represents the next position of the robot in LOCAL COORDINATE SYSTEM
     *
     * Put the computed new location in LOCAL COORDINATE SYSTEM as the return object
     * @return
     */
    public abstract Point next();

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }
}
