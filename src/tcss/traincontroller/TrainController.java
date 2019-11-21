package tcss.traincontroller;

import tcss.trainmodel.TrainModel;
import tcss.Demo.Main;

import java.util.ArrayList;

public class TrainController {

    public static ArrayList<TrainController> TrainControllerList = new ArrayList<TrainController>();


    //Instance Variables
    public int id;
    private TrainModel model;
    private float suggestedSpeed;
    private float setpointSpeed;
    private float commandedSpeed;
    private float currentSpeed;
    public boolean opMode = false; //default to automatic
    private int time;
    int authority;
    private float ki;
    private float kp;
    private boolean underground;
    private boolean eBrake;
    private float[] lastVerrs;
    private float[] lastmuk;
    final private float MAX_PWR_CMD = 120000; //KW
    final private float MAX_SPEED = 70; // KM/HR
    final private float T = 1; //sampling rate of trains
    private float PWRCMD;
    //private float oldsps;

    //Constructor method for the TrainController when instantiated with a train model
    /*
    Takes a train model, this includes the TC id
     */
    public TrainController(TrainModel m){
        System.out.println("Adding a new train controller for train model: " + m.getID());
        this.model = m;
        this.id = model.getID();
        this.authority = model.getAuthority();
        this.suggestedSpeed = model.getSSpeed();
        this.setpointSpeed = suggestedSpeed;
        //this.underground = model.getUnderground();
        this.eBrake = model.getEBrake();
        this.lastVerrs = new float[]{0, 0, 0};
        this.lastmuk = new float[]{0, 0, 0};
        this.PWRCMD = 0;
        this.ki = Main.kikp[0];
        this.kp = Main.kikp[1];
        TrainController.TrainControllerList.add(this);
        System.out.println(TrainControllerList.toString());
    }

    public TrainController(TrainModel m, int a){
        //System.out.println("Adding a new train controller for train model: " + m.getID());
        this.model = m;
        this.id = model.getID();
        this.authority = model.getAuthority();
        this.suggestedSpeed = model.getSSpeed();
        this.setpointSpeed = 0;
        //this.underground = model.getUnderground();
        this.eBrake = model.getEBrake();
        this.lastVerrs = new float[]{0, 0, 0};
        this.lastmuk = new float[]{0, 0, 0};
        this.PWRCMD = 0;
        this.ki = Main.kikp[0];
        this.kp = Main.kikp[1];
        //this.oldsps = 0;
    }

    /*
    Method responsible for updating calculations for safe travel and operation
    determines new commanded speed and
     */
    public void update(){
        model.update();
        commandedSpeed = setpointSpeed < suggestedSpeed ? setpointSpeed : suggestedSpeed;
        float result1 = getPWRCMD1(commandedSpeed);
        float result2 = getPWRCMD2(commandedSpeed);
        float result3 = getPWRCMD3(commandedSpeed);
        PWRCMD = majorityVote(result1, result2, result3);
        model.setPWRCMD(PWRCMD);
        if(PWRCMD == -2){
            model.setEBrake(true);
        }
    }


    public void changeOperationMode(){
        opMode = !opMode; //1 is manual
        if(opMode){
            //now in manual
            setpointSpeed = suggestedSpeed;
        } else { //in automatic mode
            setpointSpeed = suggestedSpeed;
        }
    }


    private float majorityVote(float a, float b, float c){
        if(a == b || a == c){ //a matches with at least one
            return a;
        } else if (b == c){ //b matches with c
            return b;
        } else
            return 0;
    }

    private float getPWRCMD1(float cmdSpeed){
        if(eBrake){
            return -2; //-2 is coded as e-brake
        }
        if(authority < 2){
            return -1; // -1 is coded as s-brake
        }
        float vErr = cmdSpeed - currentSpeed;
        float CMD = kp*vErr + ki*(lastmuk[0] + T/2*(vErr + lastVerrs[0]));
        lastVerrs[0] = vErr;
        if(CMD > MAX_PWR_CMD){
            CMD = kp*vErr + ki*lastmuk[0];
        } else {
            lastmuk[0] = lastmuk[0] + T/2*(vErr + lastVerrs[0]);
        }
        return CMD;
    }

    private float getPWRCMD2(float cmdSpeed){
        if(eBrake){
            return -2; //-2 is coded as e-brake
        }
        if(authority < 2){
            return -1; // -1 is coded as s-brake
        }
        float vErr = cmdSpeed - currentSpeed;
        float CMD = kp*vErr + ki*(lastmuk[1] + T/2*(vErr + lastVerrs[1]));
        lastVerrs[1] = vErr;
        if(CMD > MAX_PWR_CMD){
            CMD = kp*vErr + ki*lastmuk[1];
        } else {
            lastmuk[1] = lastmuk[1] + T/2*(vErr + lastVerrs[1]);
        }
        return CMD;
    }

    private float getPWRCMD3(float cmdSpeed){
        if(eBrake){
            return -2; //-2 is coded as e-brake
        }
        if(authority < 2){
            return -1; // -1 is coded as s-brake
        }
        float vErr = cmdSpeed - currentSpeed;
        float CMD = kp*vErr + ki*(lastmuk[2] + T/2*(vErr + lastVerrs[2]));
        lastVerrs[2] = vErr;
        if(CMD > MAX_PWR_CMD){
            CMD = kp*vErr + ki*lastmuk[2];
        } else {
            lastmuk[2] = lastmuk[2] + T/2*(vErr + lastVerrs[2]);
        }
        return CMD;
    }

    public void updateModelEBrake(){
        model.setEBrake(eBrake);
    }

    public void updateModelCommandedSpeed(){
        model.setCmdSpeed(commandedSpeed);
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
    public boolean getUnderground(){
        return this.underground;
    }

    public boolean getEBrake(){
        return this.eBrake;
    }

    public void setEBrake(boolean b){
        this.eBrake = b;
    }

    public void setSetpointSpeed(float sps){
        this.setpointSpeed = sps;
    }

    public boolean getOpMode() {
        return opMode;
    }

    public void setOpMode(boolean opMode) {
        this.opMode = opMode;
    }

    public String toString(){
        return(this.id + " at " + this.setpointSpeed);
    }

    public float getPWRCMD(){
        return PWRCMD;
    }

    public float getKi(){
        return ki;
    }

    public float getKp(){
        return kp;
    }

    public float getCurrentSpeed(){
        return currentSpeed;
    }

    public TrainModel getTrain() {
        return model;
    }

}
