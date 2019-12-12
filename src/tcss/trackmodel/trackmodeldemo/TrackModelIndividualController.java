package tcss.trackmodel.trackmodeldemo;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tcss.trackmodel.Block;
import tcss.trackmodel.Block.Failure;
import tcss.trackmodel.Track;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class TrackModelIndividualController implements Initializable {

    // UI variables
    @FXML private AnchorPane pane;

    @FXML private ChoiceBox lineChoice;
    @FXML private ChoiceBox blockChoice;
    @FXML private ChoiceBox failureChoice;

    @FXML private Label blockNumLabel;
    @FXML private Label sectionLabel;
    @FXML private Label sSpeedLabel;
    @FXML private Label authLabel;
    @FXML private Label lengthLabel;
    @FXML private Label gradeLabel;
    @FXML private Label speedLimitLabel;
    @FXML private Label elevLabel;
    @FXML private Label cumulativeElevLabel;
    @FXML private Label undergroundLabel;
    @FXML private Label occupiedLabel;
    @FXML private Label stationLabel;
    @FXML private Label switchLabel;
    @FXML private Label rxrLabel;
    @FXML private Label beaconLabel;
    @FXML private Label closedLabel;
    @FXML private Label failureModeLabel;

    @FXML private RadioButton rxrRaiseButton;
    @FXML private RadioButton rxrLowerButton;
    @FXML private RadioButton switchStraightButton;
    @FXML private RadioButton switchBranchButton;
    @FXML private RadioButton closeRadButton;
    @FXML private RadioButton openRadButton;

    // Radio button groups
    private ToggleGroup rxrGroup = new ToggleGroup();
    private ToggleGroup switchGroup = new ToggleGroup();
    private ToggleGroup maintenanceGroup = new ToggleGroup();

    private Track currTrack;
    private Block cur;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lineChoice.getItems().add("Select Line");
        lineChoice.getItems().add("Red Line");
        lineChoice.getItems().add("Green Line");

        blockChoice.getItems().add("Select Block");

        Track redLine = tcss.trackmodel.trackmodeldemo.Main.redLine;
        Track greenLine = tcss.trackmodel.trackmodeldemo.Main.greenLine;

        // Add radio buttons to groups
        rxrRaiseButton.setToggleGroup(rxrGroup);
        rxrLowerButton.setToggleGroup(rxrGroup);
        switchStraightButton.setToggleGroup(switchGroup);
        switchBranchButton.setToggleGroup(switchGroup);
        closeRadButton.setToggleGroup(maintenanceGroup);
        openRadButton.setToggleGroup(maintenanceGroup);

        failureChoice.getItems().add("Select Failure");
        failureChoice.getItems().add("BROKEN RAIL");
        failureChoice.getItems().add("CIRCUIT FAILURE");
        failureChoice.getItems().add("POWER FAILURE");
        failureChoice.getItems().add("NONE");


        lineChoice.setValue("Select Line");
        lineChoice.setTooltip(new Tooltip("Select a line to view"));
        lineChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                if((Integer) number2 > 0) {
//                    System.out.println(trainChoice.getItems().get((Integer) number2));
//                    System.out.println("ID: " + Main.trains.get((Integer)number2 - 1).getID());

                    if((Integer) number2 == 1){
                        currTrack = redLine;
                    }else if((Integer) number2 == 2){
                        currTrack = greenLine;
                    }

                    blockChoice.getItems().clear();
                    blockChoice.getItems().add("Select Block");
                    // Populate Line Blocks
                    for(int i=1;i<=currTrack.getBlockHashMap().size();i++) {
                        System.out.println("Adding block " + i + " to blockChoice");
                        Block b = currTrack.getBlock(i);
                        blockChoice.getItems().add(b.getSection() + Integer.toString(b.getBlockNum()));
                    }
                }
            }
        });

        blockChoice.setValue("Select Block");
        blockChoice.setTooltip(new Tooltip("Select a block to view"));
        blockChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                if((Integer) number2 > 0) {
//                    System.out.println(trainChoice.getItems().get((Integer) number2));
//                    System.out.println("ID: " + Main.trains.get((Integer)number2 - 1).getID());

                    cur = currTrack.getBlock((Integer) number2);
                    DecimalFormat df = new DecimalFormat("#.##");
                    blockNumLabel.setText("Block #: " + cur.getBlockNum());
                    sectionLabel.setText("Section #: " + cur.getSection());
                    sSpeedLabel.setText("Suggested Speed: " + Double.parseDouble(df.format(updateSuggestedSpeed(cur) * 0.621)) + " mph");
                    authLabel.setText("Authority: " + updateAuth(cur) + " blocks");
                    lengthLabel.setText("Length: " + Double.parseDouble(df.format(cur.getLength() * 3.28)) + " ft");
                    gradeLabel.setText("Grade: " + cur.getGrade() + "%");
                    speedLimitLabel.setText("Speed Limit: " + Double.parseDouble(df.format(cur.getSpeedLimit() * 0.621)) + " mph");
                    elevLabel.setText("Elevation: " + Double.parseDouble(df.format(cur.getElevation() * 3.28)) + " ft");
                    cumulativeElevLabel.setText("Cumulative Elevation: " + Double.parseDouble(df.format(cur.getCumulativeElevation() * 3.28)) + " ft");
                    undergroundLabel.setText("Underground: " + updateUnderground(cur));
                    occupiedLabel.setText("Occupied: " + updateOccupied(cur));
                    stationLabel.setText("Station: " + updateStation(cur));
                    switchLabel.setText("Switch: " + updateSwitch(cur));
                    rxrLabel.setText("RXR: " + updateRXR(cur));
                    beaconLabel.setText("Beacon: " + updateBeacon(cur));
                    closedLabel.setText("Closed: " + updateClosed(cur));
                    failureModeLabel.setText("Failure Mode: " + updateFailureMode(cur));

                } else {
                    blockNumLabel.setText("Block #: ");
                    sectionLabel.setText("Section #: ");
                    sSpeedLabel.setText("Suggested Speed: ");
                    authLabel.setText("Authority: ");
                    gradeLabel.setText("Grade: ");
                    speedLimitLabel.setText("Speed Limit: ");
                    elevLabel.setText("Elevation: ");
                    cumulativeElevLabel.setText("Cumulative Elevation: ");
                    undergroundLabel.setText("Underground: ");
                    beaconLabel.setText("Beacon: ");
                    stationLabel.setText("Station: ");
                    switchLabel.setText("Switch: ");
                    rxrLabel.setText("RXR: ");
                    closedLabel.setText("Closed: ");
                    failureModeLabel.setText("Failure Mode: ");
                }
            }
        });

        rxrGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n){


                RadioButton rb = (RadioButton)rxrGroup.getSelectedToggle();

                if (rb != null) {
                    System.out.println();
                    String s = rb.getText();
                    if(cur.getRXR() != null) {
                        if (rb.getText().equals("Raise RXR")) {
                            cur.getRXR().setDown(false);
                            rxrLabel.setText("RXR: " + updateRXR(cur));
                        } else if (rb.getText().equals("Lower RXR")) {
                            cur.getRXR().setDown(true);
                            rxrLabel.setText("RXR: " + updateRXR(cur));
                        }
                    }
                }
            }
        });

        switchGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n){

                RadioButton rb = (RadioButton)switchGroup.getSelectedToggle();
                System.out.println(rb.getText());
                if (rb != null) {
                    String s = rb.getText();
                    if(cur.getSwitch() != null) {
                        if (rb.getText().equals("Branch Switch")) {
                            cur.getSwitch().setStraight(false);
                            switchLabel.setText("Switch: " + updateSwitch(cur));
                        } else if (rb.getText().equals("Straight Switch")) {
                            cur.getSwitch().setStraight(true);
                            switchLabel.setText("Switch: " + updateSwitch(cur));
                        }
                    }
                }
            }
        });

        maintenanceGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n){

                RadioButton rb = (RadioButton)maintenanceGroup.getSelectedToggle();

                if (rb != null) {
                    String s = rb.getText();
                    if (rb.getText().equals("Open Block")) {
                        cur.setClosed(false);
                        closedLabel.setText("Closed: " + updateClosed(cur));
                    } else if (rb.getText().equals("Close Block")) {
                        cur.setClosed(true);
                        closedLabel.setText("Closed: " + updateClosed(cur));
                    }
                }
            }
        });

        failureChoice.setValue("Select Failure");
        failureChoice.setTooltip(new Tooltip("Set a failure mode"));
        failureChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                if((Integer) number2 > 0) {
//                    System.out.println(trainChoice.getItems().get((Integer) number2));
//                    System.out.println("ID: " + Main.trains.get((Integer)number2 - 1).getID());

                    switch((int) number2 - 1){
                        case 0:
                            cur.setFailure(Failure.BROKEN_RAIL);
                            break;
                        case 1:
                            cur.setFailure(Failure.CIRCUIT_FAILURE);
                            break;
                        case 2:
                            cur.setFailure(Failure.POWER_FAILURE);
                            break;
                        default:
                            cur.setFailure(Failure.NONE);
                    }
                    failureModeLabel.setText("Failure Mode: " + updateFailureMode(cur));
                }
            }
        });
    }

    public float updateSuggestedSpeed(Block cur){
        if(cur.isOccupied()){
            return cur.getTrain().getSSpeed();
        }else{
            return 0;
        }
    }

    public int updateAuth(Block cur){
        if(cur.isOccupied()){
            return cur.getTrain().getAuthority();
        }else{
            return 0;
        }
    }

    public String updateUnderground(Block cur){
        if(cur.isUnderground()){
            return "Yes";
        }else{
            return "No";
        }
    }

    public String updateOccupied(Block cur){
        if(cur.getTrain() != null){
            return "Yes";
        }else{
            return "No";
        }
    }

    public String updateStation(Block cur){
        if(cur.getStation() == null) {
            return "N/A";
        }else{
            return cur.getStation().getName();
        }
    }

    public String updateSwitch(Block cur){
        if(cur.getSwitch() == null){
            return "N/A";
        }else{
            if(cur.getSwitch().getStraight()){
                return "Straight";
            }else{
                return "Branched";
            }
        }
    }

    public String updateRXR(Block cur){
        if(cur.getRXR() == null){
            return "N/A";
        }else{
            if(cur.getRXR().isDown()){
                return "Down";
            }else{
                return "Up";
            }
        }
    }

    public String updateBeacon(Block cur){
        if(cur.getBeacon() == null){
            return "N/A";
        }else{
            return cur.getBeacon().getData().toString();
        }
    }

    public String updateClosed(Block cur){
        if(cur.isClosed()){
            return "Yes";
        }
        return "No";
    }

    public String updateFailureMode(Block cur){
        switch(cur.getFailure()){
            case BROKEN_RAIL:
                return "BROKEN RAIL";
            case CIRCUIT_FAILURE:
                return "CIRCUIT FAILURE";
            case POWER_FAILURE:
                return "POWER FAILURE";
            default:
                return "NONE";
        }
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

        FXMLLoader trackLoader = new FXMLLoader(getClass().getResource("fxml/TrackSelect.fxml"));
        Stage trackStage = new Stage();
        Parent trackRoot = trackLoader.load();
        trackStage.setTitle("Select Track Module");
        trackStage.setScene(new Scene(trackRoot));
        trackStage.setResizable(false);
        trackStage.getIcons().add(new Image("file:resources/train.png"));

        trackStage.show();

        Stage s = (Stage) lengthLabel.getScene().getWindow();
        s.close();


    }
}