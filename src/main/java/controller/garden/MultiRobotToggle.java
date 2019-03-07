package controller.garden;


import controller.controlpanel.ControlPanelFacade;
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
import model.Statisticable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

    @FXML
    private Text numberRobot;
    @FXML
    private Text listAlgorithm;
    @FXML
    private VBox listBox;

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
            fxmlLoader = new FXMLLoader(getClass().getResource("/robot_setting_page.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            Parent parent = fxmlLoader.load();
            Scene dialogScene = new Scene(parent);
            dialog.setScene(dialogScene);
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initNodesText();
        initialInfor(robots);
        boostRobotDropDownMenu();
    }

    private void initNodesText() {
        robotTag.setText("Please select one robot");
    }

    private void initialInfor(List<Robot> robots){
        numberRobot.setText("Total number of robots: "+robots.size());
        List<String> alg = new ArrayList<>();
        for(Robot robot:robots){
             alg.add(robot.getAlgorithm().getClass().getSimpleName());
        }
        Collections.sort(alg);
        String curr = alg.get(0);
        String reuslt ="\r\n";
        int count = 0;
        int index =1;
        for(String item: alg){
            if(item.equals(curr)){
                count++;
                if (index==alg.size()) {
                    reuslt += curr + ": " + count + "\r\n";
                    count = 0;
                    curr = item;
                }
            }else{
                reuslt+=curr+": "+count+"\r\n"+"\r\n";
                count=1;
                curr = item;
            }
            index++;
        }
        listBox.setPrefHeight(30*robots.size());
        listAlgorithm.setText(reuslt);
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
        if (selectedRobot.getAlgorithm() instanceof Statisticable) {
            showStatistic.setDisable(false);
            showStatistic.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    new StatisticDataDisplayHelper(gardenController, selectedRobot);
                }
            });
        } else {
            showStatistic.setDisable(true);
        }

        deleteSelected.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                gardenController.getControlPanelFacade().getRobots().remove(selectedRobot);
                gardenController.updateGarden();
                gardenController.getControlPanelFacade().removeStatisticDataByRobotTag(selectedRobot.getTag());
                ControlPanelFacade.ENABLE_STATISTIC = false;
            }
        });
        detailSetting.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                new RobotSettingHelper(gardenController,selectedRobot);
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