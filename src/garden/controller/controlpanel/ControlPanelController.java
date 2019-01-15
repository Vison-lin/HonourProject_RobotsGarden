package garden.controller.controlpanel;

import garden.controller.garden.GardenController;
import garden.model.Robot;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class ControlPanelController extends VBox {

    @FXML
    private Button prev;

    @FXML
    private Button next;

    @FXML
    private Button clean;

    @FXML
    private Button randomCreateRobots;

    @FXML
    private Text output;

    private Stack<List<Robot>> robotStackPrev = new Stack<>();

    private Stack<List<Robot>> robotStackNext = new Stack<>();

    private GardenController gardenController;

    public ControlPanelController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../view/control_panel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        prevBtnListener();
        nextBtnListener();
        cleanBtnListener();
        randomCreateConnectedRobotsBtnListener();

    }

    private void prevBtnListener() {
        prev.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!robotStackPrev.empty()) {


                    addDeepCopiedRobotList(robotStackNext, gardenController.getRobots());//store the current to the next
                    gardenController.getRobots().removeAll(gardenController.getRobots());//clean the current
                    gardenController.getRobots().addAll(robotStackPrev.pop());//restore the prev
                    gardenController.updateGarden();

                }
            }
        });
    }

    private void nextBtnListener() {
        next.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                addDeepCopiedRobotList(robotStackPrev, gardenController.getRobots());//store the current to the prev
                if (!robotStackNext.empty()) {
                    gardenController.getRobots().removeAll(gardenController.getRobots());//clean the current
                    gardenController.getRobots().addAll(robotStackNext.pop());
                } else {
                    ArrayList<Robot> localRobotsList = new ArrayList<>();
                    //deep copy (partially): Ensure each of the robot's sensor has the same copy for each step (the duration of one "next" btn click)
                    for (Robot robot : gardenController.getRobots()) {
                        Robot newRobotInstance = robot.deepCopy();
                        localRobotsList.add(newRobotInstance);
                    }

                    //run next
                    Iterator<Robot> robotIterator2 = gardenController.getRobots().iterator();
                    while (robotIterator2.hasNext()) {
                        Robot curr = robotIterator2.next();
                        Point newPosition = curr.next(localRobotsList);//ensure all the robots get the same copy in each stage (next btn)
                        newPosition = boundaryCheck(newPosition);//ensure the robot will always stay within its vision.
                        curr.moveTo(newPosition.getX(), newPosition.getY());//move the robot
                    }
                }
                gardenController.updateGarden();
            }
        });
    }

    private void cleanBtnListener() {
        clean.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                gardenController.getRobots().clear();
                gardenController.updateGarden();
                output.setText("");
            }
        });
    }

    private void randomCreateConnectedRobotsBtnListener() {
        randomCreateRobots.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //clean the screen
                gardenController.getRobots().removeAll(gardenController.getRobots());
                Random random = new Random();

                //init first one
                double maxX = (int) getWidth() + 1;
                double maxY = (int) getHeight() + 1;
                int ctr = 0;
                Robot initRobot = gardenController.robotGenerator(" =>" + ctr + "<= ", random.nextInt((int) maxX), random.nextInt((int) maxY));

                //create the rest
                for (int i = 1; i < 50; i++) {
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
                    initRobot = gardenController.robotGenerator(" =>" + ctr + "<= ", currX, currY);
                }
                gardenController.updateGarden();
            }
        });
    }

    public void setGardenController(GardenController gardenController) {
        this.gardenController = gardenController;
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

    private Point boundaryCheck(Point point) {
        //negative check
        if (point.getX() < 0) {
            point.setLocation(0, point.getY());
        }
        if (point.getY() < 0) {
            point.setLocation(point.getX(), 0);
        }

        //boundary check
        if (point.getX() > gardenController.getGarden().getWidth()) {
            point.setLocation(gardenController.getGarden().getWidth(), point.getY());
        }
        if (point.getY() > gardenController.getGarden().getHeight()) {
            point.setLocation(point.getX(), gardenController.getGarden().getHeight());
        }
        return point;
    }

    /**
     * Deep copy the robots list and add it to either robotStackPrev or robotStackNext
     *
     * @param robotStack either the robotStackPrev or the robotStackNext
     * @param robotList  the robot list that needed to be deep copied and added
     */
    private void addDeepCopiedRobotList(Stack<List<Robot>> robotStack, List<Robot> robotList) {
        Iterator<Robot> iterator = robotList.iterator();
        List<Robot> deepCopied = new ArrayList<>();
        while (iterator.hasNext()) {
            Robot curr = iterator.next();
            deepCopied.add(curr.deepCopy());
        }
        robotStack.add(deepCopied);
    }

}
