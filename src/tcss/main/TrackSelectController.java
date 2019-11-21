package tcss.main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TrackSelectController implements Initializable {

    @FXML private Button modelButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void showTrackModel() throws Exception {
        FXMLLoader modelLoader = new FXMLLoader(getClass().getResource("fxml/TrackModel.fxml"));
        Stage model = new Stage();
        Parent modelRoot = modelLoader.load();
        model.setTitle("Track Model");
        model.setScene(new Scene(modelRoot));
        model.setResizable(false);
        model.getIcons().add(new Image("file:resources/train.png"));

        model.show();
        Stage s = (Stage) modelButton.getScene().getWindow();
        s.close();

    }

    public void showTrackController() {

    }

    public void closeWindow() {
        Stage s = (Stage) modelButton.getScene().getWindow();
        s.close();
    }


}
