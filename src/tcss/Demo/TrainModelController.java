package tcss.Demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class TrainModelController implements Initializable {

    // UI variables
    @FXML private Label idLabel;
    @FXML private Label sSpeedLabel;
    @FXML private Label authLabel;
    @FXML private Label speedLimitLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idLabel.setText("ID: ");
        sSpeedLabel.setText("Suggested Speed: 0 mph");
        authLabel.setText("Authority: 0 Blocks");
        speedLimitLabel.setText("Speed Limit: 0 mph");
    }
}
