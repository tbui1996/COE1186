package tcss.main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import tcss.ctc.Train;
import tcss.trackmodel.Block;

import java.net.URL;
import java.util.ResourceBundle;



public class CTCController implements Initializable{

    // UI variables
    @FXML private Label dispatch1;
    @FXML private AnchorPane pane;
    @FXML private Button newDispatch;
    @FXML private ChoiceBox<String> lineSelector;
    @FXML private ChoiceBox<String> blockSelector;
    @FXML private Button confirmLine;
    @FXML private Button confirmBlock;
    @FXML private TableColumn<String, Train> nameList;             //Table column for train names
    @FXML private TableColumn<String, Train> locList;              //Table column for train locations
    @FXML private TableView dispatchList;           //Table holder for dispatch list
    @FXML private GridPane infoView;                                //GridPane holder
    @FXML private Label locLabel;
    @FXML private Label occLabel;
    @FXML private Label stationLabel;
    @FXML private Button closeBlockButton;
    @FXML private TextField mHour;
    @FXML private TextField mMin;
    @FXML private ChoiceBox<String> mHalf;
    @FXML private TextField closeTime;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //populates line dropdown
        lineSelector.getItems().add("Red");
        lineSelector.getItems().add("Green");
        lineSelector.setValue("Red");
        lineSelector.setTooltip(new Tooltip("Select a line to view a block"));

        //Sets up Table to display dispatches
        nameList.setCellValueFactory(new PropertyValueFactory<>("name"));
        locList.setCellValueFactory(new PropertyValueFactory<>("block"));

        // Create Timeline for periodic updating
        Timeline loop = new Timeline(new KeyFrame(Duration.seconds(.2), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateView();
            }
        }));
        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();
    }

    public void openDispatchWindow(ActionEvent actionEvent) throws Exception {
        //newDispatch.setText("Created");

        //Opens in same window for now, want it to be a new window
        Scene moduleSelect = new Scene(FXMLLoader.load(getClass().getResource("fxml/DispatchSelect.fxml")));
        Stage window = (Stage) pane.getScene().getWindow();
        window.setScene(moduleSelect);
        window.setTitle("Dispatch");
    }

    //Populates Block list once a line is selected
    public void populateBlocks(ActionEvent e) throws Exception {
        int temp = Main.ctc.lineStringToInt(lineSelector.getSelectionModel().getSelectedItem().toUpperCase());

        blockSelector.getItems().clear();
        for (int i = 0; i < Main.ctc.lineLength(temp); i++) {
            String name = Integer.toString(i+1);
            if (Main.ctc.getBlock(temp,i+1).getStation() != null) {
                name = name + ": " + Main.ctc.getBlock(temp,i+1).getStation().getName();
            }

            blockSelector.getItems().add(name);
        }

    }

    //View a block, where you can close a block
    public void viewBlock(ActionEvent e) {
            String [] split = blockSelector.getSelectionModel().getSelectedItem().split(": ",2);

            int block = Integer.parseInt(split[0]);
            Block temp = Main.ctc.getBlock(Main.ctc.lineStringToInt(lineSelector.getSelectionModel().getSelectedItem().toUpperCase()), block);

            //Fills labels with proper values
            locLabel.setText(lineSelector.getSelectionModel().getSelectedItem().toUpperCase() + " " + block);
            occLabel.setText("Occupied: " + temp.isOccupied());
            if (temp.getStation() != null) {
                stationLabel.setText("Station: " + temp.getStation().getName());
            } else {
                stationLabel.setText("Station: N/A");
            }

            mHour.setDisable(false);
            mMin.setDisable(false);
            mHalf.setDisable(false);
            closeTime.setDisable(false);
            closeBlockButton.setDisable(false);
    }

    //Closes a Block for Maintenance
    public void closeBlock(ActionEvent e) throws Exception {
        //System.out.println("Close block ");
        String [] loc = locLabel.getText().split(" ",2);

        //Finds what line is selected
        //Block block = Main.ctc.getBlock(Main.ctc.lineStringToInt(loc[0]), Integer.parseInt(loc[1]));

        //Adds maintenance request to list in CTC
        Main.ctc.addMaintenance(Integer.parseInt(mHour.getText()), Integer.parseInt(mMin.getText()), Main.ctc.lineStringToInt(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(closeTime.getText()));
    }

    //Updates dispatch list periodically
    private void updateView() {

        int selected = dispatchList.getSelectionModel().getSelectedIndex();
        dispatchList.getItems().clear();
        for (int i = 0; i < tcss.main.Main.ctc.numDispatches(); i++) {
            dispatchList.getItems().add(tcss.main.Main.ctc.getDispatch(i).getTrain());
        }
        dispatchList.getSelectionModel().select(selected);
    }

    public void closeWindow(ActionEvent actionEvent) throws Exception {
//        Parent trainModelParent = FXMLLoader.load(getClass().getResource("ModuleSelection.fxml"));
//        Scene trainModelView = new Scene(trainModelParent);
//
//        // Get stage info
//        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
//        window.setScene(trainModelView);
//        window.show();
//
//        Scene moduleSelect = new Scene(FXMLLoader.load(getClass().getResource("../Demo/ModuleSelection.fxml")));
//        Stage window = (Stage) pane.getScene().getWindow();
//        window.setScene(moduleSelect);
//        window.setTitle("Module Selection");

        Stage window = (Stage) pane.getScene().getWindow();
        window.close();

    }
}
