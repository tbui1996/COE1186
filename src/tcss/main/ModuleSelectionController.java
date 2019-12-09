package tcss.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
