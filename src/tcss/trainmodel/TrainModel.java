package tcss.trainmodel;

import tcss.traincontroller.*;

public class TrainModel {

    // Instance variables
    private TrainController controller;
    private float suggestedSpeed;
    private int authority;
    private int id;
    private float speedLimit;

    public TrainModel(float suggestedSpeed, int authority, int id, float speedLimit) {
        controller = new TrainController(this);
        this.suggestedSpeed = suggestedSpeed;
        this.authority = authority;
        this.id = id;
        this.speedLimit = speedLimit;
        controller.setSpeedLimit(speedLimit);
    }

    public void passCommands(float sSpeed, int auth) {
        suggestedSpeed = sSpeed;
        authority = auth;
        controller.passCommands(auth, sSpeed);
    }

    public int getID() {
        return id;
    }

    public void update() {

    }

//    @FXML
//    private void updateUI() {
//        idLabel.setText("ID: " + id);
//        sSpeedLabel.setText("Suggested Speed: " + suggestedSpeed);
//        authLabel.setText("Authority: " + authority);
//    }
}
