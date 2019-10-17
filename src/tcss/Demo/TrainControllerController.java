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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tcss.traincontroller.TrainController;
import tcss.trainmodel.TrainModel;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class TrainControllerController implements Initializable {

    // UI variables
    @FXML private ChoiceBox trainChoice;
    @FXML private Label idLabel;
    @FXML private Label speedLimitLabel;
    @FXML private Label sSpeedLabel;
    @FXML private TextField setPointInput;
    @FXML private Button confirmSetpoint;
    @FXML private Label authLabel;
    @FXML private AnchorPane pane;
    @FXML private ToggleButton eBrakeToggle;
    @FXML private Label undergroundDisplay;

    private TrainController temp;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DecimalFormat format = new DecimalFormat( "#.0" );
        setPointInput.setTextFormatter( new TextFormatter<>(c -> {
            if ( c.getControlNewText().isEmpty() ) {
                return c;
            }
            ParsePosition parsePosition = new ParsePosition( 0 );
            Object object = format.parse( c.getControlNewText(), parsePosition );
            if ( object == null || parsePosition.getIndex() < c.getControlNewText().length() ){
                return null;
            } else {
                return c;
            }
        }));
        eBrakeToggle.setDisable(true);
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
                    eBrakeToggle.setDisable(false);
                    TrainController cur = Main.trains.get((Integer)number2-1).getTControl();
                    cur.updateStatus();
                    temp = cur;
                    idLabel.setText("ID: " + cur.getID());
                    sSpeedLabel.setText("Suggested Speed: " + cur.getSSpeed());
                    speedLimitLabel.setText("Speed Limit: " + cur.getSpeedLimit());
                    setPointInput.setText("");
                    setPointInput.setPromptText("" + cur.getsetpointSpeed());
                    authLabel.setText("Authority: " + cur.getAuthority());
                    if (cur.getUnderground() == true){
                        undergroundDisplay.setText("True");
                        undergroundDisplay.setTextFill(Color.GREEN);
                    } else {
                        undergroundDisplay.setText("False");
                        undergroundDisplay.setTextFill(Color.RED);
                    }
                    eBrakeToggle.setSelected(cur.getEBrake());
                    if(cur.getEBrake()) {
                        eBrakeToggle.setStyle("-fx-background-color: red; -fx-text-fill: #dfdfdf");
                    } else {
                        eBrakeToggle.setStyle("-fx-background-color: #dfdfdf; -fx-text-fill: rgb(43, 39, 49)");
                    }
                } else {
                    temp = null; //deselect train
                    idLabel.setText("ID: ");
                    sSpeedLabel.setText("Suggested Speed: ");
                    authLabel.setText("Authority: ");
                    speedLimitLabel.setText("Speed Limit: ");
                    setPointInput.setPromptText("");
                    //undergroundDisplay.setText("N/A");
                    //undergroundDisplay.setTextFill(Color.YELLOW);
                    eBrakeToggle.setDisable(true);
                    eBrakeToggle.setStyle("-fx-background-color: #dfdfdf; -fx-text-fill: rgb(43, 39, 49)");
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
       try {
           try {
               float newSPSpeed = Float.parseFloat(setPointInput.getText());
               temp.setSetpointSpeed(newSPSpeed);
               temp.updateStatus();
               temp.updateModelCommandedSpeed();
               setPointInput.setText("");
               setPointInput.setPromptText("" + temp.getsetpointSpeed());
           } catch (NumberFormatException e) {
               System.out.println("nfe found");
           }
       } catch (NullPointerException e){
           System.out.println("Train is not selected!");
       }
    }

    public void toggleEBrake(ActionEvent actionEvent) throws Exception {
        boolean brakeStatus = eBrakeToggle.isSelected(); //get brake status - what we want to set it to
        temp.setEBrake(brakeStatus);
        temp.updateModelEBrake();
        if (brakeStatus) {
            eBrakeToggle.setStyle("-fx-background-color: red; -fx-text-fill: #dfdfdf");
            //eBrakeStatus.set(true);
        } else {
            eBrakeToggle.setStyle("-fx-background-color: #dfdfdf; -fx-text-fill: rgb(43, 39, 49);");
            //eBrakeStatus.set(false);
        }
    }
}
