package tcss.main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
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
  private ToggleButton manualMode;

  @FXML
  private Button importPLC;

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

    public void setTrainApp(Main main, String plcFile, Track track, WaysideController waysideController) throws IOException {
    this.track = track;
    this.main = main;
    this.wc = waysideController;
    this.plcFile = plcFile;
    //curTC.loadPLC(plcFile);
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
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
                  if ((Integer) number2 == 1)
                      curTC = Main.tc.redTC.get(0);
                  else if ((Integer) number2 == 2)
                      curTC = Main.tc.redTC.get(1);
                  else if ((Integer) number2 == 3)
                      curTC = Main.tc.redTC.get(2);
                  else if ((Integer) number2 == 4)
                      curTC = Main.tc.redTC.get(3);
                  else if ((Integer) number2 == 5)
                      curTC = Main.tc.greenTC.get(0);
                  else if ((Integer) number2 == 6)
                      curTC = Main.tc.greenTC.get(1);
                  else if ((Integer) number2 == 7)
                      curTC = Main.tc.greenTC.get(2);
                  else if ((Integer) number2 == 8)
                      curTC = Main.tc.greenTC.get(3);

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
      blockChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
          @Override
          public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
              if ((Integer) number2 > 0) {
                  Block cur = curTC.getBlock((Integer) number2);
                  blockId = cur.getBlockNum();
                  sSpeedLabel.setText("Suggested Speed: " + cur.getSuggestedSpeed() + " mph");
                  authLabel.setText("Authority: " + cur.getAuthority()+ " blocks");
                  occupiedLabel.setText("Occupied: " + (cur.isOccupied() ? "Yes" : "No"));
                  if (curTC.getSwitch((Integer) number2)==null){
                      outputLights.setText("Lights: N/A");
                      outputSwitch.setText("Switch: N/A");
                  }
                  if(curTC.getRXR((Integer) number2).getRXR() == null)
                      railroadcrossing.setText("Railroad Crossing: N/A");

              } else {
                  sSpeedLabel.setText("Suggested Speed: ");
                  authLabel.setText("Authority: ");
                  occupiedLabel.setText("Occupied: ");
                  outputSwitch.setText("Switch: ");
                  outputLights.setText("Lights: ");
              }

          }

      });

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

      manualMode.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent actionEvent) {
              if(manualMode.isSelected()){

              }
          }
      });

      // Create Timeline for periodic updating
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
  public boolean calculateSignal(boolean occupancy){
        return false;
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

  @FXML
    void setManualMode(ActionEvent event){
      if(manualMode.isSelected()){

      }

  }
  //CTC function
  public void setSwitch(){
    switchState = !switchState;
      Task<Void> task = new Task<Void>() {
          @Override
          protected Void call() throws Exception{
              Platform.runLater(new Runnable(){
                  @Override
                  public void run(){
                      if(switchState){

                      }
                  }
              });
              return null;
          }

      };
    task.setOnSucceeded(e->{

    });
      Thread thread = new Thread(task);
      thread.setDaemon(true);
      thread.start();
  }

  public void update(){
        if (curTC == null)
            return;
        if(manualMode.isSelected())
            manualMode.setText("Entering manual mode...");
        else {
            importPLC.setDisable(true);
            manualMode.setText("Exiting manual mode...");
        }
        curTC.trasmitAuthority(curTC.getBlock(blockId).getSuggestedSpeed(),blockId,curTC.getBlock(blockId).getAuthority());
        //TODO: need to rewrite getAuthority to get the actual authority from wayside controller
        authLabel.setText("Authority: " + curTC.getBlock(blockId).getAuthority() + " blocks");
        sSpeedLabel.setText("Suggesed Speed: "+ curTC.getBlock(blockId).getSuggestedSpeed() + " mph");
        outputLights.setText("Lights: " + curTC.getBlock(blockId).getSwitch().getStraight());
  }

    public void closeWindow() {
        Stage s = (Stage) trackChoice.getScene().getWindow();
        s.close();
    }

}
