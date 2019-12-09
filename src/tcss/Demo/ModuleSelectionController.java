package tcss.Demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ModuleSelectionController implements Initializable {

    @FXML private ChoiceBox rate;
    @FXML private Button trainModelButton;
    @FXML private Button quitB;
    @FXML private AnchorPane pane;
//    @FXML private Label rateLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rate.getItems().add("1x");
        rate.getItems().add("10x");
        rate.getItems().add("50x");
        rate.setValue("1x");

        quitB.setOnAction(e -> System.exit(7));

    }

    public void viewTrainController(ActionEvent actionEvent) throws Exception {
        Scene trainControllerView = new Scene(FXMLLoader.load(getClass().getResource("TrainController.fxml")));
        Stage window = (Stage) pane.getScene().getWindow();
        window.setScene(trainControllerView);
        window.setTitle("Train Controller");
    }

    public void viewTrainModel(ActionEvent actionEvent) throws Exception {
        Scene trainModelView = new Scene(FXMLLoader.load(getClass().getResource("TrainModel.fxml")));
        Stage window = (Stage) pane.getScene().getWindow();
        window.setScene(trainModelView);
        window.setTitle("Train Model");
    }

    public void viewTrackModel(ActionEvent actionEvent) throws Exception {
        Scene trackModelView = new Scene(FXMLLoader.load(getClass().getResource("TrackModel.fxml")));
        Stage window = (Stage) pane.getScene().getWindow();
        window.setScene(trackModelView);
        window.setTitle("Track Model");
    }

    public void viewTrackController(ActionEvent actionEvent) throws Exception {
        Scene trackModelView = new Scene(FXMLLoader.load(getClass().getResource("TrackController.fxml")));
        Stage window = (Stage) pane.getScene().getWindow();
        window.setScene(trackModelView);
        window.setTitle("Track Controller");
    }
    public void viewCTC(ActionEvent actionEvent) throws Exception {
        Scene trackModelView = new Scene(FXMLLoader.load(getClass().getResource("CTC.fxml")));
        Stage window = (Stage) pane.getScene().getWindow();
        window.setScene(trackModelView);
        window.setTitle("CTC");
    }

}
