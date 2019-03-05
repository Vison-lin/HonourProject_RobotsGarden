package controller.controlpanel;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class GeneralPageController extends VBox {

    private ControlPanelFacade controlPanelFacade;

    public GeneralPageController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../../resources/control_panel_component/general.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void setControlPanelFacade(ControlPanelFacade controlPanelFacade) {
        this.controlPanelFacade = controlPanelFacade;
    }
}
