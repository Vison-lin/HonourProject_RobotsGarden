package garden.controller.controlpanel.controlpanel_component;

import garden.controller.controlpanel.ControlPanelController;
import garden.model.Robot;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class AutoGenerationController extends VBox {

    @FXML
    private Button randomCreateRobots;

    @FXML
    private TextField numberOfAutoCreatedRobots;

    private ControlPanelController controlPanelController;
    private List<Robot> robots;

    public AutoGenerationController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../../view/control_panel_component/auto_generation.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initNodesText();
        randomCreateConnectedRobotsBtnListener();
    }

    private void initNodesText() {
    }

    private void randomCreateConnectedRobotsBtnListener() {
        randomCreateRobots.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                try {
                    int numOfNewRobots = Integer.valueOf(numberOfAutoCreatedRobots.getText());

                    //clean the screen
                    robots.removeAll(robots);
                    Random random = new Random();

                    //init first one
                    double maxX = (int) controlPanelController.getGardenController().getWidth() + 1;
                    double maxY = (int) controlPanelController.getGardenController().getHeight() + 1;
                    int ctr = 0;
                    //todo faked
                    Circle robotPosition = new Circle(3, Color.WHITE);
                    Circle robotBody = new Circle(10, controlPanelController.getSelectedRobotColor());
                    Circle robotBorder = new Circle(11, Color.WHITE);
                    Circle robotVision = new Circle(controlPanelController.getSelectedRobotVision(), Color.LIGHTBLUE);
                    Robot initRobot = controlPanelController.getRobotGenerationController().robotGenerator(" =>" + ctr + "<= ", random.nextInt((int) maxX), random.nextInt((int) maxY), robotPosition, robotBody, robotBorder, robotVision);

                    //create the rest
                    for (int i = 1; i < numOfNewRobots; i++) {
                        double xBoundUp = validateWithinTheEnclosingSquare(initRobot.getPositionX() + initRobot.getVision(), maxX);
                        double xBoundLow = validateWithinTheEnclosingSquare(initRobot.getPositionX() - initRobot.getVision(), maxX);
                        double yBoundUp = validateWithinTheEnclosingSquare(initRobot.getPositionY() + initRobot.getVision(), maxY);
                        double yBoundLow = validateWithinTheEnclosingSquare(initRobot.getPositionY() - initRobot.getVision(), maxY);
                        //check if is within the circle
                        double distance = Double.POSITIVE_INFINITY;
                        double currX = 0;
                        double currY = 0;
                        while (distance > initRobot.getVision()) {
                            double x = initRobot.getPositionX();
                            double y = initRobot.getPositionY();
                            currX = random.nextInt((int) (xBoundUp - xBoundLow + 1)) + xBoundLow;
                            currY = random.nextInt((int) (yBoundUp - yBoundLow + 1)) + yBoundLow;
                            double differX = currX - x;
                            double differY = currY - y;
                            distance = Math.sqrt(Math.pow(differX, 2) + Math.pow(differY, 2));
                        }
                        robotPosition = new Circle(3, Color.WHITE);
                        robotBody = new Circle(10, controlPanelController.getSelectedRobotColor());
                        robotBorder = new Circle(11, Color.WHITE);
                        robotVision = new Circle(controlPanelController.getSelectedRobotVision(), Color.LIGHTBLUE);
                        initRobot = controlPanelController.getRobotGenerationController().robotGenerator(" =>" + ctr + "<= ", currX, currY, robotPosition, robotBody, robotBorder, robotVision);
                    }
                    controlPanelController.getGardenController().updateGarden();
                } catch (NumberFormatException e) {
                    controlPanelController.getWarning().setText("The Number of Random Created Robots Must Be an Integer!!!");
                }
            }
        });
    }

    /**
     * Validate if the point is within the square that encloses the circle.
     *
     * @param original the point
     * @param bound    the radius
     * @return validated value
     */
    private double validateWithinTheEnclosingSquare(double original, double bound) {
        if (original >= bound) {
            original = bound;
        } else if (original <= 0) {
            original = 0;
        }
        return original;
    }

    public void setControlPanelController(ControlPanelController controlPanelController) {
        this.controlPanelController = controlPanelController;
        setRobots();
    }

    private void setRobots() {
        this.robots = controlPanelController.getRobots();
    }
}
