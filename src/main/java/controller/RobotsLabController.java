package controller;


import controller.controlpanel.ControlPanelController;
import controller.controlpanel.ControlPanelFacade;
import controller.garden.GardenController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class RobotsLabController extends HBox {

    @FXML
    private GardenController gardenController;

    @FXML
    private ControlPanelController controlPanelController;

    private ControlPanelFacade controlPanelFacade;

    public RobotsLabController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/robots_lab.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            //passing gardenController into controlPanelController
            controlPanelFacade = controlPanelController.getControlPanelFacade();
            controlPanelFacade.setGardenController(gardenController);
            gardenController.setControlPanelFacade(controlPanelFacade);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        resizeGardenWindow();
    }

//    private void resizeGardenWindow() {
//        heightProperty().addListener((observable, oldValue, newValue) -> {
//            gardenController.minWidthProperty().bind(observable);
//            gardenController.minHeightProperty().bind(observable);
//            gardenController.getCoordinateSystem().minWidthProperty().bind(observable);
//            gardenController.getCoordinateSystem().minHeightProperty().bind(observable);
//        });
//    }
}
