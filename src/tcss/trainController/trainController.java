package tcss.trainController;

import tcss.trainmodel.*;
import java.time.Clock;

public class trainController {
    //Instance Variables
    public int id;
    private TrainModel model;
    float suggestedSpeed;
    float setpointSpeed;
    float vSpeed;
    int opMode = 0; //default to automatic
    private int time;
    int authority;

    public trainController(TrainModel m){
        this.model = m;
        //this.id = model.getID();
        this.updateStatus();

    }

    public void updateStatus(){
        this.vSpeed = setpointSpeed < suggestedSpeed ? setpointSpeed : suggestedSpeed;
    }
}
