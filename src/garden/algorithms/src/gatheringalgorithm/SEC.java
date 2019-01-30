package garden.algorithms.src.gatheringalgorithm;

import garden.core.DisplayComponent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SEC extends DisplayComponent {

    private double centreX;

    private double centreY;

    private double radius;

    public SEC() {
        super(new Circle(50, Color.DARKGREY));
        System.out.println("SEC Created");
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

    public void move(){
        getDisplayPattern().setTranslateX(centreX);
        getDisplayPattern().setTranslateY(centreY);
    }

    @Override
    public void moveTo(double x, double y) {
        getDisplayPattern().setTranslateX(x);
        getDisplayPattern().setTranslateY(y);
    }
}
