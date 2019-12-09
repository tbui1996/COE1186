package tcss.main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tcss.trackcontroller.PLC;
import tcss.trackcontroller.TrackController;
import tcss.trackcontroller.WaysideController;
import tcss.trackmodel.Block;
import tcss.trackmodel.Track;

import java.io.File;
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

  @FXML
  private Label railroadcrossing;

  @FXML
  private Button importPLC;

  @FXML
  private ToggleButton opModeToggle;

  @FXML
  private Button manualswitchLabel;

  @FXML
  private Button manuallightLabel;

  @FXML
  private Button manualraiseCrossing;

  @FXML
  private Button manuallowerCrossing;

  @FXML
  private Button manualBranchLabel;

  @FXML
  private Button manualRedLabel;


    public WaysideController wc;
  public TrackController curTC;
  public Block blockToAdd;
  private ArrayList<String> listofTCs;
  private Main main;
  private Track track;
  private PLC plc;
  private String plcFile;
  private ArrayList<Block> currBlocks;
  private int line;
  private boolean switchState = true;
  private boolean occupied = false;
  private int blockId;

    private Track currTrack;



    @Override
  public void initialize(URL url, ResourceBundle rb) {

      Track redLine = tcss.main.Main.redLine;
      Track greenLine = tcss.main.Main.greenLine;


      trackChoice.getItems().add("Select Track Controller");
      trackChoice.getItems().add("Red Track Controller 1");
      trackChoice.getItems().add("Red Track Controller 2");
      trackChoice.getItems().add("Red Track Controller 3");
      trackChoice.getItems().add("Red Track Controller 4");
      trackChoice.getItems().add("Green Track Controller 1");
      trackChoice.getItems().add("Green Track Controller 2");
      trackChoice.getItems().add("Green Track Controller 3");
      trackChoice.getItems().add("Green Track Controller 4");

      trackChoice.setValue("Select Track Controller");
      trackChoice.setTooltip(new Tooltip("Select a Track Controller"));

      trackChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
          @Override
          public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
              if ((Integer) number2 > 0) {
                  if ((Integer) number2 == 1) {
                      curTC = Main.tc.redTC.get(0);
                      currTrack = redLine;
                  }
                  else if ((Integer) number2 == 2) {
                      curTC = Main.tc.redTC.get(1);
                      currTrack = redLine;
                  }
                  else if ((Integer) number2 == 3) {
                      curTC = Main.tc.redTC.get(2);
                      currTrack = redLine;
                  }
                  else if ((Integer) number2 == 4) {
                      curTC = Main.tc.redTC.get(3);
                      currTrack = redLine;
                  }
                  else if ((Integer) number2 == 5) {
                      curTC = Main.tc.greenTC.get(0);
                      currTrack = greenLine;
                  }
                  else if ((Integer) number2 == 6) {
                      curTC = Main.tc.greenTC.get(1);
                      currTrack = greenLine;
                  }
                  else if ((Integer) number2 == 7) {
                      curTC = Main.tc.greenTC.get(2);
                      currTrack = greenLine;
                  }
                  else if ((Integer) number2 == 8) {
                      curTC = Main.tc.greenTC.get(3);
                      currTrack = greenLine;
                  }

                  blockChoice.getItems().clear();
                  blockChoice.getItems().add("Select Block");
                  ArrayList<Block> switching = new ArrayList<>(curTC.switchHashMap.values());
                  ArrayList<Block> RXR = new ArrayList<>(curTC.RXR.values());
                  ArrayList<Block> listblock = new ArrayList<>(curTC.block.values());


                  for (Block block : switching){
                      blockChoice.getItems().add(block.getSection() + Integer.toString(block.getBlockNum()));
                      System.out.println("Adding switch to blockChoice");
                  }
                  for (Block block : RXR) {
                          blockChoice.getItems().add(block.getSection() + Integer.toString(block.getBlockNum()));
                          System.out.println("Adding RXR block to blockChoice");

                  }
                      int i = 0;
                  for (Block block : listblock) {

                          blockChoice.getItems().add(block.getSection() + Integer.toString(block.getBlockNum()));
                          i++;
                          System.out.println("Adding block to " + i + " blockChoice");
                          //System.out.println(blockChoice.getItems().add(block.getSection() + Integer.toString(block.getBlockNum())));
                  }


                  blockChoice.setDisable(false);
              }
              else{
                  blockChoice.setDisable(true);
              }
          }
      });
            blockChoice.setValue("Select Block");
            blockChoice.setTooltip(new Tooltip("Select a block to view"));
            try {
                blockChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) throws NullPointerException{
                        if ((Integer) number2 > 0) {
                            int blockNumber = (Integer) number2 - 1;
                            if (blockNumber == 0)
                                blockNumber = 1;
                            blockId = blockNumber;
                            Block cur = currTrack.getBlock(blockNumber);

                            blockId = cur.getBlockNum();
                            sSpeedLabel.setText("Suggested Speed: " + cur.getSuggestedSpeed() + " mph");
                            authLabel.setText("Authority: " + cur.getAuthority() + " blocks");
                            occupiedLabel.setText("Occupied: " + (cur.isOccupied() ? "Yes" : "No"));

                            if (currTrack.getBlock(blockNumber).getSwitch() == null) {
                                outputLights.setText("Lights: N/A");
                                outputSwitch.setText("Switch: N/A");
                            }
                            if (currTrack.getBlock(blockNumber).getRXR() == null) {
                                railroadcrossing.setText("Railroad Crossing: N/A");
                            }

                            outputSwitch.setText("Switch: " + (cur.getSwitch().getStraight() ? "Straight" : "Branch"));
                            outputLights.setText("Lights: " + (cur.getSwitch().getStraight() ? "Green:" : "Red"));
                            railroadcrossing.setText("Railroad Crossing: " + (cur.getRXR().isDown() ? "Raised" : "Lowered"));
                        } else {
                            sSpeedLabel.setText("Suggested Speed: ");
                            authLabel.setText("Authority: ");
                            occupiedLabel.setText("Occupied: ");
                            outputSwitch.setText("Switch: ");
                            outputLights.setText("Lights: ");
                            railroadcrossing.setText("Railroad Crossing: ");

                        }

                    }

                });
            } catch(NullPointerException e){
                System.out.println(e.toString());
            }


      importPLC.setText("Browse for PLC File");
      importPLC.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent actionEvent) {
              FileChooser fileChooser = new FileChooser();
              File file = fileChooser.showOpenDialog(null);
              String plcFile = file.getPath();
              try {
                  curTC.loadPLC(plcFile);
              } catch (IOException e){
                  System.out.println(e.toString());
              }
          }
      });

  }

  public void goBack(ActionEvent actionEvent) throws Exception {
      FXMLLoader trackLoader = new FXMLLoader(getClass().getResource("fxml/TrackSelect.fxml"));
      Stage trackStage = new Stage();
      Parent trackRoot = trackLoader.load();
      trackStage.setTitle("Select Track Module");
      trackStage.setScene(new Scene(trackRoot));
      trackStage.setResizable(false);
      trackStage.getIcons().add(new Image("file:resources/train.png"));

      trackStage.show();

      Stage s = (Stage) authLabel.getScene().getWindow();
      s.close();

  }
/*
 manualswitchLabel;
 manuallightLabel;
 manualraiseCrossing;
 manuallowerCrossing;
 manualBranchLabel;
 manualRedLabel;
 */
  @FXML
    void setManualMode(ActionEvent event){
      if(curTC == null) {
          return;
      }
      if(opModeToggle.isSelected()){
          opModeToggle.setText("Exit Manual Mode");
          if(currTrack.getBlock(blockId).getSwitch() != null) {
              manualswitchLabel.setVisible(true);
              manuallightLabel.setVisible(true);
              manuallightLabel.setVisible(true);
              manualRedLabel.setVisible(true);
          } else{
              manualswitchLabel.setDisable(true);
              manuallightLabel.setDisable(true);
              manuallightLabel.setDisable(true);
              manualRedLabel.setDisable(true);
          }
          if(currTrack.getBlock(blockId).getRXR() != null){
              manuallowerCrossing.setVisible(true);
              manualraiseCrossing.setVisible(true);
          } else {
              manuallowerCrossing.setVisible(false);
              manualraiseCrossing.setVisible(false);
          }

      } else {
          opModeToggle.setText("Enter Manual Mode");
          manualswitchLabel.setVisible(false);
          manuallightLabel.setVisible(false);
          manualraiseCrossing.setVisible(false);
          manuallowerCrossing.setVisible(false);
          manualBranchLabel.setVisible(false);
          manualRedLabel.setVisible(false);
      }

  }

  @FXML
  void raiseCrossing(ActionEvent event){
      railroadcrossing.setText("Raised");
      currTrack.getBlock(blockId).getRXR().setDown(false);
  }
  @FXML
  void lowerCrossing(ActionEvent event){
      railroadcrossing.setText("Lowered");
      currTrack.getBlock(blockId).getRXR().setDown(true);
  }
  @FXML
  void setGreen(ActionEvent event){
      outputLights.setText("Green");
      currTrack.getBlock(blockId).getSwitch().setLights(true);
  }
  @FXML
  void setRed(ActionEvent event){
      outputLights.setText("Red");
      currTrack.getBlock(blockId).getSwitch().setLights(false);
  }
  @FXML
  void switchClick(ActionEvent event){

  }
  @FXML
  void branchClick(ActionEvent event){

  }
    public void closeWindow() {
        Stage s = (Stage) trackChoice.getScene().getWindow();
        s.close();
    }

}
