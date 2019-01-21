package garden.controller.garden;

import garden.model.Robot;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;


public class RobotHelper extends ContextMenu {


    private ColorPicker colorPicker;

    private Robot robot;

    private String robotColor;

    private String robotVision;

    private MenuItem setColor;
    private MenuItem showVision;
    private MenuItem setVision;
    private GardenController gardenController;

    public RobotHelper(GardenController gardenController, Robot robot, String robotColor, String robotVision)
    {
        this.gardenController = gardenController;
        this.robot = robot;
        this.robotColor = robotColor;
        this.robotVision = robotVision;
        colorPicker = new ColorPicker();

        setColor = new MenuItem("Change Color");
        showVision = new MenuItem("Show Vision");
        setVision = new MenuItem("Change Vision");


        setColorPickerListener();
        setVisionListener();
        showVisionListener();
        getItems().add(setColor);
        getItems().add(showVision);
        getItems().add(setVision);
    }

    private void setColorPickerListener(){
        setColor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                robot.getGraphicalDisplay().setColor(robotColor);
                //no need to call update because it changed the node directly.
            }
        });
    }

    private void showVisionListener(){
        showVision.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                robot.getGraphicalDisplay().toggleVisionVisible();
                gardenController.updateGarden();
            }
        });
    }

    private void setVisionListener(){
        setVision.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(robotVision);
                robot.getGraphicalDisplay().setRobotVision(Integer.valueOf(robotVision));
                gardenController.updateGarden();
            }
        });
    }


}
