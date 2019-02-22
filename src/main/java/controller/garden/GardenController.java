package controller.garden;


import controller.controlpanel.ControlPanelFacade;
import core.DisplayAdapter;
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
import javafx.util.Pair;
import model.Robot;
import model.RobotGraphicalDisplay;

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

    private double nameCtr = ControlPanelFacade.ROBOT_NAME_COUNTER;

    /**
     * The garden instance
     */
    @FXML
    private Pane garden;

    public GardenController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../../resources/garden.fxml"));
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
                    Robot robot = controlPanelFacade.robotGenerator("No." + nameCtr, event.getX(), event.getY());
                    nameCtr++;
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
                    //build up a local copy for further implementation: Translate position to closest int coordinate
                    Pair<Boolean, List<Robot>> overlay = filterOfRobotListWithOnlyInTheClosestPosition(robot);
                    boolean overlayExist = overlay.getKey();
                    List<Robot> overlayRobots = overlay.getValue();
                    if (overlayExist) {//not overlay: single robot in one position
                        if (overlayRobots == null) {
                            throw new IllegalStateException("Cannot find overlaid robots");
                        }
                        MultiRobotToggle multiRobotToggle = new MultiRobotToggle(gardenController, overlayRobots);
                    } else {
                        SingleRobotToggle menu = new SingleRobotToggle(gardenController, robot, controlPanelFacade);
                        menu.show(robotGraphicalDisplay.getRobotBody(), Side.BOTTOM, 0, 0);
                        updateGarden();
                    }
                }
            }
        });
    }


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
     * Used to get the robots list which all the robots overlaid with the given robot.
     *
     * <strong>Note that: the goal is achieved by TEMPORARY (Not affect original) rounding up the coordinate of the location. For example, (38.2, 8.9) will be round up to (38, 9).</strong>
     *
     * @param robot the target robot
     * @return Pair of Boolean and List<Robot>. For Boolean: used to determine if the given location already exists points (robots). If exist, then the second LIst<Robot> will be the overlaid robot. Otherwise, the second List<Robot> <strong>will be NULL</strong>
     */
    private Pair<Boolean, List<Robot>> filterOfRobotListWithOnlyInTheClosestPosition(Robot robot) {
        Point2D.Double position = roundUpCoordinate(robot.getPosition());
        List<Robot> robotsInSamePosition = new ArrayList<>();
        int ctr = 0;
        for (Robot curr : controlPanelFacade.getRobots()) {
            Point2D.Double currRoundedUpPosition = roundUpCoordinate(curr.getPosition());
            if (position.getX() == currRoundedUpPosition.getX() && position.getY() == currRoundedUpPosition.getY()) {
                robotsInSamePosition.add(curr);
                ctr++;
            }
        }
        if (ctr > 1) {//if there is one except robot itself also in this location
            return new Pair<>(true, robotsInSamePosition);
        } else {
            return new Pair<>(false, null);
        }
    }

    /**
     *
     *
     * @param location the location to be round up
     * @return return a new Point2D.Double object with the rounded up location
     */
    private Point2D.Double roundUpCoordinate(Point2D.Double location) {
        Point2D.Double result = new Point2D.Double();
        int x = (int) Math.round(location.getX());
        int y = (int) Math.round(location.getY());
        result.setLocation(x, y);
        return result;
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
