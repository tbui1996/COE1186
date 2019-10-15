package UserSelection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private ChoiceBox rate;
    @FXML private Button trainModelButton;
//    @FXML private Label rateLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rate.getItems().add("1x");
        rate.getItems().add("10x");
        rate.getItems().add("50x");
        rate.setValue("1x");

        trainModelButton.setText("Train Model");
    }


}
