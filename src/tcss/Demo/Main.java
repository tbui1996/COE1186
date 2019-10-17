package tcss.Demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import tcss.trackmodel.TrackModel;
import tcss.trackmodel.Track;
import tcss.trackmodel.Block;
import tcss.trackcontroller.TrackController;
import tcss.trainmodel.TrainModel;

import java.util.ArrayList;

public class Main extends Application {

    // Testing Train Model UI
    static ArrayList<TrainModel> trains = new ArrayList<TrainModel>();

    // Testing Track Model UI
    static ArrayList<Block> blocks = new ArrayList<Block>();
    static Track track;

    static TrackController tc;

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

        // Testing TrackModel UI
        TrackModel tm = new TrackModel();
        track = tm.getTrack();
        Block b1 = track.getBlock(1);
        Block b2 = track.getBlock(2);
        blocks.add(b1);
        blocks.add(b2);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
