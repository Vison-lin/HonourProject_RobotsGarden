package garden.controller.garden;

import garden.model.Robot;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class RobotHelper extends ContextMenu {


    private ColorPicker colorPicker;

    private Robot robot;

    private String robotColor;

    private String robotVision;

    private MenuItem setColor;
    private MenuItem showVision;
    private MenuItem setVision;

    public RobotHelper(Robot robot, String robotColor,String robotVision)
    {
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

            }
        });
    }

    private void showVisionListener(){
        showVision.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                robot.getGraphicalDisplay().toggleVisionVisible();

            }
        });
    }

    private void setVisionListener(){
        setVision.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                robot.getGraphicalDisplay().setRobotVision(Integer.valueOf(robotVision));

            }
        });
    }


}
