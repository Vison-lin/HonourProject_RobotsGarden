package garden.controller.garden;

import garden.model.Robot;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

class RobotSettingHelper extends BorderPane {

    @FXML
    private Button setAlg;

    private Robot robot;

    @FXML
    private ColorPicker colorPicker;

    RobotSettingHelper(Robot robot, Window primaryStage) {
        this.robot = robot;
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        FXMLLoader fxmlLoader;
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
       boostAlgorithmSelectionWindow();

    }

    //todo auto search class name
    private void boostAlgorithmSelectionWindow() {
//        setAlg.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                System.out.println("!!");
////                robot.setAlgorithm(new GatheringAlgorithm(robot));
//                robot.getGraphicalDisplay().
//            }
//        });
        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

//                System.out.println("!!!!!!!");
                colorPicker.setValue(Color.RED);
            }
        });
    }





}