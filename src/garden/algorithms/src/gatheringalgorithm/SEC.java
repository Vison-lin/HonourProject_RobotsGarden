package garden.algorithms.src.gatheringalgorithm;

import garden.algorithms.GatheringAlgorithm;
import garden.core.Algorithm;
import garden.core.DisplayAdapter;
import garden.model.Robot;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class SEC extends DisplayAdapter {

    private double centreX;

    private double centreY;

    private double radius;

    private GatheringAlgorithm algorithm;

    public SEC() {
        super(new Circle(50, Color.DARKGREY), "SEC");
    }

    public void setCentreX(double centreX) {
        this.centreX = centreX;
    }

    public void setCentreY(double centreY) {
        this.centreY = centreY;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(GatheringAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public void update() {
        setVisible(!isVisible());
        List<Robot> local = new ArrayList<>();
        for (Robot robot : algorithm.getRobot().getSensor().getAllVisibleRobotsInLocalScale()) {
            try {
                local.add(robot.deepCopy());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        Point2D.Double point = algorithm.generateOneRobot(new ArrayList<>(local), algorithm.getRobot().getVision());
        point = algorithm.getRobot().getSensor().convertToGlobal(point);
        moveTo(point.x, point.y);
    }
}
