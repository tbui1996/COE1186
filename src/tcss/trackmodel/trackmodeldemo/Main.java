package tcss.trackmodel.trackmodeldemo;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tcss.trackmodel.TrackModel;
import tcss.trackmodel.Track;
import tcss.trackmodel.Block;
import tcss.trackmodel.trackmodeldemo.TrackModelIndividualController;

import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {

    static TrackModel tkm;
    static Track redLine;
    static Track greenLine;
    static TrackModelIndividualController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {

        tkm = new TrackModel();
        redLine = tkm.getRedLine();
        greenLine = tkm.getGreenLine();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("TrackModelIndividual.fxml"));
        Parent root = loader.load();
//        Parent root = FXMLLoader.load(getClass().getResource("TrainModel.fxml"));
        primaryStage.setTitle("Track Model Individual");
        primaryStage.setScene(new Scene(root));

        controller = loader.<TrackModelIndividualController>getController();
        primaryStage.setResizable(true);
        primaryStage.getIcons().add(new Image("file:resources/train.png"));
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(4);
            }
        });

        //controller.update();

//        TimeUnit.SECONDS.sleep(3);
//        controller.update();

        // Create Timer to update TrainModel in background
        //Timer t = new Timer();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
