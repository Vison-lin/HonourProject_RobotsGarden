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
     * The Algorithm will compute the next step of the robot and manipulate to it.
     * As long as called this method, the robot instance will be updated.
     */
    public abstract Point next();

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }
}
