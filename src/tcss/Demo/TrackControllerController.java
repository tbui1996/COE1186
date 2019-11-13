package tcss.Demo;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tcss.trackcontroller.TrackController;
import tcss.trackmodel.Track;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TrackControllerController implements Initializable{
    Track track;
    @FXML private Label idLabel;
    @FXML private Label sSpeedLabel;
    @FXML private Label authLabel;
    @FXML private AnchorPane pane;
    @FXML private ChoiceBox trackChoice;
    @FXML private Label occupiedLabel;

    private ArrayList<TrackController> trackControllers;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        TrackController tc = Main.tc;
        trackChoice.getItems().add("Select Track Controller");
        trackChoice.getItems().add("Red Track Controller1");
        trackChoice.getItems().add("Red Track Controller2");
        trackChoice.getItems().add("Red Track Controller3");
        trackChoice.getItems().add("Red Track Controller4");
        trackChoice.getItems().add("Green Track Controller1");
        trackChoice.getItems().add("Green Track Controller2");
        trackChoice.getItems().add("Green Track Controller3");
        trackChoice.getItems().add("Greenn Track Controller4");

        trackChoice.setValue("Select Track Controller");

        trackChoice.getSelectionModel().selectedItemProperty().addListener((obs, wasShowing, isNowShowing) -> {
            if (obs != isNowShowing) {
                trackChoice.setValue(isNowShowing);
            }
            else{
                trackChoice.setValue(wasShowing);
            }
        });
    }
    public String updateOccupied(TrackController tc){
        if(tc.setOccupancy()){
            return "Occupied";
        } else{
            return "Not Occupied";
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