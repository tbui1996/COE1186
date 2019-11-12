package tcss.trainmodel.trainmodeldemo;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import tcss.trainmodel.TrainModel;

import java.net.URL;
import java.util.ResourceBundle;

public class TrainModelController implements Initializable {

    // UI variables

    // Labels
    @FXML Label titleLabel;
    @FXML private Label curSpeedLabel;
    @FXML private Label accelLabel;
    @FXML private Label massLabel;
    @FXML private Label gradeLabel;
    @FXML private Label sBrakeLabel;
    @FXML private Label eBrakeLabel;
    @FXML private Label passengersLabel;
    @FXML private Label lightsLabel;
    @FXML private Label distanceLabel;
    @FXML private Label blocksLabel;
    @FXML private Label curBeaconLabel;
    @FXML private Label lastBeaconLabel;

    // TextFields
    @FXML private TextField powerIn;
    @FXML private TextField gradeIn;
    @FXML private TextField beaconIn;
    @FXML private TextField passengersIn;

    // Submit buttons
    @FXML private Button powerSubmit;
    @FXML private Button gradeSubmit;
    @FXML private Button beaconSubmit;
    @FXML private Button passengerSubmit;
    @FXML private Button exitButton;

    // Radio buttons
    @FXML private RadioButton sBrakeOff;
    @FXML private RadioButton sBrakeOn;
    @FXML private RadioButton eBrakeOff;
    @FXML private RadioButton eBrakeOn;
    @FXML private RadioButton lightsOff;
    @FXML private RadioButton lightsOn;

    // Radio button groups
    private ToggleGroup sBrakeGroup = new ToggleGroup();
    private ToggleGroup eBrakeGroup = new ToggleGroup();
    private ToggleGroup lightsGroup = new ToggleGroup();

    // Check boxes
    @FXML private CheckBox d1;
    @FXML private CheckBox d2;
    @FXML private CheckBox d3;
    @FXML private CheckBox d4;
    @FXML private CheckBox d5;
    @FXML private CheckBox d6;
    @FXML private CheckBox d7;
    @FXML private CheckBox d8;

    // Circles
    @FXML private Circle d1Status;
    @FXML private Circle d2Status;
    @FXML private Circle d3Status;
    @FXML private Circle d4Status;
    @FXML private Circle d5Status;
    @FXML private Circle d6Status;
    @FXML private Circle d7Status;
    @FXML private Circle d8Status;

    @FXML private AnchorPane pane;
//    @FXML private ChoiceBox trainChoice;

    // Testing input filter
//    @FXML private TextField tField;

    // Train for the current window
    TrainModel train;



    @Override
    public void initialize(URL url, ResourceBundle rb) {

//        train = Main.train;


        // Add radio buttons to groups
        sBrakeOff.setToggleGroup(sBrakeGroup);
        sBrakeOn.setToggleGroup(sBrakeGroup);

        eBrakeOff.setToggleGroup(eBrakeGroup);
        eBrakeOn.setToggleGroup(eBrakeGroup);

        lightsOff.setToggleGroup(lightsGroup);
        lightsOn.setToggleGroup(lightsGroup);

        // Add change listeners to toggle groups
        sBrakeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            public void changed(ObservableValue<? extends Toggle> ob,
                                Toggle o, Toggle n)
            {

                RadioButton rb = (RadioButton)sBrakeGroup.getSelectedToggle();

                if (rb != null) {
                    String s = rb.getText();
                    if(s.equals("Off")) {
                        train.setLights(false);
                    }
                    else if(s.equals("On")) {
                        train.setLights(true);
                    }
                }
            }
        });

//        update();





//        trainChoice.getItems().add("Select Train");
//
//        // Testing
////        for(TrainModel t: Main.trains) {
////            trainChoice.getItems().add("Train " + t.getID());
////        }
//
//        trainChoice.setValue("Select Train");
//        trainChoice.setTooltip(new Tooltip("Select a train to view"));
//        trainChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
//                if((Integer) number2 > 0) {
//
//                    TrainModel cur = Main.trains.get((Integer)number2-1);
//
//                    idLabel.setText("ID: " + cur.getID());
//                    sSpeedLabel.setText("Suggested Speed: " + cur.getSSpeed());
//                    authLabel.setText("Authority: " + cur.getAuthority());
//                    speedLimitLabel.setText("Speed Limit: " + cur.getSpeedLimit());
//                    gradeLabel.setText("Grade: " + cur.getGrade() + "%");
//                    cmdSpeed.setText("Commanded Speed: " + cur.getCmdSpeed());
//                    if(cur.getUnderground()) {
//                        underStatus.setText("True");
//                        underStatus.setTextFill(Color.GREEN);
//                    } else {
//                        underStatus.setText("False");
//                        underStatus.setTextFill(Color.RED);
//                    }
//                    if(cur.getEBrake()) {
//                        eBrakeStatus.setText("True");
//                        eBrakeStatus.setTextFill(Color.GREEN);
//                    } else {
//                        eBrakeStatus.setText("False");
//                        eBrakeStatus.setTextFill(Color.RED);
//                    }
//
//                } else {
//                    idLabel.setText("ID: ");
//                    sSpeedLabel.setText("Suggested Speed: ");
//                    authLabel.setText("Authority: ");
//                    speedLimitLabel.setText("Speed Limit: ");
//                }
//            }
//        });
//
//

    }

    public void update() {
        curSpeedLabel.setText("Current Speed: " + train.getCurV());
        accelLabel.setText("Current Acceleration: " + train.getCurA());
        massLabel.setText("Mass: " + train.getMass());
        gradeLabel.setText("Grade: " + train.getGrade());
        if(train.getSBrake()) {
            sBrakeLabel.setText("True");
            sBrakeLabel.setTextFill(Color.GREEN);
        }
        else {
            sBrakeLabel.setText("False");
            sBrakeLabel.setTextFill(Color.RED);
        }
        if(train.getEBrake())
        {
            eBrakeLabel.setText("True");
            eBrakeLabel.setTextFill(Color.GREEN);
        }
        else {
            eBrakeLabel.setText("False");
            eBrakeLabel.setTextFill(Color.RED);
        }
        passengersLabel.setText("Passengers: " + train.getPassengers());
        if(train.getLights()) {
            lightsLabel.setText("True");
            lightsLabel.setTextFill(Color.GREEN);
        }
        else {
            lightsLabel.setText("False");
            lightsLabel.setTextFill(Color.RED);
        }
        distanceLabel.setText("Distance Traveled in Block: " + train.getX());
        blocksLabel.setText("Blocks Traveled: " + train.getBlocksTraveled());
        curBeaconLabel.setText("Current Beacon: " + train.getCurBeaconSignal());
        lastBeaconLabel.setText("Last Beacon: " + train.getLastBeaconSignal());
    }

    public void passTrain(TrainModel train) {
        this.train = train;
    }

    public void closeWindow() {
        System.exit(7);
    }

    public void goBack(ActionEvent actionEvent) throws Exception {
//        Parent trainModelParent = FXMLLoader.load(getClass().getResource("ModuleSelection.fxml"));
//        Scene trainModelView = new Scene(trainModelParent);
//
//        // Get stage info
//        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
//        window.setScene(trainModelView);
//        window.show();

//        Scene moduleSelect = new Scene(FXMLLoader.load(getClass().getResource("ModuleSelection.fxml")));
//        Stage window = (Stage) pane.getScene().getWindow();
//        window.setScene(moduleSelect);
//        window.setTitle("Module Selection");

    }
}
