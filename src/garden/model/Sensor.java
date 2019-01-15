package garden.model;


import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Sensor helps robots observe world around them.
 * <br/>
 * <br/>
 * It is in charge of converting objects in global scale to local scale for robot's distributed computer to compute.
 * <br/>
 * <br/>
 * For instance:
 * <br/>
 * It converts other robots' location in global coordinating system to it's own robot's local coordinating system.
 * <br/>
 * It also convert it's robot's local location to global location
 */
public class Sensor {

    private List<Robot> globalRobots;

    private Robot robot;

    public Sensor(Robot robot) {
        this.robot = robot;
        this.globalRobots = new ArrayList<>();
    }

    public Point convertToGlobal(Point point) {

        double x = point.getX() + robot.getPositionX();

        double y = point.getY() + robot.getPositionY();

        Point result = new Point();

        result.setLocation(x, y);

        return result;

    }

    public Point convertToLocal(Point point) {

        double x = point.getX() - robot.getPositionX();

        double y = point.getY() - robot.getPositionY();

        Point result = new Point();

        result.setLocation(x, y);

        return result;

    }


    /**
     * Test if a given robot is within the vision
     * @param point the point that represent the global coordinate of the robot to test
     * @return
     */
    public boolean isWithinVision(Point point) {
        return distance(point.getX(), point.getY()) <= robot.getGraphicalDisplay().getRobotVision().getRadius();
    }

    private double distance(double rx, double ry) {
        double diffx = (double) 0 - rx;
        double diffy = (double) 0 - ry;
        return Math.sqrt(Math.pow(diffx, 2) + Math.pow(diffy, 2));
    }

    /**
     * Convert all the global robots' coordinate into local and remove all the robots that is outside of the vision of current robot.
     * @return
     */
    public List<Robot> getAllVisibleRobotsInLocalScale(){
        ArrayList<Robot> localRobotsList = new ArrayList<>(); //special comment:when init, the localRobotsLisy has size 0 because the globalRobots will only be set when the "next" btn in ControlPanelController has been clicked

        //deep copy (partially): Ensure the modification, especially for location on the localRobotList will not affect the globalRobotsList
        for (Robot robot : globalRobots) {
//            Circle graphicalDisplay = new Circle(robot.getGraphicalDisplay().getRadius(), robot.getGraphicalDisplay().getFill());
//            graphicalDisplay.setTranslateX(robot.getGraphicalDisplay().getTranslateX());
//            graphicalDisplay.setTranslateY(robot.getGraphicalDisplay().getTranslateY());
//            Robot newRobotInstance = new Robot(graphicalDisplay, robot.getSensor().vision, robot.getLog());
//            newRobotInstance.setAlgorithm(robot.getAlgorithm());
//            newRobotInstance.setSensor(this);
            Robot newRobotInstance = robot.deepCopy();
            localRobotsList.add(newRobotInstance);
        }

        //remove all robots outside of the vision
        Iterator<Robot> iterator1 = localRobotsList.iterator();
        while (iterator1.hasNext()) {
            Robot curr = iterator1.next();
            if (!isWithinVision(convertToLocal(curr.getPosition()))) {
                iterator1.remove();
            }
        }

        Iterator<Robot> iterator2 = localRobotsList.iterator();
        while (iterator2.hasNext()) {
            Robot curr = iterator2.next();
            double x = curr.getPositionX();
            double y = curr.getPositionY();
            Point point = new Point();
            point.setLocation(x, y);
            Point localPoint = convertToLocal(point);
            System.out.println(localPoint.getX() + ", " + localPoint.getY());
            curr.moveTo(localPoint.getX(), localPoint.getY());
//            curr.getGraphicalDisplay().setTranslateX(localPoint.x);
//            curr.getGraphicalDisplay().setTranslateY(localPoint.y);
        }
        return localRobotsList;//should use deep copy
    }

    public List<Robot> getGlobalRobots() {
        return globalRobots;
    }

    public void setGlobalRobots(List<Robot> globalRobots) {
        this.globalRobots = globalRobots;
    }

}
