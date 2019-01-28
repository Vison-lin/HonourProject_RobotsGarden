package garden.core;

import javafx.scene.shape.Shape;

public abstract class DisplayComponent {

    private Shape displayPattern;

    public DisplayComponent(Shape displayPattern) {
        this.displayPattern = displayPattern;
    }

    public Shape getDisplayPattern() {
        return this.displayPattern;
    }

    public void setDisplayPattern(Shape displayPattern) {
        this.displayPattern = displayPattern;
    }

    public abstract void moveTo(double x, double y);

}
