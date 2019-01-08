package garden.model;


import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Sensor {

    private double vision;

    private double globalX;

    private double globalY;

    private List<Robot> globalRobots;

    public Sensor(double vision, double globalX, double globalY) {
        this.vision = vision;
        this.globalX = globalX;
        this.globalY = globalY;
        this.globalRobots = new ArrayList<>();
    }

    public Point convertToGlobal(Point point) {

        double x = point.getX() + globalX;

        double y = point.getY() + globalY;

        Point result = new Point();

        result.setLocation(x, y);

        return result;

    }

    public Point convertToLocal(Point point) {

        double x = point.getX() - globalX;

        double y = point.getY() - globalY;

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

    public double getGlobalX() {
        return globalX;
    }

    public void setGlobalX(double globalX) {
        this.globalX = globalX;
    }

    public double getGlobalY() {
        return globalY;
    }

    public void setGlobalY(double globalY) {
        this.globalY = globalY;
    }

    /**
     * Test if a given robot is within the vision
     * @param robot the robot to test
     * @return
     */
    public boolean isWithinVision(Robot robot){
        System.out.println(distance(this.globalX, this.globalY, robot.getPositionX(), robot.getPositionY()));
        return distance(this.globalX, this.globalY, robot.getPositionX(), robot.getPositionY())<=vision;
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
        ArrayList<Robot> localRobotsList = new ArrayList<>(globalRobots);
        for (Robot robot:localRobotsList){
            if(!isWithinVision(robot)){
                localRobotsList.remove(robot);
            }
        }
        for (Robot robot:localRobotsList){
            double x = robot.getGraphicalDisplay().getTranslateX();
            double y = robot.getGraphicalDisplay().getTranslateY();
            Point point = new Point();
            point.setLocation(x, y);
            Point localPoint = convertToLocal(point);
            robot.getGraphicalDisplay().setTranslateX(localPoint.x);
            robot.getGraphicalDisplay().setTranslateX(localPoint.y);
        }
        return localRobotsList;
    }

    public List<Robot> getGlobalRobots() {
        return globalRobots;
    }

    public void setGlobalRobots(List<Robot> globalRobots) {
        this.globalRobots = globalRobots;
    }

}
