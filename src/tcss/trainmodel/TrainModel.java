package tcss.trainmodel;

//import tcss.traincontroller.*;

public class TrainModel {

    // Instance variables
//    private TrainController controller;
    private float suggestedSpeed;
    private int authority;
    private int id;

//    public TrainModel() {
//        controller = new TrainController(this);
//    }

    public void passCommands(float sSpeed, int auth) {
        suggestedSpeed = sSpeed;
        authority = auth;
//        controller.updateModel(sSpeed, auth);

//        updateUI();


    }

    public int getID() {
        return id;
    }

//    @FXML
//    private void updateUI() {
//        idLabel.setText("ID: " + id);
//        sSpeedLabel.setText("Suggested Speed: " + suggestedSpeed);
//        authLabel.setText("Authority: " + authority);
//    }
}
