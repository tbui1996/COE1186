package tcss.Demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("ModuleSelection.fxml"));
        primaryStage.setTitle("Module Selection");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
//        primaryStage.setResizable(false);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
