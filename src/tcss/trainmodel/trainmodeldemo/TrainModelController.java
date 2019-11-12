package tcss.trainmodel.trainmodeldemo;

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

        curBeaconLabel.setWrapText(true);
        lastBeaconLabel.setWrapText(true);

        // Add radio buttons to groups
        sBrakeOff.setToggleGroup(sBrakeGroup);
        sBrakeOn.setToggleGroup(sBrakeGroup);

        eBrakeOff.setToggleGroup(eBrakeGroup);
        eBrakeOn.setToggleGroup(eBrakeGroup);

        lightsOff.setToggleGroup(lightsGroup);
        lightsOn.setToggleGroup(lightsGroup);

        // Set radio buttons to train's current state
//        if(train.getSBrake()) {
//            sBrakeOn.setSelected(true);
//        }
//        else {
//            sBrakeOff.setSelected(true);
//        }

        // Add change listeners to toggle groups
        lightsGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            public void changed(ObservableValue<? extends Toggle> ob,
                                Toggle o, Toggle n)
            {

                RadioButton rb = (RadioButton)lightsGroup.getSelectedToggle();

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

        sBrakeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            public void changed(ObservableValue<? extends Toggle> ob,
                                Toggle o, Toggle n)
            {

                RadioButton rb = (RadioButton)sBrakeGroup.getSelectedToggle();

                if (rb != null) {
                    String s = rb.getText();
                    if(s.equals("Off")) {
                        train.setSBrake(false);
                    }
                    else if(s.equals("On")) {
                        train.setSBrake(true);
                    }
                }
            }
        });

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

        // Make sure certain input text fields accept only numbers
        // Power command text field
        powerIn.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
//                if (!newValue.matches("\\d*")) {
                if(!newValue.matches("\\d*(\\.\\d*)?")) {
                    powerIn.setText(oldValue);
                }
            }
        });

        // Grade text field
        gradeIn.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
//                if (!newValue.matches("\\d*")) {
                if(!newValue.matches("\\d*(\\.\\d*)?")) {
                    gradeIn.setText(oldValue);
                }
            }
        });

        // Passengers text field
        passengersIn.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
//                if (!newValue.matches("\\d*")) {
                if(!newValue.matches("\\d*")) {
                    passengersIn.setText(oldValue);
                }
            }
        });

        // Set character limit for beacon input
//        beaconIn.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
//                if(t1.length() > 255) {
//                    beaconIn.setText(beaconIn.getText().substring(0, 128));
//                }
//            }
//        });

        // Add listeners to check boxes
        d1.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                train.setDoor(0, t1);
            }
        });

        d2.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                train.setDoor(1, t1);
            }
        });

        d3.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                train.setDoor(2, t1);
            }
        });

        d4.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                train.setDoor(3, t1);
            }
        });

        d5.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                train.setDoor(4, t1);
            }
        });

        d6.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                train.setDoor(5, t1);
            }
        });

        d7.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                train.setDoor(6, t1);
            }
        });

        d8.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                train.setDoor(7, t1);
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
    }

    public void inputPower() {
        if(!powerIn.getText().isEmpty()) {
            float p = Float.parseFloat(powerIn.getText());
            powerIn.clear();
//            System.out.println(p);
            train.powerCmd(p);
        }
    }

    public void inputGrade() {
        if(!gradeIn.getText().isEmpty()) {
            float p = Float.parseFloat(gradeIn.getText());
            gradeIn.clear();
            train.setGrade(p);
        }
    }

    public void inputBeacon() {
        if(!beaconIn.getText().isEmpty()) {
            String s = beaconIn.getText();
            if(s.length() > 128) {
                s = s.substring(0, 128);
            }
            beaconIn.clear();
            train.setBeacon(s.toCharArray());
        }
    }

    public void inputPassengers() {
        if(!passengersIn.getText().isEmpty()) {
            int i = Integer.parseInt(passengersIn.getText());
            passengersIn.clear();
            train.addPassengers(i);
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
