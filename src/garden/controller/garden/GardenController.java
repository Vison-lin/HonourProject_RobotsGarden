package garden.controller.garden;

import garden.controller.controlpanel.ControlPanelController;
import garden.controller.controlpanel.controlpanel_component.RobotGenerationController;
import garden.core.DisplayComponent;
import garden.model.Robot;
import garden.model.RobotGraphicalDisplay;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

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

    private ControlPanelController controlPanelController;

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

    }

    public void updateGarden() {
        garden.getChildren().removeAll(garden.getChildren());//remove all the element
        List<Circle> robotsPosition = new ArrayList<>();
        List<Circle> robotsBodyAndBorder = new ArrayList<>();
        List<Circle> robotsVision = new ArrayList<>();
        List<List<Node>> listOfSetOfBottomLayers = new ArrayList<>();

        //get the index of the deepest bottom layer for all robots
        int deepestBottomLayer = 0;
        for (Robot robot : controlPanelController.getRobots()) {
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
            for (Robot robot : controlPanelController.getRobots()) {
                DisplayComponent currDisplayComponent;
                try {
                    currDisplayComponent = robot.getGraphicalDisplay().getBottomLayers().get(i);
                    if (currDisplayComponent.isVisible()) {
                        listOfSetOfBottomLayers.get(i).add(currDisplayComponent.getDisplayPattern());
                    }
                } catch (IndexOutOfBoundsException ignored) {
                }//do nothing for the not-exist layer
            }
        }

        //add those layers into the garden display list
        for (int i = deepestBottomLayer - 1; i >= 0; i--) {
            garden.getChildren().addAll(listOfSetOfBottomLayers.get(i));
        }


        for (Robot robot : controlPanelController.getRobots()) {
            RobotGraphicalDisplay robotGraphicalDisplay = robot.getGraphicalDisplay();
            Circle robotPosition = robotGraphicalDisplay.getRobotPosition();
            System.out.println("The drawn robot positioned at: " + robotPosition.getTranslateX() +","+robotPosition.getTranslateY());
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
     * Init the robots: add on click to the pane(garden) so where ever click the pane, a new robots will be created.
     */
    private void robotsInitBooster() {
        //set onClickListener for creating robots
        garden.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {// add listener for left click
                    Circle robotPosition = new Circle(3, Color.WHITE);
                    Circle robotBody = new Circle(10, Color.BLACK);
                    Circle robotBorder = new Circle(11, Color.WHITE);
                    Circle robotVision = new Circle(RobotGenerationController.DEFAULT_ROBOT_VISION, Color.LIGHTBLUE);
                    controlPanelController.getRobotGenerationController().robotGenerator("New Robot", event.getX(), event.getY(), robotPosition, robotBody, robotBorder, robotVision);
                    //adding to the graph
                    updateGarden();//using this method for insert in order to ensure the robot position is always overlapped the robot body and the robot body is always in front of the robot vision.
                }
            }
        });
    }

    /**
     *
     * Add the generated Robot to garden and assign necessary listener to it.
     *
     * @param robot the robot object need to add to the garden pane.
     */
    public void addGeneratedRobotToGarden(Robot robot) {
        addOnClickListenerToRobot(robot);
    }

    private void addOnClickListenerToRobot(Robot robot) {
        RobotGraphicalDisplay robotGraphicalDisplay = robot.getGraphicalDisplay();
        //set onClickListener for opening robot setting & displaying vision range
        robotGraphicalDisplay.getRobotBody().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.MIDDLE) {// for each of the btn that has added event, add one right click listener for it.
                    RobotSettingHelper robotSettingHelper = new RobotSettingHelper(robot, garden.getScene().getWindow());
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    RobotHelper menu = new RobotHelper(gardenController, robot, controlPanelController.getRobotGenerationController().getSelectedRobotColor(), controlPanelController.getSelectedRobotVision());
                    menu.show(robotGraphicalDisplay.getRobotBody(),Side.BOTTOM,0,0);
                    updateGarden();
                }
            }
        });
    }

    public Pane getGarden() {
        return garden;
    }

    public void setControlPanelController(ControlPanelController controlPanelController) {
        this.controlPanelController = controlPanelController;
    }
}
