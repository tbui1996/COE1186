package tcss.Demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tcss.trainmodel.TrainModel;

import java.util.ArrayList;

public class Main extends Application {

    // Testing Train Model UI
    static ArrayList<TrainModel> trains = new ArrayList<TrainModel>();


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("ModuleSelection.fxml"));
        primaryStage.setTitle("Module Selection");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


        // Testing Train Model UI
        TrainModel t1 = new TrainModel(22.0f, 2, 4, 25.0f);
        TrainModel t2 = new TrainModel(17.0f, 13, 82, 43.5f);
        trains.add(t1);
        trains.add(t2);

        t1.setEBrake(true);


    }


    public static void main(String[] args) {
        launch(args);
    }
}
