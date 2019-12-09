package tcss.traincontroller;

import tcss.trainmodel.TrainModel;
import tcss.main.Main;
import java.util.ArrayList;

/**
 * This class is responsible for controlling the motion of the train through a power command and control of the brakes.
 * Additionally, this object is responsible for controlling the train's doors/lights/and horn
 *
 * This module is used by the train driver, and will be utilized by the train model.
 * Additionally, this module must be vital, and utilizes TMR to satisfy this goal
 */
public class TrainController {

    //Static class variables
    public static ArrayList<TrainController> TrainControllerList = new ArrayList<TrainController>();

    //Instance Variables
    public int id;
    private TrainModel model;
    private float suggestedSpeed;
    private float setpointSpeed;
    private float commandedSpeed;
    private float currentSpeed;
    private float speedLimit;
    private float temp;
    public boolean opMode = false; //default to automatic
    int authority;
    private float ki;
    private float kp;
    private boolean underground;
    private boolean eBrake;
    private boolean sBrake;
    private float[] lastVerrs; //TODO: These should be pushed to a singular vErr and muk with multiple power calculators
    private float[] lastmuk;
    final private float MAX_PWR_CMD = 120000; //120 KW == 120,000 W
    final private float T = 1; //sampling rate of trains - PWRCMD calculation rate
    private float PWRCMD;
    private boolean[] doors = {true, true, true, true, true, true, true, true};
    private float SLOW_OPERATING_SPEED = 2.235f;


    /**
     * Constructor method for the train controller. Initializes each of the standard variables before
     * Adds the traincontroller to the traincontroller list.
     * @param model The Train Model that this controller is installed within.
     */
    public TrainController(TrainModel model){
        System.out.println("Adding a new train controller for train model: " + model.getID());
        this.model = model;
        this.id = model.getID();
        this.authority = model.getAuthority();
        this.suggestedSpeed = model.getSSpeed();
        this.setpointSpeed = suggestedSpeed; //since we start in auto, we can set sps to the suggested
        this.speedLimit = model.getSpeedLimit();
        this.underground = model.getUnderground();
        this.eBrake = model.getEBrake(); //gets the ebrake status from the model.
        this.lastVerrs = new float[]{0, 0, 0}; //for use in the vital pwrcmd calcs
        this.lastmuk = new float[]{0, 0, 0};
        this.PWRCMD = 0;
        this.ki = Main.kikp[0]; //get the current desired KI and KP
        this.kp = Main.kikp[1];
        this.temp = model.getTemp();
        TrainController.TrainControllerList.add(this);
        //System.out.println(TrainControllerList.toString());
    }

    public TrainController(int id, int authority, float suggestedSpeed, float speedLimit, boolean underground, float temp){
        this.id = id;
        this.authority = authority;
        this.suggestedSpeed = suggestedSpeed;
        this.speedLimit = speedLimit;
        this.underground = underground;
        this.temp = temp;
        this.setpointSpeed = suggestedSpeed; //since we start in auto, we can set sps to the suggested
        this.eBrake = true;
        this.lastVerrs = new float[]{0, 0, 0}; //for use in the vital pwrcmd calcs
        this.lastmuk = new float[]{0, 0, 0};
        this.PWRCMD = 0;
        this.ki = Main.kikp[0]; //get the current desired KI and KP
        this.kp = Main.kikp[1];
        TrainController.TrainControllerList.add(this);
    }

    /**
     * This constructor is used for special cases. Specifically this is used to create a trainController without adding it to the traincontroller list
     * @param m The train model that this controller is installed within
     * @param a A dumby int. Unused.
     */
    public TrainController(TrainModel m, int a){
        //System.out.println("Adding a new train controller for train model: " + m.getID());
        this.model = m;
        this.id = model.getID();
        this.authority = model.getAuthority();
        this.suggestedSpeed = model.getSSpeed();
        this.setpointSpeed = 0;
        this.underground = model.getUnderground();
        this.eBrake = model.getEBrake();
        this.lastVerrs = new float[]{0, 0, 0};
        this.lastmuk = new float[]{0, 0, 0};
        this.PWRCMD = 0;
        this.ki = Main.kikp[0];
        this.kp = Main.kikp[1];
    }

    /*
    Method responsible for updating calculations for safe travel and operation
    determines new commanded speed and
     */

    /**
     * This method updates the train model and traincontroller so that all of the information between the modules are consistent
     * Updates the model, then updates the controller with relevant information from the model.
     * Updates the Ebrake if it was pulled by a passenger
     * Updates the currentSpeed from the newly calculated speed
     * Authority and Suggested Speed are updated through the passcommands method
     */
    public void update(){
        // TODO Remove calls to update model; that's handled by SimTime
        currentSpeed = model.getCurV();
        if(model.getEBrake()) this.eBrake = true;
        suggestedSpeed = model.getSSpeed();
        authority = model.getAuthority();

        float[] pwrVotes = new float[3];
        int [] brakeVotes = new int[3];

        prepareVoters(pwrVotes, brakeVotes);

        Float PWRCMD = 0f;
        Integer BRAKECMD = 0;
        System.out.println("Entering majorityVote: PWRCMD = " + PWRCMD);
        majorityVote(pwrVotes, PWRCMD);
        System.out.println("Exiting majorityVote: PWRCMD = " + PWRCMD);
        if(PWRCMD > 0){
            model.setPWRCMD(PWRCMD);
        } else if (PWRCMD == -1){
            prepareVoters(pwrVotes, brakeVotes);
            majorityVote(pwrVotes, PWRCMD);
            if(PWRCMD > 0){
                model.setPWRCMD(PWRCMD);
            } else {
                eBrake = true;
                model.setEBrake(true);
            }
        }
    }

    /**
     * This method changes the operation mode. since the opMode is a boolean, we are able to invert it easily.
     * Additionally, this method will adjust the setpointspeed to the suggestedspeed regardless of state change
     *      to automatic --> setpoint changes to suggested
     *      to manual --> setpoint changes to suggested, but should already have been suggested
     */
    public void changeOperationMode(){
        opMode = !opMode; //1 is manual
        setpointSpeed = suggestedSpeed;
    }

    public void prepareVoters(float[] pwrcmds, int[] brakecmds){
        commandedSpeed = setpointSpeed < suggestedSpeed ? setpointSpeed : suggestedSpeed;
        commandedSpeed = suggestedSpeed < speedLimit ? suggestedSpeed : speedLimit;
        for(int id = 0; id < 3; id++) {
            getPWRCMD(commandedSpeed, pwrcmds, id);
        }
    }


    /**
     * This method will compare three responses and determine the proper output through Two Majority Report.
     * If no two of the responses agree, the voter returns -1
     * Agreement is determined to be a ~1% of the maximumPower (120). the agreed values will be averaged.
     *  e.g. if votes are: 100, 32, and 302.84, voters A and B will be considered in agreement, and the output energy command will be 66
     * @param pwrVotes An array of floats of size 3 containing the votes to choose between.
     * @param RESOLVED_PWR_CMD a Float type which references the power command that will be sent to the engine
     * @return
     */
    private void majorityVote(float[] pwrVotes, Float RESOLVED_PWR_CMD){
        if(pwrVotes[1] - MAX_PWR_CMD <= pwrVotes[0] && pwrVotes[0] <= pwrVotes[1] + MAX_PWR_CMD){ //At least A and B agree
            if(pwrVotes[2] - MAX_PWR_CMD <= pwrVotes[0] && pwrVotes[0] <= pwrVotes[2] + MAX_PWR_CMD){ //A also agrees with C
                RESOLVED_PWR_CMD = (pwrVotes[0] + pwrVotes[1] + pwrVotes[2])/3;
            } else { //A only agrees with B and not with C
                RESOLVED_PWR_CMD = (pwrVotes[0] + pwrVotes[1])/2;
            }
        } else if(pwrVotes[2]-MAX_PWR_CMD <= pwrVotes[0] && pwrVotes[0] <= pwrVotes[2]+MAX_PWR_CMD ) { //A does not agree with b but does agree with c
            RESOLVED_PWR_CMD = (pwrVotes[0] + pwrVotes[2])/2;
        } else if(pwrVotes[1] - MAX_PWR_CMD <= pwrVotes[2] && pwrVotes[2] <= pwrVotes[1] + MAX_PWR_CMD){ //b agrees with c
            RESOLVED_PWR_CMD = (pwrVotes[1] + pwrVotes[2])/2;
        }
    }

    private void getPWRCMD(float cmdSpeed, float[] pwrcmds, int id){
        if(eBrake){
            pwrcmds[id] = -1000; //-1000 will be considered eBrake
        }
        if(authority < 3 && authority > 0 && currentSpeed >= SLOW_OPERATING_SPEED){ //manage speed down to safe slow speed
            pwrcmds[id] = -240; //sBrake
        }
        if(authority == 0){
            pwrcmds[id] = -240; // -1 is coded as s-brake
        }
        float vErr = cmdSpeed - currentSpeed;
        float CMD = kp*vErr + ki*(lastmuk[id] + T/2*(vErr + lastVerrs[id]));
        lastVerrs[id] = vErr;
        if(CMD > MAX_PWR_CMD){
            CMD = kp*vErr + ki*lastmuk[id];
        } else {
            lastmuk[id] = lastmuk[id] + T/2*(vErr + lastVerrs[id]);
        }
        //TODO: Find out what negative value is absurd to be sent and determine values where break is desired instead.
        if(CMD < 0){
            pwrcmds[id] = 0;
        } else if(CMD > 120000){
            pwrcmds[id] = 120000;
        } else {
            pwrcmds[id] = CMD;
        }
    }

    public void adjustDoors(boolean [] doors){
        if(currentSpeed == 0){
            this.doors = doors;
        }
    }

    public boolean[] getDoorStatus(){
        return this.doors;
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

    public void setTemp(float temp){
        this.temp = temp;
        model.setTemp(this.temp);
    }

    public float getTemp(){
        return temp;
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

    public void setSpeedLimit(float speedLimit){
        this.speedLimit = speedLimit;
    }

    public float getSpeedLimit(){
        return speedLimit;
    }

    public boolean issBrake() {
        return sBrake;
    }

    public void setsBrake(boolean sBrake) {
        this.sBrake = sBrake;
    }

    /**
     * Method responsible for updating all the train controllers through the SimTime module
     */
    public static void updateAll() {
        for(TrainController tc: TrainControllerList) {
            tc.update();
        }
    }
}
