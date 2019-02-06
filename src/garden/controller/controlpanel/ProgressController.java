package garden.controller.controlpanel;

import garden.core.Algorithm;
import garden.model.Robot;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class ProgressController extends VBox {

    private static final String AUTO_RUN_BTN_TO_START = "Start Auto Run";
    private static final String AUTO_RUN_BTN_TO_STOP = "STOP";
    private static final String PREV_BUTTON = "PREV";
    private static final String NEXT_BUTTON = "NEXT";
    private static final String CLEAN_BUTTON = "CLEAN";
    private static final String AUTO_TEXT = "Auto run in: ";
    private static final String AUTO_TIME_TEXT = " ms";

    @FXML
    private Button prev;
    @FXML
    private Button next;
    @FXML
    private Button clean;
    @FXML
    private Button autoRun;
    @FXML
    private TextField autoRunTimeInterval;
    @FXML
    private Text autoText;
    @FXML
    private Text autotimeText;

    private ControlPanelFacade controlPanelFacade;
    private List<Robot> robots;
    private boolean isRunning = false;
//    private String selectedAlgorithm;//used for check if all the robots runs the same algorithm. If yes, can than run timeToTerminate.
//    private boolean singleAlgorithm = true;
//    private Stack<Boolean> preSingleAlgorithm = new Stack<>();


    private Stack<List<Robot>> robotStackPrev = new Stack<>();

    private Stack<List<Robot>> robotStackNext = new Stack<>();


    public ProgressController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../view/control_panel_component/progress_control.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initNodesText();
        autoRunListener();
        prevBtnListener();
        nextBtnListener();
        cleanBtnListener();
    }

    private void initNodesText() {
        autoRun.setText(AUTO_RUN_BTN_TO_START);
        prev.setText(PREV_BUTTON);
        next.setText(NEXT_BUTTON);
        clean.setText(CLEAN_BUTTON);
        autoText.setText(AUTO_TEXT);
        autotimeText.setText(AUTO_TIME_TEXT);
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
                                    Platform.runLater(ProgressController.this::nextAction);//update in UI thread
                                    Thread.sleep(timeInterval);
                                } catch (NumberFormatException e) {
                                    controlPanelFacade.getWarning().setText("Invalid Input! The ms must be an integer!");
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

    private void prevBtnListener() {
        prev.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!robotStackPrev.empty()) {
                    addDeepCopiedRobotList(robotStackNext, robots);//store the current to the next
                    robots.removeAll(robots);//clean the current
                    robots.addAll(robotStackPrev.pop());//restore the prev
                    controlPanelFacade.updateGarden();
//                    singleAlgorithm = preSingleAlgorithm.pop();

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
                controlPanelFacade.reset();
            }
        });
    }

    private void nextAction() {

        //remove unnecessary info (to ensure obliviousness <- KEY OF THE PROJECT)
        for (Robot robot : robots) {
            robot.iForgot();
        }
//        if (robots.size() != 0) {
//            selectedAlgorithm = robots.get(0).getAlgorithm().getClass().getSimpleName();
//        }

        addDeepCopiedRobotList(robotStackPrev, robots);//store the current to the prev
//        preSingleAlgorithm.add(singleAlgorithm);

        if (!robotStackNext.empty()) {
            robots.removeAll(robots);//clean the current
            robots.addAll(robotStackNext.pop());
        } else {
            ArrayList<Robot> localRobotsList = new ArrayList<>();
            //deep copy (partially): Ensure each of the robot's sensor has the same copy for each step (the duration of one "next" btn click)
            for (Robot robot : robots) {
                Robot newRobotInstance = null;

                try {
                    newRobotInstance = robot.deepCopy();

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    System.exit(0);
                }
                localRobotsList.add(newRobotInstance);
            }


            //run next
            Iterator<Robot> robotIterator2 = robots.iterator();

            while (robotIterator2.hasNext()) {
                Robot curr = robotIterator2.next();
//                checkSingleAlgortihm(curr);

                Point2D.Double newPosition = curr.next(localRobotsList);//ensure all the robots get the same copy in each stage (next btn)
                newPosition = boundaryCheck(newPosition);//ensure the robot will always stay within its vision.
                curr.moveTo(newPosition.getX(), newPosition.getY());//move the robot

//                if (singleAlgorithm) {
                //check if need to terminate the program: since we assume each robot runs same algorithm, we can use any robot instance to do the check

//                }
            }

            Iterator<Robot> robotIterator3 = robots.iterator();

            boolean afterAllTerminate = true;

            while (robotIterator3.hasNext()) {
                Algorithm algorithm = robotIterator3.next().getAlgorithm();
                boolean timeToTerminate = algorithm.timeToTerminate(this.robots);
                afterAllTerminate = afterAllTerminate && timeToTerminate;
            }

            if (afterAllTerminate) {
                next.setDisable(true);
                prev.setDisable(true);
//                            autoRun.setText(AUTO_RUN_BTN_TO_START);
                controlPanelFacade.getWarning().setText("Terminated!");
            }

        }
        controlPanelFacade.updateGarden();

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
            try {
                Robot robot = curr.deepCopy();
                controlPanelFacade.addListenerToGivenRobot(robot);
                deepCopied.add(robot);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        robotStack.add(deepCopied);
    }

    private Point2D.Double boundaryCheck(Point2D.Double point) {
        //negative check
        if (point.getX() < 0) {
            point.setLocation(0, point.getY());
        }
        if (point.getY() < 0) {
            point.setLocation(point.getX(), 0);
        }

        //boundary check
        if (point.getX() > controlPanelFacade.getGardenController().getGarden().getWidth()) {
            point.setLocation(controlPanelFacade.getGardenController().getGarden().getWidth(), point.getY());
        }
        if (point.getY() > controlPanelFacade.getGardenController().getGarden().getHeight()) {
            point.setLocation(point.getX(), controlPanelFacade.getGardenController().getGarden().getHeight());
        }
        return point;
    }

    public void reset() {
        robotStackPrev.clear();
        robotStackNext.clear();
//        preSingleAlgorithm.clear();
//        singleAlgorithm = true;
        prev.setDisable(false);
        next.setDisable(false);
        clean.setDisable(false);
        autoRun.setDisable(false);
        autoRun.setText(AUTO_RUN_BTN_TO_START);
        autoRunTimeInterval.setText("");
    }

    public void setControlPanelFacade(ControlPanelFacade controlPanelFacade) {
        this.controlPanelFacade = controlPanelFacade;
        setRobots();
    }

    private void setRobots() {
        this.robots = controlPanelFacade.getRobots();
    }

//    private void checkSingleAlgortihm(Robot robot) {
//        if (!robot.getAlgorithm().getClass().getSimpleName().equals(selectedAlgorithm)) {
//            singleAlgorithm = false;
//        }
//    }

}
