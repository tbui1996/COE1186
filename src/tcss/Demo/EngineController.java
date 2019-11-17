package tcss.Demo;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tcss.traincontroller.TrainController;
import tcss.trainmodel.TrainModel;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class EngineController implements Initializable {

    // UI variables
    /**
     * The {@link Label} instance which is used to display to the user a descriptive string and the currentKi.
     */
    @FXML private Label currentKi;
    /**
     * The {@link Label} instance which is used to display to the user a descriptive string and the currentKp.
     */
    @FXML private Label currentKp;
    /**
     * The {@link TextField} instance which is used to accept a new ki input as a float. It parses the user's input string and filters none decimal characters out.
     */
    @FXML private TextField kiTextField;
    /**
     * The {@link TextField} instance which is used to accept a new kp input as a float. It parses the user's input string and filters none decimal characters out.
     */
    @FXML private TextField kpTextField;
    /**
     * The {@link Button} instance which is used to confirm the new ki and kp values. This shall change the ki and kp values for all newly created trains.
     */
    @FXML private Button confirmKiKp;
    /**
     * The {@link AnchorPane} instance which is used to organize and present the rest of the FXML variables described within this class.
     */
    @FXML private AnchorPane pane;

    //Data variables
    /**
     * The {@link float} instances which are stored within this class to reduce the overhead of accessing Main.kikp. These should be identical to Main.kikp and should change accordingly.
     */
    private float ki, kp;

    /**
     * This initializes the Controller for adjusting the engine values
     * kiTextField and kpTextField are prepared to only accept decimal values (e.g. 0,1,2,3,4,5,6,7,8,9,.)
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ki = Main.kikp[0];
        kp = Main.kikp[1];
        DecimalFormat format = new DecimalFormat( "#.0" ); //TODO Make sure that this formatter does not accept '.'
        kiTextField.setTextFormatter( new TextFormatter<>(c -> {
            if ( c.getControlNewText().isEmpty() ) {
                return c;
            }
            ParsePosition parsePosition = new ParsePosition( 0 );
            Object object = format.parse( c.getControlNewText(), parsePosition );
            if ( object == null || parsePosition.getIndex() < c.getControlNewText().length() ){
                return null;
            } else {
                return c;
            }
        }));
        kpTextField.setTextFormatter( new TextFormatter<>(c -> {
            if ( c.getControlNewText().isEmpty() ) {
                return c;
            }
            ParsePosition parsePosition = new ParsePosition( 0 );
            Object object = format.parse( c.getControlNewText(), parsePosition );
            if ( object == null || parsePosition.getIndex() < c.getControlNewText().length() ){
                return null;
            } else {
                return c;
            }
        }));
        kiTextField.setPromptText(Float.toString(ki));
        kpTextField.setPromptText(Float.toString(kp));
    }

    /**
     * This function is responsible for changing the global ki and kp values for new traincontrollers that we instantiate
     * It checks to see if the user input any values, and if they did assigns ki and kp to the new values
     * If the user did not enter a value, the previous ki and kp are used
     * @param actionEvent XML event -- the user shall select the confirmKiKp button
     * @throws Exception In case of Null Pointer Exception
     */
    private void confirmEngineConstants(ActionEvent actionEvent) throws Exception{
        Main.kikp[0] = Float.parseFloat(kiTextField.getText() == "" ? ""+kp : kpTextField.getText()); //update global ki and kp values
        Main.kikp[1] = Float.parseFloat(kpTextField.getText() == "" ? ""+kp : kpTextField.getText());
        kp = Main.kikp[1]; //update ki and kp from the new changes
        ki = Main.kikp[0];
        kpTextField.setText(""); //clear the text fields
        kiTextField.setText("");
        kpTextField.setPromptText(Float.toString(kp)); //reassign the prompt texts
        kiTextField.setPromptText(Float.toString(ki));

    }

    public void goBack(ActionEvent actionEvent) throws Exception {
        Scene moduleSelect = new Scene(FXMLLoader.load(getClass().getResource("ModuleSelection.fxml")));
        Stage window = (Stage) pane.getScene().getWindow();
        window.setScene(moduleSelect);
        window.setTitle("Module Selection");

    }
}