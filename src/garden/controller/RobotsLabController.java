package garden.controller;

import garden.controller.controlpanel.ControlPanelController;
import garden.controller.garden.GardenController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class RobotsLabController extends HBox {

    @FXML
    private GardenController gardenController;
    @FXML
    private ControlPanelController controlPanelController;

    public RobotsLabController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/robots_lab.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            //passing gardenController into controlPanelController
            controlPanelController.setGardenController(gardenController);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
