package garden.core;

import javafx.scene.shape.Shape;

public abstract class DisplayComponent {

    private Shape displayPattern;

    private boolean isVisible;

    private String componentTag;

    public DisplayComponent(Shape displayPattern) {
        this.displayPattern = displayPattern;
        this.isVisible = false;
    }

    public Shape getDisplayPattern() {
        return this.displayPattern;
    }

    public void setDisplayPattern(Shape displayPattern) {
        this.displayPattern = displayPattern;
    }

    public abstract void moveTo(double x, double y);

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public String getComponentTag() {
        return componentTag;
    }

    public void setComponentTag(String componentTag) {
        this.componentTag = componentTag;
    }

    public void update() {
    }
}
