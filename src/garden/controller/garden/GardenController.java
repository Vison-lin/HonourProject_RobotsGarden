package garden.controller.garden;

import garden.controller.controlpanel.ControlPanelFacade;
import garden.core.DisplayAdapter;
import garden.model.Robot;
import garden.model.RobotGraphicalDisplay;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
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

    private ControlPanelFacade controlPanelFacade;

    private GardenController gardenController = this;

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
        gardenMouseHoverListener();
        gardenMouseMoveOutListener();

    }

    public void updateGarden() {
        garden.getChildren().removeAll(garden.getChildren());//remove all the element
        List<Circle> robotsPosition = new ArrayList<>();
        List<Circle> robotsBodyAndBorder = new ArrayList<>();
        List<Circle> robotsVision = new ArrayList<>();
        List<List<Node>> listOfSetOfBottomLayers = new ArrayList<>();

        //get the index of the deepest bottom layer for all robots
        int deepestBottomLayer = 0;
        for (Robot robot : controlPanelFacade.getRobots()) {
            int currLayer = robot.getGraphicalDisplay().getBottomLayers().size();
            if (currLayer > deepestBottomLayer) {
                deepestBottomLayer = currLayer;
            }
        }

        //create list for each layer
        for (int i = 0; i < deepestBottomLayer; i++) {
            List<Node> currLayer = new ArrayList<>();
            listOfSetOfBottomLayers.add(currLayer);
        }

        //for each robot, insert the bottomLayer into the corresponding bottomLayer set
        for (int i = 0; i < deepestBottomLayer; i++) {
            for (Robot robot : controlPanelFacade.getRobots()) {
                DisplayAdapter currDisplayAdapter;
                try {
                    currDisplayAdapter = robot.getGraphicalDisplay().getBottomLayers().get(i);
                    if (currDisplayAdapter.isVisible()) {
                        listOfSetOfBottomLayers.get(i).add(currDisplayAdapter.getDisplayPattern());
                    }
                } catch (IndexOutOfBoundsException | NullPointerException ignored) {
                }//do nothing for the not-exist layer
            }
        }

        //add those layers into the garden display list
        for (int i = deepestBottomLayer - 1; i >= 0; i--) {
            garden.getChildren().addAll(listOfSetOfBottomLayers.get(i));
        }


        for (Robot robot : controlPanelFacade.getRobots()) {
            RobotGraphicalDisplay robotGraphicalDisplay = robot.getGraphicalDisplay();
            Circle robotPosition = robotGraphicalDisplay.getRobotPosition();
//            System.out.println("The drawn robot positioned at: " + robotPosition.getTranslateX() +","+robotPosition.getTranslateY());
            Circle robotBody = robotGraphicalDisplay.getRobotBody();
            Circle robotBorder = robotGraphicalDisplay.getRobotBorder();
            Circle robotVision = robotGraphicalDisplay.getRobotVision();
            if (robotGraphicalDisplay.isVisionVisible()) {
                robotsVision.add(robotVision);
            }
            robotsBodyAndBorder.add(robotBorder);
            robotsBodyAndBorder.add(robotBody);
            robotsPosition.add(robotPosition);
        }
        //ensure the vision is always under the border
        garden.getChildren().addAll(robotsVision);

        garden.getChildren().addAll(robotsBodyAndBorder);

        //ensure the position is always in front of anything
        garden.getChildren().addAll(robotsPosition);

        //remove unnecessary info (to ensure obliviousness <- KEY OF THE PROJECT)
//        for (Robot robot : controlPanelController.getRobots()) {
//            robot.getGraphicalDisplay().cleanBottomLayers();//todo why change color of position will change directly?
//        }
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
     * Init the robots: add on click to the pane(garden) so wherever click the pane, a new robots will be created.
     */
    private void robotsInitBooster() {
        //set onClickListener for creating robots
        garden.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {// add listener for left click
                    controlPanelFacade.robotGenerator("New Robot", event.getX(), event.getY());//todo name?
                    //adding to the graph
                    updateGarden();//using this method for insert in order to ensure the robot position is always overlapped the robot body and the robot body is always in front of the robot vision.
                }
            }
        });
    }

    private void gardenMouseHoverListener() {
        garden.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controlPanelFacade.setMouseCoordinate(event.getX(), event.getY());
            }
        });
    }

    private void gardenMouseMoveOutListener() {
        garden.hoverProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    controlPanelFacade.cleanMouseCoordinate();
                }
            }
        });
    }

    /**
     *
     * Assign necessary listener to the given robot -- in order to response user click.
     *
     * @param robot the robot object need to add to the garden pane.
     */
    public void addListenerToRobot(Robot robot) {
        RobotGraphicalDisplay robotGraphicalDisplay = robot.getGraphicalDisplay();
        //set onClickListener for opening robot setting & displaying vision range
        robotGraphicalDisplay.getRobotBody().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    System.out.println("Clicked");
                    //build up a local copy for further implementation: Translate position to closest int coordinate
                    List<Robot> localCopied = new ArrayList<>();
                    for (Robot mRobot : controlPanelFacade.getRobots()) {
                        try {
                            Robot copied = mRobot.deepCopy();
                            Point2D.Double position = roundUpCoordinate(copied.getPosition());
                            copied.moveTo(position.getX(), position.getY());
                            localCopied.add(copied);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    Point2D.Double location = robot.getPosition();
                    Point2D.Double tempLocation = new Point2D.Double(location.x, location.y);
                    tempLocation = roundUpCoordinate(tempLocation);
                    if (isOverlay(tempLocation, localCopied)) {//not overlay: single robot in one position
                        RobotSettingHelper robotSettingHelper = new RobotSettingHelper(robot, garden.getScene().getWindow());
                    } else {
                        RobotToggle menu = new RobotToggle(gardenController, robot, controlPanelFacade);
                        menu.show(robotGraphicalDisplay.getRobotBody(), Side.BOTTOM, 0, 0);
                        updateGarden();
                    }
                }
            }
        });
    }

    /**
     * Used to determine if the given location already exists points (robots)
     *
     * @param location The location to be check
     * @return ture if there exist at least one point in the given location. False otherwise.
     */
    private boolean isOverlay(Point2D.Double location, List<Robot> localCopiedRobotsList) {
        System.out.println(location.getX() + ", " + location.getY());
        int ctr = 0;
        for (Robot robot : localCopiedRobotsList) {
            if (robot.getPosition().getX() == location.getX() && robot.getPosition().getY() == location.getY()) {
                ctr++;
                if (ctr > 1) {//if there is one except robot itself also in this location
                    return true;
                }
            }
        }
        if (ctr == 0) {
            throw new IllegalStateException("Cannot find robot itself while adding listener.");
        }
        return false;
    }

    /**
     * Round up the coordinate of the location. For example, (38.2, 8.9) will be round up to (38, 9).
     *
     * @param location the location to be round up
     * @return return the given object, reference unchanged, with the rounded up location
     */
    private Point2D.Double roundUpCoordinate(Point2D.Double location) {
        int x = (int) Math.round(location.getX());
        int y = (int) Math.round(location.getY());
        location.setLocation(x, y);
        return location;
    }

    public Pane getGarden() {
        return garden;
    }

    public ControlPanelFacade getControlPanelFacade() {
        return this.controlPanelFacade;
    }

    public void setControlPanelFacade(ControlPanelFacade controlPanelFacade) {
        this.controlPanelFacade = controlPanelFacade;
    }

}
