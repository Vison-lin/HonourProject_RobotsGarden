package model;


import java.awt.*;
import java.awt.geom.Point2D;
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

    public Point.Double convertToGlobal(Point2D.Double point) {
        //DecimalFormat nf = new DecimalFormat("#.000000"); //keep the fraction digit to 6 number
        double x = point.getX() + robot.getPositionX();
       // x=Double.valueOf(nf.format(x));

        double y = point.getY() + robot.getPositionY();
       // y=Double.valueOf(nf.format(y));
        Point2D.Double result = new Point2D.Double();

        result.setLocation(x, y);

        return result;

    }

    public Point2D.Double convertToLocal(Point2D.Double point) {
        //DecimalFormat nf = new DecimalFormat("#.000000"); //keep the fraction digit to 6 number
        double x = point.getX() - robot.getPositionX();
       // x=Double.valueOf(nf.format(x));
        double y = point.getY() - robot.getPositionY();
       // y=Double.valueOf(nf.format(y));
        Point2D.Double result = new Point2D.Double();

        result.setLocation(x, y);

        return result;

    }


    /**
     * Test if a given robot is within the vision
     * @param point the point that represent the global coordinate of the robot to test
     * @return
     */
    public boolean isWithinVision(Point2D.Double point) {
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
            Robot newRobotInstance = null;
            try {
                newRobotInstance = robot.deepCopy();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.exit(0);
            }
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
            Point2D.Double point = new Point2D.Double();
            point.setLocation(x, y);
            Point2D.Double localPoint = convertToLocal(point);
            curr.setUnit(Double.POSITIVE_INFINITY);//to ensure to move to the desert position: when having unit, robot cannot move to desert position. No impact for global list since it is just a local (deep) copy
            curr.moveTo(localPoint.getX(), localPoint.getY());
        }
        return localRobotsList;//should use deep copy
    }

    public List<Robot> getGlobalRobots() {
        return globalRobots;
    }

    public void setGlobalRobots(List<Robot> globalRobots) {
        this.globalRobots = globalRobots;
    }

    public static Point2D.Double c(Point2D.Double mDouble){
        Point2D.Double returnValue = new Point2D.Double();
        returnValue.setLocation(mDouble.getX(), mDouble.getY());
        return returnValue;
    }

}
