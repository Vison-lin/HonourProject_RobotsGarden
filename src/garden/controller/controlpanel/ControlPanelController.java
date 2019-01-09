package garden.controller.controlpanel;

import garden.controller.garden.GardenController;
import garden.model.Robot;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Iterator;

public class ControlPanelController extends VBox {

    @FXML
    private Button next;

    @FXML
    private Button clean;

    @FXML
    private Text output;

    private GardenController gardenController;

    public ControlPanelController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../view/control_panel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        nextBtnListener();
        cleanBtnListener();

    }

    private void nextBtnListener() {
        next.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //update the robots' position for each robot's sensor
                Iterator<Robot> robotIterator1 = gardenController.getRobots().iterator();
                while (robotIterator1.hasNext()) {
                    Robot curr = robotIterator1.next();
                    curr.getSensor().setGlobalRobots(gardenController.getRobots());
                }
                //run next
                Iterator<Robot> robotIterator2 = gardenController.getRobots().iterator();
                while (robotIterator2.hasNext()) {
                    Robot curr = robotIterator2.next();
                    curr.next(gardenController.getRobots());
                    if (gardenController.getSelectedRobots() != null && gardenController.getSelectedRobots().equals(curr)) {//display the selected robot's log
                        output.setText(curr.getLog().toString());
                    }
                }
                gardenController.updateGarden();
            }
        });
    }

    private void cleanBtnListener() {
        clean.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                gardenController.getRobots().clear();
                gardenController.updateGarden();
                output.setText("");
            }
        });
    }

    public void setGardenController(GardenController gardenController) {
        this.gardenController = gardenController;
    }
}
