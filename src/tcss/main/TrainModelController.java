package tcss.main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
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
    @FXML private Label powerLabel;
    @FXML private Label forceLabel;


    // Radio buttons
    @FXML private RadioButton eBrakeOff;
    @FXML private RadioButton eBrakeOn;

    // Radio button groups
    private ToggleGroup eBrakeGroup = new ToggleGroup();

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

        curBeaconLabel.setWrapText(true);
        lastBeaconLabel.setWrapText(true);

        // Add radio buttons to groups
        eBrakeOff.setToggleGroup(eBrakeGroup);
        eBrakeOn.setToggleGroup(eBrakeGroup);

        // Add change listeners to toggle groups
        eBrakeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            public void changed(ObservableValue<? extends Toggle> ob,
                                Toggle o, Toggle n)
            {

                RadioButton rb = (RadioButton)eBrakeGroup.getSelectedToggle();

                if (rb != null) {
                    String s = rb.getText();
                    if(s.equals("Off")) {
                        train.setEBrake(false);
                    }
                    else if(s.equals("On")) {
                        train.setEBrake(true);
                    }
                }
            }
        });

        // Create Timeline for periodic updating
        Timeline loop = new Timeline(new KeyFrame(Duration.seconds(.2), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                update();
//                System.out.println("GUI Updated!!");
            }
        }));
        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();

    }

    public void update() {
        curSpeedLabel.setText("Current Speed: " + train.getCurV());
        accelLabel.setText("Current Acceleration: " + train.getCurA());
        powerLabel.setText("Power Command: " + train.getPower());
        massLabel.setText("Mass: " + train.getMass());
        gradeLabel.setText("Grade: " + train.getGrade());
        forceLabel.setText("Force: " + train.getForce());

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
        distanceLabel.setText("Δx in Block: " + train.getX());
        blocksLabel.setText("Blocks Traveled: " + train.getBlocksTraveled());
        curBeaconLabel.setText("Current Beacon: " + train.getCurBeaconSignal().toString());
        lastBeaconLabel.setText("Last Beacon: " + train.getLastBeaconSignal());

        //Update door statuses
        if(train.getDoor(0)) {
            d1Status.setFill(Color.GREEN);
        }
        else {
            d1Status.setFill(Color.RED);
        }

        if(train.getDoor(1)) {
            d2Status.setFill(Color.GREEN);
        }
        else {
            d2Status.setFill(Color.RED);
        }

        if(train.getDoor(2)) {
            d3Status.setFill(Color.GREEN);
        }
        else {
            d3Status.setFill(Color.RED);
        }

        if(train.getDoor(3)) {
            d4Status.setFill(Color.GREEN);
        }
        else {
            d4Status.setFill(Color.RED);
        }

        if(train.getDoor(4)) {
            d5Status.setFill(Color.GREEN);
        }
        else {
            d5Status.setFill(Color.RED);
        }

        if(train.getDoor(5)) {
            d6Status.setFill(Color.GREEN);
        }
        else {
            d6Status.setFill(Color.RED);
        }

        if(train.getDoor(6)) {
            d7Status.setFill(Color.GREEN);
        }
        else {
            d7Status.setFill(Color.RED);
        }

        if(train.getDoor(7)) {
            d8Status.setFill(Color.GREEN);
        }
        else {
            d8Status.setFill(Color.RED);
        }


    }

    public void passTrain(TrainModel train) {
        this.train = train;

        // Set radio buttons to train's current values
        // EBrake
        if(train.getEBrake()) {
            eBrakeOn.setSelected(true);
        }
        else {
            eBrakeOff.setSelected(true);
        }
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
