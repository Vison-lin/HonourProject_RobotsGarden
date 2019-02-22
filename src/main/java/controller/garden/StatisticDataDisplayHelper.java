package controller.garden;

import core.StatisticData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Robot;

import java.io.IOException;
import java.util.List;

public class StatisticDataDisplayHelper extends VBox {

    @FXML
    private Text statisticDataDisplay;

    public StatisticDataDisplayHelper(GardenController gardenController, Robot robot) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(gardenController.getGarden().getScene().getWindow());
        FXMLLoader fxmlLoader;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("../../../resources/robot_statistic_display.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            Parent parent = fxmlLoader.load();
            Scene dialogScene = new Scene(parent, 200, 400);
            dialog.setScene(dialogScene);
            dialog.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String displayInfo = "";
        System.out.println(robot);
        List<StatisticData> statisticDataList = gardenController.getControlPanelFacade().getStatisticDataByRobotTag(robot.getTag());
//        System.out.println(statisticDataList.get(0));
        for (StatisticData statisticData : statisticDataList) {
            displayInfo = displayInfo + "\n" + robot + ": " + statisticData.display();
        }
        statisticDataDisplay.setText(displayInfo);
    }
}
