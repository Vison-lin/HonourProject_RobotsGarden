package garden.controller.controlpanel;

import garden.model.Robot;
import garden.model.RobotGraphicalDisplay;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Pair;
import javafx.util.StringConverter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class RobotGenerationController extends VBox {

    public static final double DEFAULT_ROBOT_VISION = 100;

    @FXML
    private ComboBox<Pair<String, String>> algorithmSelection;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private TextField inputVision;

    private ControlPanelFacade controlPanelFacade;

    private AlgorithmLoadingHelper algorithmLoadingHelper = new AlgorithmLoadingHelper();

    private String selectedAlgorithm;

    private double selectedRobotVision;

    private Paint selectedRobotColor = Color.BLACK;

    private List<Robot> robots;

    public RobotGenerationController() {


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../../view/control_panel_component/robot_customization_control.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initNodesText();
        inputVisionListner();
        colorPickerListener();

        try {
            algorithmSelectionInit();
        } catch (InstantiationException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();//todo handle it and display it on the screen.!!!
        }
        algorithmSelectionListener();
    }

    private void initNodesText() {
        inputVision.setText(DEFAULT_ROBOT_VISION + "");
        selectedRobotVision = DEFAULT_ROBOT_VISION;
    }

    private void inputVisionListner() {
        inputVision.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    selectedRobotVision = Integer.valueOf(inputVision.getText());//todo must press return to trigger, beeter way? use int or double?
                } catch (NumberFormatException e) {
                    controlPanelFacade.getWarning().setText("Robot Vision must be an int");
                }

            }
        });


    }


    private void algorithmSelectionInit() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ObservableList<Pair<String, String>> value = FXCollections.observableArrayList();
        List<Pair<String, String>> allAlgInfo = algorithmLoadingHelper.getAlgorithmList();
        value.addAll(allAlgInfo);
        algorithmSelection.setItems(value);
        algorithmSelection.getSelectionModel().select(0);
        selectedAlgorithm = algorithmSelection.getSelectionModel().getSelectedItem().getValue();//assign the first algorithm as the default selected item
        algorithmSelection.setConverter(new StringConverter<Pair<String, String>>() {
            @Override
            public String toString(Pair<String, String> object) {
                return object.getKey();
            }

            @Override
            public Pair<String, String> fromString(String string) {
                return null;
            }
        });
    }

    private void algorithmSelectionListener() {
        algorithmSelection.valueProperty().addListener(
                (obs, oldVal, newVal) -> selectedAlgorithm = newVal.getValue()
        );
    }

    private void colorPickerListener() {
        colorPicker.setValue(Color.BLACK);
        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedRobotColor = colorPicker.getValue();

            }
        });
    }


    /**
     * @param tag todo
     * @param x
     * @param y   for now, generate
     * @return
     */
    public Robot robotGenerator(String tag, double x, double y, Circle robotPosition, Circle robotBody, Circle robotBorder, Circle robotVision) {
        RobotGraphicalDisplay robotGraphicalDisplay = new RobotGraphicalDisplay(robotPosition, robotBody, robotBorder, robotVision, false);
        Robot robot = new Robot(robotGraphicalDisplay);
        robot.moveTo(x, y);
        robot.setTag(tag);
        algorithmLoadingHelper.assignAlgorithmToRobot(robot, selectedAlgorithm);
        robots.add(robot);//add the robot into the robots list
        robot.getSensor().setGlobalRobots(robots);//pass the global list into robot sensor immediately: so that without clicking next, the robot's sensor start to scan its surroundings immediately todo add an start btn instead?
        controlPanelFacade.addGeneratedRobotToGarden(robot);
        return robot;
    }

    public void setControlPanelFacade(ControlPanelFacade controlPanelFacade) {
        this.controlPanelFacade = controlPanelFacade;
        setRobots();
    }

    private void setRobots() {
        this.robots = controlPanelFacade.getRobots();
    }

    public String getSelectAlgorithm(){return selectedAlgorithm;}

    public TextField getInputVision() {
        return inputVision;
    }

    public ComboBoxBase<Color> getColorPicker() {
        return this.colorPicker;
    }

    public double getSelectedRobotVision() {
        return this.selectedRobotVision;
    }

    public void setSelectedRobotVision(double newSelectedRobotVision) {
        this.selectedRobotVision = newSelectedRobotVision;
    }

    public void reset() {
        inputVision.setText(DEFAULT_ROBOT_VISION + "");
        selectedRobotVision = DEFAULT_ROBOT_VISION;
        colorPicker.setValue(Color.BLACK);
        selectedRobotColor = Color.BLACK;
        algorithmSelection.getSelectionModel().select(0);
    }

    public Paint getSelectedRobotColor() {
        return this.selectedRobotColor;
    }
}
