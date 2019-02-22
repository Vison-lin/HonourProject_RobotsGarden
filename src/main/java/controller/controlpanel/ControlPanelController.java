package controller.controlpanel;

import controller.garden.GardenController;
import core.Statistic;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Robot;
import statistics.LongestRun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ControlPanelController extends VBox {

    private static final String STATUS_CONTROL_TEXT ="Status Control";
    private static final String AUTO_GENERATE_TEXT ="Auto Generate";
    private static final String CUSTOMIZE_ROBOTS_TEXT = "Customize Robots";
    private static final String SAVE_BUTTON = "Save";
    private static final String WARING_TEXT = "Warning information";

    private static final String DEFAULT_MOUSE_COORDINATE_DISPLAY = "( --, -- )";
    @FXML
    private ProgressController progressController;
    @FXML
    private AutoGenerationController autoGenerationController;
    @FXML
    private Text statuscontrolText;
    @FXML
    private Text autogenerateText;
    @FXML
    private Text customizeText;
    @FXML
    private Button SaveButton;
    @FXML
    private Text warning;
    @FXML
    private Text statisticDisplay;

    /**
     * Global robots list
     */
    private static List<Robot> robots = Collections.synchronizedList(new ArrayList<>());

    private static List<Statistic> statistics = new ArrayList<>();


    private GardenController gardenController;


    @FXML
    private RobotGenerationController robotGenerationController;
    @FXML
    private Text mouseCoordinate;

    ControlPanelFacade controlPanelFacade;

    public ControlPanelController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../../resources/control_panel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        controlPanelFacade = new ControlPanelFacade(this, autoGenerationController, progressController, robotGenerationController);
        this.progressController.setControlPanelFacade(controlPanelFacade);
        this.autoGenerationController.setControlPanelFacade(controlPanelFacade);
        this.robotGenerationController.setControlPanelFacade(controlPanelFacade);
        initNodesText();
        saveListener();
        initStatisticDisplay();
    }

    public ControlPanelFacade getControlPanelFacade() {
        return controlPanelFacade;
    }

    private void initNodesText() {
        statuscontrolText.setText(STATUS_CONTROL_TEXT);
        autogenerateText.setText(AUTO_GENERATE_TEXT);
        customizeText.setText(CUSTOMIZE_ROBOTS_TEXT);
        SaveButton.setText(SAVE_BUTTON);
        warning.setText(WARING_TEXT);
        mouseCoordinate.setText(DEFAULT_MOUSE_COORDINATE_DISPLAY);
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

    /**
     * get the list of robots.
     * @return the list of the robots
     */
    List<Robot> getRobots() {
        return robots;
    }

    /**
     * Set the list of robots.
     * Note, one should avoid to use this method, instead, change the content of the robots directly.
     * @param robots the new list of the robots
     */
    void setRobots(ArrayList<Robot> robots) {
        ControlPanelController.robots = robots;
    }


    void resetAll() {
        AlgorithmLoadingHelper helper = new AlgorithmLoadingHelper();
        robots.clear();
        gardenController.getGarden().getChildren().removeAll(gardenController.getGarden().getChildren());
        gardenController.updateGarden();
        robotGenerationController.reset();
        //clean prev and next
        progressController.reset();
        autoGenerationController.reset();
        warning.setText(WARING_TEXT);
    }

    Text getWarning() {
        return warning;
    }

    GardenController getGardenController() {
        return this.gardenController;
    }

    void setGardenController(GardenController gardenController) {
        this.gardenController = gardenController;
    }

    void setMouseCoordinate(double x, double y) {
        mouseCoordinate.setText("( " + x + ", " + y + " )");
    }

    void cleanMouseCoordinate() {
        mouseCoordinate.setText(DEFAULT_MOUSE_COORDINATE_DISPLAY);
    }

    public Text getStatisticDisplay() {
        return statisticDisplay;
    }

    private void initStatisticDisplay() {
        statisticDisplay.setText("Hi");
        //todo faked file scan for Statistic
        statistics.add(new LongestRun());

        for (Statistic statistic : statistics) {
            statisticDisplay.setText(statistic.show(robots, Arrays.asList(controlPanelFacade.getStatisticDataList())));
        }
    }

    List<Statistic> getStatistics() {
        return statistics;
    }
}
