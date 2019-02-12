package garden.controller.garden;

import garden.controller.controlpanel.AlgorithmLoadingHelper;
import garden.controller.controlpanel.ControlPanelFacade;
import garden.model.Robot;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.List;

public class RobotSettingHelper extends VBox {

    public static final double DEFAULT_ROBOT_VISION = 100;
    public static final double DEFAULT_ROBOT_UNIT = Double.POSITIVE_INFINITY;
    public static final String DEFAULT_COLOR_TEXT = "Select an color for robots: ";
    public static final String DEFAULT_VISION_TEXT = "Select an vision for robots: ";
    public static final String DEFAULT_UNIT_TEXT = "Select an unit for robots: ";

    @FXML
    private ComboBox<Pair<String, String>> algorithmSelection;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private TextField inputVision;
    @FXML
    private TextField inputUnit;
    @FXML
    private Text colorText;
    @FXML
    private Text visionText;
    @FXML
    private Text unitText;


    private ControlPanelFacade controlPanelFacade;

    private AlgorithmLoadingHelper algorithmLoadingHelper = new AlgorithmLoadingHelper();

    private String selectedAlgorithm;

    private double selectedRobotVision;

    private Paint selectedRobotColor = Color.BLACK;

    private double selectedRobotUnit;

    private List<Robot> robots;

    public RobotSettingHelper(GardenController gardenController) {


        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(gardenController.getGarden().getScene().getWindow());
        FXMLLoader fxmlLoader;
        this.robots = robots;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("../../view/control_panel_component/robot_customization_control.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            Parent parent = fxmlLoader.load();
            Scene dialogScene = new Scene(parent, 200, 400);
            dialog.setScene(dialogScene);
            dialog.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        initNodesText();
//        inputVisionListener();
//        inputUnitListener();
//        colorPickerListener();
//
//        try {
////            algorithmSelectionInit();
//        } catch (InstantiationException | InvocationTargetException | ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();//todo handle it and display it on the screen.!!!
//        }
//        algorithmSelectionListener();
    }
}
