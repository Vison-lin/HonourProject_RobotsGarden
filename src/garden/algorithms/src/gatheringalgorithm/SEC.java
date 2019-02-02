package garden.algorithms.src.gatheringalgorithm;

import garden.algorithms.GatheringAlgorithm;
import garden.algorithms.GatheringGoToSec;
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

    private Algorithm algorithm;

    private GatheringAlgorithm goToCenter;

    private GatheringGoToSec  goToSec;

    private String tag;


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

    public void setAlgorithm(Algorithm algorithm,String tag) {
        this.tag=tag;
        if(tag.equals("gotosec")){
            goToSec =(GatheringGoToSec)algorithm;
            this.algorithm = goToSec;
        }else if(tag.equals("gotocenter")){
            goToCenter =(GatheringAlgorithm) algorithm;
            this.algorithm = goToCenter;

        }
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
        Point2D.Double point = new Point2D.Double();
        if(tag.equals("gotosec")){
            point = goToSec.calculateSEC(new ArrayList<>(local), algorithm.getRobot().getVision());
        }else if(tag.equals("gotocenter")){
            point = goToCenter.calculateSEC(new ArrayList<>(local), algorithm.getRobot().getVision());
        }
        point = algorithm.getRobot().getSensor().convertToGlobal(point);
        setDisplayPattern(new Circle(radius,Color.LIGHTGREEN));
        moveTo(point.x, point.y);
    }
}
