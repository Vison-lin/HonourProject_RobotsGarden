package garden.model;


import javafx.scene.shape.Circle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Sensor {

    private double vision;

//    private double globalX;
//
//    private double globalY;

    private List<Robot> globalRobots;

    private Robot robot;

    public Sensor(Robot robot, double vision, double globalX, double globalY) {
        this.robot = robot;
        this.vision = vision;
//        this.globalX = globalX;
//        this.globalY = globalY;
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

    public double getVision() {
        return vision;
    }

    public void setVision(double vision) {
        this.vision = vision;
    }

//    public double getGlobalX() {
//        return globalX;
//    }
//
//    public void setGlobalX(double globalX) {
//        this.globalX = globalX;
//    }
//
//    public double getGlobalY() {
//        return globalY;
//    }
//
//    public void setGlobalY(double globalY) {
//        this.globalY = globalY;
//    }

    /**
     * Test if a given robot is within the vision
     * @param point the point that represent the global coordinate of the robot to test
     * @return
     */
    public boolean isWithinVision(Point point) {
        robot.getLog().addToLog("The distance between these two robots is: " + distance(point.getX(), point.getY(), 0, 0) + ". Compare to its vision" + vision);
        return distance(point.getX(), point.getY(), 0, 0) <= vision;
    }

    private double distance(double r1x, double r1y, double r2x, double r2y){
        double diffx = r2x - r1x;
        double diffy = r2y - r1y;
        return Math.sqrt(Math.pow(diffx, 2) + Math.pow(diffy, 2));
    }

    /**
     * Convert all the global robots' coordinate into local and remove all the robots that is outside of the vision of current robot.
     * @return
     */
    public List<Robot> getAllVisibleRobotsInLocalScale(){
        ArrayList<Robot> localRobotsList = new ArrayList<>(); //special comment:when init, the localRobotsLisy has size 0 because the globalRobots will only be set when the "next" btn in ControlPanelController has been clicked

        //deep copy: Ensure the modification, especially for location on the localRobotList will not affect the globalRobotsList
        for (Robot robot : globalRobots) {
            Circle graphicalDisplay = new Circle(robot.getGraphicalDisplay().getRadius(), robot.getGraphicalDisplay().getFill());
            graphicalDisplay.setTranslateX(robot.getGraphicalDisplay().getTranslateX());
            graphicalDisplay.setTranslateY(robot.getGraphicalDisplay().getTranslateY());
            Robot newRobotInstance = new Robot(graphicalDisplay, robot.getSensor().vision, robot.getLog());
            newRobotInstance.setAlgorithm(robot.getAlgorithm());
            newRobotInstance.setSensor(this);
            localRobotsList.add(newRobotInstance);
        }

        //remove all robots outside of the vision
        Iterator<Robot> iterator1 = localRobotsList.iterator();
        while (iterator1.hasNext()) {
            Robot curr = iterator1.next();
            robot.getLog().addToLog("Now, robot " + robot.getTag() + "at position (" + this.robot.getPositionX() + ", " + this.robot.getPositionY() + ") is determining whether the robot " + curr.getTag() + "at position (" + curr.getPositionX() + ", " + curr.getPositionY() + ") is within its vision");
            if (!isWithinVision(convertToLocal(curr.getPosition()))) {
                iterator1.remove();
                robot.getLog().addToLog("SO: The target robot " + curr.getTag() + "is not in its vision, REMOVED");
            } else {
                robot.getLog().addToLog("S0: The target robot " + curr.getTag() + "is in its vision, KEEP");
            }
        }

        Iterator<Robot> iterator2 = localRobotsList.iterator();
        while (iterator2.hasNext()) {
            Robot curr = iterator2.next();
            double x = curr.getGraphicalDisplay().getTranslateX();
            double y = curr.getGraphicalDisplay().getTranslateY();
            Point point = new Point();
            point.setLocation(x, y);
            Point localPoint = convertToLocal(point);
            curr.getGraphicalDisplay().setTranslateX(localPoint.x);
            curr.getGraphicalDisplay().setTranslateX(localPoint.y);
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
