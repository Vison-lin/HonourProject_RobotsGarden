package controller.garden;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Robot;

import java.io.IOException;
import java.util.List;

class MultiRobotToggle extends VBox {

    @FXML
    private ComboBox<Robot> overlayRobotSelection;

    @FXML
    private Text robotTag;

    @FXML
    private Button toggleVision;

    @FXML
    private Button showStatistic;

    @FXML
    private Button deleteSelected;

    @FXML
    private Button detailSetting;

    private List<Robot> robots;

    private Robot selectedRobot;

    private GardenController gardenController;

    MultiRobotToggle(GardenController gardenController, List<Robot> robots) {
        this.gardenController = gardenController;
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(gardenController.getGarden().getScene().getWindow());
        FXMLLoader fxmlLoader;
        this.robots = robots;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("../../../resources/robot_setting_page.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            Parent parent = fxmlLoader.load();
            Scene dialogScene = new Scene(parent, 200, 400);
            dialog.setScene(dialogScene);
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initNodesText();
        boostRobotDropDownMenu();
    }

    private void initNodesText() {
        robotTag.setText("Please select one robot");
    }

    private void generateRobotSettingPage() {
        robotTag.setText(selectedRobot.getTag());
        toggleVision.setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        selectedRobot.getGraphicalDisplay().toggleVisionVisible();
                        gardenController.updateGarden();
                    }
                }
        );
        showStatistic.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                new StatisticDataDisplayHelper(gardenController, selectedRobot);
            }
        });
        deleteSelected.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                gardenController.getControlPanelFacade().removeStatisticDataByRobotTag(selectedRobot.getTag());
            }
        });
        detailSetting.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                new RobotSettingHelper(gardenController);
            }
        });
    }

    private void boostRobotDropDownMenu() {
        ObservableList<Robot> value = FXCollections.observableArrayList();
        value.addAll(robots);
        overlayRobotSelection.setItems(value);
        overlayRobotSelection.setConverter(new StringConverter<Robot>() {
            @Override
            public String toString(Robot object) {
                return object.getTag();
            }

            @Override
            public Robot fromString(String string) {
                return null;
            }
        });
        //select and display first one by default
        overlayRobotSelection.getSelectionModel().select(0);
        selectedRobot = overlayRobotSelection.getSelectionModel().getSelectedItem();
        generateRobotSettingPage();

        //add listener
        overlayRobotSelection.valueProperty().addListener(new ChangeListener<Robot>() {
            @Override
            public void changed(ObservableValue<? extends Robot> observable, Robot oldValue, Robot newValue) {
                selectedRobot = newValue;
                generateRobotSettingPage();
            }
        });
    }


}