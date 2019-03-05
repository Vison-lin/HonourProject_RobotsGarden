package controller.controlpanel;

import core.Statisticable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import model.Robot;
import model.RobotGraphicalDisplay;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RobotGenerationController extends VBox {

    private static final double DEFAULT_ROBOT_VISION = 100;
    private static final double DEFAULT_ROBOT_UNIT = Double.POSITIVE_INFINITY;
    public static final double DEFAULT_ROBOT_Radius = 10;
    private static final String DEFAULT_COLOR_TEXT = "Color of robot body";
    private static final String DEFAULT_VISION_TEXT = "Vision of robot";
    private static final String DEFAULT_UNIT_TEXT = "Pace ";
    private static final String RANDOM_CREATE_ROBOT_BUTTON = "Random Create Connected Robots";
    static double ROBOT_NAME_COUNTER = 0;

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
    @FXML
    private RadioButton unitRandom;
    @FXML
    private RadioButton unitCustomize;
    @FXML
    private RadioButton clickGenerate;
    @FXML
    private RadioButton autoGenerate;
    @FXML
    private RadioButton visionCustomize;
    @FXML
    private VBox autoGenerateBox;
    @FXML
    private Button randomCreateRobots;
    @FXML
    private TextField numberOfAutoCreatedRobots;



    private ControlPanelFacade controlPanelFacade;

    private AlgorithmLoadingHelper algorithmLoadingHelper = new AlgorithmLoadingHelper();

    private String selectedAlgorithm;

    private double selectedRobotVision;

    private Paint selectedRobotColor = Color.BLACK;

    private double selectedRobotUnit;

    private boolean selectRandom = false;

    private List<Robot> robots;

    public RobotGenerationController() {


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../../resources/control_panel_component/robot_customization.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initNodesText();
        randomCreateConnectedRobotsBtnListener();
        inputVisionListener();
        inputUnitListener();
        colorPickerListener();
        visionCheckListener();
        generateCheckListener();
        unitCheckListener();
        algorithmSelectionInit();
        algorithmSelectionListener();
    }

    private void initNodesText() {
        randomCreateRobots.setText(RANDOM_CREATE_ROBOT_BUTTON);
        inputVision.setText(DEFAULT_ROBOT_VISION + "");
        selectedRobotVision = DEFAULT_ROBOT_VISION;
        inputUnit.setText("");
        inputUnit.setDisable(true);
        autoGenerateBox.setDisable(true);
        selectedRobotUnit = DEFAULT_ROBOT_UNIT;
        colorText.setText(DEFAULT_COLOR_TEXT);
        visionText.setText(DEFAULT_VISION_TEXT);
        unitText.setText(DEFAULT_UNIT_TEXT);
        unitCheck.setUserData("Infinity");
        unitRandom.setUserData("Random");
        unitCustomize.setUserData("Customize");
        visionCustomize.setUserData("Customize");
        visionCheck.setUserData("Infinity");
        clickGenerate.setUserData("Click");
        autoGenerate.setUserData("Auto");

    }

    private void inputVisionListener() {

        inputVision.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    selectedRobotVision = Integer.valueOf(inputVision.getText());//todo must press return to trigger, beeter way? use int or double?
                } catch (NumberFormatException e) {
                    controlPanelFacade.getWarning().setText("Robot Vision must be an int");
                }

            }
        });

    }

    private void inputUnitListener(){
        inputUnit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try{
                    selectedRobotUnit = Double.valueOf(inputUnit.getText());
                }catch (NumberFormatException e){
                    controlPanelFacade.getWarning().setText("Robot unit number must be an int or double");
                }
            }
        });
    }


    private void generateCheckListener(){
        ToggleGroup group = new ToggleGroup();
        autoGenerate.setToggleGroup(group);
        clickGenerate.setSelected(true);
        clickGenerate.setToggleGroup(group);

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(group.getSelectedToggle().getUserData().toString().equals("Auto")){
                    autoGenerateBox.setDisable(false);//todo disable click generate function
                }else if(group.getSelectedToggle().getUserData().toString().equals("Click")){
                    autoGenerateBox.setDisable(true);


                }


            }
        });

    }


    private void visionCheckListener(){
        ToggleGroup group = new ToggleGroup();
        visionCheck.setToggleGroup(group);
        visionCustomize.setSelected(true);
        visionCustomize.setToggleGroup(group);

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(group.getSelectedToggle().getUserData().toString().equals("Infinity")){
                    selectedRobotVision = Math.sqrt(800*800*2);
                    //inputVision.setText("");
                    inputVision.setDisable(true);
                }else if(group.getSelectedToggle().getUserData().toString().equals("Customize")){
                    inputVision.setDisable(false);
                    inputVision.setText(DEFAULT_ROBOT_VISION+"");
                    selectedRobotVision = DEFAULT_ROBOT_VISION;

                }


            }
        });


    }

    private void unitCheckListener(){
        ToggleGroup group = new ToggleGroup();
        unitCheck.setToggleGroup(group);
        unitCheck.setSelected(true);
        unitRandom.setToggleGroup(group);
        unitCustomize.setToggleGroup(group);


        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(group.getSelectedToggle().getUserData().toString().equals("Infinity")){
                    inputUnit.setText("");
                    inputUnit.setDisable(true);
                    selectedRobotUnit = DEFAULT_ROBOT_UNIT;
                    selectRandom = false;
                }else if(group.getSelectedToggle().getUserData().toString().equals("Customize")){
                    inputUnit.setText("");
                    inputUnit.setDisable(false);
                    selectRandom = false;

                }else if(group.getSelectedToggle().getUserData().toString().equals("Random")){
                    inputUnit.setText("");
                    inputUnit.setDisable(true);
                    selectRandom = true;

                }


            }
        });

    }


    private void algorithmSelectionInit() {//todo how to deal with no algorithm found?
        ObservableList<Pair<String, String>> value = FXCollections.observableArrayList();
        List<Pair<String, String>> allAlgInfo = null;
        try {
            allAlgInfo = algorithmLoadingHelper.getAlgorithmList();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        value.addAll(allAlgInfo);
        algorithmSelection.setItems(value);
        algorithmSelection.getSelectionModel().select(0);
        if (allAlgInfo.size() > 0) {
            selectedAlgorithm = algorithmSelection.getSelectionModel().getSelectedItem().getValue();//assign the first algorithm as the default selected item
        }
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
     *
     * Generate the robot, add all necessary listener to it, and insert it to the global robot list.
     *
     * <br/> No need for inserting the generated robot to the global robot list again.
     *
     * @param tag the TAG of the robot
     * @param x the x coordinate of the robot
     * @param y the y coordinate of the robot
     * @return the generated robot: for further operation. Note that any modification on this robot will reflect on the screen.
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
        robot.setRandomUnit(selectRandom); //set the boolean whether random unit;
        robot.setUnit(selectedRobotUnit);//set the unit number for the robot;
        robot.getSensor().setGlobalRobots(robots);//pass the global list into robot sensor immediately: so that without clicking next, the robot's sensor start to scan its surroundings immediately todo add an start btn instead?
        controlPanelFacade.addListenerToGivenRobot(robot);

        if (robot.getAlgorithm() instanceof Statisticable) {
            Statisticable statisticable = (Statisticable) robot.getAlgorithm();
//            System.out.println(statisticable + "==========");
            controlPanelFacade.insertToStatisticDataTempStoringList(robot.getTag(), statisticable.init());
        }
        return robot;
    }

    /****************** Auto Generate ******************************/

    private void randomCreateConnectedRobotsBtnListener() {
        randomCreateRobots.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                try {
                    int numOfNewRobots = Integer.valueOf(numberOfAutoCreatedRobots.getText());

                    Random random = new Random();

                    //init first one
                    double maxX = (int) controlPanelFacade.getGardenController().getWidth() + 1;
                    double maxY = (int) controlPanelFacade.getGardenController().getHeight() + 1;
                    double ctr = ControlPanelFacade.ROBOT_NAME_COUNTER;
                    Robot currRobot = controlPanelFacade.robotGenerator("Auto No." + ctr + "", random.nextInt((int) maxX), random.nextInt((int) maxY));

                    ArrayList<Robot> generatedRobots = new ArrayList<>();
                    generatedRobots.add(currRobot);

                    //create the rest
                    for (int i = 1; i < numOfNewRobots; i++) {
                        ctr++;
                        Point2D.Double position = getNextRandomGeneratedRobotPosition(generatedRobots, maxX, maxY);
                        Robot newRobot = controlPanelFacade.robotGenerator(" =>" + ctr + "<= ", position.x, position.y);
                        generatedRobots.add(newRobot);
                    }
                    controlPanelFacade.getGardenController().updateGarden();
                } catch (NumberFormatException e) {
                    controlPanelFacade.getWarning().setText("The Number of Random Created Robots Must Be an Integer!!!");
                }
            }
        });
    }

    private Point2D.Double getNextRandomGeneratedRobotPosition(List<Robot> robots, double maxX, double maxY) {
        Random random = new Random();
        double currX = 0;
        double currY = 0;
        Robot curr = robots.get(random.nextInt(robots.size()));//random choose one
        double xBoundUp = validateWithinTheEnclosingSquare(curr.getPositionX() + curr.getVision(), maxX);
        double xBoundLow = validateWithinTheEnclosingSquare(curr.getPositionX() - curr.getVision(), maxX);
        double yBoundUp = validateWithinTheEnclosingSquare(curr.getPositionY() + curr.getVision(), maxY);
        double yBoundLow = validateWithinTheEnclosingSquare(curr.getPositionY() - curr.getVision(), maxY);
        //check if is within the circle
        double distance = Double.POSITIVE_INFINITY;

        while (distance > curr.getVision()) {
            double x = curr.getPositionX();
            double y = curr.getPositionY();
            currX = random.nextInt((int) (xBoundUp - xBoundLow + 1)) + xBoundLow;
            currY = random.nextInt((int) (yBoundUp - yBoundLow + 1)) + yBoundLow;
            double differX = currX - x;
            double differY = currY - y;
            distance = Math.sqrt(Math.pow(differX, 2) + Math.pow(differY, 2));
        }
        return new Point2D.Double(currX, currY);
    }

    /**
     * Validate if the point is within the square that encloses the circle.
     *
     * @param original the point
     * @param bound    the radius
     * @return validated value
     */
    private double validateWithinTheEnclosingSquare(double original, double bound) {
        if (original >= bound) {
            original = bound;
        } else if (original <= 0) {
            original = 0;
        }
        return original;
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
        randomCreateRobots.setDisable(false);
        numberOfAutoCreatedRobots.setText("");
        algorithmSelection.getSelectionModel().select(0);
    }

    public Paint getSelectedRobotColor() {
        return this.selectedRobotColor;
    }
}