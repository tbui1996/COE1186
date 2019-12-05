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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;



public class CTCController implements Initializable{

    // UI variables
    @FXML private Label dispatch1;
    @FXML private AnchorPane pane;
    @FXML private Button newDispatch;
    @FXML private Accordion dispatchList;
    @FXML private ChoiceBox<String> lineSelector;
    @FXML private ChoiceBox<Integer> blockSelector;
    @FXML private Button confirmLine;
    @FXML private Button confirmBlock;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //populates line dropdown
        lineSelector.getItems().add("Red");
        lineSelector.getItems().add("Green");
        lineSelector.setValue("Red");
        lineSelector.setTooltip(new Tooltip("Select a line to view a block"));

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
        int temp = tcss.main.Main.ctc.lineStringToInt(lineSelector.getSelectionModel().getSelectedItem().toUpperCase());

        blockSelector.getItems().clear();
        for (int i = 0; i < Main.ctc.lineLength(temp); i++) {
            blockSelector.getItems().add(i+1);
        }

    }

    //Closes a Block for Maintenance
    public void closeBlock(ActionEvent e) throws Exception {
        System.out.println("Close block ");

        //Finds what line is selected
        int temp = tcss.main.Main.ctc.lineStringToInt(lineSelector.getSelectionModel().getSelectedItem().toUpperCase());

        //Assigns temp to blockId
        //Red Line
        if (temp == 1) {
            temp = blockSelector.getSelectionModel().getSelectedItem();
        }
        //Green Line
        else {
            temp = blockSelector.getSelectionModel().getSelectedItem() + tcss.main.Main.ctc.lineLength(1);
        }

        System.out.println(temp);
        //tcss.main.Main.tc.getNextStop(-1,-1,temp)
    }

    //Updates dispatch list periodically
    private void updateView() {

        dispatchList.getPanes().clear();
        for (int i = 0; i < tcss.main.Main.ctc.numDispatches(); i++)
            dispatchList.getPanes().add(new TitledPane(tcss.main.Main.ctc.getDispatch(i).getName(), new Label(tcss.main.Main.ctc.getDispatchString(i))));
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
