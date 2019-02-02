package garden.controller.garden;

import garden.controller.controlpanel.ControlPanelFacade;
import garden.core.DisplayAdapter;
import garden.model.Robot;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Paint;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;


public class RobotToggle extends ContextMenu {

    private Robot robot;

    private Paint robotColor;

    private double robotVision;


    private MenuItem setColor;
    private MenuItem showVision;
    private MenuItem setVision;
    private List<Pair<DisplayAdapter, MenuItem>> setDisplayComponentsVisibility = new ArrayList<>();
    private GardenController gardenController;
    private static final String SHOW_VISION_TO_SHOW = "show vision";
    private static final String SHOW_VISION_TO_NOT_SHOW = "Hide vision";
    private static final String CHANGE_VISION = "Change Vision";
    private static final String CHANGE_COLOR = "Change Color";
    private ControlPanelFacade controlPanelFacade;

    RobotToggle(GardenController gardenController, Robot robot, ControlPanelFacade controlPanelFacade) {
        this.gardenController = gardenController;
        this.robot = robot;
        this.robotColor = controlPanelFacade.getSelectedRobotColor();
        this.robotVision = controlPanelFacade.getSelectedRobotVision();
        this.controlPanelFacade = controlPanelFacade;

        setColor = new MenuItem(CHANGE_COLOR);
        showVision = new MenuItem(SHOW_VISION_TO_SHOW);
        setVision = new MenuItem(CHANGE_VISION);

        boolean isShowingVision = !robot.getGraphicalDisplay().toggleVisionVisible();
        if (isShowingVision) {
            showVision.setText(SHOW_VISION_TO_NOT_SHOW);
        } else {
            showVision.setText(SHOW_VISION_TO_SHOW);
        }
        robot.getGraphicalDisplay().toggleVisionVisible();//toggle again to make it unchanged.

        for (DisplayAdapter displayAdapter : robot.getGraphicalDisplay().getBottomLayers()) {
            MenuItem newDisplayComponent = new MenuItem(displayAdapter.getComponentTag());
            System.out.println(displayAdapter.getComponentTag());
            setDisplayComponentsVisibility.add(new Pair<>(displayAdapter, newDisplayComponent));
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
        for (Pair<DisplayAdapter, MenuItem> pair : setDisplayComponentsVisibility) {
            pair.getValue().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    robot.getSensor().setGlobalRobots(controlPanelFacade.getRobots());//todo need deep copy?
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
