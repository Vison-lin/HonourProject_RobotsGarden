package garden.model;

import garden.core.DisplayComponent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class RobotGraphicalDisplay {

    private Circle robotPosition;
    private Circle robotBody;
    private Circle robotVision;
    private Circle robotBorder;
    private boolean visionVisible;
    private List<DisplayComponent> layers;

    /**
     * init with the default value:
     * <br/>
     * For robot position: the default radius is 1, and the color is white
     * <br/>
     * For robot body: the default radius is 3, and the color is black
     * <br/>
     * For robot vision: the default radius is 9, and the color is light blue
     * <br/>
     * By default, the robot vision is invisible.
     */
    public RobotGraphicalDisplay() {
        this.robotPosition = new Circle(3, Color.WHITE);
        this.robotBody = new Circle(10, Color.BLACK);
        this.robotBorder = new Circle(11, Color.WHITE);
        this.robotVision = new Circle(100, Color.LIGHTBLUE);
        this.visionVisible = false;
        this.layers = new ArrayList<>();
    }

    /**
     * Init with the customized parameters
     *
     * @param robotPosition the precise position of the robot.
     * @param robotBody     the body of the robot. This should be set as having exact the same position as the robotPosition.
     * @param robotBorder   the border of the robot. This should be set as having 1 more radius than robotBody.
     * @param robotVision   the vision range of the robot. This should be set as having exact the same position as the robotPosition.
     * @param visionVisible the visibility of the robot vision.
     */
    public RobotGraphicalDisplay(Circle robotPosition, Circle robotBody, Circle robotBorder, Circle robotVision, boolean visionVisible) {
        if (robotBody == null || robotVision == null || robotPosition == null || robotBorder == null) {
            String errMsg = "All the arguments cannot be null: ";
            if (robotBody == null) {
                errMsg += "robotBody is null ";
            }
            if (robotVision == null) {
                errMsg += "robotvision is null ";
            }
            if (robotBorder == null) {
                errMsg += "robotBorder is null ";
            }
            if (robotPosition == null) {
                errMsg += "robotPosition is null ";
            }
            throw new IllegalArgumentException(errMsg);
        }
        if ((robotPosition.getTranslateX() != robotBody.getTranslateX()) || (robotPosition.getTranslateX() != robotVision.getTranslateX()) ||
                (robotPosition.getTranslateY() != robotBorder.getTranslateY()) || (robotPosition.getTranslateY() != robotVision.getTranslateY())) {
            String errMsg = "The robotPosition does not have the same position as set for robotBody, robotBorder or selectedRobotVision. All four of them must not be null and must always be in the same location: \n";
            errMsg += "The coordinate of robotPosition is: " + robotPosition.getTranslateX() + ", " + robotPosition.getTranslateY() + ";\n";
            errMsg += "The coordinate of robotBody is: " + robotBody.getTranslateX() + ", " + robotBody.getTranslateY() + ";\n";
            errMsg += "The coordinate of robotBorder is: " + robotBorder.getTranslateX() + ", " + robotBorder.getTranslateY() + ";\n";
            errMsg += "The coordinate of robotVision is: " + robotVision.getTranslateX() + ", " + robotVision.getTranslateY() + ".";

            throw new IllegalArgumentException(errMsg);
        }
        this.robotPosition = robotPosition;
        this.robotBody = robotBody;
        this.robotBorder = robotBorder;
        this.robotVision = robotVision;
        this.visionVisible = visionVisible;
        this.layers = new ArrayList<>();
    }

    /**
     * Since it represents the precise position of the robot, it is too small to click. So it is <strong>NOT RECOMMEND</strong> TO ADD ANY ON_CLICK_LISTENER to this. Use robotBody instead.
     *
     * @return the robot precise position.
     */
    public Circle getRobotPosition() {
        return robotPosition;
    }

    public void setRobotPosition(Circle robotPosition) {
        this.robotPosition = robotPosition;
    }

    /**
     * Since it represents the robot body, it is RECOMMEND TO ADD ON_CLICK_LISTENER to this.
     *
     * @return the graphical display that represent the robot's body
     */
    public Circle getRobotBody() {
        return robotBody;
    }

    public void setRobotBody(Circle robotBody) {
        this.robotBody = robotBody;
    }

    /**
     *
     * @return the graphical display that represent the robot's vision
     */
    public Circle getRobotVision() {
        return robotVision;
    }

    public void setRobotVision(double robotVision) {
        this.robotVision.setRadius(robotVision);
    }

    /**
     * return a boolean that represent whether the vision of the robot is visible or not
     *
     * @return true if is visible, false otherwise
     */
    public boolean isVisionVisible() {
        return visionVisible;
    }

    public void setVisionVisible(boolean visionVisible) {
        this.visionVisible = visionVisible;
    }

    /**
     * Move robotBody, robotPosition, and selectedRobotVision to the same position at the same time
     *
     * @param x the new x
     * @param y the new y
     */
    public void moveTo(double x, double y) {
        this.robotBody.setTranslateX(x);
        this.robotBody.setTranslateY(y);

        this.robotPosition.setTranslateX(x);
        this.robotPosition.setTranslateY(y);

        this.robotVision.setTranslateX(x);
        this.robotVision.setTranslateY(y);

        this.robotBorder.setTranslateX(x);
        this.robotBorder.setTranslateY(y);
    }
    /**
     *  Change the color of robot body
     *
     *  @param color the new color value
     */

    public void setColor(Paint color) {
        this.robotBody.setFill(color);
    }


    /**
     * toggle the visibility of the robot's vision.
     * <br/>
     * It's recommended to always use this method instead of setting the visibility directly.
     */
    public void toggleVisionVisible() {
        this.setVisionVisible(!this.isVisionVisible());
    }

    public RobotGraphicalDisplay deepCopy() {

        //deep copy robotPosition
        Circle newRobotPosition = new Circle(this.robotPosition.getRadius());
        double red1 = ((Color) this.robotPosition.getFill()).getRed();
        double green1 = ((Color) this.robotPosition.getFill()).getGreen();
        double blue1 = ((Color) this.robotPosition.getFill()).getBlue();
        double opacity1 = ((Color) this.robotPosition.getFill()).getOpacity();
        newRobotPosition.setTranslateX(this.robotPosition.getTranslateX());
        newRobotPosition.setTranslateY(this.robotPosition.getTranslateY());
        deepCopyFillHelper(newRobotPosition, red1, green1, blue1, opacity1);

        //deep copy robotBody
        Circle newRobotBody = new Circle(this.robotBody.getRadius());
        double red2 = ((Color) this.robotBody.getFill()).getRed();
        double green2 = ((Color) this.robotBody.getFill()).getGreen();
        double blue2 = ((Color) this.robotBody.getFill()).getBlue();
        double opacity2 = ((Color) this.robotBody.getFill()).getOpacity();
        newRobotBody.setTranslateX(this.robotBody.getTranslateX());
        newRobotBody.setTranslateY(this.robotBody.getTranslateY());
        deepCopyFillHelper(newRobotBody, red2, green2, blue2, opacity2);

        //deep copy robotBorder
        Circle newRobotBorder = new Circle(this.robotBorder.getRadius());
        double red3 = ((Color) this.robotBorder.getFill()).getRed();
        double green3 = ((Color) this.robotBorder.getFill()).getGreen();
        double blue3 = ((Color) this.robotBorder.getFill()).getBlue();
        double opacity3 = ((Color) this.robotBorder.getFill()).getOpacity();
        newRobotBorder.setTranslateX(this.robotBorder.getTranslateX());
        newRobotBorder.setTranslateY(this.robotBorder.getTranslateY());
        deepCopyFillHelper(newRobotBorder, red3, green3, blue3, opacity3);

        //deep copy selectedRobotVision
        Circle newRobotVision = new Circle(this.robotVision.getRadius());
        double red4 = ((Color) this.robotVision.getFill()).getRed();
        double green4 = ((Color) this.robotVision.getFill()).getGreen();
        double blue4 = ((Color) this.robotVision.getFill()).getBlue();
        double opacity4 = ((Color) this.robotVision.getFill()).getOpacity();
        newRobotVision.setTranslateX(this.robotVision.getTranslateX());
        newRobotVision.setTranslateY(this.robotVision.getTranslateY());
        deepCopyFillHelper(newRobotVision, red4, green4, blue4, opacity4);

        return new RobotGraphicalDisplay(newRobotPosition, newRobotBody, newRobotBorder, newRobotVision, this.visionVisible);

    }

    private void deepCopyFillHelper(Circle newRobotPosition, double red2, double green2, double blue2, double opacity2) {
        Color color2 = new Color(red2, green2, blue2, opacity2);
        newRobotPosition.setFill(color2);
    }

    public Circle getRobotBorder() {
        return robotBorder;
    }

    public void setRobotBorder(Circle robotBorder) {
        this.robotBorder = robotBorder;
    }

    public void insertBottomLayer(DisplayComponent displayComponent) {
        this.layers.add(displayComponent);
    }

    public List<DisplayComponent> getBottomLayers() {
        return this.layers;
    }

    public void cleanBottomLayers() {
        this.layers.clear();
    }
}
