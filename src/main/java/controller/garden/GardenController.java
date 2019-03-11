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
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
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
     * THe zooming factor used to implement zooming feature. The default value is 1.01
     */
    private static double ZOOMING_FACTOR = 1.01;

    /**
     * The garden pane. The garden pane is used as the background for the coordinateSystem. It is a child of the gardenFrame pane
     */
    @FXML
    private Pane garden;

    /**
     * The gardenFrame pane. The gardenFrame pane is the base of the garden.fxml. Its size is fixed unless users change it. The gardenFrame is the parent of both the garden pane and the coordinateSystem group.
     * <br/>
     * Any listener should be set onto this pane since its size and position are both fixed.
     */
    @FXML
    private Pane gardenFrame;

    /**
     * The coordinateSystem group. The coordinateSystem group is a group that represent the coordinate system of this application. Its X-axis is increasing from left to right (>) and its Y-axis increasing is from bottom to top (^).
     * <br/>
     * <strong>All the nodes (robots, etc.) are stored in this group.</strong>
     * <br/>
     * Its' init position, at the time the app start, without any zooming or dragging, is in the middle of the screen.
     */
    private Group coordinateSystem;

    /**
     * The instance of ControlPanelFacade. Used to retrieve date from all the control panel related controllers.
     */
    private ControlPanelFacade controlPanelFacade;

    /**
     * Used for implementing dragging feature: the position of the mouse when user press the mouse in order to drag.
     */
    private Point2D.Double mousePressPosition = new Point2D.Double(0, 0);

    /**
     * Used for implementing dragging feature: the position of the coordinateSystem at the time the current drag session start.
     */
    private Point2D.Double storedCoordinateSystemLocation = new Point2D.Double(0, 0);

    /**
     * The scale that used for scaling the coordinateSystem. This scale has default pivot point, (0, 0). And it is only responsible for scaling the x and y axis for the zooming feature
     */
    private Scale coordinateScale;

    /**
     * The scale that used for scaling the garden. This scale has default pivot point, (0, 0). And it is only responsible for scaling the x and y axis for the zooming feature.
     */
    private Scale gardenScale;

    public GardenController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/garden.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gardenFrame.setScaleY(-1);
        clipChildren(gardenFrame);// clip anything outside of the gardenFrame
        initCoordinateSystem();
        gardenFrameOnMouseClickListeners();
        gardenFrameOnMousePressListener();
        gardenFrameOnMouseDraggedListener();
        gardenFrameMouseHoverListener();
        gardenMouseMoveOutListener();
        gardenFrameScrollListener();
        gardenFrameZoomingListener();
    }

    private void initCoordinateSystem() {
        coordinateSystem = new Group();
        coordinateSystem.setTranslateX(gardenFrame.getPrefWidth()/2);
        coordinateSystem.setTranslateY(gardenFrame.getPrefHeight()/2);
        gardenFrame.getChildren().add(coordinateSystem);
        //Scale transformation for coordinate
        coordinateScale = new Scale();
        coordinateSystem.getTransforms().add(coordinateScale);

        //Scale transformation for garden
        gardenScale = new Scale();
        garden.getTransforms().add(gardenScale);
    }

    /**
     * <p>The listenerRegister for <strong>mouse click</strong> for gardenFrame pane.</p>
     * <br/>
     * <p>This listenerRegister is used for implementing the following feature(s):</p>
     * <br/>
     * <ul>
     *     <li>Create new robots</li>
     * </ul>
     *
     */
    private void gardenFrameOnMouseClickListeners() {
        //set onClickListener for creating robots
        gardenFrame.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                /*
                 * Listeners for LEFT CLICK
                 */

                if (event.getButton() == MouseButton.PRIMARY) {

                    /* CreateRobot
                     *Init the robots: add on click to the pane(coordinateSystem) so wherever click the pane, a new robots will be created.
                     * When the right click is used for creating robots, right clicking on gardenFrame will create a new robot in the current cursor position where the position is converted into coordinateSystem coordinate space.
                     */

                    if (controlPanelFacade.getCurrentRightClickFunction() == RightClickFunction.CreateRobot) {// add listener for left click
                        double moveX = coordinateSystem.parentToLocal(event.getX(), event.getY()).getX();
                        double moveY = coordinateSystem.parentToLocal(event.getX(), event.getY()).getY();
                        Robot robot = controlPanelFacade.robotGenerator("No." + controlPanelFacade.getRobotNameCounter(), moveX, moveY);
                        System.out.println(robot.getPosition());
                        controlPanelFacade.increaseRobotNameCounter();
                        //adding to the graph
                        updateGarden();//using this method for insert in order to ensure the robot position is always overlapped the robot body and the robot body is always in front of the robot vision.

                    }
                }




            }
        });
    }

    /**
     * <p>The listenerRegister for <strong>mouse press</strong> for gardenFrame pane.</p>
     * <br/>
     * <p>This listenerRegister is used for implementing the following feature(s):</p>
     * <br/>
     * <ul>
     * <li>Dragging feature starting position handler</li>
     * </ul>
     */
    private void gardenFrameOnMousePressListener() {

        gardenFrame.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                /*
                 * Listeners for LEFT CLICK
                 */

                if (event.getButton() == MouseButton.PRIMARY) {

                    /*
                     * Dragging Feature starting position handler
                     * Getting the current mouse position as the dragging start position
                     * Saving the current coordinateSystem location as the position of the coordinateSystem before dragging.
                     */
                    if (controlPanelFacade.getCurrentRightClickFunction() == RightClickFunction.Drag) {// add listener for left click
                        mousePressPosition.setLocation(event.getX(), event.getY());
                        storedCoordinateSystemLocation.setLocation(coordinateSystem.getTranslateX(), coordinateSystem.getTranslateY());
                    }
                }
            }
        });


    }

    /**
     * <p>The listenerRegister for <strong>mouse dragged</strong> for gardenFrame pane.</p>
     * <br/>
     * <p>This listenerRegister is used for implementing the following feature(s):</p>
     * <br/>
     * <ul>
     * <li>Dragging feature ending position handler</li>
     * </ul>
     */
    private void gardenFrameOnMouseDraggedListener() {
        gardenFrame.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                /*
                 * Listeners for LEFT CLICK
                 */

                if (event.getButton() == MouseButton.PRIMARY) {

                    /*
                     * Dragging Feature ending position handler
                     */
                    if (controlPanelFacade.getCurrentRightClickFunction() == RightClickFunction.Drag) {// add listener for left click
                        double movingDistanceX = event.getX() - mousePressPosition.getX();
                        double movingDistanceY = event.getY() - mousePressPosition.getY();
                        coordinateSystem.setTranslateX(storedCoordinateSystemLocation.getX() + movingDistanceX);
                        coordinateSystem.setTranslateY(storedCoordinateSystemLocation.getY() + movingDistanceY);
                    }

                }
            }
        });
    }

    /**
     * To be used when an update is needed. This method will do the following, in sequence:
     * <ul>
     * <li>Clean all the children within the coordinateSystem group</li>
     * <li>Adding all the robots all layers, starting from the bottom. The sequence is the following, in sequence:
     * <ul>
     * <li>Adding all the displayAdapters from all the robots into the screen, starting from the most bottom one, the deepest one,</li>
     * <li>Adding all the visions from all the robots which its vision is visible into the screen</li>
     * <li>Adding all the borders from all the robots into the screen</li>
     * <li>Adding all the bodies from all the robots into the screen</li>
     * <li>Adding all the position from all the robots into the screen</li>
     * </ul>
     * </li>
     *
     * </ul>
     */
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

        //remove unnecessary info (to ensure obliviousness <- KEY OF THE PROJECT)
//        for (Robot robot : controlPanelController.getRobots()) {
//            robot.getGraphicalDisplay().cleanBottomLayers();//todo FRED: why change color of position will change directly?
//        }
    }

    /**
     * <p>The listenerRegister for <strong>mouse move out</strong> for gardenFrame pane.</p>
     * <br/>
     * <p>This listenerRegister is used for implementing the following feature(s):</p>
     * <br/>
     * <ul>
     *     <li>Clean the coordinate display when the cursor is outside of the gardenFrame region</li>
     * </ul>
     *
     */
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

    /**
     * <p>The listenerRegister for <strong>mouse move, or hover,</strong> for gardenFrame pane.</p>
     * <br/>
     * <p>This listenerRegister is used for implementing the following feature(s):</p>
     * <br/>
     * <ul>
     *     <li>Update the coordinate display when the cursor is inside the gardenFrame region</li>
     * </ul>
     *
     */
    private void gardenFrameMouseHoverListener() {
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
     * Assign necessary listener to the given robot -- in order to response user click. It also add the robots into the list through calling updateGarden method.
     *
     * @param robot the robot object need to add to the coordinateSystem pane.
     */
    public void addListenerToRobot(Robot robot) {
        RobotGraphicalDisplay robotGraphicalDisplay = robot.getGraphicalDisplay();
        GardenController gardenController = this;
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
     * Round up the coordinate. Used for determine the accuracy of overlay pending.
     *
     * For example, (0.3, 0.3) will be seen as (0, 0)
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

    /**
     *
     * Get the current coordinateSystem
     *
     * @return the coordinateSystem instance.
     */
    public Group getCoordinateSystem() {
        return coordinateSystem;
    }

    /**
     *
     * Get the current controlPanelFacade
     *
     * @return the controlPanelFacade instance.
     */
    ControlPanelFacade getControlPanelFacade() {
        return this.controlPanelFacade;
    }

    /**
     *
     * Set the current controlPanelFacade. Or pass the controlPanelFacade into this controller.
     *
     */
    public void setControlPanelFacade(ControlPanelFacade controlPanelFacade) {
        this.controlPanelFacade = controlPanelFacade;
    }

    /**
     *
     * The realization of zooming feature.
     *
     * @param event The event when occurring the zooming.
     * @param pixelAmount The number of movement for this event, usually is the pixel movement.
     */
    private void zoomingImplementer(GestureEvent event, double pixelAmount) {



        /*
         *
         * Developing cost: $110.
         *
         * Got pulled over by police because of the expired licence plate sticker.
         *
         * Planed to renew the sticker last night but coding till 5:30 A.M. and completely forgot... :(
         *
         */


        double scale = coordinateScale.getX(); // get the current scale from coordinateScale. Note that X and Y always has the same scale
        double oldScale = scale;// duplicate the this scale value since we will change the scale value below

        scale *= Math.pow(ZOOMING_FACTOR, pixelAmount); // to get the new scale.
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

    /**
     * <p>The listenerRegister for <strong>mouse scrolling</strong> for gardenFrame pane.</p>
     * <br/>
     * <p>This listenerRegister is used for implementing the following feature(s):</p>
     * <br/>
     * <ul>
     * <li>Zooming feature</li>
     * </ul>
     */
    private void gardenFrameScrollListener() {
        gardenFrame.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                System.out.println("..." + event.getDeltaY());
                zoomingImplementer(event, event.getDeltaY());
            }
        });
    }

    /**
     * <p>The listenerRegister for <strong>mouse zooming</strong> for gardenFrame pane.</p>
     * <br/>
     * <p>This listenerRegister is used for implementing the following feature(s):</p>
     * <br/>
     * <ul>
     * <li>Zooming feature</li>
     * </ul>
     */
    private void gardenFrameZoomingListener() {
        gardenFrame.setOnZoom(new EventHandler<ZoomEvent>() {
            @Override
            public void handle(ZoomEvent event) {
                zoomingImplementer(event, (event.getTotalZoomFactor() - 1) * 1.01);//todo FRED: make smoother
            }
        });
    }

    /**
     *
     * Clip all the children that are outside of the given region. For the child that is partially outside of the region, clip the outside-part body only.
     *
     * @param region the region to be clipped
     */
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
