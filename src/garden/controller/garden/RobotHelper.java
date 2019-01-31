package garden.controller.garden;

import garden.core.DisplayComponent;
import garden.model.Robot;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Paint;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;


public class RobotHelper extends ContextMenu {


    private ColorPicker colorPicker;

    private Robot robot;

    private Paint robotColor;

    private double robotVision;

    private MenuItem setColor;
    private MenuItem showVision;
    private MenuItem setVision;
    private List<Pair<DisplayComponent, MenuItem>> setDisplayComponentsVisibility = new ArrayList<>();
    private GardenController gardenController;

    public RobotHelper(GardenController gardenController, Robot robot, Paint robotColor, double robotVision) {
        this.gardenController = gardenController;
        this.robot = robot;
        this.robotColor = robotColor;
        this.robotVision = robotVision;
        colorPicker = new ColorPicker();

        setColor = new MenuItem("Change Color");
        showVision = new MenuItem("Show Vision");
        setVision = new MenuItem("Change Vision");
        System.out.println(robot.getGraphicalDisplay().getBottomLayers().size());
        for (DisplayComponent displayComponent : robot.getGraphicalDisplay().getBottomLayers()) {
            MenuItem newDisplayComponent = new MenuItem(displayComponent.getComponentTag());
            System.out.println(displayComponent.getComponentTag());
            setDisplayComponentsVisibility.add(new Pair<>(displayComponent, newDisplayComponent));
            getItems().add(newDisplayComponent);
        }


        setColorPickerListener();
        setVisionListener();
        showVisionListener();
        setDisplayComponentsVisibilityListener();
        getItems().add(setColor);
        getItems().add(showVision);
        getItems().add(setVision);

    }

    private void setDisplayComponentsVisibilityListener() {
        for (Pair<DisplayComponent, MenuItem> pair : setDisplayComponentsVisibility) {
            pair.getValue().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    pair.getKey().setVisible(!pair.getKey().isVisible());
                    pair.getKey().update();
                    gardenController.updateGarden();
                }
            });
        }
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
                robot.getGraphicalDisplay().setRobotVision(robotVision);
                gardenController.updateGarden();
            }
        });
    }


}
