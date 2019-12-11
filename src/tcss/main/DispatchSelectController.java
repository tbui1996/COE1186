package tcss.main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx;
import tcss.ctc.Dispatch;
import tcss.traincontroller.TrainController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DispatchSelectController implements Initializable {
    //UI variables
    @FXML private AnchorPane pane;                  //Anchor Pane
    @FXML private Button lineConfirm;               //Select Line button
    @FXML private Button backButton;                //Back Button
    @FXML private ChoiceBox<String> lineSelector;   //Select Line Box
    @FXML private Button stopConfirm;               //Add stop button
    @FXML private Button dispatchConfirm;           //Create Dispatch Button
    @FXML private ChoiceBox<String> stopSelector;   //Select Stop Block Box
    @FXML private TextField timeToStop;             //Added Dwell Time for each stop
    @FXML private TextField scheduleName;           //Schedule name text box
    @FXML private TextField trainName;              //Train name text box
    @FXML private TextField dHour;                  //Departure hour
    @FXML private TextField dMin;                   //Departure minute
    @FXML private ChoiceBox<String> dHalf;          //AM/PM option
    @FXML private HBox dTimeLine;                   //HBox for departure time selection
    @FXML private CheckBox autoButton;              //Automatic choice box button
    @FXML private Button browseSchedule;

    //Other variables
    private Dispatch curr;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Populates drop down with lines
        lineSelector.getItems().clear();
        lineSelector.getItems().add("Red");
        lineSelector.getItems().add("Green");
        lineSelector.setValue("Red");
        lineSelector.setTooltip(new Tooltip("Select a line for the dispatch"));

        //Populates dHalf drop down
        dHalf.getItems().clear();
        dHalf.getItems().add("AM");
        dHalf.getItems().add("PM");
        dHalf.setValue("AM");

        //Sets Listener for Auto/Manual mode
        autoButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override

            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                //if checkbox ticked
                if (!newValue) {
                    lineConfirm.setDisable(false);
                    lineSelector.setDisable(false);
                    lineConfirm.setDisable(false);
                    trainName.setDisable(false);
                    scheduleName.setDisable(false);
                    dHour.setDisable(false);
                    dMin.setDisable(false);
                    dHalf.setDisable(false);
                    stopSelector.setDisable(false);
                    timeToStop.setDisable(false);
                    stopConfirm.setDisable(false);
                    browseSchedule.setDisable(true);
                } else {
                    lineConfirm.setDisable(true);
                    lineSelector.setDisable(true);
                    lineConfirm.setDisable(true);
                    trainName.setDisable(true);
                    scheduleName.setDisable(true);
                    dHour.setDisable(true);
                    dMin.setDisable(true);
                    dHalf.setDisable(true);
                    stopSelector.setDisable(true);
                    timeToStop.setDisable(true);
                    stopConfirm.setDisable(true);
                    browseSchedule.setDisable(false);
                }
            }
        });
    }

    public void importSchedule() throws IOException {
        FileChooser fileChooser = new FileChooser();
        // Configure FileChooser
        fileChooser.setTitle("Select your schedule file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel", "*.xlsx")
                //new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        File temp = fileChooser.showOpenDialog(new Stage());
        if(temp != null) {
            Main.ctc.automaticDispatch(temp);
        }

        //Goes back to CTC window
        Scene moduleSelect = new Scene(FXMLLoader.load(getClass().getResource("fxml/CTC.fxml")));
        Stage window = (Stage) pane.getScene().getWindow();
        window.setScene(moduleSelect);
        window.setTitle("CTC");
    }

    public void confirmLine(ActionEvent e) throws Exception {
        curr = new Dispatch(lineSelector.getSelectionModel().getSelectedItem().toUpperCase(),("Train " + tcss.main.Main.ctc.numDispatches() + 1));
        //System.out.println(curr.getLine());
        curr.createSchedule(curr.getLine());

        //Populates stop drop down once line is selected
        stopSelector.getItems().clear();
        String [] temp = tcss.main.Main.ctc.getAllStops(curr.getLine());
        for (int i = 0; i < temp.length; i++) {
            stopSelector.getItems().add(temp[i]);
        }
    }

    //Approves each stop added to the Schedule
    public void confirmStop(ActionEvent e) throws Exception {
        //curr.schedule.addStop(stopSelector.getSelectionModel().getSelectedItem(), Float.parseFloat(timeToStop.getText()) * 60);
    }

    //Approves overall dispatch and adds it to the CTC list
    public void confirmDispatch(ActionEvent e) throws Exception {

        //If automatic mode
        if (autoButton.isSelected()) {
            //curr.setScheduleFile();
        } else {
            //Set Departure Time if one was entered
            //If hour and minute times are valid
            if (1 <= Integer.parseInt(dHour.getText()) && Integer.parseInt(dHour.getText()) <= 12 && Integer.parseInt(dMin.getText()) >= 1 && Integer.parseInt(dMin.getText()) <= 59) {
                //if AM
                if (dHalf.getSelectionModel().getSelectedItem().equals("AM")) {
                    if (dHour.getText().equals("12")) {
                        curr.setDepartureTime(0, Integer.parseInt(dMin.getText()));
                    } else {
                        curr.setDepartureTime(Integer.parseInt(dHour.getText()), Integer.parseInt(dMin.getText()));
                    }
                //if PM
                } else {
                    if (dHour.getText().equals("12")) {
                        curr.setDepartureTime(12, Integer.parseInt(dMin.getText()));
                    } else {
                        curr.setDepartureTime(Integer.parseInt(dHour.getText()) + 12, Integer.parseInt(dMin.getText()));
                    }
                }

                if (!trainName.getText().equals("")) {
                    curr.setName(trainName.getText());
                }

            }
            //Time not valid, display to user
            else {

            }
        }

        curr.setRequests();


        tcss.main.Main.ctc.addDispatch(curr);
        System.out.println(curr);

        //Goes back to CTC window
        Scene moduleSelect = new Scene(FXMLLoader.load(getClass().getResource("fxml/CTC.fxml")));
        Stage window = (Stage) pane.getScene().getWindow();
        window.setScene(moduleSelect);
        window.setTitle("CTC");
    }

    public void goBack(ActionEvent e) throws Exception {
        //Should return back to CTC module screen.  Does not save current progress
        Scene moduleSelect = new Scene(FXMLLoader.load(getClass().getResource("fxml/CTC.fxml")));
        Stage window = (Stage) pane.getScene().getWindow();
        window.setScene(moduleSelect);
        window.setTitle("CTC");
    }
}
