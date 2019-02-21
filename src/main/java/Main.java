import controller.RobotsLabController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        RobotsLabController robotsLabController = new RobotsLabController();
        primaryStage.setScene(new Scene(robotsLabController));
        primaryStage.show();
    }
}
