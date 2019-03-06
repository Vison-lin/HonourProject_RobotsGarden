package controller.controlpanel;


import core.RightClickFunction;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import model.*;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.*;

public class ProgressSectionController extends VBox {

    private static final String AUTO_RUN_BTN_TO_START = "Auto Run";
    private static final String AUTO_RUN_BTN_TO_STOP = "STOP";
    private static final String PREV_BUTTON = "PREV";
    private static final String NEXT_BUTTON = "NEXT";
    private static final String CLEAN_BUTTON = "CLEAN";
    //    private static final String AUTO_TEXT = "Speed: ";
    private static final String AUTO_TIME_TEXT = " ms";
    public static RightClickFunction rightClickFunction = RightClickFunction.Drag;

    @FXML
    private Button prev;
    @FXML
    private Button next;
    @FXML
    private Button clean;
    @FXML
    private Button autoRun;
    @FXML
//    private TextField autoRunTimeInterval;
    private Slider autoRunSpeed;
    @FXML
    private Pane autoRunHintText;
    //    @FXML
//    private Text autotimeText;
    @FXML
    private Button drag;

    @FXML
    private Button create;

    private ControlPanelFacade controlPanelFacade;
    private List<Robot> robots;
    private boolean isRunning = false;
    private HashMap<String, HashMap<String, StatisticData>> statisticDataTempStoringList = new HashMap<>();
    private boolean afterAllTerminate = false;

//    private String selectedAlgorithm;//used for check if all the robots runs the same algorithm. If yes, can than run timeToTerminate.
//    private boolean singleAlgorithm = true;
//    private Stack<Boolean> preSingleAlgorithm = new Stack<>();


    private Stack<Pair<List<Robot>, HashMap<String, HashMap<String, StatisticData>>>> robotStackPrev = new Stack<>();

    private Stack<Pair<List<Robot>, HashMap<String, HashMap<String, StatisticData>>>> robotStackNext = new Stack<>();


    public ProgressSectionController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../../resources/control_panel_component/progress_control.fxml"));
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
        rightClickFunctionSwitcherListener();
    }

    private void rightClickFunctionSwitcherListener() {
        drag.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                rightClickFunction = RightClickFunction.Drag;
            }
        });
        create.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                rightClickFunction = RightClickFunction.CreateRobot;
                System.out.println("-=-");
                System.out.println(rightClickFunction);
            }
        });
    }

    private void initNodesText() {
//        autoRun.setText(AUTO_RUN_BTN_TO_START);
//        prev.setText(PREV_BUTTON);
//        next.setText(NEXT_BUTTON);
//        clean.setText(CLEAN_BUTTON);
//        autoRunHintText.setText(AUTO_TEXT);
//        autotimeText.setText(AUTO_TIME_TEXT);
    }

    private void autoRunListener() {
        autoRunSpeed.setMin(0);
        autoRunSpeed.setMax(5000);
        autoRunSpeed.setValue(2500);
        autoRunSpeed.setMajorTickUnit(500);
        autoRunSpeed.setMinorTickCount(10);
        autoRunSpeed.setShowTickMarks(true);
        autoRun.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (isRunning) {//if is already auto running, stop it
                    isRunning = false;
                    autoRun.setStyle("-fx-background-radius: 5em; -fx-min-width: 3em; -fx-min-height: 3em; -fx-min-height: 3em; -fx-max-height: 3em; -fx-background-image: url(/img/autorun.png); -fx-background-size: contain;");
                    prev.setDisable(false);
                    if (!afterAllTerminate) {
                        next.setDisable(false);
                    }
                    drag.setDisable(false);
                    create.setDisable(false);
                    clean.setDisable(false);
                    autoRunSpeed.setDisable(false);
                } else {
                    System.out.println("===");
                    isRunning = true;
                    autoRun.setStyle("-fx-background-radius: 5em; -fx-min-width: 3em; -fx-min-height: 3em; -fx-min-height: 3em; -fx-max-height: 3em; -fx-background-image: url(/img/stop.png); -fx-background-size: contain;");
                    Task task = new Task<Void>() {//create a new task
                        @Override
                        protected Void call() throws InterruptedException {
                            while (isRunning) {
                                try {
                                    int timeInterval = (int) autoRunSpeed.getValue() + 1;// Note: by cast, 6.99 -> 6
                                    Platform.runLater(ProgressSectionController.this::nextAction);//update in UI thread
                                    Thread.sleep(timeInterval);
                                } catch (NumberFormatException e) {
                                    controlPanelFacade.getWarning().setText("Invalid Input! The ms must be an integer!");
                                    isRunning = false;
                                }
                            }

                            prev.setDisable(false);
                            if (!afterAllTerminate) {
                                next.setDisable(false);
                            }
                            drag.setDisable(false);
                            create.setDisable(false);
                            clean.setDisable(false);
                            autoRunSpeed.setDisable(false);
                            autoRun.setStyle("-fx-background-radius: 5em; -fx-min-width: 3em; -fx-min-height: 3em; -fx-min-height: 3em; -fx-max-height: 3em; -fx-background-image: url(/img/autorun.png); -fx-background-size: contain;");
                            return null;
                        }
                    };
                    prev.setDisable(true);
                    next.setDisable(true);
                    drag.setDisable(true);
                    create.setDisable(true);
                    clean.setDisable(true);
                    autoRunSpeed.setDisable(true);
                    //todo FRED: Disable more btns?
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
                    addDeepCopiedRobotList(robotStackNext, robots, statisticDataTempStoringList);//store the current to the next
                    robots.removeAll(robots);//clean the current
                    Pair<List<Robot>, HashMap<String, HashMap<String, StatisticData>>> pop = robotStackPrev.pop();
                    robots.addAll(pop.getKey());//restore the prev
                    statisticDataTempStoringList.clear();
                    statisticDataTempStoringList.putAll(pop.getValue());
                    controlPanelFacade.updateGarden();
//                    singleAlgorithm = preSingleAlgorithm.pop();
                    showStatistic();
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

        addDeepCopiedRobotList(robotStackPrev, robots, statisticDataTempStoringList);//store the current to the prev
//        preSingleAlgorithm.add(singleAlgorithm);

        if (!robotStackNext.empty()) {
            robots.removeAll(robots);//clean the current
            Pair<List<Robot>, HashMap<String, HashMap<String, StatisticData>>> pop = robotStackNext.pop();
            robots.addAll(pop.getKey());
            statisticDataTempStoringList.clear();
            statisticDataTempStoringList.putAll(pop.getValue());
            showStatistic();
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
                Point2D.Double newPosition = curr.next(localRobotsList);//ensure all the robots get the same copy in each stage (next btn)
                newPosition = boundaryCheck(newPosition);//ensure the robot will always stay within its vision.
                curr.moveTo(newPosition.getX(), newPosition.getY());//move the robot

//                if (singleAlgorithm) {
                //check if need to terminate the program: since we assume each robot runs same algorithm, we can use any robot instance to do the check

//                }
            }

            Iterator<Robot> robotIterator3 = robots.iterator();

            afterAllTerminate = true;
            while (robotIterator3.hasNext()) {
                Robot curr = robotIterator3.next();
                Algorithm algorithm = curr.getAlgorithm();
                boolean timeToTerminate = algorithm.timeToTerminate(this.robots);
                if (algorithm instanceof Statisticable) {
                    Statisticable statisticable = (Statisticable) algorithm;
                    HashMap<String, StatisticData> newStatisticDataList;

                    if (!statisticDataTempStoringList.containsKey(curr.getTag())) {
                        throw new IllegalStateException("Unregistered Statisticable Robot exist! Possible caused by: unrecognized robot TAG change");
                    }

                    HashMap<String, StatisticData> oldStatisticDataList = statisticDataTempStoringList.get(curr.getTag());
                    newStatisticDataList = statisticable.update(oldStatisticDataList);
                    statisticDataTempStoringList.replace(curr.getTag(), newStatisticDataList);

                }
                afterAllTerminate = afterAllTerminate && timeToTerminate;
            }

            if (afterAllTerminate) {
                next.setDisable(true);
                isRunning = false;
//                prev.setDisable(true);
//                            autoRun.setText(AUTO_RUN_BTN_TO_START);
                controlPanelFacade.getWarning().setText("Terminated!");
            }

            showStatistic();

        }
        controlPanelFacade.updateGarden();

    }

    /**
     * Deep copy the robots list and add it to either robotStackPrev or robotStackNext
     * @param robotStack either the robotStackPrev or the robotStackNext
     * @param robotList  the robot list that needed to be deep copied and added
     * @param statisticDataList
     */
    private void addDeepCopiedRobotList(Stack<Pair<List<Robot>, HashMap<String, HashMap<String, StatisticData>>>> robotStack, List<Robot> robotList, HashMap<String, HashMap<String, StatisticData>> statisticDataList) {
        Iterator<Robot> iterator = robotList.iterator();
        Pair<List<Robot>, HashMap<String, HashMap<String, StatisticData>>> deepCopied = new Pair<>(new ArrayList<>(), new HashMap<>());
        while (iterator.hasNext()) {
            Robot curr = iterator.next();
            try {
                Robot robot = curr.deepCopy();
                controlPanelFacade.addListenerToGivenRobot(robot);
                deepCopied.getKey().add(robot);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        Iterator<Map.Entry<String, HashMap<String, StatisticData>>> iterator1 = statisticDataList.entrySet().iterator();
        while (iterator1.hasNext()) {
            Map.Entry<String, HashMap<String, StatisticData>> curr = iterator1.next();


            HashMap<String, StatisticData> deepCopiedDataList = new HashMap<>();
            Iterator<Map.Entry<String, StatisticData>> statisticDataIterator = curr.getValue().entrySet().iterator();
            while (statisticDataIterator.hasNext()) {
                Map.Entry<String, StatisticData> statisticData = statisticDataIterator.next();
                StatisticData deepCopiedData = statisticData.getValue().deepCopy();
                deepCopiedDataList.put(
                        statisticData.getKey(), // StatisticData TAG
                        deepCopiedData);
            }
            deepCopied.getValue().put(
                    curr.getKey(), //robot TAG
                    deepCopiedDataList);
        }
        robotStack.add(deepCopied);
    }

    private Point2D.Double boundaryCheck(Point2D.Double point) {
//        //negative check
//        if (point.getX() < 0) {
//            point.setLocation(0, point.getY());
//        }
//        if (point.getY() < 0) {
//            point.setLocation(point.getX(), 0);
//        }
//
//        //boundary check
//        if (point.getX() > controlPanelFacade.getGardenController().getGarden().getWidth()) {
//            point.setLocation(controlPanelFacade.getGardenController().getGarden().getWidth(), point.getY());
//        }
//        if (point.getY() > controlPanelFacade.getGardenController().getGarden().getHeight()) {
//            point.setLocation(point.getX(), controlPanelFacade.getGardenController().getGarden().getHeight());
//        }
//        return point;
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
        autoRun.setStyle("-fx-background-radius: 5em; -fx-min-width: 3em; -fx-min-height: 3em; -fx-min-height: 3em; -fx-max-height: 3em; -fx-background-image: url(/img/autorun.png); -fx-background-size: contain;");
//        autoRunTimeInterval.setText("");
    }

    public HashMap<String, HashMap<String, StatisticData>> getStatisticDataTempStoringList() {
        return statisticDataTempStoringList;
    }

    public void setControlPanelFacade(ControlPanelFacade controlPanelFacade) {
        this.controlPanelFacade = controlPanelFacade;
        setRobots();
    }

    private void setRobots() {
        this.robots = controlPanelFacade.getRobots();
    }

    private void showStatistic() {
        String display = "";
        for (Statistic statistic : controlPanelFacade.getStatisticList()) {
            if (statistic.showingCondition(robots) && ControlPanelFacade.ENABLE_STATISTIC) {
                Collection<HashMap<String, StatisticData>> list = controlPanelFacade.getStatisticDataList().values();
                display += statistic.tag();
                display += ": ";
                display += statistic.result(robots, new ArrayList<>(list));
                display += "\n";
            }
        }
        if (display.equals("")) {
            display = StatisticPageController.STATISTIC_UNAVAILABLE_DISPLAY;
        }
        controlPanelFacade.getStatisticDisplay().setText(display);
    }

//    private void checkSingleAlgortihm(Robot robot) {
//        if (!robot.getAlgorithm().getClass().getSimpleName().equals(selectedAlgorithm)) {
//            singleAlgorithm = false;
//        }
//    }

}
