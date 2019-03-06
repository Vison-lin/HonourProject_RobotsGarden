package controller.controlpanel;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class SettingPageController extends VBox {
    private ControlPanelFacade controlPanelFacade;

    public SettingPageController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/control_panel_component/setting.fxml"));
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
