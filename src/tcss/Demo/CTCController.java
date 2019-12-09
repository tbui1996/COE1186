package tcss.Demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import tcss.ctc.Dispatch;
import tcss.trainmodel.TrainModel;

import java.net.URL;
import java.util.ResourceBundle;



public class CTCController implements Initializable{

    // UI variables
    @FXML private Button dispatch;
    @FXML private TextField SS;
    @FXML private TextField auth;
    @FXML private VBox dispatchList;
    @FXML Label dispatch1;
    @FXML private AnchorPane pane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        idLabel.setText("ID: ");
//        sSpeedLabel.setText("Suggested Speed: 0 mph");
//        authLabel.setText("Authority: 0 Blocks");
//        speedLimitLabel.setText("Speed Limit: 0 mph");
    }

    public void sendDispatch(ActionEvent actionEvent) throws Exception {
        TrainModel temp = new TrainModel(Float.parseFloat(SS.getText()), Integer.parseInt(auth.getText()), Main.ctc.trainList.size(), 40);
        //Main.ctc.createDispatch("train 1", Float.parseFloat(SS.getText()), Integer.parseInt(auth.getText()), temp);
        Main.trains.add(temp);
        dispatch.setText("DISPATCH");
        //Main.tc.initTrain();
    }

    public void getDispatches() {
//        Dispatch currDispatch = Main.ctc.getFirstDispatch();
//        dispatch1 = new Label(currDispatch.toString());
//        dispatchList.getChildren().add(dispatch1);

        //dispatchList = 0;
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
