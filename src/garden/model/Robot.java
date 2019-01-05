package garden.model;

import javafx.scene.shape.Circle;

/**
 * Class that represent Robot object.
 * <p>
 * A Robot should have a circle to represent itself in JavaFX and a Sensor to discover its surroundings.
 */
public class Robot {

    /**
     * The representation of the robot.
     * A robot is represented as a circle in the screen
     */
    private Circle graphicalDisplay;

    /**
     * The sensor of the robot.
     * The sensor tells robots the surroundings
     */
    private Sensor sensor;

    /**
     * Create a new robot object
     *
     * @param graphicalDisplay the Circle object which represent this robot on the screen
     * @param sensor           the Sensor object which is the sensor of this robot
     */
    public Robot(Circle graphicalDisplay, Sensor sensor) {
        this.graphicalDisplay = graphicalDisplay;
        this.sensor = sensor;
    }

    /**
     * Get the robot's screen representation
     *
     * @return return the robot's representation (Circle)
     */
    public Circle getGraphicalDisplay() {
        return graphicalDisplay;
    }

    /**
     * Set the robot's screen representation
     *
     * @param graphicalDisplay the new representation of the robot
     */
    public void setGraphicalDisplay(Circle graphicalDisplay) {
        this.graphicalDisplay = graphicalDisplay;
    }

    /**
     * Get the robot's sensor
     *
     * @return return the robot's sensor
     */
    public Sensor getSensor() {
        return sensor;
    }

    /**
     * Set the robot's screen representation
     *
     * @param sensor the new sensor of the robot
     */
    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    /**
     * Move the robot to the new position
     * Note that this is the same if you get the graphicalDisplay (Circle) first and then call setTranslateX(x) and setTranslateY(y) respectively.
     *
     * @param x the x coordinate of the new position
     * @param y the y coordinate of the new position
     */
    public void moveTo(double x, double y) {
        graphicalDisplay.setTranslateX(x);
        graphicalDisplay.setTranslateY(y);
    }

    /**
     * Get the x coordinate of robot's position
     * Note that this is the same if you get the graphicalDisplay (Circle) first and then call getTranslateX().
     *
     * @return the x of the robot's position
     */
    public double getPositionX() {
        return graphicalDisplay.getTranslateX();
    }

    /**
     * Get the y coordinate of robot's position
     * Note that this is the same if you get the graphicalDisplay (Circle) first and then call getTranslateY().
     *
     * @return the y of the robot's position
     */
    public double getPositionY() {
        return graphicalDisplay.getTranslateY();
    }

}
