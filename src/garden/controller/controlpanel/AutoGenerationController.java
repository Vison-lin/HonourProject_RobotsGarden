package garden.controller.controlpanel;

import garden.model.Robot;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AutoGenerationController extends VBox {

    private static final String RANDOM_CREATE_ROBOT_BUTTON = "Random Create Connected Robots";
    @FXML
    private Button randomCreateRobots;

    @FXML
    private TextField numberOfAutoCreatedRobots;

    private ControlPanelFacade controlPanelFacade;

    public AutoGenerationController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/garden/view/control_panel_component/auto_generation.fxml"));
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
        randomCreateRobots.setText(RANDOM_CREATE_ROBOT_BUTTON);
    }

    private void randomCreateConnectedRobotsBtnListener() {
        randomCreateRobots.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                try {
                    int numOfNewRobots = Integer.valueOf(numberOfAutoCreatedRobots.getText());

                    Random random = new Random();

                    //init first one
                    double maxX = (int) controlPanelFacade.getGardenController().getWidth() + 1;
                    double maxY = (int) controlPanelFacade.getGardenController().getHeight() + 1;
                    int ctr = 0;
                    Robot currRobot = controlPanelFacade.robotGenerator(" =>" + ctr + "<= ", random.nextInt((int) maxX), random.nextInt((int) maxY));

                    ArrayList<Robot> generatedRobots = new ArrayList<>();
                    generatedRobots.add(currRobot);

                    //create the rest
                    for (int i = 1; i < numOfNewRobots; i++) {
                        ctr++;
                        Point2D.Double position = getNextRandomGeneratedRobotPosition(generatedRobots, maxX, maxY);
                        Robot newRobot = controlPanelFacade.robotGenerator(" =>" + ctr + "<= ", position.x, position.y);
                        generatedRobots.add(newRobot);
                    }
                    controlPanelFacade.getGardenController().updateGarden();
                } catch (NumberFormatException e) {
                    controlPanelFacade.getWarning().setText("The Number of Random Created Robots Must Be an Integer!!!");
                }
            }
        });
    }

    private Point2D.Double getNextRandomGeneratedRobotPosition(List<Robot> robots, double maxX, double maxY) {
        Random random = new Random();
        double currX = 0;
        double currY = 0;
        Robot curr = robots.get(random.nextInt(robots.size()));//random choose one
        double xBoundUp = validateWithinTheEnclosingSquare(curr.getPositionX() + curr.getVision(), maxX);
        double xBoundLow = validateWithinTheEnclosingSquare(curr.getPositionX() - curr.getVision(), maxX);
        double yBoundUp = validateWithinTheEnclosingSquare(curr.getPositionY() + curr.getVision(), maxY);
        double yBoundLow = validateWithinTheEnclosingSquare(curr.getPositionY() - curr.getVision(), maxY);
        //check if is within the circle
        double distance = Double.POSITIVE_INFINITY;

        while (distance > curr.getVision()) {
            double x = curr.getPositionX();
            double y = curr.getPositionY();
            currX = random.nextInt((int) (xBoundUp - xBoundLow + 1)) + xBoundLow;
            currY = random.nextInt((int) (yBoundUp - yBoundLow + 1)) + yBoundLow;
            double differX = currX - x;
            double differY = currY - y;
            distance = Math.sqrt(Math.pow(differX, 2) + Math.pow(differY, 2));
        }
        return new Point2D.Double(currX, currY);
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

    public void setControlPanelFacade(ControlPanelFacade controlPanelFacade) {
        this.controlPanelFacade = controlPanelFacade;
    }

    public void reset() {
        randomCreateRobots.setDisable(false);
        numberOfAutoCreatedRobots.setText("");
    }
}
