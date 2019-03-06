package controller.controlpanel;

import controller.garden.GardenController;
import core.AlgorithmLoadingHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Robot;

import java.io.IOException;
import java.util.ArrayList;
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
    private ProgressSectionController progressSectionController;

    @FXML
    private StatisticPageController statisticPageController;

    @FXML
    private SettingPageController settingPageController;

    @FXML
    private Text autogenerateText;
    @FXML
    private Text customizeText;
    //    @FXML
//    private Button SaveButton;
    @FXML
    private Text warning;

    @FXML
    private Tab robotCustomizationPageBtn;

    @FXML
    private Tab statisticPageBtn;

    @FXML
    private Tab settingPageBtn;

    /**
     * Global robots list
     */
    private static List<Robot> robots = Collections.synchronizedList(new ArrayList<>());




    private GardenController gardenController;


    @FXML
    private RobotGenerationPageController robotGenerationPageController;
    @FXML
    private Text mouseCoordinate;

    ControlPanelFacade controlPanelFacade;

    public ControlPanelController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/control_panel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        controlPanelFacade = new ControlPanelFacade(this, progressSectionController, robotGenerationPageController, statisticPageController, settingPageController);
        this.progressSectionController.setControlPanelFacade(controlPanelFacade);
//        this.autoGenerationController.setControlPanelFacade(controlPanelFacade);
        this.robotGenerationPageController.setControlPanelFacade(controlPanelFacade);
        this.statisticPageController.setControlPanelFacade(controlPanelFacade);
        this.settingPageController.setControlPanelFacade(controlPanelFacade);
        initNodesText();

        //set the default page as general page
//        disableAllPages();
//        enableRobotGenerationPage();
//        saveListener();

//        pageSwitchingHandler();
    }

//    private void pageSwitchingHandler() {
//
//        robotCustomizationPageBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                disableAllPages();
//                enableRobotGenerationPage();
//            }
//        });
//
//        statisticPageBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                disableAllPages();
//                enableStatisticPage();
//            }
//        });
//
//        settingPageBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                disableAllPages();
//                enableSettingPage();
//            }
//        });
//
//
//    }

    public ControlPanelFacade getControlPanelFacade() {
        return controlPanelFacade;
    }

    private void initNodesText() {
//        autogenerateText.setText(AUTO_GENERATE_TEXT);
//        customizeText.setText(CUSTOMIZE_ROBOTS_TEXT);
//        SaveButton.setText(SAVE_BUTTON);
        warning.setText(WARING_TEXT);
        mouseCoordinate.setText(DEFAULT_MOUSE_COORDINATE_DISPLAY);
    }

//    private void saveListener(){
//        SaveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                if(robots.size()!=0){
//                File f = new File("C:/Users/user/Desktop/a.txt");
//                try {
//                    FileOutputStream fop = new FileOutputStream(f);
//                    OutputStreamWriter writer = new OutputStreamWriter(fop);
//                    for(Robot robot:robots){
//
//                        writer.append("Tag: " + robot.getTag() + ", Algorithm: ??, " + "selectedRobotVision: " + (int) robot.getVision());
//                        writer.append("\r\n");
//
//                    }
//                    writer.close();
//                    fop.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            }
//        });
//
//
//    }

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
        robotGenerationPageController.reset();
        //clean prev and next
        progressSectionController.reset();
//        autoGenerationController.reset();
        warning.setText(WARING_TEXT);
        progressSectionController.getStatisticDataTempStoringList().clear();
        ControlPanelFacade.ENABLE_STATISTIC = true;
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

//    private void disableSettingPage() {
//        settingPageController.setManaged(false);
//        settingPageController.setVisible(false);
//    }
//
//    private void enableSettingPage() {
//        settingPageController.setManaged(true);
//        settingPageController.setVisible(true);
//    }
//
//    private void disableStatisticPage() {
//        statisticPageController.setManaged(false);
//        statisticPageController.setVisible(false);
//    }
//
//    private void enableStatisticPage() {
//        statisticPageController.setManaged(true);
//        statisticPageController.setVisible(true);
//    }
//
//    private void disableRobotGenerationPage() {
//        robotGenerationPageController.setManaged(false);
//        robotGenerationPageController.setVisible(false);
//    }
//
//    private void enableRobotGenerationPage() {
//        robotGenerationPageController.setManaged(true);
//        robotGenerationPageController.setVisible(true);
//    }
//
//    private void disableAllPages() {
//        disableRobotGenerationPage();
//        disableStatisticPage();
//        disableSettingPage();
//    }

}
