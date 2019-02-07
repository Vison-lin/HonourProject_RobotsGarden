package garden.controller.controlpanel;

import garden.controller.garden.GardenController;
import garden.model.Robot;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.util.List;

public class ControlPanelFacade {

    private ControlPanelController controlPanelController;

    private AutoGenerationController autoGenerationController;

    private ProgressController progressController;

    private RobotGenerationController robotGenerationController;

    ControlPanelFacade(ControlPanelController controlPanelController, AutoGenerationController autoGenerationController, ProgressController progressController, RobotGenerationController robotGenerationController) {
        this.controlPanelController = controlPanelController;
        this.autoGenerationController = autoGenerationController;
        this.progressController = progressController;
        this.robotGenerationController = robotGenerationController;
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
        return robotGenerationController.getSelectedRobotVision();
    }

    public Paint getSelectedRobotColor() {
        return robotGenerationController.getSelectedRobotColor();
    }

    public Robot robotGenerator(String new_robot, double x, double y) {
        return this.robotGenerationController.robotGenerator(new_robot, x, y);
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

        /*
                                       === === === END OF PRIVATE CONTROL PANEL APIs === === ===
     */
}
