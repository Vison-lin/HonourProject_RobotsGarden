package controller.garden;


import controller.controlpanel.ControlPanelFacade;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.util.Pair;
import model.DisplayAdapter;
import model.Robot;
import model.Statisticable;

import java.util.ArrayList;
import java.util.List;

public class SingleRobotToggle extends ContextMenu {

    private static final String SHOW_VISION_TO_SHOW = "Show vision";
    private static final String SHOW_VISION_TO_NOT_SHOW = "Hide vision";
    private static final String SETTING_PAGE = "Setting";
    private static final String CHANGE_COLOR = "Change Color";
    private static final String DELETION = "Delete";
    private static final String STATISITC_INFO = "Show Statistic";
    private Robot robot;
    private MenuItem setColor;
    private MenuItem showVision;
    private MenuItem showSetting;
    private MenuItem statisticInfo;
    private MenuItem delete;
    private List<Pair<DisplayAdapter, MenuItem>> setDisplayComponentsVisibility = new ArrayList<>();
    private GardenController gardenController;
    private ControlPanelFacade controlPanelFacade;

    SingleRobotToggle(GardenController gardenController, Robot robot, ControlPanelFacade controlPanelFacade) {
        this.gardenController = gardenController;
        this.robot = robot;
        this.controlPanelFacade = controlPanelFacade;

        showVision = new MenuItem(SHOW_VISION_TO_SHOW);
        showSetting = new MenuItem(SETTING_PAGE);
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


        setVisionListener();
        showVisionListener();
        if (robot.getAlgorithm() instanceof Statisticable) {
            setStatisticInfoDisplayListener();
        }
        setDeletionListener();
        setDisplayComponentsVisibilityListener();

        getItems().add(showVision);
        getItems().add(showSetting);
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
                ControlPanelFacade.ENABLE_STATISTIC = false;
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
//                    pair.getKey().getDisplayPattern()//todo adjust display
                    gardenController.updateGarden();
                }
            });
        }
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
        showSetting.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new RobotSettingHelper(gardenController,robot);
            }
        });
    }


}
