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
    private float suggestedSpeed; //km/hr
    private float setpointSpeed; //km/hr
    private float commandedSpeed; //meters/second
    private float currentSpeed; //km/hr
    private float speedLimit; //meters/second
    private float temp;
    public boolean opMode = false; //default to automatic
    int authority;
    private float ki;
    private float kp;
    private boolean underground;
    private boolean eBrake;
    private boolean sBrake;
    private float[] lastVerrs;
    private float[] lastmuk;

    private float[] powerVotes = {0,0,0};
    private boolean[] sBrakeVotes = {true, true, true};
    private boolean[] eBrakeVotes = {true, true, true};
    private float RESOLVED_PWR_CMD;
    private boolean RESOLVED_S_BRAKE;
    private boolean RESOLVED_E_BRAKE;

    final private float MAX_PWR_CMD = 120000; //120 KW == 120,000 W
    final private float T = 1; //sampling rate of trains - PWRCMD calculation rate
    private float PWRCMD;
    private boolean[] doors = {false, false, false, false, false, false, false, false};
    private float MAX_OPERATING_SPEED = 70f*1000f*100f/2.54f/12f/5280f; //km/h converted to miles/hours
    private float SLOW_OPERATING_SPEED = MAX_OPERATING_SPEED;//4*2.235f; //~5 MPH but in meters/s



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
        temp = 75;
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
        this.eBrake = false;
        this.lastVerrs = new float[]{0, 0, 0}; //for use in the vital pwrcmd calcs
        this.lastmuk = new float[]{0, 0, 0};
        this.PWRCMD = 0;
        temp = 75;
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

    /**
     * This method updates the train model and traincontroller so that all of the information between the modules are consistent
     * Updates the model, then updates the controller with relevant information from the model.
     * Updates the Ebrake if it was pulled by a passenger
     * Updates the currentSpeed from the newly calculated speed
     * Authority and Suggested Speed are updated through the passcommands method
     */
    public void update(){
        // TODO Remove calls to update model; that's handled by SimTime
        for(int i = 0; i< 8; i++){
            boolean d = doors[i];
            model.setDoor(i, d);
        }
        if(currentSpeed > 0){
            setDoors(new boolean[]{false, false, false, false, false, false, false, false});
        } else if(currentSpeed == 0){
            setDoors(new boolean[]{true, true, true, true, true, true, true, true});
        } else {
            //System.out.println("The current speed is negative");
        }
        currentSpeed = model.getCurV()*3600/1000;
        if(model.getEBrake()){
            //System.out.println("EBRAKE SET TO TRUE BECAUSE IT WAS TRUE IN THE MODEL");
            eBrake = true;
        }
        if(eBrake) {
            this.PWRCMD = 0;
        }

        //System.out.println("THE CURRENT SUGGESTED SPEED: " + suggestedSpeed + " AND AUTHORITY " + authority);
        //System.out.println("THE CURRENT SBRAKE: " + sBrake + " AND EBRAKE " + eBrake);

        prepareVoters();
        majorityVote();
        PWRCMD = RESOLVED_PWR_CMD;
        sBrake = RESOLVED_S_BRAKE;
        if(!eBrake) { //only go automatically from no ebrake to ebrake
            eBrake = RESOLVED_E_BRAKE;
        }

        //System.out.println("THE CALCED PWRCMD: " + PWRCMD);
        //System.out.println("THE CALCED SBRAKE: " + sBrake);
        //System.out.println("THE CALCED EBRAKE: " + eBrake);

        if(PWRCMD >= 0){
            model.setPWRCMD(PWRCMD);
            model.setEBrake(eBrake);
            model.setSBrake(sBrake);
        } else {
            prepareVoters();
            majorityVote();
            PWRCMD = RESOLVED_PWR_CMD;
            sBrake = RESOLVED_S_BRAKE;
            eBrake = RESOLVED_E_BRAKE;
            if(PWRCMD > 0){
                model.setPWRCMD(PWRCMD);
                model.setEBrake(eBrake);
                model.setSBrake(sBrake);
            } else {
                model.setPWRCMD(0);
                model.setEBrake(true);
                model.setSBrake(false);
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

    public void prepareVoters(){
        commandedSpeed = Math.min(setpointSpeed, suggestedSpeed);
        commandedSpeed = Math.min(commandedSpeed, speedLimit);
        commandedSpeed = Math.min(commandedSpeed, MAX_OPERATING_SPEED); //these should all be in km/hr
        System.out.println("Commanded Speed is: " + commandedSpeed);
        if(eBrake){
            commandedSpeed = 0;
        }
        for(int id = 0; id < 3; id++) {
            getPWRCMD(commandedSpeed, id);
        }

    }


    /**
     * This method will compare three responses and determine the proper output through Two Majority Report.
     * If no two of the responses agree, the voter returns -1
     * Agreement is determined to be a ~1% of the maximumPower (120). the agreed values will be averaged.
     *  e.g. if votes are: 100, 32, and 302.84, voters A and B will be considered in agreement, and the output energy command will be 66
     * @return
     */
    private void majorityVote(){
        RESOLVED_S_BRAKE = (sBrakeVotes[0] && sBrakeVotes[1]) || (sBrakeVotes[0] && sBrakeVotes[2]) || (sBrakeVotes[1] && sBrakeVotes[2]);
        RESOLVED_E_BRAKE = (eBrakeVotes[0] && eBrakeVotes[1]) || (eBrakeVotes[0] && eBrakeVotes[2]) || (eBrakeVotes[1] && eBrakeVotes[2]);

        if(powerVotes[1] - MAX_PWR_CMD*0.03 <= powerVotes[0] && powerVotes[0] <= powerVotes[1] + MAX_PWR_CMD*0.03){ //At least A and B agree
            if(powerVotes[2] - MAX_PWR_CMD*0.03 <= powerVotes[0] && powerVotes[0] <= powerVotes[2] + MAX_PWR_CMD*0.03){ //A also agrees with C
                RESOLVED_PWR_CMD = (powerVotes[0] + powerVotes[1] + powerVotes[2])/3;
            } else { //A only agrees with B and not with C
                RESOLVED_PWR_CMD = (powerVotes[0] + powerVotes[1])/2;
            }
        } else if(powerVotes[2]-MAX_PWR_CMD*0.03 <= powerVotes[0] && powerVotes[0] <= powerVotes[2]+MAX_PWR_CMD*0.03 ) { //A does not agree with b but does agree with c
            RESOLVED_PWR_CMD = (powerVotes[0] + powerVotes[2])/2;
        } else if(powerVotes[1] - MAX_PWR_CMD*0.03 <= powerVotes[2] && powerVotes[2] <= powerVotes[1] + MAX_PWR_CMD*0.03){ //b agrees with c
            RESOLVED_PWR_CMD = (powerVotes[1] + powerVotes[2])/2;
        }
        System.out.println("MAJORITY VOTE; S: " + RESOLVED_S_BRAKE + " E: " + RESOLVED_E_BRAKE + " PWR: " + RESOLVED_PWR_CMD);
    }

    private void getPWRCMD(float cmdSpeed, int id){
        if(cmdSpeed > MAX_OPERATING_SPEED){
            cmdSpeed = MAX_OPERATING_SPEED;
        }
        float CMD = 0;
        float vErr = cmdSpeed - currentSpeed;
        CMD = kp*vErr + ki*(lastmuk[id] + T/2*(vErr + lastVerrs[id]));
        lastVerrs[id] = vErr;
        if(CMD > MAX_PWR_CMD){
            CMD = kp*vErr + ki*lastmuk[id];
        } else {
            lastmuk[id] = lastmuk[id] + T/2*(vErr + lastVerrs[id]);
        }

        eBrakeVotes[id] = false;

        if(eBrake) {
            eBrakeVotes[id] = true;
            System.out.println("EBRAKEVOTE SET TO TRUE BECAUSE IT IS CURRENTLY ACTIVE");
        } else if(authority > 0 && authority < 3 && currentSpeed > SLOW_OPERATING_SPEED){ //manage speed down to safe slow speed
            sBrakeVotes[id] = true;
            System.out.println("SBRAKEVOTE SET TO TRUE BECAUSE WE WANT TO SLOW THE TRAIN DOWN");
        } else if (authority > 0 && authority < 3 && currentSpeed <= SLOW_OPERATING_SPEED) {
            sBrakeVotes[id] = false;
            System.out.println("SBRAKEVOTE set to FALSE because we are already below the slow operating speed");
        } else if(authority == 0){
            sBrakeVotes[id] = true;
            System.out.println("SBRAKEVOTE SET TO TRUE BECAUSE WE HAVE REACHED OUR DESTINATION");
        } else if(authority < 0){
            eBrakeVotes[id] = true;
            System.out.println("EBRAKEVOTE SET TO TRUE BECAUSE WE HAVE PASSED OUR DESTINATION");
        } else {
            eBrakeVotes[id] = false;
            sBrakeVotes[id] = false;
            System.out.println("EBRAKEVOTE SET TO FALSE BECAUSE WE HAVE NO REASON TO EBRAKE");
        }
        //TODO: Find out what negative value is absurd to be sent and determine values where break is desired instead.

        if(CMD > 120000){
            powerVotes[id] = 120000;
        } else if(CMD < 0){
            powerVotes[id] = 0;
            sBrakeVotes[id] = true;
            //System.out.println("SBRAKEVOTE SET TO TRUE BECAUSE WE HAVE A NEGATIVE POWER COMMAND");
        } else { //CMD is in the normal range
            powerVotes[id] = CMD;
        }
    }

    public void adjustDoors(boolean [] doors){
        if(currentSpeed == 0){
            this.doors = doors;
        }
    }

    public void enterNewBlock(){
        authority-=1;
        System.out.println("WE HAVE ENTERED A NEW BLOCK SO WE HAVE DECREMENTED AUTHORITY");
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

    public float getCommandedSpeedInCustomary(){
        return commandedSpeed;
    }

    public void passCommands(int a, float ss){
        authority = a;
        System.out.println("SS from passCOmmands: " + ss);
        suggestedSpeed = ss*1000/3600; //convert km/h to meters/second
        System.out.println("SS as mps: " + suggestedSpeed);
    }

    public int getID(){
        return id;
    }

    public float getSSpeed(){
        return suggestedSpeed;
    }

    public float getsetpointSpeed(){
        return setpointSpeed;
    }

    public float getsetpointSpeedInCustomary(){
        //System.out.println("METRIC SPS: " + setpointSpeed + " CUSTOMARY: " + setpointSpeed*1000*100/2.54f/12/5280);
        return setpointSpeed*1000*100/2.54f/12/5280;
    }

    public int getAuthority() {
        return authority;
    }
    public boolean getUnderground(){
        return underground;
    }

    public boolean getEBrake(){
        return eBrake;
    }

    public void setEBrake(boolean eBrake){
        this.eBrake = eBrake;
    }

    public void setSetpointSpeed(float setpointSpeed){
        this.setpointSpeed = setpointSpeed;
    }

    public void setSetpointSpeedFromCustomary(float setpointSpeed){
        this.setpointSpeed =  (float)(setpointSpeed/3600*5280*12*2.54/1000);
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

    public double getCurrentSpeedInCustomary(){ //convert from meters/second to miles/hour
        return (currentSpeed*100/2.54f/12/5280*3600);
    }

    public double getSuggestedSpeedInMPH(){
        return (suggestedSpeed*100/2.54f/12/5280*3600);
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

    public double getSpeedLimitInCustomary(){
        return speedLimit*100/2.54/12/5280*3600;
    }

    public boolean issBrake() {
        return sBrake;
    }

    public void setsBrake(boolean sBrake) {
        this.sBrake = sBrake;
    }

    public void setDoors(boolean [] doors){
        //System.out.println("new doors: " + doors);
        this.doors = doors;
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
