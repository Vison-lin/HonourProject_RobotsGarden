package garden.controller.garden;

import garden.Algorithm.caculate2D;
import garden.model.Robot;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

class RobotSettingHelper extends BorderPane {

    @FXML
    private Button setAlg;

    private Robot robot;

    RobotSettingHelper(Robot robot, Window primaryStage) {
        this.robot = robot;
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("../../view/robot_setting_page.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            Parent parent = fxmlLoader.load();
            Scene dialogScene = new Scene(parent, 800, 800);
            dialog.setScene(dialogScene);
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boostAlgorithmSelectionWindow();
    }

    //todo auto search class name
    private void boostAlgorithmSelectionWindow() {
        setAlg.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                robot.setAlgorithm(new caculate2D(robot));
            }
        });
    }
}