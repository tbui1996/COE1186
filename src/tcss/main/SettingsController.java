package tcss.main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class SettingsController implements Initializable {

    @FXML private Button kButton;
    @FXML private Button importTrackButton;
    @FXML private Button exitButton;
    @FXML private Button submitButton;
    @FXML private Button cancelButton;

    File file;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void importTrack() {
        FileChooser fileChooser = new FileChooser();
        // Configure FileChooser
        fileChooser.setTitle("Select your track file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel", "*.xlsx"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        File temp = fileChooser.showOpenDialog(new Stage());
        if(temp != null) {
            file = temp;
        }

    }

    public void kikp() throws Exception{
        FXMLLoader engineLoader = new FXMLLoader(getClass().getResource("fxml/Engine.fxml"));
        Parent engineRoot = engineLoader.load();
        Stage engine = new Stage();
        engine.initModality(Modality.APPLICATION_MODAL);
        engine.setScene(new Scene(engineRoot));
        engine.setTitle("Engine Settings");
        engine.getIcons().add(new Image("file:resources/train.png"));
        engine.showAndWait();
    }

    public void submitSettings() throws Exception {

        if(file != null) {
            //TODO Delete this testing
            Scanner f = new Scanner(file);
            while (f.hasNextLine()) {
                System.out.println(f.nextLine());
            }
        }

        Stage s = (Stage) submitButton.getScene().getWindow();
        s.close();
    }

    public void cancelSettings() {
        Stage s = (Stage) cancelButton.getScene().getWindow();
        s.close();
    }

}
