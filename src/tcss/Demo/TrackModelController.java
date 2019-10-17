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
    @FXML private Label idLabel;
    @FXML private Label sSpeedLabel;
    @FXML private Label authLabel;
    @FXML private Label speedLimitLabel;
    @FXML private AnchorPane pane;
    @FXML private ChoiceBox blockChoice;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        blockChoice.getItems().add("Select Block");
        Track track = Main.track;

        // Testing
        for(Block b: Main.blocks) {
            blockChoice.getItems().add("Block " + b.getBlockNum());
        }

        blockChoice.setValue("Select Block");
        blockChoice.setTooltip(new Tooltip("Select a block to view"));
        blockChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                if((Integer) number2 > 0) {
//                    System.out.println(trainChoice.getItems().get((Integer) number2));
//                    System.out.println("ID: " + Main.trains.get((Integer)number2 - 1).getID());

                    Block cur = Main.blocks.get((Integer)number2-1);

                    idLabel.setText("Block #: " + cur.getBlockNum());
                    sSpeedLabel.setText("Suggested Speed: " + track.getSuggestedSpeed());
                    authLabel.setText("Authority: " + track.getAuthority());
                    speedLimitLabel.setText("Speed Limit: " + cur.getSpeedLimit());

                } else {
                    idLabel.setText("Block #: ");
                    sSpeedLabel.setText("Suggested Speed: ");
                    authLabel.setText("Authority: ");
                    speedLimitLabel.setText("Speed Limit: ");
                }
            }
        });



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