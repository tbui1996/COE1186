package tcss.Demo;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tcss.trainmodel.TrainModel;

import java.net.URL;
import java.util.ResourceBundle;

public class TrainControllerController implements Initializable {

    // UI variables
    @FXML private ChoiceBox trainChoice;
    @FXML private Label idLabel;
    @FXML private Label speedLimitLabel;
    @FXML private Label sSpeedLabel;
    @FXML private TextField setPointInput;
    @FXML private Button confirmSetpoint;
    @FXML private Label authLabel;
    @FXML private Label undergroundLabel;
    @FXML private AnchorPane pane;
    @FXML private Button eBreakToggle;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        trainChoice.getItems().add("Select Train");
        // Testing
        for(TrainModel t: Main.trains) {
            trainChoice.getItems().add("Train " + t.getID());
        }

        trainChoice.setValue("Select Train");
        trainChoice.setTooltip(new Tooltip("Select a train to view"));
        trainChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                if((Integer) number2 > 0) {
//                    System.out.println(trainChoice.getItems().get((Integer) number2));
//                    System.out.println("ID: " + Main.trains.get((Integer)number2 - 1).getID());

                    TrainController cur = Main.trains.get((Integer)number2-1).getTControl();

                    idLabel.setText("ID: " + cur.getID());
                    sSpeedLabel.setText("Suggested Speed: " + cur.getSSpeed());
                    speedLimitLabel.setText("Speed Limit: " + cur.getSpeedLimit());
                    setPointInput.setTooltip("")
                    authLabel.setText("Authority: " + cur.getAuthority());

                    setPointInput.setText(cur.g"")

                    @FXML private ChoiceBox trainChoice;
                    @FXML private Label idLabel;
                    @FXML private Label speedLimitLabel;
                    @FXML private Label sSpeedLabel;
                    @FXML private TextField setPointInput;
                    @FXML private Button confirmSetpoint;
                    @FXML private Label authLabel;
                    @FXML private Label undergroundLabel;
                    @FXML private AnchorPane pane;
                    @FXML private Button eBreakToggle;

                } else {
                    idLabel.setText("ID: ");
                    sSpeedLabel.setText("Suggested Speed: ");
                    authLabel.setText("Authority: ");
                    speedLimitLabel.setText("Speed Limit: ");
                }
            }
        });
    }

    public void goBack(ActionEvent actionEvent) throws Exception {
        Scene moduleSelect = new Scene(FXMLLoader.load(getClass().getResource("ModuleSelection.fxml")));
        Stage window = (Stage) pane.getScene().getWindow();
        window.setScene(moduleSelect);
        window.setTitle("Module Selection");

    }

    public void confirmSetpoint(ActionEvent actionEvent) throws Exception{

    }
}
