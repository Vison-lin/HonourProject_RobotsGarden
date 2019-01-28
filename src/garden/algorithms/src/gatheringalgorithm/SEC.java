package garden.algorithms.src.gatheringalgorithm;

import garden.core.DisplayComponent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SEC extends DisplayComponent {

    public SEC() {
        super(new Circle(50, Color.DARKGREY));
        System.out.println("SEC Created");
    }

    @Override
    public void moveTo(double x, double y) {
        getDisplayPattern().setTranslateX(x);
        getDisplayPattern().setTranslateY(y);
    }
}
