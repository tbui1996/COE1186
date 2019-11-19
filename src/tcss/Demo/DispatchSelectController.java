package tcss.Demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DispatchSelectController {
    //UI variables
    @FXML private AnchorPane pane;
    @FXML private Button lineConfirm;
    @FXML private Button backButton;
    @FXML private ChoiceBox lineSelector;


    public void goBack(ActionEvent e) throws Exception {
        //Should return back to CTC module screen.  Does not save current progress
        Scene moduleSelect = new Scene(FXMLLoader.load(getClass().getResource("CTC.fxml")));
        Stage window = (Stage) pane.getScene().getWindow();
        window.setScene(moduleSelect);
        window.setTitle("CTC");
    }
}
