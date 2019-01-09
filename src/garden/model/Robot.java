package garden.model;

import garden.core.Algorithm;
import garden.core.llibrary.DefaultAlgorithm;
import javafx.scene.shape.Circle;

import java.awt.*;
import java.util.List;

/**
 * Class that represent Robot object.
 * <p>
 * A Robot should have a circle to represent itself in JavaFX and a Sensor to discover its surroundings.
 */
public class Robot {

    /**
     * The name of the robot
     */
    private String tag;

    /**
     * The Log specific to this robot
     */
    private RobotLog log;

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
     * The algorithm associate with this robot. Note that the default value is DefaultAlgorithm, which do nothing.
     */
    private Algorithm algorithm = new DefaultAlgorithm(this);

    /**
     * Create a new robot object
     *  @param graphicalDisplay the Circle object which represent this robot on the screen
     * @param vision           the vision of the robot. This value stores in the built-in sensor object.
     * @param log
     */
    public Robot(Circle graphicalDisplay, double vision, RobotLog log) {
        this.graphicalDisplay = graphicalDisplay;
        this.log = log;
        this.sensor = new Sensor(this, vision, graphicalDisplay.getTranslateX(), graphicalDisplay.getTranslateY());
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
     * The robot (reference) will update after called this method.
     */
    public void next(List<Robot> robots) {
        sensor.setGlobalRobots(robots);//set curr gobal robots to sensor
        Point point = algorithm.next(robots);
        Point globalPoint = this.sensor.convertToGlobal(point);
        moveTo(globalPoint.x, globalPoint.y);
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

    /**
     * Get the current position (Global)
     *
     * @return the x and y values of current position in a Point object where the first is x and the second is y.
     */
    public Point getPosition(){
        Point point = new Point();
        point.setLocation(getPositionX(), getPositionY());
        return point;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public RobotLog getLog() {
        return log;
    }

    public void setLog(RobotLog log) {
        this.log = log;
    }
}
