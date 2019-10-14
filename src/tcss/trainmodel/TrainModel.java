package tcss.trainmodel;

import tcss.traincontroller.*;
import javafx.application.Application;

public class TrainModel {

    // Instance variables
    private TrainController controller;
    float suggestedSpeed;
    int authority;

    public TrainModel() {
        controller = new TrainController(this);
    }

    public void passCommands(float sSpeed, int auth) {
        suggestedSpeed = sSpeed;
        authority = auth;
        controller.updateModel(sSpeed, auth);
    }

}
