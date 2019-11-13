package tcss.Demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.EventHandler;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class PLCController implements Initializable {
    @FXML private Stage stage;
    @FXML private Button openButton;
    @FXML private FileChooser fileChooser;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stage.setTitle("Upload PLC File");

        fileChooser = new FileChooser();

        openButton = new Button("Open a PLC file");
        openButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e){
                        File file = fileChoose.showOpenDialog(stage);
                        if(file !=null){
                            openFile(file);
                        }
                    }
                }
        );

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
