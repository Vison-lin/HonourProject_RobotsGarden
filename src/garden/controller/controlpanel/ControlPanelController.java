package garden.controller.controlpanel;

import garden.controller.garden.GardenController;
import garden.model.Robot;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ControlPanelController extends VBox {

    @FXML
    RobotGenerationController robotGenerationController;
    @FXML
    private ProgressController progressController;
    @FXML
    private AutoGenerationController autoGenerationController;



    @FXML
    private Button SaveButton;

    /**
     * Global robots list
     */
    private static List<Robot> robots = Collections.synchronizedList(new ArrayList<>());



    private GardenController gardenController;

    @FXML
    private Text warning;

    ControlPanelFacade controlPanelFacade;

    public ControlPanelController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../view/control_panel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        controlPanelFacade = new ControlPanelFacade(this, autoGenerationController, progressController, robotGenerationController);
        this.progressController.setControlPanelFacade(controlPanelFacade);
        this.autoGenerationController.setControlPanelFacade(controlPanelFacade);
        this.robotGenerationController.setControlPanelFacade(controlPanelFacade);
        initNodesText();
        saveListener();
    }

    public ControlPanelFacade getControlPanelFacade() {
        return controlPanelFacade;
    }

    private void initNodesText() {
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

    /**
     * get the list of robots.
     * @return the list of the robots
     */
    List<Robot> getRobots() {
        return robots;
    }

    /**
     * Set the list of robots.
     * Note, one should avoid to use this method, instead, change the content of the robots directly.
     * @param robots the new list of the robots
     */
    void setRobots(ArrayList<Robot> robots) {
        ControlPanelController.robots = robots;
    }


    void resetAll() {
        AlgorithmLoadingHelper helper = new AlgorithmLoadingHelper();
        robots.clear();
        gardenController.updateGarden();
        robotGenerationController.reset();
        //clean prev and next
        progressController.reset();
        autoGenerationController.reset();
    }

    Text getWarning() {
        return warning;
    }

    GardenController getGardenController() {
        return this.gardenController;
    }

    void setGardenController(GardenController gardenController) {
        this.gardenController = gardenController;
    }
}
