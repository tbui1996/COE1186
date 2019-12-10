package tcss.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ModuleSelectionController implements Initializable {

    @FXML private Button quitB;
    @FXML private AnchorPane pane;
//    @FXML private Label rateLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void viewTrainController(ActionEvent actionEvent) throws Exception {

    }

    public void viewTrainModel(ActionEvent actionEvent) throws Exception {
        FXMLLoader trainLoader = new FXMLLoader(getClass().getResource("fxml/TrainFailures.fxml"));
        Parent trainRoot = trainLoader.load();
        Stage train = new Stage();
        train.setScene(new Scene(trainRoot));
        train.setTitle("Train Model Failures");
        train.getIcons().add(new Image("file:resources/train.png"));

        train.show();
    }

    public void viewTrackModel(ActionEvent actionEvent) throws Exception {

    }

    public void viewTrackController(ActionEvent actionEvent) throws Exception {

    }
    public void viewCTC(ActionEvent actionEvent) throws Exception {

    }

    public void closeWindow(){
        Stage s = (Stage) pane.getScene().getWindow();
        s.close();
    }


}
