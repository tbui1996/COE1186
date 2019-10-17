package tcss.Demo;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tcss.trainmodel.TrainModel;

import java.net.URL;
import java.util.ResourceBundle;

public class TrainModelController implements Initializable {

    // UI variables
    @FXML private Label idLabel;
    @FXML private Label sSpeedLabel;
    @FXML private Label authLabel;
    @FXML private Label speedLimitLabel;
    @FXML private Label gradeLabel;
    @FXML private Label cmdSpeed;
    @FXML private Label underground;
    @FXML private Label eBrake;
    @FXML private Label eBrakeStatus;
    @FXML private Label underStatus;
    @FXML private AnchorPane pane;
    @FXML private ChoiceBox trainChoice;

    // Testing input filter
//    @FXML private TextField tField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {


        // Testing input filter
//        DecimalFormat format = new DecimalFormat( "#.0" );
//
//        tField.setTextFormatter(new TextFormatter<Object>(c ->
//           {
//               if ( c.getControlNewText().isEmpty() )
//                {
//                    return c;
//                }
//
//                ParsePosition parsePosition = new ParsePosition( 0 );
//                Object object = format.parse( c.getControlNewText(), parsePosition );
//
//                if ( object == null || parsePosition.getIndex() < c.getControlNewText().length() )
//                {
//                    return null;
//                }
//                else
//                {
//                    return c;
//                }
//           }));


        trainChoice.getItems().add("Select Train");

        // Testing
        for(TrainModel t: Main.trains) {
            trainChoice.getItems().add("Train " + t.getID());
        }

        trainChoice.setValue("Select Train");
        trainChoice.setTooltip(new Tooltip("Select a train to view"));
        trainChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                if((Integer) number2 > 0) {
//                    System.out.println(trainChoice.getItems().get((Integer) number2));
//                    System.out.println("ID: " + Main.trains.get((Integer)number2 - 1).getID());

                    TrainModel cur = Main.trains.get((Integer)number2-1);

                    idLabel.setText("ID: " + cur.getID());
                    sSpeedLabel.setText("Suggested Speed: " + cur.getSSpeed());
                    authLabel.setText("Authority: " + cur.getAuthority());
                    speedLimitLabel.setText("Speed Limit: " + cur.getSpeedLimit());
                    gradeLabel.setText("Grade: " + cur.getGrade());
                    cmdSpeed.setText("Commanded Speed: " + cur.getCmdSpeed());
                    if(cur.getUnderground() == true) {
                        underStatus.setText("True");
                        underStatus.setTextFill(Color.GREEN);
                    } else {
                        underStatus.setText("False");
                        underStatus.setTextFill(Color.RED);
                    }
                    if(cur.getEBrake() == true) {
                        eBrakeStatus.setText("True");
                        eBrakeStatus.setTextFill(Color.GREEN);
                    } else {
                        eBrakeStatus.setText("False");
                        eBrakeStatus.setTextFill(Color.RED);
                    }

                } else {
                    idLabel.setText("ID: ");
                    sSpeedLabel.setText("Suggested Speed: ");
                    authLabel.setText("Authority: ");
                    speedLimitLabel.setText("Speed Limit: ");
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
