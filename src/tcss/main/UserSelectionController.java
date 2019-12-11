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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

import static tcss.main.Main.T;

public class UserSelectionController implements Initializable {

    // ChoiceBox
    @FXML private ChoiceBox rate;

    // Buttons
    @FXML private Button playpause;
    @FXML private Button close;
    @FXML private Button dispatcher;
    @FXML private Button trackEngineer;
    @FXML private Button driver;
    @FXML private Button murphy;
    @FXML private Button settingsButton;

    // Labels
    @FXML private Label clockLabel;

    int r = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rate.getItems().add("1x");
        rate.getItems().add("10x");
        rate.getItems().add("50x");
        rate.getSelectionModel().select(0);
        clockLabel.setText("00:00");

        // Set change listener on ChoiceBox
        rate.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                rate.getSelectionModel().select(t1.intValue());
                r = Integer.parseInt(rate.getValue().toString().replaceAll("\\D+", ""));
            }
        });

        // Create Timeline for GUI updating
        Timeline loop = new Timeline(new KeyFrame(Duration.seconds(.2), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                update();
            }
        }));
        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();
    }

    public void update() {
        clockLabel.setText(T.getLongTime());
        if(r != T.getRate()) {
            T.changeRate(r);
        }
    }

    /*
    TODO
    USE THESE METHODS TO OPEN YOUR GUI VIEWS
     */

    public void dispatcherView() throws Exception{
        System.out.println("OPEN DISPATCHER VIEW");

        FXMLLoader dispatchLoader = new FXMLLoader(getClass().getResource("fxml/ctc.fxml"));
        Stage dispatch = new Stage();
        Parent dispatchRoot = dispatchLoader.load();
        dispatch.setTitle("CTC");
        dispatch.setScene(new Scene(dispatchRoot));
        dispatch.getIcons().add(new Image("file:resources/train.png"));

        dispatch.show();
    }

    public void trackEngineerView() throws Exception{
        System.out.println("OPEN TRACK ENGINEER VIEW");

        FXMLLoader trackLoader = new FXMLLoader(getClass().getResource("fxml/TrackSelect.fxml"));
        Stage trackStage = new Stage();
        Parent trackRoot = trackLoader.load();
        trackStage.setTitle("Select Track Module");
        trackStage.setScene(new Scene(trackRoot));
        trackStage.setResizable(false);
        trackStage.getIcons().add(new Image("file:resources/train.png"));

        trackStage.show();
    }

    public void driverView() throws Exception{
        System.out.println("OPEN TRAIN DRIVER VIEW");

        FXMLLoader driverLoader = new FXMLLoader(getClass().getResource("fxml/TrainController.fxml"));
        Stage driver = new Stage();
        Parent driverRoot = driverLoader.load();
        driver.setTitle("Train Controller");
        driver.setScene(new Scene(driverRoot));
        driver.setResizable(false);
        driver.getIcons().add(new Image("file:resources/train.png"));

        driver.show();



    }

    public void murphyView() throws Exception{
        System.out.println("OPEN MURPHY VIEW");

        FXMLLoader murphyLoader = new FXMLLoader(getClass().getResource("fxml/ModuleSelection.fxml"));
        Stage murphy = new Stage();
        Parent murphyRoot = murphyLoader.load();
        murphy.setTitle("Module Failure States");
        murphy.setScene(new Scene(murphyRoot));
        murphy.setResizable(false);
        murphy.getIcons().add(new Image("file:resources/train.png"));

        murphy.show();

    }

    public void openSettings() throws Exception {
        System.out.println("OPEN SETTINGS POPUP");

        FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource("fxml/settings.fxml"));
        Parent settingsRoot = settingsLoader.load();
        Stage settings = new Stage();
        settings.initModality(Modality.APPLICATION_MODAL);
        settings.setScene(new Scene(settingsRoot));
        settings.setTitle("System Settings");
        settings.getIcons().add(new Image("file:resources/train.png"));
        settings.showAndWait();
    }

    public void passengerView() throws Exception {
        System.out.println("OPEN PASSENGER VIEW");

        FXMLLoader passLoader = new FXMLLoader(getClass().getResource("fxml/Passenger.fxml"));
        Stage passenger = new Stage();
        Parent passRoot = passLoader.load();
        passenger.setTitle("Passenger View");
        passenger.setScene(new Scene(passRoot));
        passenger.setResizable(false);
        passenger.getIcons().add(new Image("file:resources/train.png"));

        passenger.show();
    }


    public void closeWindow() throws Exception{

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setResizable(false);
        popup.getIcons().add(new Image("file:resources/train.png"));

        AnchorPane pane = new AnchorPane();
        pane.getStyleClass().add("body");

        VBox vbox = new VBox();
        vbox.getStyleClass().add("v-box");

        Label confirmLabel = new Label("Are you sure you want to exit?");

        HBox hbox = new HBox(40);
        hbox.getStyleClass().add("h-box");

        Button quitButton = new Button("Quit");
        Button cancelButton = new Button("Cancel");
        quitButton.getStyleClass().add("quitButton");
        cancelButton.getStyleClass().add("cancelButton");

        pane.getChildren().add(vbox);
        vbox.getChildren().addAll(confirmLabel, hbox);
        hbox.getChildren().addAll(quitButton, cancelButton);

        quitButton.setOnAction((actionEvent -> {
            System.exit(7);
        }));

        cancelButton.setOnAction((actionEvent -> {
            popup.close();
        }));

        Scene popupScene = new Scene(pane);
        popupScene.getStylesheets().add(getClass().getResource("css/ConfirmExit.css").toExternalForm());
        popup.setScene(popupScene);
        popup.showAndWait();


    }

    public void togglePlay() {
        String cur = playpause.getStyleClass().toString();

        if(cur.toLowerCase().equals("button play")) {
            playpause.getStyleClass().remove(1);
            playpause.getStyleClass().add("pause");
            playpause.setStyle("-fx-shape: \"M39.104,6.708c-8.946-8.943-23.449-8.946-32.395,0c-8.946,8.944-8.946,23.447,0,32.394 c8.944,8.946,23.449,8.946,32.395,0C48.047,30.156,48.047,15.653,39.104,6.708z M20.051,31.704c0,1.459-1.183,2.64-2.641,2.64 s-2.64-1.181-2.64-2.64V14.108c0-1.457,1.182-2.64,2.64-2.64s2.641,1.183,2.641,2.64V31.704z M31.041,31.704 c0,1.459-1.183,2.64-2.64,2.64s-2.64-1.181-2.64-2.64V14.108c0-1.457,1.183-2.64,2.64-2.64s2.64,1.183,2.64,2.64V31.704z\"");

            // Start background timer
            String s = rate.getValue().toString();
            s = s.replaceAll("\\D+", "");
            int r = Integer.parseInt(s);
            T.play(r);
        }
        else if(cur.toLowerCase().equals("button pause")) {
            playpause.getStyleClass().remove(1);
            playpause.getStyleClass().add("play");
            playpause.setStyle("-fx-shape: \"M873,256.9C820.4,183.3,747,127.1,662.1,96.1c-86.2-31.5-182.7-34.5-270.9-9.5  c-85.3,24.2-163.2,74.8-219.5,143.3C113.3,301,78,388,71.1,479.8c-6.8,90.7,15.3,183.2,62.5,261c45.8,75.5,114.5,137.3,195.1,173.5  c85.3,38.4,180.7,49.2,272.4,30.7C688.5,927.4,770,882,831.5,817.6c61.7-64.6,103.7-148,117.1-236.5c3.5-22.9,5.5-45.9,5.5-69V512  C954,421.2,925.8,330.9,873,256.9z M722,529.3c-0.2,0.1-0.4,0.2-0.6,0.3c-12.8,7.9-26,15.1-39,22.7l-79.7,46.4  c-30.8,17.9-61.7,35.9-92.5,53.8c-25.4,14.8-50.8,29.5-76.1,44.3c-10.6,6.2-21.3,12.4-31.9,18.6c-2.9,1.7-6,2.5-9,2.6  c0,0-0.1,0-0.1,0c-0.3,0-0.7,0-1,0.1c-2.4,0.1-4.7-0.3-6.9-1.1c-1-0.4-1.9-0.8-2.8-1.3c-0.9-0.5-1.8-1.1-2.6-1.8  c-1.1-0.9-2.1-1.8-2.9-2.9c0,0,0,0,0,0c0,0,0,0,0,0c-0.7-0.8-1.5-1.8-2.1-2.7c-2-3.2-2.8-6.9-2.8-10.5V326c0-15.2,17-24.9,30.2-17.3  c0.2,0.1,0.4,0.4,0.6,0.5c13.2,7.2,26,14.8,39,22.8h0c26.6,15,53.1,30.8,79.7,46.2c30.8,17.9,61.7,35.8,92.5,53.8  c25.4,14.8,50.8,29.5,76.1,44.3c10.6,6.2,21.3,12.4,31.9,18.5C735,502.3,735,521.7,722,529.3z\"");

            // Pause background timer
            T.pause();
//            T.cancel();
        }


    }
}


