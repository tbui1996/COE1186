package tcss.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tcss.ctc.Dispatch;

import java.net.URL;
import java.util.ResourceBundle;

public class DispatchSelectController implements Initializable {
    //UI variables
    @FXML private AnchorPane pane;
    @FXML private Button lineConfirm;
    @FXML private Button backButton;
    @FXML private ChoiceBox<String> lineSelector;
    @FXML private Button stopConfirm;
    @FXML private Button dispatchConfirm;
    @FXML private ChoiceBox<String> stopSelector;
    @FXML private TextField timeToStop;
    @FXML private TextField departureTime;
    @FXML private TextField dispatchName;
    @FXML private TextField dHour;
    @FXML private TextField dMin;
    @FXML private TextField dHalf;
    @FXML private TextField aHour;
    @FXML private TextField aMin;
    @FXML private TextField aHalf;

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
        curr = new Dispatch(lineSelector.getSelectionModel().getSelectedItem().toUpperCase(),("Dispatch " + tcss.main.Main.ctc.numDispatches() + 1));
        //System.out.println(curr.getLine());
        curr.createSchedule(curr.getLine());

        //Populates stop drop down once line is selected
        stopSelector.getItems().clear();
        String [] temp = tcss.main.Main.ctc.getAllStops(curr.getLine());
        for (int i = 0; i < temp.length; i++)
            stopSelector.getItems().add(temp[i]);
    }

    //Approves each stop added to the Schedule
    public void confirmStop(ActionEvent e) throws Exception {
        curr.schedule.addStop(stopSelector.getSelectionModel().getSelectedItem(), Float.parseFloat(timeToStop.getText()) * 60);
    }

    //Approves overall dispatch and adds it to the CTC list
    public void confirmDispatch(ActionEvent e) throws Exception {
        //Set Departure Time if one was entered
        if (!dHour.getText().equals("")) {
            if (dHalf.getText().equals("AM")) {
                if (dHour.getText().equals("12"))
                    curr.setDepartureTime(0, Integer.parseInt(dMin.getText()));
                else
                    curr.setDepartureTime(Integer.parseInt(dHour.getText()), Integer.parseInt(dMin.getText()));
            }
            else {
                if (dHour.getText().equals("12"))
                    curr.setDepartureTime( 12, Integer.parseInt(dMin.getText()));
                else
                    curr.setDepartureTime(Integer.parseInt(dHour.getText()) + 12, Integer.parseInt(dMin.getText()));
            }
        }

        //Set Arrival Time if one was entered
        if (!aHour.getText().equals("")) {
            System.out.println("aHalf: " + aHalf.getText().equals("AM"));
            if (aHalf.getText().equals("AM")) {
                curr.setArrivalTime(Integer.parseInt(aHour.getText()), Integer.parseInt(aMin.getText()));
            }
            else {
                curr.setArrivalTime(Integer.parseInt((aHour.getText())) + 12, Integer.parseInt(aMin.getText()));
            }
        }

        curr.setRequests();
        if (!dispatchName.getText().equals(""))
            curr.setName(dispatchName.getText());

        tcss.main.Main.ctc.addDispatch(curr);
        System.out.println(curr);
    }

    public void goBack(ActionEvent e) throws Exception {
        //Should return back to CTC module screen.  Does not save current progress
        Scene moduleSelect = new Scene(FXMLLoader.load(getClass().getResource("fxml/CTC.fxml")));
        Stage window = (Stage) pane.getScene().getWindow();
        window.setScene(moduleSelect);
        window.setTitle("CTC");
    }
}
