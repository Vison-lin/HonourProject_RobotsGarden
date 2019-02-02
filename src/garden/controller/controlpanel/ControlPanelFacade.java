package garden.controller.controlpanel;

import garden.controller.garden.GardenController;
import garden.model.Robot;
import garden.model.RobotGraphicalDisplay;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
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

    public Robot robotGenerator(String new_robot, double x, double y, RobotGraphicalDisplay robotGraphicalDisplay) {
        return this.robotGenerationController.robotGenerator(new_robot, x, y, robotGraphicalDisplay);
    }

    public RobotGraphicalDisplay getCustomizedRobotGraphicalDisplay() {
        Circle robotPosition = new Circle(3, Color.WHITE);
        Circle robotBody = new Circle(10, this.getSelectedRobotColor());
        Circle robotBorder = new Circle(11, Color.WHITE);
        Circle robotVision = new Circle(this.getSelectedRobotVision(), Color.LIGHTBLUE);
        return new RobotGraphicalDisplay(robotPosition, robotBody, robotBorder, robotVision, false);
    }

    /*
                                        === === === END OF PUBLIC CONTROL PANEL APIs === === ===
     */

    /*
                        === === === PRIVATE CONTROL PANEL API: ONLY OPEN TO THOSE CLASSES WITHIN THIS PACKAGE === === ===
     */

    void addGeneratedRobotToGarden(Robot robot) {
        this.controlPanelController.getGardenController().addGeneratedRobotToGarden(robot);
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
