package garden.controller.controlpanel;

import garden.controller.controlpanel.controlpanel_component.AutoGenerationController;
import garden.controller.controlpanel.controlpanel_component.ProgressController;
import garden.controller.controlpanel.controlpanel_component.RobotGenerationController;
import garden.controller.garden.GardenController;
import garden.model.Robot;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ControlPanelController extends VBox {

    private static final String STATUS_CONTROL_TEXT ="Status Control";
    private static final String AUTO_GENERATE_TEXT ="Auto Generate";
    private static final String CUSTOMIZE_ROBOTS_TEXT = "Customize Robots";
    private static final String SAVE_BUTTON = "Save";
    private static final String WARING_TEXT = "Warning information";
    @FXML
    RobotGenerationController robotGenerationController;
    @FXML
    private ProgressController progressController;
    @FXML
    private AutoGenerationController autoGenerationController;
    @FXML
    private Text statuscontrolText;
    @FXML
    private Text autogenerateText;
    @FXML
    private Text customizeText;
    @FXML
    private Button SaveButton;
    @FXML
    private Text warning;

    /**
     * Global robots list
     */
    private static List<Robot> robots = Collections.synchronizedList(new ArrayList<>());


    private GardenController gardenController;



    public ControlPanelController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../view/control_panel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            this.progressController.setControlPanelController(this);
            this.autoGenerationController.setControlPanelController(this);
            this.robotGenerationController.setControlPanelController(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initNodesText();
        saveListener();
    }

    private void initNodesText() {
        statuscontrolText.setText(STATUS_CONTROL_TEXT);
        autogenerateText.setText(AUTO_GENERATE_TEXT);
        customizeText.setText(CUSTOMIZE_ROBOTS_TEXT);
        SaveButton.setText(SAVE_BUTTON);
        warning.setText(WARING_TEXT);
    }

    private void saveListener(){
        SaveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(robots.size()!=0){
                File f = new File("C:/Users/user/Desktop/a.txt");
                try {
                    FileOutputStream fop = new FileOutputStream(f);
                    OutputStreamWriter writer = new OutputStreamWriter(fop);
                    for(Robot robot:robots){

                        writer.append("Tag: " + robot.getTag() + ", Algorithm: ??, " + "selectedRobotVision: " + (int) robot.getVision());
                        writer.append("\r\n");

                    }
                    writer.close();
                    fop.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            }
        });


    }





    public void setGardenController(GardenController gardenController) {
        this.gardenController = gardenController;
    }

    /**
     * get the list of robots.
     * @return the list of the robots
     */
    public static List<Robot> getRobots() {
        return robots;
    }

    /**
     * Set the list of robots.
     * Note, one should avoid to use this method, instead, change the content of the robots directly.
     * @param robots the new list of the robots
     */
    public void setRobots(ArrayList<Robot> robots) {
        ControlPanelController.robots = robots;
    }


    public void resetAll() {
        AlgorithmLoadingHelper helper = new AlgorithmLoadingHelper();
        robots.clear();
        gardenController.updateGarden();
        robotGenerationController.reset();
        //clean prev and next
        progressController.reset();
        autoGenerationController.reset();
    }


    public double getSelectedRobotVision() {
        return robotGenerationController.getSelectedRobotVision();
    }

    public Paint getSelectedRobotColor() {
        return robotGenerationController.getSelectedRobotColor();
    }

    public GardenController getGardenController() {
        return gardenController;
    }

    public Text getWarning() {
        return warning;
    }

    public RobotGenerationController getRobotGenerationController() {
        return robotGenerationController;
    }
}
