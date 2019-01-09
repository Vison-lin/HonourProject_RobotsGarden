package garden.controller.garden;

import garden.model.Robot;
import garden.model.RobotLog;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The controller of the garden.fxml
 * The Garden is the window that contains all the robots
 */
public class GardenController extends VBox {

    /**
     * The size (radius) of the robot that displays on the screen. The default value is 21.
     */
    private static double ROBOT_SIZE = 9;

    private List<Robot> robots = Collections.synchronizedList(new ArrayList<>());

    private Robot selectedRobots = null;

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

    public void updateGarden() {
        garden.getChildren().removeAll(garden.getChildren());//remove all the element
        for (Robot robot : robots) {
            Circle graphicalDisplay = robot.getGraphicalDisplay();
            garden.getChildren().add(graphicalDisplay);
        }
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
     * get the list of robots.
     * @return the list of the robots
     */
    public List<Robot> getRobots() {
        return robots;
    }

    /**
     * Set the list of robots.
     * Note, one should avoid to use this method, instead, change the content of the robots directly.
     * @param robots the new list of the robots
     */
    public void setRobots(ArrayList<Robot> robots) {
        this.robots = robots;
    }

    /**
     * Init the robots: add on click to the pane(garden) so where ever click the pane, a new robots will be created.
     */
    private void robotsInitBooster() {
        //set onClickListener for creating robots
        garden.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {// add listener for left click
                    Robot robot = robotGenerator(event);
                    Circle robotGraphicalDisplay = robot.getGraphicalDisplay();
                    //set onClickListener for opening robot setting & displaying vision range
                    robotGraphicalDisplay.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getButton() == MouseButton.SECONDARY) {// for each of the btn that has added event, add one right click listener for it.
                                RobotSettingHelper robotSettingHelper = new RobotSettingHelper(robot);
                            } else if (event.getButton() == MouseButton.MIDDLE) {
                                Circle visionRange = new Circle(robot.getSensor().getVision(), Color.YELLOW);
                                visionRange.setTranslateX(robotGraphicalDisplay.getTranslateX());
                                visionRange.setTranslateY(robotGraphicalDisplay.getTranslateY());
                                garden.getChildren().add(visionRange);
                                selectedRobots = robot;
                            }
                        }
                    });
                    garden.getChildren().add(robotGraphicalDisplay);
                    robot.getLog().addToLog("The robot has been successfully created at the position x: " + event.getX() + " y: " + event.getY() + "!");
                    robots.add(robot);//add the robot into the robots list
                }
            }
        });
    }

    public Robot getSelectedRobots() {
        return selectedRobots;
    }

    /**
     * !@param event Generate the robots based on the mouseEvent
     * @return return a Circle that represent the robot.
     */
    private Robot robotGenerator(MouseEvent event) {
        Robot robot = new Robot(new Circle(ROBOT_SIZE, Color.BLACK), 50, new RobotLog("Robot Inited!"));
        robot.moveTo(event.getX(), event.getY());
        return robot;
    }
}
