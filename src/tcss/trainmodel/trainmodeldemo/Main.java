package tcss.trainmodel.trainmodeldemo;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tcss.trainmodel.TrainModel;

import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {

//    static ArrayList<TrainModel> trains = new ArrayList<>();

    static TrainModel train;
    static TrainModelController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {

        train = new TrainModel(10);
        train.setID(7);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("TrainModel.fxml"));
        Parent root = loader.load();
//        Parent root = FXMLLoader.load(getClass().getResource("TrainModel.fxml"));
        primaryStage.setTitle("Train Model");
        primaryStage.setScene(new Scene(root));

        controller = loader.<TrainModelController>getController();
        controller.passTrain(train);
        controller.titleLabel.setText("Train " + controller.train.getID());
        primaryStage.setResizable(true);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(4);
            }
        });

        controller.update();

//        TimeUnit.SECONDS.sleep(3);
//        controller.update();

        // Create Timer to update TrainModel in background
        new Timer().schedule(
                new TimerTask() {

                    @Override
                    public void run() {
                        System.out.println("ping");
                        train.update();
                    }
                }, 0, 1000);

    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
