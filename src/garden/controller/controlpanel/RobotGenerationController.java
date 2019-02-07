package garden.controller.controlpanel;

import garden.model.Robot;
import garden.model.RobotGraphicalDisplay;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Pair;
import javafx.util.StringConverter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class RobotGenerationController extends VBox {

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
    @FXML
    private RadioButton visionCheck;
    @FXML
    private RadioButton unitCheck;

    private ControlPanelFacade controlPanelFacade;

    private AlgorithmLoadingHelper algorithmLoadingHelper = new AlgorithmLoadingHelper();

    private String selectedAlgorithm;

    private double selectedRobotVision;

    private Paint selectedRobotColor = Color.BLACK;

    private double selectedRobotUnit;

    private List<Robot> robots;

    public RobotGenerationController() {


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../view/control_panel_component/robot_customization_control.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initNodesText();
        inputVisionListener();
        inputUnitListener();
        colorPickerListener();
        visionCheckListener();
        unitCheckListener();

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
        inputUnit.setText("");
        inputUnit.setDisable(true);
        selectedRobotUnit = DEFAULT_ROBOT_UNIT;
        colorText.setText(DEFAULT_COLOR_TEXT);
        visionText.setText(DEFAULT_VISION_TEXT);
        unitText.setText(DEFAULT_UNIT_TEXT);

    }

    private void inputVisionListener() {
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

    private void inputUnitListener(){
        inputUnit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    selectedRobotUnit = Double.valueOf(inputUnit.getText());
                }catch (NumberFormatException e){
                    controlPanelFacade.getWarning().setText("Robot unit number must be an int or double");
                }
            }
        });
    }

    private void visionCheckListener(){
        visionCheck.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(visionCheck.isSelected()){
                    selectedRobotVision = Math.sqrt(500*500*2);
                    inputVision.setText("");
                    inputVision.setDisable(true);

                }else{
                    inputVision.setDisable(false);
                    inputVision.setText(DEFAULT_ROBOT_VISION+"");
                    selectedRobotVision = DEFAULT_ROBOT_VISION;
                }
            }
        });
    }

    private void unitCheckListener(){
            unitCheck.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(unitCheck.isSelected()){
                        inputUnit.setText("");
                        inputUnit.setDisable(true);
                        selectedRobotUnit = DEFAULT_ROBOT_UNIT;

                    }else{
                        inputUnit.setDisable(false);
                        inputUnit.setText("");

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
    public Robot robotGenerator(String tag, double x, double y) {
        Circle robotPosition = new Circle(3, Color.WHITE);
        Circle robotBody = new Circle(10, this.getSelectedRobotColor());
        Circle robotBorder = new Circle(11, Color.WHITE);
        Circle robotVision = new Circle(this.getSelectedRobotVision(), Color.LIGHTBLUE);
        RobotGraphicalDisplay robotGraphicalDisplay = new RobotGraphicalDisplay(robotPosition, robotBody, robotBorder, robotVision, false);
        Robot robot = new Robot(robotGraphicalDisplay);
        robot.moveTo(x, y);
        robot.setTag(tag);
        algorithmLoadingHelper.assignAlgorithmToRobot(robot, selectedAlgorithm);
        robots.add(robot);//add the robot into the robots list
        robot.setUnit(selectedRobotUnit);//set the unit number for the robot;
        robot.getSensor().setGlobalRobots(robots);//pass the global list into robot sensor immediately: so that without clicking next, the robot's sensor start to scan its surroundings immediately todo add an start btn instead?
        controlPanelFacade.addListenerToGivenRobot(robot);
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

    public double getSelectedRobotUnit(){return this.selectedRobotUnit;}

    public void setSelectedRobotVision(double newSelectedRobotVision) {
        this.selectedRobotVision = newSelectedRobotVision;
    }

    public void reset() {
        inputVision.setText(DEFAULT_ROBOT_VISION + "");
        inputVision.setDisable(false);
        selectedRobotVision = DEFAULT_ROBOT_VISION;
        inputUnit.setText("");
        inputUnit.setDisable(true);
        selectedRobotUnit = DEFAULT_ROBOT_UNIT;
        colorPicker.setValue(Color.BLACK);
        selectedRobotColor = Color.BLACK;
        visionCheck.setSelected(false);
        unitCheck.setSelected(true);

        algorithmSelection.getSelectionModel().select(0);
    }

    public Paint getSelectedRobotColor() {
        return this.selectedRobotColor;
    }
}
