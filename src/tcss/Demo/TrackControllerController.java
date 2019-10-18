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
import tcss.trackcontroller.TrackController;
import tcss.trackmodel.Block;
import tcss.trackmodel.Track;
import tcss.trackcontroller.TrackController;

import java.net.URL;
import java.util.ResourceBundle;

public class TrackControllerController implements Initializable{
    @FXML private Label sSpeedLabel;
    @FXML private Label authLabel;
    @FXML private AnchorPane pane;
    @FXML private ChoiceBox trackChoice;
    @FXML private Label occupiedLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TrackController tc = Main.tc;

        trackChoice.getItems().add("Select Block");
        trackChoice.setValue("Select Block");
        trackChoice.setTooltip(new Tooltip("Select a block to view"));


            trackChoice.getItems().add("Track Controller " + tc.getTrackNum());


        trackChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {

                TrackController cur = Main.ctc.getTrackController((Integer)number2-1);
                if((Integer) number2 > 0) {
                    System.out.println(cur.getSs());
                    sSpeedLabel.setText("Suggested Speed: " + cur.getSs() + " mph");
                    occupiedLabel.setText("Occupancy: "+ updateOccupied(cur));
                    authLabel.setText("Authority: "+ cur.getAuth());

                }
                else{
                    sSpeedLabel.setText("Suggested Speed: ");
                    occupiedLabel.setText("Occupancy: ");
                    authLabel.setText("Authority: ");
                }
            }
        });
    }
    public String updateOccupied(TrackController tc){
        if(tc.getOccupancy()){
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