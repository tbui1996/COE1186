package tcss.main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
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
import javafx.util.Duration;
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
  private boolean isTCChosen = false;
  private boolean isblockChosen = false;

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
                  blockChoice.setDisable(false);
                  //Integer.parseInt(rate.getValue().toString().replaceAll("\\D+", ""));

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
                  isTCChosen = true;
                  update();
              }
              else{
                  blockChoice.setDisable(true);
                  isTCChosen = false;
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

                            ObservableList<String> l = blockChoice.getItems();
                            String s = l.get((int)number2);
                            int blockNum = Integer.parseInt(s.replaceAll("\\D+", ""));
                            blockId = blockNum;
                            Block cur = curTC.getBlock(blockNum);






                            blockId = cur.getBlockNum();
                            sSpeedLabel.setText("Suggested Speed: " + cur.getSuggestedSpeed() + " mph");
                            authLabel.setText("Authority: " + cur.getAuthority() + " blocks");
                            occupiedLabel.setText("Occupied: " + (cur.isOccupied() ? "Yes" : "No"));

                            if (currTrack.getBlock(blockNum).getSwitch() == null) {
                                outputLights.setText("Lights: N/A");
                                outputSwitch.setText("Switch: N/A");
                            } else{
                                outputSwitch.setText("Switch: " + (cur.getSwitch().getStraight() ? "Straight" : "Branch"));
                                outputLights.setText("Lights: " + (cur.getSwitch().getStraight() ? "Green:" : "Red"));
                            }
                            if (currTrack.getBlock(blockNum).getRXR() == null) {
                                railroadcrossing.setText("Railroad Crossing: N/A");
                            } else{
                                railroadcrossing.setText("Railroad Crossing: " + (cur.getRXR().isDown() ? "Raised" : "Lowered"));
                            }


                            isblockChosen = true;
                            update();
                        } else {
                            sSpeedLabel.setText("Suggested Speed: ");
                            authLabel.setText("Authority: ");
                            occupiedLabel.setText("Occupied: ");
                            outputSwitch.setText("Switch: ");
                            outputLights.setText("Lights: ");
                            railroadcrossing.setText("Railroad Crossing: ");
                            isblockChosen = false;

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
        Timeline loop = new Timeline(new KeyFrame(Duration.seconds(.2), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                update();
//                System.out.println("GUI Updated!!");
            }
        }));
        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();

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
      manualRedLabel.setText("Red");
      currTrack.getBlock(blockId).getSwitch().setLights(false);
  }
  @FXML
  void switchClick(ActionEvent event){
    outputSwitch.setText("Straight");
    currTrack.getBlock(blockId).getSwitch().setStraight(true);
  }
  @FXML
  void branchClick(ActionEvent event){
    outputSwitch.setText("Branch");
    currTrack.getBlock(blockId).getSwitch().setStraight(false);
  }
    public void closeWindow() {
        Stage s = (Stage) trackChoice.getScene().getWindow();
        s.close();
    }

    public void update(){
      if(currTrack==null || curTC == null ){
          return;
      }
        if(isblockChosen && isTCChosen){
            Block cur = currTrack.getBlock(blockId);
            sSpeedLabel.setText("Suggested Speed: " + cur.getSuggestedSpeed() + " mph");
            authLabel.setText("Authority: " + cur.getAuthority() + " blocks");
            occupiedLabel.setText("Occupied: " + (cur.isOccupied() ? "Yes" : "No"));

            if (currTrack.getBlock(blockId).getSwitch() == null) {
                outputLights.setText("Lights: N/A");
                outputSwitch.setText("Switch: N/A");
            } else{
                outputSwitch.setText("Switch: " + (cur.getSwitch().getStraight() ? "Straight" : "Branch"));
                outputLights.setText("Lights: " + (cur.getSwitch().getStraight() ? "Green" : "Red"));
            }
            if (currTrack.getBlock(blockId).getRXR() == null) {
                railroadcrossing.setText("Railroad Crossing: N/A");
            } else{
                railroadcrossing.setText("Railroad Crossing: " + (cur.getRXR().isDown() ? "Raised" : "Lowered"));

            }


        }

            if(opModeToggle.isSelected() && isblockChosen && isTCChosen){
                importPLC.setDisable(false);
                importPLC.setVisible(true);
                opModeToggle.setText("Exit Manual Mode");
                manualswitchLabel.setVisible(true);
                manuallightLabel.setVisible(true);
                manualraiseCrossing.setVisible(true);
                manuallowerCrossing.setVisible(true);
                manualBranchLabel.setVisible(true);
                manualRedLabel.setVisible(true);
                if(currTrack.getBlock(blockId).getSwitch() != null) {
                    manualswitchLabel.setDisable(false);
                    manuallightLabel.setDisable(false);
                    manualRedLabel.setDisable(false);
                    manualBranchLabel.setDisable(false);

                } else{
                    manualswitchLabel.setDisable(true);
                    manuallightLabel.setDisable(true);
                    manualRedLabel.setDisable(true);
                    manualBranchLabel.setDisable(true);
                }
                if(currTrack.getBlock(blockId).getRXR() != null){
                    manuallowerCrossing.setDisable(false);
                    manualraiseCrossing.setDisable(false);
                } else {
                    manuallowerCrossing.setDisable(true);
                    manualraiseCrossing.setDisable(true);
                }

            } else {
                opModeToggle.setText("Enter Manual Mode");
                manualswitchLabel.setVisible(false);
                manuallightLabel.setVisible(false);
                manualraiseCrossing.setVisible(false);
                manuallowerCrossing.setVisible(false);
                manualBranchLabel.setVisible(false);
                manualRedLabel.setVisible(false);
                importPLC.setVisible(false);
            }


    }
}
