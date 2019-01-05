package garden;

import garden.controller.RobotsLabController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("view/robots_lab.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();
        RobotsLabController robotsLabController = new RobotsLabController();
//        robotsLabController.setText("Hello!");

        primaryStage.setScene(new Scene(robotsLabController));
        primaryStage.setTitle("Custom Control");
        primaryStage.setWidth(300);
        primaryStage.setHeight(200);
        primaryStage.show();
    }
}
