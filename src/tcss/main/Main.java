package tcss.main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Load opening scene - track input

        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/UserSelection.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("User Selection");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                //TODO create exit confirmation window
            }
        });

    }

    public static void main(String [] args) throws Exception {
        launch(args);
    }
}
