package controller.garden;


import controller.controlpanel.ControlPanelFacade;
import core.RightClickFunction;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.util.Pair;
import model.DisplayAdapter;
import model.Robot;
import model.RobotGraphicalDisplay;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The controller of the coordinateSystem.fxml
 * The Garden is the window that contains all the robots
 */
public class GardenController extends VBox {

    /**
     * The size (radius) of the robot that displays on the screen. The default value is 21.
     */
    private static double ROBOT_SIZE = 9;

    private ControlPanelFacade controlPanelFacade;

    private GardenController gardenController = this;

    private static double zoomingFactor = 0.1;

    private static Point2D.Double currentCursorAbsulatePosition = new Point2D.Double(0, 0);

    private static double defaultAxisStrokeWidth = 3;

    private static double strokeWidthIncrement = 0.5;

    private static boolean isScrollingUp = false;

    private Line xAxis;

    private Line yAxis;

    /**
     * The garden instance
     */
    @FXML
    private Pane garden;

    @FXML
    private Pane gardenFrame;


    private Group coordinateSystem;

    public GardenController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/garden.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        coordinateSystem.setBorder();
//        clipChildren(garden);
        initCoordinateSystem();
//        drawXYAxis();
        robotsInitBooster();
        gardenMouseHoverListener();
        gardenMouseMoveOutListener();
        zoomingImplementer();
    }

    private void initCoordinateSystem() {
        coordinateSystem = new Group();
        coordinateSystem.setTranslateX(gardenFrame.getPrefWidth()/2);
        coordinateSystem.setTranslateY(gardenFrame.getPrefHeight()/2);
        gardenFrame.getChildren().add(coordinateSystem);
    }

    public static Point2D.Double adjustCoordinate(Point2D.Double coordinate) {
        Point2D.Double graphicalPoint = new Point2D.Double();
        double x = coordinate.getX();
        double y = coordinate.getY();
        double cursorX = currentCursorAbsulatePosition.getX();
        double cursorY = currentCursorAbsulatePosition.getY();
//        System.out.println("The cursor position is: " + cursorX + ", " + cursorY);
        double newX = (cursorX + zoomingFactor * (x - cursorX));
        double newY = (cursorY + zoomingFactor * (y - cursorY));
        graphicalPoint.setLocation(newX, newY);
//        System.out.println(zoomingFactor);
//        System.out.println(x==graphicalPoint.getX() && y==graphicalPoint.getY());
        return graphicalPoint;
    }

    private void drawXYAxis() {
        xAxis = new Line();//LineBuilder.create()
        xAxis.setStartX(0);
        xAxis.setStartY(0);
        xAxis.setEndX(garden.getPrefWidth() - defaultAxisStrokeWidth);
        xAxis.setEndY(0);
        xAxis.setFill(Color.BLACK);
        xAxis.setStrokeWidth(defaultAxisStrokeWidth);
//        xAxis.setTranslateX(defaultAxisStrokeWidth / 2);
//        xAxis.setTranslateY(garden.getPrefHeight() / 2);
        coordinateSystem.getChildren().add(xAxis);
//        coordinateSystem.layoutBoundsProperty().

        yAxis = new Line();//LineBuilder.create()
        yAxis.setStartX(0);
        yAxis.setStartY(0);
        yAxis.setEndX(0);
        yAxis.setEndY(garden.getPrefHeight() - defaultAxisStrokeWidth);
        yAxis.setFill(Color.BLACK);
        yAxis.setStrokeWidth(defaultAxisStrokeWidth);
        yAxis.setTranslateX(garden.getPrefWidth() / 2);
        yAxis.setTranslateY(defaultAxisStrokeWidth / 2);
        coordinateSystem.getChildren().add(yAxis);
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
     * Init the robots: add on click to the pane(coordinateSystem) so wherever click the pane, a new robots will be created.
     */
    private void robotsInitBooster() {
        //set onClickListener for creating robots
        gardenFrame.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY && controlPanelFacade.getCurrentRightClickFunction() == RightClickFunction.CreateRobot) {// add listener for left click
                    double moveX = coordinateSystem.parentToLocal(event.getX(), event.getY()).getX();
                    double moveY = coordinateSystem.parentToLocal(event.getX(), event.getY()).getY();
                    Point2D.Double coordinateSystemPosition = new Point2D.Double(coordinateSystem.getTranslateX(), coordinateSystem.getTranslateY());
//                    System.out.println(coordinateSystem.getTranslateX() +", "+coordinateSystem.getTranslateY());
//                    System.out.println(garden.getTranslateX() +", "+garden.getTranslateY());
//                    System.out.println(gardenFrame.getTranslateX() +", "+gardenFrame.getTranslateY());
//                    System.out.println("The cursor is at: " +event.getX() +", "+ event.getY() +". While the one is at : "+moveX +", " +moveY);

                    Robot robot = controlPanelFacade.robotGenerator("No." + controlPanelFacade.getRobotNameCounter(), moveX, moveY);
//                    System.out.println(robot.getPosition());
//                    System.out.println(robot.getGraphicalDisplay().getRobotPosition().getTranslateX()+","+robot.getGraphicalDisplay().getRobotPosition().getTranslateY());
                    controlPanelFacade.increaseRobotNameCounter();
                    //adding to the graph

                    updateGarden();//using this method for insert in order to ensure the robot position is always overlapped the robot body and the robot body is always in front of the robot vision.
//                    coordinateSystem.setTranslateX(coordinateSystemPosition.getX());
//                    coordinateSystem.setTranslateY(coordinateSystemPosition.getY());
//                    System.out.println(coordinateSystem.getTranslateX() +", "+coordinateSystem.getTranslateY());
//                    System.out.println(garden.getTranslateX() +", "+garden.getTranslateY());
//                    System.out.println(gardenFrame.getTranslateX() +", "+gardenFrame.getTranslateY());
                }
            }
        });
    }

    public void updateGarden() {
        coordinateSystem.getChildren().removeAll(coordinateSystem.getChildren());//remove all the element
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

        //add those layers into the coordinateSystem display list
        for (int i = deepestBottomLayer - 1; i >= 0; i--) {
            coordinateSystem.getChildren().addAll(listOfSetOfBottomLayers.get(i));
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
        coordinateSystem.getChildren().addAll(robotsVision);

        coordinateSystem.getChildren().addAll(robotsBodyAndBorder);

        //ensure the position is always in front of anything
        coordinateSystem.getChildren().addAll(robotsPosition);

        //redraw the coordinate
//        coordinateSystem.getChildren().add(xAxis);
//        coordinateSystem.getChildren().add(yAxis);

        //remove unnecessary info (to ensure obliviousness <- KEY OF THE PROJECT)
//        for (Robot robot : controlPanelController.getRobots()) {
//            robot.getGraphicalDisplay().cleanBottomLayers();//todo FRED: why change color of position will change directly?
//        }
    }

    private void gardenMouseMoveOutListener() {
        gardenFrame.hoverProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    controlPanelFacade.cleanMouseCoordinate();
                }
            }
        });
    }

    private void gardenMouseHoverListener() {
        gardenFrame.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                javafx.geometry.Point2D relativePositionInCoordinateSystem = coordinateSystem.parentToLocal(event.getX(), event.getY());
                controlPanelFacade.setMouseCoordinate(relativePositionInCoordinateSystem.getX(), relativePositionInCoordinateSystem.getY());
            }
        });
    }

    /**
     *
     * Assign necessary listener to the given robot -- in order to response user click.
     *
     * @param robot the robot object need to add to the coordinateSystem pane.
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

    public Group getCoordinateSystem() {
        return coordinateSystem;
    }

    public ControlPanelFacade getControlPanelFacade() {
        return this.controlPanelFacade;
    }

    public void setControlPanelFacade(ControlPanelFacade controlPanelFacade) {
        this.controlPanelFacade = controlPanelFacade;
    }

    private void zoomingImplementer() {

        /*
         *
         * Developing cost: $110.
         *
         * Got pulled over by police because of the expired licence plate sticker.
         *
         * Planed to renew the sticker last night but coding till 5:30 A.M. and completely forgot... :(
         *
         */

        //Scale transformation for coordinate
        Scale coordinateScale = new Scale();
        coordinateSystem.getTransforms().add(coordinateScale);

        //Scale transformation for garden
        Scale gardenScale = new Scale();
        garden.getTransforms().add(gardenScale);

        gardenFrame.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {

                double scale = coordinateScale.getX(); // get the current scale from coordinateScale. Note that X and Y always has the same scale
                double oldScale = scale;// duplicate the this scale value since we will change the scale value below

                scale *= Math.pow(1.01, event.getDeltaY()); // to get the new scale.
                /*
                 * The new scale is calculated as following:
                 *
                 * We first init a delta to be the factor to be used to let the scale performs by factor. Scaling in factor can make the scaling more smoothly.
                 * Then we use the built-in event.getDeltaY() to detect the mouse orientation. Note that the value is completely DEPENDED ON different mouse/system setting.
                 * We then adjust our scale accordingly.
                 * The above is coded as below and is the same as "scale *= Math.pow(1.01, event.getDeltaY())";
                 *
                 * double delta = 1.01;
                 *
                 * if (event.getDeltaY() < 0) {
                 *     scale /= delta;
                 * } else {
                 *     scale *= delta;
                 * }
                 */

                // bounds the scale value
                if (scale <= 0) {
                    scale = 0;
                } else if (scale >= 1) {
                    scale = 1;
                }

                // calculate the f, which is used for factoring the moving step
                double f = (scale / oldScale) - 1;

                // scale the coordinateSystem
                coordinateScale.setX(scale);
                coordinateScale.setY(scale);


                /*
                 *             .
                 *            ..
                 *           . .
                 *          .  .
                 *         ..  .
                 *        . .  .
                 *       .  .  .
                 *      .   .  .
                 *     .........
                 */

                // calculate the dx and dy. The dx and dy is the x and y distance, IN PARENT SCALE: gardenFrame, between the current mouse position, IN PARENT SCALE: gardenFrame, and the coordinate center point (0, 0), IN PARENT SCALE: gardenFrame.
                double dx = event.getX() - coordinateSystem.localToParent(0, 0).getX();
                double dy = event.getY() - coordinateSystem.localToParent(0, 0).getY();

                // move the coordinateSystem. the moving distance is the moving factor, f, times the moving distance, dx and dy.
                coordinateSystem.setTranslateX(coordinateSystem.getTranslateX() - f * dx);
                coordinateSystem.setTranslateY(coordinateSystem.getTranslateY() - f * dy);

                // increment the garden and scale it accordingly so that the size of the garden increases but the display size unchanged.
                garden.setPrefWidth(gardenFrame.getPrefWidth() / scale);
                garden.setPrefHeight(gardenFrame.getPrefHeight() / scale);
                gardenScale.setX(scale);
                gardenScale.setY(scale);

            }
        });
    }

    public void clipChildren(Region region) {
        final Rectangle clipPane = new Rectangle();
        region.setClip(clipPane);

        // In case we want to make a resizable pane we need to update
        // our clipPane dimensions
        region.layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
            clipPane.setWidth(newValue.getWidth());
            clipPane.setHeight(newValue.getHeight());
        });
    }

}
