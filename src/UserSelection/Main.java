package UserSelection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("UserSelection.fxml"));
//        primaryStage.setTitle("User Selection");
//        primaryStage.setScene(new Scene(root));
//        primaryStage.show();
//        primaryStage.setResizable(false);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserSelection.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("User Selection");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
