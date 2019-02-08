package garden.controller.garden;

import garden.model.Robot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.List;

class MultiRobotToggle extends BorderPane {

    @FXML
    private ComboBox<Robot> overlayRobotSelection;

    private List<Robot> robots;

    MultiRobotToggle(GardenController gardenController, List<Robot> robots) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(gardenController.getGarden().getScene().getWindow());
        FXMLLoader fxmlLoader;
        this.robots = robots;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("../../view/robot_setting_page.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            Parent parent = fxmlLoader.load();
            Scene dialogScene = new Scene(parent, 200, 400);
            dialog.setScene(dialogScene);
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boostRobotDropDownMenu();
        gardenController.updateGarden();

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
    }


}