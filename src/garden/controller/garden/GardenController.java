package garden.controller.garden;

import garden.model.Robot;
import garden.model.Sensor;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;

/**
 * The controller of the garden.fxml
 * The Garden is the window that contains all the robots
 */
public class GardenController extends VBox {

    /**
     * The size (radius) of the robot that displays on the screen. The default value is 21.
     */
    private static double ROBOT_SIZE = 21;
    /**
     * The size of the screenSize.
     * Note that the screen has fixed size, so we achieve the different resolutions (width) by multiple this rate
     */
    private static double screenSizeMultiplierWidth = 1;
    /**
     * The size of the screenSize.
     * Note that the screen has fixed size, so we achieve the different resolutions (height) by multiple this rate
     */
    private static double screenSizeMultiplierHeight = 1;
    /**
     * The garden instance
     */
    @FXML
    private Pane garden;

    public GardenController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../view/garden.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        robotsInitBooster();

    }

    /**
     * Get the size of the robots that display on the screen
     *
     * @return size of the robots
     */
    public static double getRobotSize() {
        return ROBOT_SIZE;
    }

    /**
     * Set the size of the robots that display on the screen
     *
     * @param robotSize size of the robots. Throw IllegalArgumentException if the size is not bigger than 0.
     */
    public static void setRobotSize(double robotSize) {
        if (robotSize <= 0) {
            throw new IllegalArgumentException("The robot size must bigger than 0");
        }
        ROBOT_SIZE = robotSize;
    }

    /**
     * Get the rate of the screen size
     *
     * @return the rate of the size
     */
    public static double getScreenSizeMultiplierWidth() {
        return screenSizeMultiplierWidth;
    }

    /**
     * Set the rate of the screen size
     *
     * @return the rate of the size
     */
    public static void setScreenSizeMultiplierWidth(double screenSizeMultiplierWidth) {
        GardenController.screenSizeMultiplierWidth = screenSizeMultiplierWidth;
    }

    /**
     * Get the rate of the screen size
     *
     * @return the rate of the size
     */
    public static double getScreenSizeMultiplierHeight() {
        return screenSizeMultiplierHeight;
    }

    /**
     * Set the rate of the screen size
     *
     * @return the rate of the size
     */
    public static void setScreenSizeMultiplierHeight(double screenSizeMultiplierHeight) {
        GardenController.screenSizeMultiplierHeight = screenSizeMultiplierHeight;
    }

    /**
     * Init the robots: add on click to the pane(garden) so where ever click the pane, a new robots will be created.
     */
    private void robotsInitBooster() {
        garden.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Robot robot = robotGenerator(event);
                garden.getChildren().add(robot.getGraphicalDisplay());
                System.out.println("x: " + event.getX() * screenSizeMultiplierWidth + " y: " + event.getY() * screenSizeMultiplierHeight);
            }
        });
    }

    /**
     * @param event Generate the robots based on the mouseEvent
     * @return return a Circle that represent the robot.
     */
    private Robot robotGenerator(MouseEvent event) {
        Robot robot = new Robot(new Circle(ROBOT_SIZE, Color.BLACK), new Sensor());
        robot.moveTo(event.getX(), event.getY());
        return robot;
    }
}
