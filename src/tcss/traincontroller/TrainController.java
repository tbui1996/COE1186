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
    private boolean underground;
    private boolean eBrake;

    //Constructor method for the TrainController when instantiated with a train model
    /*
    Takes a train model, this includes the TC id
     */
    public TrainController(TrainModel m){
        this.model = m;
        this.id = model.getID();
        this.authority = model.getAuthority();
        this.suggestedSpeed = model.getSSpeed();
        this.setpointSpeed = 0;
        //this.underground = model.getUnderground();
        this.eBrake = model.getEBrake();
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

    public void updateModelEBrake(){
        this.model.setEBrake(this.eBrake);
    }

    public void updateModelCommandedSpeed(){
        this.model.setCmdSpeed(this.commandedSpeed);
    }

    public void passCommands(int a, float ss){
        this.authority = a;
        this.suggestedSpeed = ss;
    }

    public int getID(){
        return this.id;
    }

    public float getSSpeed(){
        return this.suggestedSpeed;
    }

    public float getsetpointSpeed(){
        return this.setpointSpeed;
    }

    public int getAuthority() {
        return authority;
    }

    public float getSpeedLimit(){
        return this.speedLimit;
    }

    public boolean getUnderground(){
        return this.underground;
    }

    public boolean getEBrake(){
        return this.eBrake;
    }
    public void setSpeedLimit(float sl){
        this.speedLimit = sl;
    }

    public void setEBrake(boolean b){
        this.eBrake = b;
        //this.model.setEBrake(b);
    }

    public void setSetpointSpeed(float sps){
        this.setpointSpeed = sps;
    }

}
