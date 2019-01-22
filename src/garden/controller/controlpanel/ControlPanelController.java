package garden.controller.controlpanel;

import garden.controller.garden.GardenController;
import garden.model.Robot;
import garden.model.RobotGraphicalDisplay;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Pair;
import javafx.util.StringConverter;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;

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
    private ComboBox<Pair<String, String>> algorithmSelection;

    @FXML
    private Button autoRun;

    @FXML
    private Button SaveButton;

    @FXML
    private ColorPicker colorPicker;

    private static final String AUTO_RUN_BTN_TO_START = "Start Auto Run";
    private static final String AUTO_RUN_BTN_TO_STOP = "STOP";
    private static final double DEFAULT_ROBOT_VISION = 100;

    AlgorithmLoadingHelper algorithmLoadingHelper = new AlgorithmLoadingHelper();

    private List<Robot> robots = Collections.synchronizedList(new ArrayList<>());

    private Stack<List<Robot>> robotStackPrev = new Stack<>();

    private Stack<List<Robot>> robotStackNext = new Stack<>();

    private GardenController gardenController;

    private String selectedAlgorithm;

    private boolean isRunning = false;

    public static String robotColor = null;

    @FXML
    private TextField autoRunTimeInterval;

    @FXML
    private TextField inputVision;
    @FXML
    private Text warning;
    private double selectedRobotVision;

    public ControlPanelController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../view/control_panel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initNodesText();
        inputVisionListner();
        prevBtnListener();
        nextBtnListener();
        cleanBtnListener();
        randomCreateConnectedRobotsBtnListener();
        autoRunListener();
        colorPickerListener();
        saveListener();
        try {
            algorithmSelectionInit();
        } catch (InstantiationException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();//todo handle it and display it on the screen.!!!
        }
        algorithmSelectionListener();

    }

    private void initNodesText() {
        autoRun.setText(AUTO_RUN_BTN_TO_START);
        inputVision.setText("100");
        selectedRobotVision = DEFAULT_ROBOT_VISION;
    }

    private void autoRunListener() {
        autoRun.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (isRunning) {//if is already auto running, stop it
                    isRunning = false;
                    autoRun.setText(AUTO_RUN_BTN_TO_START);
                } else {
                    isRunning = true;
                    Task task = new Task<Void>() {//create a new task
                        @Override
                        protected Void call() throws InterruptedException {
                            while (isRunning) {
                                try {
                                    int timeInterval = Integer.valueOf(autoRunTimeInterval.getText());
                                    Platform.runLater(ControlPanelController.this::nextAction);//update in UI thread
                                    Thread.sleep(timeInterval);
                                } catch (NumberFormatException e) {
                                    warning.setText("Invalid Input! The ms must be an integer!");
                                    isRunning = false;
                                }
                            }
                            return null;
                        }
                    };
                    autoRun.setText(AUTO_RUN_BTN_TO_STOP);//todo disable other Btn
                    new Thread(task).start();
                }
            }
        });
    }

    private void saveListener(){
        SaveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(robots.size()!=0){
                File f = new File("C:/Users/user/Desktop/a.txt");
                try {
                    FileOutputStream fop = new FileOutputStream(f);
                    OutputStreamWriter writer = new OutputStreamWriter(fop);
                    for(Robot robot:robots){

                        writer.append("Tag: " + robot.getTag() + ", Algorithm: ??, " + "selectedRobotVision: " + (int) robot.getVision());
                        writer.append("\r\n");

                    }
                    writer.close();
                    fop.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            }
        });


    }



    private void inputVisionListner(){
        inputVision.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    selectedRobotVision = Integer.valueOf(inputVision.getText());//todo must press return to trigger, beeter way? use int or double?
                } catch (NumberFormatException e) {
                    warning.setText("Robot Vision must be an int");
                }

            }
        });



    }


    private void nextAction() {
        addDeepCopiedRobotList(robotStackPrev, robots);//store the current to the prev
        if (!robotStackNext.empty()) {
            robots.removeAll(robots);//clean the current
            robots.addAll(robotStackNext.pop());
        } else {
            ArrayList<Robot> localRobotsList = new ArrayList<>();
            //deep copy (partially): Ensure each of the robot's sensor has the same copy for each step (the duration of one "next" btn click)
            for (Robot robot : robots) {
                Robot newRobotInstance = robot.deepCopy();
                localRobotsList.add(newRobotInstance);
            }

            //run next
            Iterator<Robot> robotIterator2 = robots.iterator();
            while (robotIterator2.hasNext()) {
                Robot curr = robotIterator2.next();
                Point newPosition = curr.next(localRobotsList);//ensure all the robots get the same copy in each stage (next btn)
                newPosition = boundaryCheck(newPosition);//ensure the robot will always stay within its vision.
                curr.moveTo(newPosition.getX(), newPosition.getY());//move the robot
            }
        }

        gardenController.updateGarden();

    }

    private void prevBtnListener() {
        prev.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!robotStackPrev.empty()) {
                    addDeepCopiedRobotList(robotStackNext, robots);//store the current to the next
                    robots.removeAll(robots);//clean the current
                    robots.addAll(robotStackPrev.pop());//restore the prev
                    gardenController.updateGarden();
                }
            }
        });
    }

    private void nextBtnListener() {
        next.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                nextAction();
            }
        });
    }

    private void cleanBtnListener() {
        clean.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                AlgorithmLoadingHelper helper = new AlgorithmLoadingHelper();
                robots.clear();
                gardenController.updateGarden();
                inputVision.setText("100");
                selectedRobotVision = DEFAULT_ROBOT_VISION;
                colorPicker.setValue(Color.BLACK);
                robotColor = ""+Color.BLACK;
                //clean prev and next
                robotStackPrev.clear();
                robotStackNext.clear();
            }
        });
    }

    private void randomCreateConnectedRobotsBtnListener() {
        randomCreateRobots.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //clean the screen
                robots.removeAll(robots);
                Random random = new Random();

                //init first one
                double maxX = (int) gardenController.getWidth() + 1;
                double maxY = (int) gardenController.getHeight() + 1;
                int ctr = 0;
                Robot initRobot = robotGenerator(" =>" + ctr + "<= ", random.nextInt((int) maxX), random.nextInt((int) maxY));

                //create the rest
                for (int i = 1; i < 15; i++) {
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
                    initRobot = robotGenerator(" =>" + ctr + "<= ", currX, currY);
                }
                gardenController.updateGarden();
            }
        });
    }

    private void algorithmSelectionInit() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ObservableList<Pair<String, String>> value = FXCollections.observableArrayList();
        List<Pair<String, String>> allAlgInfo = algorithmLoadingHelper.getAlgorithmList();
        value.addAll(allAlgInfo);
        algorithmSelection.setItems(value);
        algorithmSelection.getSelectionModel().select(0);
        selectedAlgorithm = algorithmSelection.getSelectionModel().getSelectedItem().getValue();//assign the first algorithm as the default selected item
        algorithmSelection.setConverter(new StringConverter<Pair<String, String>>() {
            @Override
            public String toString(Pair<String, String> object) {
                return object.getKey();
            }

            @Override
            public Pair<String, String> fromString(String string) {
                return null;
            }
        });
    }

    private void algorithmSelectionListener() {
        algorithmSelection.valueProperty().addListener(
                (obs, oldVal, newVal) -> selectedAlgorithm = newVal.getValue()
        );
    }

    private void colorPickerListener() {
        colorPicker.setValue(Color.BLACK);
        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                robotColor = ""+colorPicker.getValue();
            }
        });
    }




    /**
     * @param tag todo
     * @param x
     * @param y   for now, generate
     * @return
     */
    public Robot robotGenerator(String tag, double x, double y) {
        Circle robotPosition = new Circle(3, Color.WHITE);
        Circle robotBody = new Circle(10, Color.BLACK);
        Circle robotBorder = new Circle(11, Color.WHITE);
        Circle robotVision = new Circle(selectedRobotVision, Color.LIGHTBLUE);
        RobotGraphicalDisplay robotGraphicalDisplay = new RobotGraphicalDisplay(robotPosition, robotBody, robotBorder, robotVision, false);
        Robot robot = new Robot(robotGraphicalDisplay);
        robot.moveTo(x, y);
        robot.setTag(tag);

        algorithmLoadingHelper.assignAlgorithmToRobot(robot, selectedAlgorithm);

        robots.add(robot);//add the robot into the robots list
        gardenController.addGeneratedRobotToGarden(robot);
        return robot;
    }

    public void setGardenController(GardenController gardenController) {
        this.gardenController = gardenController;
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

    public double getSelectedRobotVision() {
        return selectedRobotVision;
    }

    public void setSelectedRobotVision(double selectedRobotVision) {
        this.selectedRobotVision = selectedRobotVision;
    }
}
