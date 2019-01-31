package garden.core;

import javafx.scene.shape.Shape;

public abstract class DisplayAdapter {

    private Shape displayPattern;

    private boolean isVisible;

    private String componentTag;

    public DisplayAdapter(Shape displayPattern, String componentTag) {
        this.displayPattern = displayPattern;
        this.isVisible = false;
        this.componentTag = componentTag;
    }

    public Shape getDisplayPattern() {
        return this.displayPattern;
    }

    public void setDisplayPattern(Shape displayPattern) {
        this.displayPattern = displayPattern;
    }

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

    /**
     * Method to be called when user clicked the corresponding trigger on the robot toggle.
     * <br/>
     * By default, it is used to trigger the visibility of robot only.
     * <br/>
     * However, it can also be used to do action between steps (between click next button). For example, one can use this method to recompute and move robot to a new position by:
     * <br/>
     * <br/>
     * public class mAdapter extends DisplayAdapter {
     * <br/>
     * &nbsp&nbsp&nbsp&nbsp     private Random random = new Random();
     * <br/>
     * <br/>
     * &nbsp&nbsp&nbsp&nbsp     private Algorithm algorithm;
     * <br/>
     * <br/>
     * &nbsp&nbsp&nbsp&nbsp     public mAdapter() {
     * <br/>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp    //create a new AdditionalAdapter and inited it with shape Circle and tag "Random move this robot".
     * <br/>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp    super(new Circle(27, Color.GRAY), "Random move this robot");
     * <br/>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp    //make this adapter's shape invisible.
     * <br/>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp    setVisible(false);
     * <br/>&nbsp&nbsp&nbsp&nbsp }
     * <br/>
     * <br/>&nbsp&nbsp&nbsp&nbsp //Customized method to pass the algorithm into its adapter
     * <br/>&nbsp&nbsp&nbsp&nbsp public void setAlgorithm(Algorithm algorithm) {
     * <br/>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp    this.algorithm = algorithm;
     * <br/>&nbsp&nbsp&nbsp&nbsp }
     * <br/>
     * <br/>&nbsp&nbsp&nbsp&nbsp //Core method, to move the robot to a random position
     * <br/>&nbsp&nbsp&nbsp&nbsp @Override
     * <br/>&nbsp&nbsp&nbsp&nbsp public void update() {
     * <br/>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp    moveTo(algorithm.getRobot().getPositionX(), algorithm.getRobot().getPositionY());
     * <br/>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp    algorithm.getRobot().moveTo(random.nextInt(150), random.nextInt(150));
     * <br/>&nbsp&nbsp&nbsp&nbsp }
     * <br/>
     * }
     */
    public void update() {
        setVisible(!isVisible);
    }

    /**
     *
     * Move the shape to new location
     *
     * @param x the new x position
     * @param y the new y position
     */
    public void moveTo(double x, double y) {
        getDisplayPattern().setTranslateX(x);
        getDisplayPattern().setTranslateY(y);
    }
}
