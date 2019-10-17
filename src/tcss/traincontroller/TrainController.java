package tcss.traincontroller;

import tcss.trainmodel.TrainModel;
//import java.time.Clock;

public class TrainController {
    //Instance Variables
    public int id;
    private TrainModel model;
    private float suggestedSpeed;
    private float speedLimit;
    private float setpointSpeed;
    private float commandedSpeed;
    private float currentSpeed;
    private float errorSpeed;
    int opMode = 0; //default to automatic
    private int time;
    int authority;
    private int ki;
    private int kp;

    //Constructor method for the TrainController when instantiated with a train model
    /*
    Takes a train model, this includes the TC id
     */
    public TrainController(TrainModel m){
        this.model = m;
        this.id = model.getID();
        this.updateStatus();
    }

    /*
    Method responsible for updating calculatikns for safe travel and operation
    determines new commanded speed and
     */
    public void updateStatus(){
        this.model.update();

        //this.currentSpeed = model.getCurrSpeed();
        this.commandedSpeed = this.setpointSpeed < this.suggestedSpeed ? this.setpointSpeed : this.suggestedSpeed;
        this.commandedSpeed = this.setpointSpeed < this.speedLimit ? this.setpointSpeed : this.speedLimit;
    }

    public void passCommands(int a, float ss){
        this.authority = a;
        this.suggestedSpeed = ss;
    }

    public float getSpeedLimit(){
        return this.speedLimit;
    }

    public void setSpeedLimit(float sl){
        this.speedLimit = sl;
    }

}
