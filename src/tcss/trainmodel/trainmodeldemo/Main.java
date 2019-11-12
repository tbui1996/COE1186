package tcss.trainmodel.trainmodeldemo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tcss.trainmodel.TrainModel;

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
        primaryStage.show();

        controller.update();



    }

    public static void main(String[] args) {
        launch(args);
    }
}
