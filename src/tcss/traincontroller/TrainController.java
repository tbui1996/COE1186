package tcss.traincontroller;

import tcss.trainmodel.*;
//import java.time.Clock;

public class TrainController {
    //Instance Variables
    public int id;
    private TrainModel model;
    private float suggestedSpeed;
    private float setpointSpeed;
    private float vSpeed;
    int opMode = 0; //default to automatic
    private int time;
    int authority;
    private int ki;
    private int kp;

    public TrainController(TrainModel m){
        this.model = m;
        //this.id = model.getID();
        this.updateStatus();

    }

    public void updateStatus(){
        this.vSpeed = setpointSpeed < suggestedSpeed ? setpointSpeed : suggestedSpeed;
    }
}
