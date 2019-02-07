package garden.model;

import garden.algorithms.DefaultAlgorithm;
import garden.core.Algorithm;
import garden.core.AlgorithmClassLoader;
import garden.algorithms.src.gatheringalgorithm.Vector;

import java.awt.geom.Point2D;
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
     * The representation of the robot.
     * A robot is represented as a circle in the screen
     */
    private RobotGraphicalDisplay graphicalDisplay;

    /**
     * The sensor of the robot.
     * The sensor tells robots the surroundings
     */
    private Sensor sensor;

    /**
     * The algorithm associate with this robot. Note that the default value is DefaultAlgorithm, which do nothing.
     */
    private Algorithm algorithm;

    /**
     *  The unit number of robot which control the amount of the step of moiving
     */
    private double unit;


    /**
     * Create a new robot object. It will be positioned in (0, 0)
     * @param graphicalDisplay the Circle object which represent this robot on the screen
     */

    public Robot(RobotGraphicalDisplay graphicalDisplay) {
        this.graphicalDisplay = graphicalDisplay;
        this.sensor = new Sensor(this);
        this.algorithm = new DefaultAlgorithm();
        this.unit = Double.POSITIVE_INFINITY;
        algorithm.setRobot(this);
    }

    /**
     * Move the robot (its location, vision, and body) to the new position
     *
     * @param x the x coordinate of the new position
     * @param y the y coordinate of the new position
     */
    public void moveTo(double x, double y) {
        Point2D.Double end = new Point2D.Double();
        end.setLocation(x,y);
        Vector vector = new Vector(getPosition(),end);
        if(vector.getNorm()<= unit){
            graphicalDisplay.moveTo(x, y);
        }else{
            end = vector.resize(unit).getEnd();
            graphicalDisplay.moveTo(end.getX(), end.getY());

        }



    }

    /**
     *
     * The robot (reference) will run the algorithm and return the new position that it should go next.
     * <br/>
     * <br/>
     * Note that the robot will not move to the new location unless the moveTo method has been called.
     *
     * @param robots the list of global robots. In most cases, this is the snapshot of the current global robots. And all the robots should have the same snapshot at the same stage.
     * @return the new location that the robot is going to move.
     */
    public Point2D.Double next(List<Robot> robots) {
        sensor.setGlobalRobots(robots);//set curr global robots to sensor
//        algorithm.getRobot().getGraphicalDisplay().insertBottomLayer();
        Point2D.Double point = algorithm.next(robots);
        Point2D.Double globalPoint = this.sensor.convertToGlobal(point);


        return globalPoint;
    }

    /**
     * Get the robot's screen representation
     *
     * @return return the robot's representation (Circle)
     */
    public RobotGraphicalDisplay getGraphicalDisplay() {
        return graphicalDisplay;
    }

    /**
     * Set the robot's screen representation
     *
     * @param graphicalDisplay the new representation of the robot
     */
    public void setGraphicalDisplay(RobotGraphicalDisplay graphicalDisplay) {
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
        return graphicalDisplay.getRobotPosition().getTranslateX();
    }

    /**
     * Get the y coordinate of robot's position
     * Note that this is the same if you get the graphicalDisplay (Circle) first and then call getTranslateY().
     *
     * @return the y of the robot's position
     */
    public double getPositionY() {
        return graphicalDisplay.getRobotPosition().getTranslateY();
    }

    /**
     * Get the current position, in global coordinate scale.
     *
     * <br/>
     * <strong>Note: It will return a new Point2D.Double object. So any modification on the returned object will have no effect on robot position. To move the robot, please use moveTo() method.</strong>
     * <br/>
     *
     * @return the NEW point2D.Double object represent the coordinate.
     */
    public Point2D.Double getPosition() {
        Point2D.Double point = new Point2D.Double();
        point.setLocation(getPositionX(), getPositionY());
        return point;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
        this.algorithm.setRobot(this);
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public  double getUnit(){return  unit;}

    public void setUnit(Double unit){this.unit = graphicalDisplay.getRobotBody().getRadius()*unit;}

    /**
     * Deep copy the robot object. Note that it does not deep copy neither the algorithm that assigned to this robot nor the global robot list that passed into the sensor.
     *
     * @return the new deepCopied Robot Object.
     */
    public Robot deepCopy() throws ClassNotFoundException {
        RobotGraphicalDisplay newRobotGraphicalDisplay = this.graphicalDisplay.deepCopy();
        Robot newRobot = new Robot(newRobotGraphicalDisplay);
        Sensor newSensor = new Sensor(newRobot);
        newRobot.setSensor(newSensor);
        newRobot.setUnit(this.getUnit());
        //deep copy algorithm: create a new algorithm instance and return it.
        Algorithm algorithm = AlgorithmClassLoader.getAlgorithmInstanceByName(this.algorithm.getClass().getSimpleName());
        algorithm.setRobot(newRobot);
        newRobot.setAlgorithm(algorithm);
        return newRobot;
    }

    public double getVision() {
        return this.getGraphicalDisplay().getRobotVision().getRadius();
    }

    /**
     * <strong>ENSURE THE ROBOT IS OBLIVIOUS</strong>
     * <br/>
     * <br/>
     * <strong>CORE METHOD OF THIS LIBRARY </strong>
     * <br/>
     * <strong>Clean everything that happened in the previous step: to simulate DISTRIBUTIVE COMPUTING</strong>
     */
    public void iForgot() {
        this.getGraphicalDisplay().cleanBottomLayers();//todo why change color of position will change directly?
        //deep copy algorithm: to ensure algorithm itself cannot store historical data. Do this later to ensure each time the algorithm's constructor can be called.
        try {
            Algorithm algorithm = AlgorithmClassLoader.getAlgorithmInstanceByName(this.getAlgorithm().getClass().getSimpleName());

            this.setAlgorithm(algorithm);
            algorithm.setRobot(this);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }

    }
}
