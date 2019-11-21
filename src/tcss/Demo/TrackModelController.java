package tcss.Demo;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import tcss.trackmodel.Block;
import tcss.trackmodel.Track;

import java.net.URL;
import java.util.ResourceBundle;

public class TrackModelController implements Initializable {

    // UI variables
    @FXML private AnchorPane pane;

    @FXML private ChoiceBox lineChoice;
    @FXML private ChoiceBox blockChoice;

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

    private Track currTrack;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lineChoice.getItems().add("Select Line");
        lineChoice.getItems().add("Red Line");
        lineChoice.getItems().add("Green Line");

        blockChoice.getItems().add("Select Block");

        Track redLine = Main.redLine;
        Track greenLine = Main.greenLine;


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

                    blockChoice = new ChoiceBox();
                    blockChoice.getItems().add("Select Block");
                    // Populate Line Blocks
                    for(int i=1;i<=redLine.getBlockHashMap().size();i++) {
                        Block b = redLine.getBlock(i);
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

                    Block cur = redLine.getBlock((Integer) number2-1);

                    blockNumLabel.setText("Block #: " + cur.getBlockNum());
                    sectionLabel.setText("Section #: " + cur.getSection());
                    sSpeedLabel.setText("Suggested Speed: " + cur.getSuggestedSpeed() + " mph");
                    authLabel.setText("Authority: " + cur.getAuthority());
                    lengthLabel.setText("Length: " + cur.getLength() + " m");
                    gradeLabel.setText("Grade: " + cur.getGrade() + "%");
                    speedLimitLabel.setText("Speed Limit: " + cur.getSpeedLimit() + " mph");
                    elevLabel.setText("Elevation: " + cur.getElevation() + " m");
                    cumulativeElevLabel.setText("Cumulative Elevation: " + cur.getCumulativeElevation() + " m");
                    undergroundLabel.setText("Underground: " + updateUnderground(cur));
                    occupiedLabel.setText("Occupied: " + updateOccupied(cur));
                    stationLabel.setText("Station: " + updateStation(cur));
                    switchLabel.setText("Switch: " + updateSwitch(cur));
                    rxrLabel.setText("RXR: " + updateRXR(cur));
                    beaconLabel.setText("Beacon: " + updateBeacon(cur));

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
                }
            }
        });
    }

    public String updateUnderground(Block cur){
        if(cur.isUnderground()){
            return "Yes";
        }else{
            return "No";
        }
    }

    public String updateOccupied(Block cur){
        if(cur.isOccupied()){
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

    public void goBack(ActionEvent actionEvent) throws Exception {
//        Parent trainModelParent = FXMLLoader.load(getClass().getResource("ModuleSelection.fxml"));
//        Scene trainModelView = new Scene(trainModelParent);
//
//        // Get stage info
//        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
//        window.setScene(trainModelView);
//        window.show();

        Scene moduleSelect = new Scene(FXMLLoader.load(getClass().getResource("ModuleSelection.fxml")));
        Stage window = (Stage) pane.getScene().getWindow();
        window.setScene(moduleSelect);
        window.setTitle("Module Selection");

    }
}