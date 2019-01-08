package garden.model;

import java.awt.*;

public class Sensor {

    private double vision;

    private double globalX;

    private double globalY;

    public Sensor(double vision, double globalX, double globalY) {
        this.vision = vision;
        this.globalX = globalX;
        this.globalY = globalY;
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
}
