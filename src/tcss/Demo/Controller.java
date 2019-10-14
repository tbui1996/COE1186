package tcss.Demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private ChoiceBox rate;
//    @FXML private Label rateLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rate.getItems().add("1x");
        rate.getItems().add("10x");
        rate.getItems().add("50x");
        rate.setValue("1x");
    }

}
