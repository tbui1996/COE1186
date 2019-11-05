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
    @FXML private Label suggestedSpeedLabel;
    @FXML private TextField setPointInput;
    @FXML private Button confirmSetpoint;
    @FXML private Label authLabel;
    @FXML private AnchorPane pane;
    @FXML private ToggleButton eBrakeToggle;
    @FXML private ToggleButton opModeToggle;
    @FXML private Label undergroundDisplay;


    private TrainController tc;

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
                    TrainController cur = Main.trains.get((Integer)number2-1).getTControl();
                    cur.update();
                    tc = cur;
                    update();
                } else {
                    tc = null; //deselect train
                    idLabel.setText("ID: ");
                    suggestedSpeedLabel.setText("Suggested Speed: ");
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

    public void update(){
        idLabel.setText("ID: " + tc.getID());
        System.out.println("tc is: " + tc.toString());
        suggestedSpeedLabel.setText("Suggested Speed: " + tc.getSSpeed());
        System.out.println("" + tc.getSSpeed() + " with model: ");
        speedLimitLabel.setText("Speed Limit: " + tc.getSpeedLimit());
        if(tc.getOpMode()){
            opModeToggle.setStyle("-fx-background-color: red; -fx-text-fill: #dfdfdf");
        } else {
            opModeToggle.setStyle("-fx-background-color: #dfdfdf; -fx-text-fill: rgb(43, 39, 49)");
        }
        setPointInput.setText("");
        setPointInput.setPromptText("" + tc.getsetpointSpeed());
        authLabel.setText("Authority: " + tc.getAuthority());
        if (tc.getUnderground()){
            undergroundDisplay.setText("True");
            undergroundDisplay.setTextFill(Color.GREEN);
        } else {
            undergroundDisplay.setText("False");
            undergroundDisplay.setTextFill(Color.RED);
        }
        eBrakeToggle.setSelected(tc.getEBrake());
        if(tc.getEBrake()) {
            eBrakeToggle.setStyle("-fx-background-color: red; -fx-text-fill: #dfdfdf");
        } else {
            eBrakeToggle.setStyle("-fx-background-color: #dfdfdf; -fx-text-fill: rgb(43, 39, 49)");
        }
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
               tc.setSetpointSpeed(newSPSpeed);
               tc.update();
               tc.updateModelCommandedSpeed();
               setPointInput.setText("");
               setPointInput.setPromptText("" + tc.getsetpointSpeed());
           } catch (NumberFormatException e) {
               System.out.println("nfe found");
           }
       } catch (NullPointerException e){
           System.out.println("Train is not selected!");
       }
    }

    public void toggleEBrake(ActionEvent actionEvent) throws Exception {
        boolean brakeStatus = eBrakeToggle.isSelected(); //get brake status - what we want to set it to
        tc.setEBrake(brakeStatus);
        tc.updateModelEBrake();
        if (brakeStatus) {
            eBrakeToggle.setStyle("-fx-background-color: red; -fx-text-fill: #dfdfdf");
            //eBrakeStatus.set(true);
        } else {
            eBrakeToggle.setStyle("-fx-background-color: #dfdfdf; -fx-text-fill: rgb(43, 39, 49);");
            //eBrakeStatus.set(false);
        }
    }

    public void changeOperationMode(ActionEvent actionEvent) throws Exception {
       // boolean opMode =
    }
}
