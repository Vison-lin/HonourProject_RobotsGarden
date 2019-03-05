package controller.controlpanel;


import controller.garden.GardenController;
import core.Statistic;
import core.StatisticData;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import model.Robot;

import java.util.HashMap;
import java.util.List;

public class ControlPanelFacade {

    private ControlPanelController controlPanelController;

    public static double ROBOT_NAME_COUNTER = RobotGenerationPageController.ROBOT_NAME_COUNTER;
    private ProgressSectionController progressSectionController;
    private RobotGenerationPageController robotGenerationPageController;
    private StatisticPageController statisticPageController;
    private SettingPageController settingPageController;
    private GeneralPageController generalPageController;

    public static final String STATISTIC_UNAVAILABLE_DISPLAY = "No Statistic Available!";

    public static final String STATISTIC_DEFAULT_DISPLAY = "Click Next Button to See Available Statistic Data";

    public static boolean ENABLE_STATISTIC = true;

    ControlPanelFacade(ControlPanelController controlPanelController, ProgressSectionController progressSectionController, RobotGenerationPageController robotGenerationPageController, StatisticPageController statisticPageController, SettingPageController settingPageController, GeneralPageController generalPageController) {
        this.controlPanelController = controlPanelController;
        this.progressSectionController = progressSectionController;
        this.robotGenerationPageController = robotGenerationPageController;
        this.statisticPageController = statisticPageController;
        this.settingPageController = settingPageController;
        this.generalPageController = generalPageController;
    }

    /*
                        === === === PUBLIC CONTROL PANEL API: OPEN FOR EVERY CLASSES === === ===
     */

    /**
     * get the list of robots.
     *
     * @return the list of the robots
     */
    public List<Robot> getRobots() {
        return controlPanelController.getRobots();
    }

    public double getSelectedRobotVision() {
        return robotGenerationPageController.getSelectedRobotVision();
    }

    public Paint getSelectedRobotColor() {
        return robotGenerationPageController.getSelectedRobotColor();
    }

    public Robot robotGenerator(String new_robot, double x, double y) {
        return this.robotGenerationPageController.robotGenerator(new_robot, x, y);
    }

    public void setMouseCoordinate(double x, double y) {
        controlPanelController.setMouseCoordinate(x, y);
    }

    public void cleanMouseCoordinate() {
        controlPanelController.cleanMouseCoordinate();
    }

    /*
                                        === === === END OF PUBLIC CONTROL PANEL APIs === === ===
     */

    /*
                        === === === PRIVATE CONTROL PANEL API: ONLY OPEN TO THOSE CLASSES WITHIN THIS PACKAGE === === ===
     */

    void addListenerToGivenRobot(Robot robot) {
        this.controlPanelController.getGardenController().addListenerToRobot(robot);
    }

    void updateGarden() {
        this.controlPanelController.getGardenController().updateGarden();
    }

    GardenController getGardenController() {
        return controlPanelController.getGardenController();
    }

    public void setGardenController(GardenController gardenController) {
        this.controlPanelController.setGardenController(gardenController);
    }

    Text getWarning() {
        return controlPanelController.getWarning();
    }

    void reset() {
        controlPanelController.resetAll();
    }

    Text getStatisticDisplay() {
        return statisticPageController.getStatisticDisplay();
    }

    public HashMap<String, StatisticData> getStatisticDataByRobotTag(String robotTag) {
        return progressSectionController.getStatisticDataTempStoringList().get(robotTag);
    }

    void insertToStatisticDataTempStoringList(String robotTag, HashMap<String, StatisticData> newStatisticData) {
        progressSectionController.getStatisticDataTempStoringList().put(robotTag, newStatisticData);
    }

    public void removeStatisticDataByRobotTag(String robotTag) {
        progressSectionController.getStatisticDataTempStoringList().remove(robotTag);
    }

    HashMap<String, HashMap<String, StatisticData>> getStatisticDataList() {
        return progressSectionController.getStatisticDataTempStoringList();
    }

    List<Statistic> getStatisticList() {
        return statisticPageController.getStatistics();
    }

        /*
                                       === === === END OF PRIVATE CONTROL PANEL APIs === === ===
     */
}
