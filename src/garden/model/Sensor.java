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

    public Sensor(double vision, double globalX, double globalY, List<Robot> globalRobots) {
        this.vision = vision;
        this.globalX = globalX;
        this.globalY = globalY;
        this.globalRobots = globalRobots;
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

    public boolean isWithinVision(Robot robot){
        return distance(this.globalX, this.globalY, robot.getPositionX(), robot.getPositionY())<=vision;
    }

    private double distance(double r1x, double r1y, double r2x, double r2y){
        double diffx = r2x - r1x;
        double diffy = r2y - r1y;
        return Math.sqrt(Math.pow(diffx, 2) + Math.pow(diffy, 2));
    }

    public List<Robot> getAllVisibleRobotsInLocalScale(){
        List<Robot> localRobotsList = new ArrayList<>();
        Collections.copy(localRobotsList, globalRobots);
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

}
