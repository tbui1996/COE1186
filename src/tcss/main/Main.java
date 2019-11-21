package tcss.main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tcss.ctc.CTC;
import tcss.trackcontroller.TrackController;
import tcss.trackmodel.Track;
import tcss.trainmodel.TrainModel;

public class Main extends Application {

    static SimTime T;
    static CTC ctc;
    public static TrackController tc;
    public static Track track;

    @Override
    public void start(Stage primaryStage) throws Exception {

        track = new Track();

        // Init CTC
        ctc = new CTC();

        // Load opening scene - track input

        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/UserSelection.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("User Selection");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("file:resources/train.png"));


        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent)  {
                windowEvent.consume();
            }
        });

        primaryStage.show();

        // TESTING TIMER PAUSE
        T = new SimTime();

        // TODO DELETE THIS
        // TESTING TRAIN CONTROLLER GUI
        TrainModel train = new TrainModel(100);




    }

    // Method called in SimTime to update modules
    public static void update() {
        // Place update calls here
        TrainModel.updateAll();
    }

    public static void main(String [] args) throws Exception {
        launch(args);
    }

}




