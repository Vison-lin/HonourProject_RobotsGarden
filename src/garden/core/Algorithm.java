package garden.core;

import garden.model.Robot;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;


public abstract class Algorithm {

    /**
     * The robot instance that this algorithm deal with
     */
    private Robot robot;

    private List<DisplayAdapter> displayAdapters;

    public Algorithm() {
        displayAdapters = new ArrayList<>();
    }

    /**
     * Optional method that will only valid (only been called) if all the robots in the garden run the same algorithm.
     * <br/>
     * By default, it will return false, i.e., robots never terminate.
     * <br/>
     * <br/>
     * Note that since it has been called after the next() called, the globalRobotList will not be initialized at the time the algorithm has been called. Therefore, any attempt to get the globalRobotList outside of this method will through NullPointerException.
     *
     * @param globalRobotList The list of global robots
     * @return true if all the robots (always, run the same algorithm) should be terminated, false otherwise
     */
    public boolean timeToTerminate(List<Robot> globalRobotList) {
        return false;
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
    public abstract Point2D.Double next(List<Robot> localRobotList);

    /**
     * The name of the algorithm that needs to be displayed on the screen
     *
     * @return the name of the algorithm
     */
    public abstract String algorithmName();

    /**
     * The description of the algorithm that needs to be displayed on the screen
     *
     * @return the description of the algorithm
     */
    public abstract String algorithmDescription();

    /**
     * Get the robot instance that associated with this algorithm
     *
     * <p>
     * <strong>NOTE: getRobot() CAN ONLY BE CALLED WITHIN the next() method or any other method that will be called AFTER the next() method has been called.</strong>
     * <br/>Attempting to call it within the constructor or any method that runs prior the next() method will cause NullPointerException.
     * </p>
     *
     * @return the robot instance that associated with this algorithm
     */
    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
        robot.getGraphicalDisplay().getBottomLayers().clear();
        for (DisplayAdapter displayAdapter : displayAdapters) {
            robot.getGraphicalDisplay().insertBottomLayer(displayAdapter);
        }

    }

    public void registerDisplayAdapter(DisplayAdapter displayAdapter) {
        this.displayAdapters.add(displayAdapter);
        if (this.robot != null && this.robot.getGraphicalDisplay() != null && this.robot.getGraphicalDisplay().getBottomLayers() != null) {
            robot.getGraphicalDisplay().insertBottomLayer(displayAdapter);
        }
    }

}
