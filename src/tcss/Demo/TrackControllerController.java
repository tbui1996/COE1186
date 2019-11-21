package tcss.Demo;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import tcss.trackcontroller.PLC;
import tcss.trackcontroller.TrackController;
import tcss.trackcontroller.WaysideController;
import tcss.trackmodel.Block;
import tcss.trackmodel.Track;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TrackControllerController implements Initializable {
  @FXML
  private Label idLabel;
  @FXML
  private Label sSpeedLabel;
  @FXML
  private Label authLabel;
  @FXML
  private AnchorPane pane;
  @FXML
  private ChoiceBox trackChoice;
  @FXML
  private ChoiceBox blockChoice;
  @FXML
  private Label occupiedLabel;
  @FXML
  private Label outputLights;

  @FXML
  private Label outputSwitch;

  public WaysideController wc;
  private TrackController curTC;
  private Track track;
  private PLC plc;
  private String plcFile;
  private ArrayList<Block> currBlocks;

  public void setTrainApp(Main main, String plcFile, Track track, WaysideController waysideController) throws IOException {
    this.track = track;
    this.wc = waysideController;
    this.plcFile = plcFile;
    curTC.loadPLC(plcFile);
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    trackChoice.getItems().add("Select Track Controller");
    String st[] = {"Red Track Controller 1", "Red Track Controller 2", "Red Track Controller 3", "Red Track Controller 4", "Green Track Controller 1", "Green Track Controller 2", "Green Track Controller 3", "Green Track Controller 4"};
    trackChoice = new ChoiceBox(FXCollections.observableArrayList(st));
    trackChoice.setValue("Select Track Controller");
    trackChoice.setTooltip(new Tooltip("Select a Track Controller"));

    blockChoice.setValue("Select Block:");
    blockChoice.setTooltip(new Tooltip("Select a Block"));


    trackChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
      public void changed(ObservableValue ov, Number value, Number new_value) {
        trackChoice.setValue(st[new_value.intValue()]);
        int trackControllerId = trackChoice.getSelectionModel().getSelectedIndex();
        if(trackControllerId<0)
            return;
        if((st[new_value.intValue()]==st[0] || (st[new_value.intValue()]==st[1]) || ((st[new_value.intValue()]==st[2])||(st[new_value.intValue()]==st[3])))){
          for(TrackController tc: wc.redTC){
            trackChoice.setValue(tc.getTCID());
          }
        }else{
          for(TrackController tc: wc.greenTC){
            trackChoice.setValue(tc.getTCID());
          }
        }
        updateBlockChoiceBox();
      }
    });


  }
  private void updateBlockChoiceBox(){
    int tcID = trackChoice.getSelectionModel().getSelectedIndex();
    if(tcID<0)
      return;

    if(0<=tcID && tcID<=3)
      curTC = wc.redTC.get(tcID);
    if(4<=tcID && tcID<=7)
      curTC = wc.greenTC.get(tcID);

    ArrayList<Block> blocks = new ArrayList<>(curTC.block.values());
    ArrayList<Block> rxr = new ArrayList<>(curTC.RXR.values());
    ArrayList<Block> switching = new ArrayList<>(curTC.switchHashMap.values());

    currBlocks = blocks;
    blockChoice= new ChoiceBox(FXCollections.observableArrayList(currBlocks));
    blockChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
      public void changed(ObservableValue ov, Number value, Number new_value) {
          if(!blockChoice.getSelectionModel().isEmpty()){
            for(Block block: currBlocks){
              String blockstatus = curTC.blockState(block.getBlockNum());
            }
            String occupied = (currBlocks.get(blockChoice.getSelectionModel().getSelectedIndex()).isOccupied()) ? "Occupied" : "Not Occupied";
            occupiedLabel.setText("Occupancy: "+ occupied);
            sSpeedLabel.setText("Suggested Speed: "+ currBlocks.get(blockChoice.getSelectionModel().getSelectedIndex()).getSuggestedSpeed());
            authLabel.setText("Authority: "+currBlocks.get(blockChoice.getSelectionModel().getSelectedIndex()).getAuthority());
            String switches = (currBlocks.get(blockChoice.getSelectionModel().getSelectedIndex()).getSwitch() != null) ? "Switch" : "Not switched";
            outputSwitch.setText("Switch: "+ switches);
          }
          else{
            occupiedLabel.setText("Occupancy: ");
            sSpeedLabel.setText("Suggested Speed: ");
            authLabel.setText("Authority: ");
            outputSwitch.setText("Switch: ");
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