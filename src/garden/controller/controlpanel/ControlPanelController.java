package garden.controller.controlpanel;

import garden.controller.garden.GardenController;
import garden.model.Robot;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Iterator;

public class ControlPanelController extends VBox {

    @FXML
    private Button next;

    private GardenController gardenController;

    public ControlPanelController() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../view/control_panel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        registerListener();

    }

    private void registerListener() {
        //todo -? next -> setting -> error
        next.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Iterator<Robot> robotIterator1 = gardenController.getRobots().iterator();
                while (robotIterator1.hasNext()) {
                    Robot curr = robotIterator1.next();
                    curr.getSensor().setGlobalRobots(gardenController.getRobots());
                }
                Iterator<Robot> robotIterator2 = gardenController.getRobots().iterator();
                while (robotIterator2.hasNext()) {
                    Robot curr = robotIterator2.next();
                    curr.next(gardenController.getRobots());
                }
                gardenController.updateGarden();
            }
        });
    }

    public void setGardenController(GardenController gardenController) {
        this.gardenController = gardenController;
    }
}
