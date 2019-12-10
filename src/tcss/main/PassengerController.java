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
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import tcss.Demo.Main;
import tcss.trainmodel.TrainModel;

import java.net.URL;
import java.util.ResourceBundle;

public class PassengerController implements Initializable {

	private final float MPS_T0_MPH = 2.236936f;		// Convert from meters/second to miles/hour

	// UI variables
	@FXML private Label idLabel;
	@FXML private Label speedLabel;
	@FXML private ToggleButton eBrakeButton;
	@FXML private AnchorPane pane;
	@FXML private ChoiceBox trainChoice;

	TrainModel cur;

	// Testing input filter
//    @FXML private TextField tField;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		trainChoice.getItems().add("Select Train");

		// Testing
		for(TrainModel t: TrainModel.getAllTrains()) {
			trainChoice.getItems().add("Train " + t.getID());
		}

		eBrakeButton.setDisable(true);

		trainChoice.setValue("Select Train");
		trainChoice.setTooltip(new Tooltip("Select a train to view"));
		trainChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				if((Integer) number2 > 0) {
//                    System.out.println(trainChoice.getItems().get((Integer) number2));
//                    System.out.println("ID: " + Main.trains.get((Integer)number2 - 1).getID());

					cur = TrainModel.getAllTrains().get((Integer)number2-1);

					idLabel.setText("ID: " + cur.getID());
					eBrakeButton.setDisable(false);
					if(cur.getEBrake()) {
						eBrakeButton.setStyle("-fx-background-color: red; -fx-text-fill: #dfdfdf");
					} else {
						eBrakeButton.setStyle("-fx-background-color: #dfdfdf; -fx-text-fill:rgb(43,39,49)");
					}

				} else {
					idLabel.setText("ID: ");
					speedLabel.setText("Current Speed: ");
					eBrakeButton.setDisable(true);
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

	public void toggleEBrake(ActionEvent actionEvent) throws Exception {
		boolean brakeStatus = eBrakeButton.isSelected();
		cur.setEBrake(brakeStatus);
	}

	public void update(){
		if(cur == null)
			return;
		speedLabel.setText("Current Speed: " + Math.round(cur.getCurV()*MPS_T0_MPH * 100f)/100f + " MPH");
		if(cur.getEBrake()) {
			eBrakeButton.setStyle("-fx-background-color: red; -fx-text-fill: #dfdfdf");
		} else {
			eBrakeButton.setStyle("-fx-background-color: #dfdfdf; -fx-text-fill:rgb(43,39,49)");
		}
	}

	public void closeWindow() {
		Stage s = (Stage) trainChoice.getScene().getWindow();
		s.close();
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
