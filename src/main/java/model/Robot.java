package model;


import algorithms.DefaultAlgorithm;
import controller.garden.GardenController;
import core.AlgorithmClassLoader;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;

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
     *  The distance of moving of robot.
     */
    private double unit;

    /**
     *  The unit number of robot which control the amount of the step of moiving.
     */
    private double step;

    /**
     *  Check whether unit is a random number.
     */
    private boolean randomUnit;

    /*
     *  The coordinate that represent the robot position: in Point2D.Double form
     */
    private Point2D.Double coordinate;


    /**
     * Create a new robot object. It will be positioned in (0, 0)
     * @param graphicalDisplay the Circle object which represent this robot on the screen
     */

    public Robot(RobotGraphicalDisplay graphicalDisplay) {
        this.graphicalDisplay = graphicalDisplay;
        this.sensor = new Sensor(this);
        this.algorithm = new DefaultAlgorithm();
        this.unit = Double.POSITIVE_INFINITY;
        this.coordinate = new Point2D.Double(0, 0);
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
        //Vector vector = new Vector(getPosition(), end);
        double norm = calculateNorm(getPosition(),end);
        if(isRandomUnit()){
            Random ran = new Random();
            int num = (int) (norm/ graphicalDisplay.getRobotBody().getRadius());
            if (num >= 1) {
                this.unit = graphicalDisplay.getRobotBody().getRadius() * (1 + ran.nextInt(num));
            }
        }
        if(norm<= unit){
            coordinate = new Point2D.Double(x, y);
            Point2D.Double positionForGraphicalDisplay = GardenController.adjustCoordinate(coordinate);
            graphicalDisplay.moveTo(positionForGraphicalDisplay);
        }else{
            end = resize(norm,getPosition(),end);
            coordinate = new Point2D.Double(end.getX(), end.getY());
            Point2D.Double positionForGraphicalDisplay = GardenController.adjustCoordinate(coordinate);
            graphicalDisplay.moveTo(positionForGraphicalDisplay);
        }

    }



    /**
     * Resizes a Vector of norm "x" to a norm of "newNorm" (keeping the same orientation)
     *
     * @param newNorm the new norm of the vector
     * @param p1 Start point of the vector
     * @param p2 End point of the vector
     */
    public Point2D.Double resize(double newNorm,Point2D.Double p1, Point2D.Double p2){

        double deltaX = p2.getX() - p1.getX();
        double deltaY = p2.getY() - p1.getY();
        double norm = Math.sqrt(Math.pow(deltaX,2)+Math.pow(deltaY,2));

        Point2D.Double res = new Point2D.Double();
        res = p2;
        if(norm!=newNorm){
            double newDeltaX = deltaX*newNorm/norm;
            double newDeltaY = deltaY*newNorm/norm;
            Point2D.Double newEnd= new Point2D.Double();
            newEnd.setLocation(p1.getX()+newDeltaX,p1.getY()+newDeltaY);
            res = newEnd;
            return res;
        }
        return res;

    }

    /**
     *  This method is used to calculate the distance between two points.
     *
     * @param p1
     * @param p2
     * @return
     */
    private double calculateNorm(Point2D.Double p1, Point2D.Double p2){
        double deltaX = p2.getX() - p1.getX();
        double deltaY = p2.getY() - p1.getY();
        double norm = Math.sqrt(Math.pow(deltaX,2)+Math.pow(deltaY,2));

        return norm;
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
        Point2D.Double point = algorithm.next(this.sensor.getAllVisibleRobotsInLocalScale());
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
        return coordinate.getX();
//        return graphicalDisplay.getRobotPosition().getTranslateX();
    }

    /**
     * Get the y coordinate of robot's position
     * Note that this is the same if you get the graphicalDisplay (Circle) first and then call getTranslateY().
     *
     * @return the y of the robot's position
     */
    public double getPositionY() {
        return coordinate.getY();
//        return graphicalDisplay.getRobotPosition().getTranslateY();
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
        return coordinate;
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

    public boolean isRandomUnit() {
        return randomUnit;
    }

    public void setRandomUnit(boolean randomUnit) {
        this.randomUnit = randomUnit;
//        System.out.println("random:" +randomUnit);
    }

    public double getStep() {
        return step;
    }

    public void setUnit(Double unit){
        this.step = unit;
        this.unit = graphicalDisplay.getRobotBody().getRadius()*unit;
    }

    /**
     * Deep copy the robot object. Note that it does not deep copy neither the algorithm that assigned to this robot nor the global robot list that passed into the sensor.
     *
     * @return the new deepCopied Robot Object.
     */
    public Robot deepCopy() throws ClassNotFoundException {
        RobotGraphicalDisplay newRobotGraphicalDisplay = this.graphicalDisplay.deepCopy();
        Robot newRobot = new Robot(newRobotGraphicalDisplay);
        Sensor newSensor = new Sensor(newRobot);
        newRobot.setTag(this.tag);
        newRobot.moveTo(this.coordinate.getX(), this.coordinate.getY());
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
        this.getGraphicalDisplay().cleanBottomLayers();//todo FRED: why change color of position will change directly?
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
