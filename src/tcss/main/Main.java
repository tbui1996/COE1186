package tcss.main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tcss.ctc.CTC;

public class Main extends Application {

    static SimTime T;
    static CTC ctc;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Init CTC
        ctc = new CTC();

        // Load opening scene - track input

        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/UserSelection.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("User Selection");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("file:resources/train.png"));


        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent)  {
                windowEvent.consume();
            }
        });

        primaryStage.show();

        // TESTING TIMER PAUSE
        T = new SimTime();
//        T = new Timer();
//        T.schedule(
//                new TimerTask() {
//                    @Override
//                    public void run() {
//                        System.out.println("Ping!");
//                    }
//                }, 0, 1000
//        );





    }

    public static void main(String [] args) throws Exception {
        launch(args);
    }

}




