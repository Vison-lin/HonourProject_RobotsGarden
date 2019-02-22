package controller.garden;


import controller.controlpanel.ControlPanelFacade;
import core.DisplayAdapter;
import core.Statisticable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Paint;
import javafx.util.Pair;
import model.Robot;

import java.util.ArrayList;
import java.util.List;

public class SingleRobotToggle extends ContextMenu {

    private static final String SHOW_VISION_TO_SHOW = "show vision";
    private static final String SHOW_VISION_TO_NOT_SHOW = "Hide vision";
    private static final String CHANGE_VISION = "Change Vision";
    private static final String CHANGE_COLOR = "Change Color";
    private static final String DELETION = "Delete";
    private static final String STATISITC_INFO = "Show Statistic";
    private Robot robot;
    private Paint robotColor;
    private double robotVision;
    private MenuItem setColor;
    private MenuItem showVision;
    private MenuItem setVision;
    private MenuItem statisticInfo;
    private MenuItem delete;
    private List<Pair<DisplayAdapter, MenuItem>> setDisplayComponentsVisibility = new ArrayList<>();
    private GardenController gardenController;
    private ControlPanelFacade controlPanelFacade;

    SingleRobotToggle(GardenController gardenController, Robot robot, ControlPanelFacade controlPanelFacade) {
        this.gardenController = gardenController;
        this.robot = robot;
        this.robotColor = controlPanelFacade.getSelectedRobotColor();
        this.robotVision = controlPanelFacade.getSelectedRobotVision();
        this.controlPanelFacade = controlPanelFacade;

        setColor = new MenuItem(CHANGE_COLOR);
        showVision = new MenuItem(SHOW_VISION_TO_SHOW);
        setVision = new MenuItem(CHANGE_VISION);
        statisticInfo = new MenuItem(STATISITC_INFO);
        delete = new MenuItem(DELETION);

        boolean isShowingVision = !robot.getGraphicalDisplay().toggleVisionVisible();
        if (isShowingVision) {
            showVision.setText(SHOW_VISION_TO_NOT_SHOW);
        } else {
            showVision.setText(SHOW_VISION_TO_SHOW);
        }
        robot.getGraphicalDisplay().toggleVisionVisible();//toggle again to make it unchanged.

        for (DisplayAdapter displayAdapter : robot.getGraphicalDisplay().getBottomLayers()) {
            MenuItem newDisplayComponent = new MenuItem(displayAdapter.getComponentTag());
            setDisplayComponentsVisibility.add(new Pair<>(displayAdapter, newDisplayComponent));
            getItems().add(newDisplayComponent);
        }


        setColorPickerListener();
        setVisionListener();
        showVisionListener();
        if (robot.getAlgorithm() instanceof Statisticable) {
            setStatisticInfoDisplayListener();
        }
        setDeletionListener();
        setDisplayComponentsVisibilityListener();


        getItems().add(setColor);
        getItems().add(showVision);
        getItems().add(setVision);
        if (robot.getAlgorithm() instanceof Statisticable) {
            getItems().add(statisticInfo);
        }
        getItems().add(delete);


    }

    private void setStatisticInfoDisplayListener() {
        statisticInfo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new StatisticDataDisplayHelper(gardenController, robot);
            }
        });
    }

    private void setDeletionListener() {
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Can completely clean the robot.
                // Example: delete a default robot cleans three layers while delete a robot that tuns on vision delete 4 layers.
                // It works because in the gardenController, a certain layer has been added to the pane IFF it is visible
                controlPanelFacade.getRobots().remove(robot);
                gardenController.updateGarden();
                gardenController.getControlPanelFacade().removeStatisticDataByRobotTag(robot.getTag());
            }
        });
    }

    private void setDisplayComponentsVisibilityListener() {
        for (Pair<DisplayAdapter, MenuItem> pair : setDisplayComponentsVisibility) {
            pair.getValue().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // No need deep copy here because the sensor will do the deep copy.
                    // Note that the reason why we need another deep copy in the NEXT action method is because we need to manipulate on the deep copied version and apply the result to the original one after ALL ROBOT FINISHED the calculation for current state. In other word, to ensure concurrently calculation.
                    robot.getSensor().setGlobalRobots(controlPanelFacade.getRobots());
                    pair.getKey().update();
                    gardenController.updateGarden();
                }
            });
        }
    }

    private void setColorPickerListener() {
        setColor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                robot.getGraphicalDisplay().setColor(robotColor);
                //no need to call update because it changed the node directly.
            }
        });
    }

    private void showVisionListener() {
        showVision.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                robot.getGraphicalDisplay().toggleVisionVisible();
                gardenController.updateGarden();
            }
        });
    }

    private void setVisionListener() {
        setVision.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                robot.getGraphicalDisplay().setRobotVision(robotVision);
                gardenController.updateGarden();
            }
        });
    }


}
