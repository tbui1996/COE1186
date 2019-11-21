package tcss.main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import tcss.traincontroller.TrainController;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ResourceBundle;

public class TrainControllerController implements Initializable {

    // UI variables
    @FXML private ChoiceBox trainChoice;
    @FXML private Label idLabel;
    @FXML private Label speedLimitLabel;
    @FXML private Label suggestedSpeedLabel;
    @FXML private TextField setPointInput;
    @FXML private Button confirmSetpoint;
    @FXML private Button trainButton;
    @FXML private Label authLabel;
    @FXML private AnchorPane pane;
    @FXML private ToggleButton eBrakeToggle;
    @FXML private ToggleButton opModeToggle;
    @FXML private Label lightsDisplay;
    @FXML private Label powerCommandLabel, kilabel, kplabel;
    @FXML private Label currentSpeedLabel;
    @FXML private TextField setTempInput;
    @FXML private Circle d1Status, d2Status, d3Status, d4Status, d5Status, d6Status, d7Status, d8Status;

    private TrainController tc;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DecimalFormat format = new DecimalFormat( "#.0" );
        setPointInput.setTextFormatter( new TextFormatter<>(c -> {
                if ( c.getControlNewText().isEmpty() )
                    return c;
                ParsePosition parsePosition = new ParsePosition( 0 );
                Object object = format.parse( c.getControlNewText(), parsePosition );
                if ( object == null || parsePosition.getIndex() < c.getControlNewText().length() ){
                    return null;
                } else {
                    return c;
                }
        }));
        setTempInput.setTextFormatter( new TextFormatter<>(c -> {
            if ( c.getControlNewText().isEmpty() )
                return c;
            ParsePosition parsePosition = new ParsePosition( 0 );
            Object object = format.parse( c.getControlNewText(), parsePosition );
            if ( object == null || parsePosition.getIndex() < c.getControlNewText().length() ){
                return null;
            } else {
                return c;
            }
        }));
        eBrakeToggle.setDisable(true);
        trainButton.setDisable(true);
        trainChoice.getItems().add("Select Train");
        // Testing
        for(TrainController t: TrainController.TrainControllerList)
            trainChoice.getItems().add("Train " + t.getID());
        trainChoice.setValue("Select Train");
        trainChoice.setTooltip(new Tooltip("Select a train to view"));
        trainChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override

            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                if((Integer) number2 > 0) {
                    TrainController cur = TrainController.TrainControllerList.get((Integer)number2-1);
                    cur.update();
                    tc = cur;
                    idLabel.setText("ID: " + tc.getID());
                    suggestedSpeedLabel.setText("Suggested Speed: " + tc.getSSpeed());
                    setPointInput.setPromptText("" + tc.getsetpointSpeed());
                    setTempInput.setPromptText("" + tc.getTemp());
                    trainButton.setDisable(false);
                    update();
                } else {
                    tc = null; //deselect train
                    idLabel.setText("ID: ");
                    suggestedSpeedLabel.setText("Suggested Speed: ");
                    authLabel.setText("Authority: ");
                    setPointInput.setText("");
                    setTempInput.setText("");
                    setPointInput.setPromptText("");
                    setTempInput.setPromptText("");
                    eBrakeToggle.setDisable(true);
                    trainButton.setDisable(true);
                    eBrakeToggle.setStyle("-fx-background-color: #dfdfdf; -fx-text-fill: rgb(43, 39, 49)");
                    powerCommandLabel.setText("Power: ");
                    kilabel.setText("Ki: ");
                    kplabel.setText("Kp: ");
                    currentSpeedLabel.setText("Current Speed: ");
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

    /**
     * In this method we want to allow the user to see other information
     * @param actionEvent
     * @throws Exception
     */
    public void settingsView(ActionEvent actionEvent) throws Exception{
        Scene engineView = new Scene(FXMLLoader.load(getClass().getResource("EngineSettings.fxml")));
        Stage window = (Stage) pane.getScene().getWindow();
        window.setScene(engineView);
        window.setTitle("Engine");
    }

    public void update(){
        if(tc == null) { return; }
        if(opModeToggle.isSelected()){
            opModeToggle.setText("Exit Manual Mode");
        } else {
            opModeToggle.setText("Enter Manual Mode");
        }
        authLabel.setText("Authority: " + tc.getAuthority());
        if (tc.getUnderground()){
            lightsDisplay.setText("ON");
            lightsDisplay.setTextFill(Color.GREEN);
        } else {
            lightsDisplay.setText("OFF");
            lightsDisplay.setTextFill(Color.RED);
        }
        eBrakeToggle.setDisable(false);
        eBrakeToggle.setSelected(tc.getEBrake());
        if(tc.getEBrake()) {
            eBrakeToggle.setStyle("-fx-background-color: red; -fx-text-fill: #dfdfdf");
        } else {
            eBrakeToggle.setStyle("-fx-background-color: #dfdfdf; -fx-text-fill: rgb(43, 39, 49)");
        }
        opModeToggle.setSelected(tc.getOpMode()); //set toggle to true if it is in manual
        powerCommandLabel.setText("Power: " + tc.getPWRCMD());
        kilabel.setText("Ki: " + tc.getKi());
        kplabel.setText("Kp: " + tc.getKp());
        currentSpeedLabel.setText("Current Speed: " + tc.getCurrentSpeed());

    }

    public void goBack(ActionEvent actionEvent) throws Exception {
        Scene moduleSelect = new Scene(FXMLLoader.load(getClass().getResource("ModuleSelection.fxml")));
        Stage window = (Stage) pane.getScene().getWindow();
        window.setScene(moduleSelect);
        window.setTitle("Module Selection");
    }

    public void closeWindow() {
        Stage s = (Stage) trainChoice.getScene().getWindow();
        s.close();
    }

    public void showTrain() throws Exception {
//        Stage trainStage = (Stage) trainChoice.getScene().getWindow();
        Stage trainStage = new Stage();
        FXMLLoader trainloader = new FXMLLoader(getClass().getResource("fxml/TrainModel.fxml"));
        Parent trainRoot = trainloader.load();
        trainStage.setTitle("Train Model View");
        trainStage.setWidth(784);
        trainStage.setHeight(609);
        trainStage.setScene(new Scene(trainRoot));
        trainStage.getIcons().add(new Image("file:resources/train.png"));

        TrainModelController controller = trainloader.getController();
        controller.passTrain(tc.getTrain());

        trainStage.show();
    }

    public void confirmSetpoint(ActionEvent actionEvent) throws Exception{
       try {
           try {
               float newSPSpeed = Float.parseFloat(setPointInput.getText());
               float suggestedSpeed = tc.getSSpeed();
               float speedLimit = tc.getSpeedLimit();
               System.out.printf("SPI: " + newSPSpeed + " with ss: " + suggestedSpeed + " and sl: " + speedLimit);
               if(newSPSpeed > suggestedSpeed){
                   newSPSpeed = suggestedSpeed;
               }
               if(newSPSpeed > speedLimit){
                   newSPSpeed = speedLimit;
               }
               tc.setSetpointSpeed(newSPSpeed);
               tc.update();
               tc.updateModelCommandedSpeed();
               setPointInput.setText("");
               setPointInput.setPromptText("" + tc.getsetpointSpeed());
           } catch (NumberFormatException e) {
               System.out.println("nfe found");
           }
       } catch (NullPointerException e){
           System.out.println("Train is not selected!");
       }
       update();
    }

    public void confirmTemp(ActionEvent actionEvent) throws Exception{
        try {
            try {
                float newTemp = Float.parseFloat(setTempInput.getText());
                if(newTemp < 55){
                    newTemp = 55;
                } else if (newTemp > 90){
                    newTemp = 90;
                }
                tc.setTemp(newTemp);
                setTempInput.setText("");
                setTempInput.setPromptText("" + newTemp);
            } catch (NumberFormatException e) {
                System.out.println("nfe found");
            }
        } catch (NullPointerException e){
            System.out.println("Train is not selected!");
        }
    }


    public void toggleEBrake(ActionEvent actionEvent) throws Exception {
        boolean brakeStatus = eBrakeToggle.isSelected(); //get brake status - what we want to set it to
        tc.setEBrake(brakeStatus);
        tc.updateModelEBrake();
        if (brakeStatus) {
            eBrakeToggle.setStyle("-fx-background-color: red; -fx-text-fill: #dfdfdf");
        } else {
            eBrakeToggle.setStyle("-fx-background-color: #dfdfdf; -fx-text-fill: rgb(43, 39, 49);");
        }
    }

    public void toggleOpMode(Event event) throws Exception {
        boolean opMode = opModeToggle.isSelected();
        tc.changeOperationMode();
        opModeToggle.setSelected(opMode);
        if (opMode) {
            opModeToggle.setText("Exit Manual Mode");
            update();
        } else {
            opModeToggle.setText("Enter Manual Mode");
            update();
        }
    }

    public void toggleDoor0(Event event) throws Exception{
        boolean[] doors = tc.getDoorStatus();
        //System.out.println("you pressed it!");
        doors[0]=!doors[0];
        tc.adjustDoors(doors);
        doors = tc.getDoorStatus();
        if(doors[0]){
            d1Status.setFill(Color.GREEN);
        } else {
            d1Status.setFill(Color.RED);
        }
    }

    public void toggleDoor1(Event event) throws Exception{
        boolean[] doors = tc.getDoorStatus();
        doors[1]=!doors[1];
        tc.adjustDoors(doors);
        doors = tc.getDoorStatus();
        if(doors[1]){
            d2Status.setFill(Color.GREEN);
        } else {
            d2Status.setFill(Color.RED);
        }
    }

    public void toggleDoor2(Event event) throws Exception{
        boolean[] doors = tc.getDoorStatus();
        doors[2]=!doors[2];
        tc.adjustDoors(doors);
        doors = tc.getDoorStatus();
        if(doors[2]){
            d3Status.setFill(Color.GREEN);
        } else {
            d3Status.setFill(Color.RED);
        }
    }

    public void toggleDoor3(Event event) throws Exception{
        boolean[] doors = tc.getDoorStatus();
        doors[3]=!doors[3];
        tc.adjustDoors(doors);
        doors = tc.getDoorStatus();
        if(doors[3]){
            d4Status.setFill(Color.GREEN);
        } else {
            d4Status.setFill(Color.RED);
        }
    }

    public void toggleDoor4(Event event) throws Exception{
        boolean[] doors = tc.getDoorStatus();
        doors[4]=!doors[4];
        tc.adjustDoors(doors);
        if(doors[4]){
            d5Status.setFill(Color.GREEN);
        } else {
            d5Status.setFill(Color.RED);
        }
    }

    public void toggleDoor5(Event event) throws Exception{
        boolean[] doors = tc.getDoorStatus();
        doors[5]=!doors[5];
        tc.adjustDoors(doors);
        if(doors[5]){
            d6Status.setFill(Color.GREEN);
        } else {
            d6Status.setFill(Color.RED);
        }
    }

    public void toggleDoor6(Event event) throws Exception{
        boolean[] doors = tc.getDoorStatus();
        doors[6]=!doors[6];
        tc.adjustDoors(doors);
        if(doors[6]){
            d7Status.setFill(Color.GREEN);
        } else {
            d7Status.setFill(Color.RED);
        }
    }

    public void toggleDoor7(Event event) throws Exception{
        boolean[] doors = tc.getDoorStatus();
        doors[7]=!doors[7];
        tc.adjustDoors(doors);
        if(doors[7]){
            d8Status.setFill(Color.GREEN);
        } else {
            d8Status.setFill(Color.RED);
        }
    }




}
