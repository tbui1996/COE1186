package tcss.Demo;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import tcss.ctc.Dispatch;

public class DispatchSelectController implements Initializable {
    //UI variables
    @FXML private AnchorPane pane;
    @FXML private Button lineConfirm;
    @FXML private Button backButton;
    @FXML private ChoiceBox<String> lineSelector;
    @FXML private Button stopConfirm;
    @FXML private Button dispatchConfirm;
    @FXML private ChoiceBox<String> stopSelector;

    //Other variables
    private Dispatch curr;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Populates drop down with lines
        lineSelector.getItems().add("Red");
        lineSelector.getItems().add("Green");
        lineSelector.setValue("Red");
        lineSelector.setTooltip(new Tooltip("Select a line for the dispatch"));
        //lineConfirm.setText("Works");
    }

    public void confirmLine(ActionEvent e) throws Exception {
        curr = new Dispatch(lineSelector.getSelectionModel().getSelectedItem().toString().toUpperCase());
        //System.out.println(curr.getLine());

        //Populates stop drop down once line is selected
        String [] temp = Main.ctc.getAllStops(curr.getLine());
        for (int i = 0; i < temp.length; i++)
            stopSelector.getItems().add(temp[i]);
    }

    //Approves each stop added to the Schedule
    public void confirmStop(ActionEvent e) throws Exception {
        curr.createSchedule();
    }

    //Approves overall dispatch and adds it to the CTC list
    public void confirmDispatch(ActionEvent e) throws Exception {
        curr.setRequests();
        Main.ctc.addDispatch(curr);
        System.out.println(curr);
    }

    public void goBack(ActionEvent e) throws Exception {
        //Should return back to CTC module screen.  Does not save current progress
        Scene moduleSelect = new Scene(FXMLLoader.load(getClass().getResource("CTC.fxml")));
        Stage window = (Stage) pane.getScene().getWindow();
        window.setScene(moduleSelect);
        window.setTitle("CTC");
    }
}
