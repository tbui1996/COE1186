package tcss.main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.paint.Color;
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
    @FXML private Label undergroundDisplay;
    @FXML private Label powerCommandLabel, kilabel, kplabel;
    @FXML private Label currentSpeedLabel;
    //@FXML private Button settings;
    //@FXML private CheckBox light1, light2, light3, light4, light5, light6, light7, light8, door1, door2, door3, door4, door5, door6, door7, door8;

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
                    trainButton.setDisable(false);
                    update();
                } else {
                    tc = null; //deselect train
                    idLabel.setText("ID: ");
                    suggestedSpeedLabel.setText("Suggested Speed: ");
                    authLabel.setText("Authority: ");
                    setPointInput.setPromptText("");
                    //undergroundDisplay.setText("N/A");
                    //undergroundDisplay.setTextFill(Color.YELLOW);
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
        if(tc == null) {
            return;
        }
        idLabel.setText("ID: " + tc.getID());
        System.out.println("tc is: " + tc.toString());
        suggestedSpeedLabel.setText("Suggested Speed: " + tc.getSSpeed());
        System.out.println("" + tc.getSSpeed() + " with model: ");
        if(opModeToggle.isSelected()){
            opModeToggle.setText("Exit Manual Mode");
        } else {
            opModeToggle.setText("Enter Manual Mode");
        }
//        setPointInput.setText("");
//        setPointInput.setPromptText("" + tc.getsetpointSpeed());
        authLabel.setText("Authority: " + tc.getAuthority());
        if (tc.getUnderground()){
            undergroundDisplay.setText("True");
            undergroundDisplay.setTextFill(Color.GREEN);
        } else {
            undergroundDisplay.setText("False");
            undergroundDisplay.setTextFill(Color.RED);
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

        TrainModelController controller = trainloader.<TrainModelController>getController();
        controller.passTrain(tc.getTrain());

        trainStage.show();
    }

    public void confirmSetpoint(ActionEvent actionEvent) throws Exception{
       try {
           try {
               float newSPSpeed = Float.parseFloat(setPointInput.getText());
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

    public void toggleOpMode(ActionEvent actionEvent) throws Exception {
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
}
