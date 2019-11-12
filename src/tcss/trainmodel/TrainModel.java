package tcss.trainmodel;

import tcss.trackmodel.Block;
import tcss.traincontroller.TrainController;

import static java.lang.Math.*;

//import static java.lang.Math.sin;

public class TrainModel {

    // Instance variables
    private TrainController controller;
    private Block block;
    private float suggestedSpeed;
    private int authority;
    private int id;
    private float speedLimit;
    private boolean sBrake;
    private boolean eBrake;
    private float cmdSpeed;
    private boolean underground;
    private int blocksTraveled;
    private String curBeaconSignal;
    private String lastBeaconSignal;
    private boolean lights;
    private boolean[] doors = new boolean[8];
    private int passengers;

    // Variables for physics
    private float grade;            // Grade of the current block
    private float length;           // Length of the current block
    private float power;            // The current power command
    private float mass;             // in kg
    private float force;            // Force vector for x axis (positive is forward, negative is backward)
    private float mu = 0.002f;      // Coefficient of rolling friction for train wheels on steel rails
    private float lastV;            // Previous velocity
    private float curV;             // New velocity
    private float lastA;            // Previous acceleration
    private float curA;             // New acceleration
    private float x;                // Meters traveled IN THE CURRENT BLOCK
    private float period = 0.2f;    // The sample period for the system in seconds.

    /**
     * A TrainModel class constructor
     * @param suggestedSpeed The initial suggested speed from the CTC via the Track Model
     * @param authority The initial authority from the CTC via the Track Model
     * @param id The new train's id number
     * @param speedLimit Speed limit of the track the train is initiated on
     */
    public TrainModel(float suggestedSpeed, int authority, int id, float speedLimit) {
        this.suggestedSpeed = suggestedSpeed;
        this.authority = authority;
        this.id = id;
        this.speedLimit = speedLimit;
        this.eBrake = false;
        this.underground = false;

        controller = new TrainController(this);
        controller.setSpeedLimit(speedLimit);
        mass = 409000;
        curA = 0f;
        curV = 0f;

    }

    public TrainModel(int length) {
        id = 1;
        eBrake = false;
        sBrake = false;
        underground = false;
        lights = false;
        this.length = length;
    }

    public TrainModel() {
        id = 1;
        eBrake = false;
        sBrake = false;
        underground = false;
        lights = false;
        mass = 409000;
    }

    public TrainModel(float suggestedSpeed, int authority, int id, Block block) {
        this.block = block;
        this.suggestedSpeed = suggestedSpeed;
        this.authority = authority;
        this.id = id;
        this.speedLimit = block.getSpeedLimit();
        this.eBrake = false;
        this.underground = block.isUnderground();
        this.grade = block.getGrade();

        controller = new TrainController(this);
        controller.setSpeedLimit(speedLimit);
    }

    /**
     * This method uses the current power command and environment of the train
     * (ie., grade of the track) to calculate the forces acting on the train,
     * update the acceleration of the train, update the velocity, and finally
     * update how far in the block it has traveled, moving it to the next block
     * if necessary.
     */
    public void update() {

        // If braking, set acceleration manually
        // If service brake...
        if(eBrake) {
            if(curV <= 0) {
                curA = 0;
                curV = 0;
            }
            else {
                lastA = curA;
                curA = -2.73f;
            }
        }
        // If emergency brake...
        else if(sBrake) {
            if(curV <= 0) {
                curA = 0;
                curV = 0;
            }
            else {
                lastA = curA;
                curA = -1.2f;
            }
        }
        // Else (some power command)
        else {
            calculateForces();
            calculateAccel();
        }

        calculateVelocity();
        // Update distance traveled
        x = x + curV / period;
        // If the train entered a new block...
        if(x > length) {
            x = x - length;
            //TODO Uncomment when Justin adds getNextBlock() to Block class
//            block = block.getNextBlock();
            length = block.getLength();
            grade = block.getGrade();
            speedLimit = block.getSpeedLimit();
            blocksTraveled = blocksTraveled + 1;
            if(curBeaconSignal != null) {
                lastBeaconSignal = curBeaconSignal;
                curBeaconSignal = null;
            }
            if(block.getBeacon() != null) {
                curBeaconSignal = new String(block.getBeacon().getData());
            }
        }
    }

    /**
     * This method uses the previous and current accelerations to calculate
     * the train's new velocity.
     */
    void calculateVelocity() {
        lastV = curV;
        curV = lastV + (period / 2f) * (lastA + curA);
    }

    /**
     * This method uses the calculated force vector to calculate the train's
     * new acceleration.
     */
    void calculateAccel() {
        lastA = curA;
        curA = force / mass;
    }


    /**
     * This method uses the current state of the train to calculate the
     * total force vector acting on it in the x dimension.
     */
    void calculateForces() {
        // Convert grade to angle theta
        // Negate it for ease of calculation when decomposing
        // gravity force vector
        float theta = -(float)atan((double)grade/100);
        // Calculate force due to gravity
        float g = mass*9.8f;
        // Decompose force due to gravity into x and y component vectors
        float gx = g * (float)sin(theta);
        float gy = g * (float)cos(theta);
        // Calculate force due to friction; negate it, as it is always backwards
        float friction = -(mu * gy);
        // Calculate thrust force from engine
        //TODO Add support for when curV = 0
        float thrust = power / curV;
        // Add forces in x dimension
        force = thrust + friction + gx;
    }


    public void powerCmd(float p) {
        power = p;
    }

    public void passCommands(float sSpeed, int auth) {
        suggestedSpeed = sSpeed;
        authority = auth;
        controller.passCommands(auth, sSpeed);
    }

    public int getID() {
        return id;
    }

    public void setEBrake(boolean brake) {
        eBrake = brake;
        if(controller != null) {
            this.controller.setEBrake(brake);
        }
    }

    public boolean getEBrake() {
        return eBrake;
    }

    public float getGrade() {
        return grade;
    }

    public float getCmdSpeed() {
        return cmdSpeed;
    }

    public void setCmdSpeed(float s) {
        cmdSpeed = s;
    }

    public boolean getUnderground() {
        return underground;
    }

    public float getSSpeed() {
        return suggestedSpeed;
    }

    public int getAuthority() {
        return authority;
    }

    public float getSpeedLimit() {
        return speedLimit;
    }

    public TrainController getTControl() {
        return this.controller;
    }

    public String getCurBeaconSignal() {
        if(curBeaconSignal != null){
            return this.curBeaconSignal;
        }
        else {
            return "NULL";
        }
    }

    public String getLastBeaconSignal() {
        if(lastBeaconSignal != null) {
            return this.lastBeaconSignal;
        }
        return "NULL";
    }

    public boolean getLights() {
        return lights;
    }

    public void setLights(boolean l) {
        lights = l;
    }

    public float getCurV() {
        return curV;
    }

    public float getLastV() {
        return lastV;
    }

    public float getCurA() {
        return curA;
    }

    public float getLastA() {
        return lastA;
    }

    public float getMass() {
        return mass;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setBeacon(char[] c) {
        if(curBeaconSignal != null) {
            lastBeaconSignal = curBeaconSignal;
        }
        if(c != null) {
            curBeaconSignal = new String(c);
        }
        else {
            curBeaconSignal = null;
        }
    }

    public void addPassengers(int p) {
        passengers += p;
    }

    public void setLength(float l) {
        length = l;
    }

    public void setDoor(int i, boolean b) {
        doors[i] = b;
    }

    public boolean getDoor(int i) {
        return doors[i];
    }

    public void setSBrake(boolean b) {
        sBrake = b;
    }

    public boolean getSBrake() {
        return sBrake;
    }

    public float getX() {
        return x;
    }

    public int getBlocksTraveled() {
        return blocksTraveled;
    }

    public void setID(int id) {
        this.id = id;
    }









    /**
     * For testing purposes only
     * @param v New curV
     */
    public void setCurV(float v) {
        curV = v;
    }

    /**
     * For testing purposes only
     * @param v New lastV
     */
    public void setLastV(float v) {
        lastV = v;
    }

    /**
     * For testing purposes only
     * @param a New curA
     */
    public void setCurA(float a) {
        curA = a;
    }

    /**
     * For testing purposes only
     * @param a New lastA
     */
    public void setLastA(float a) {
        lastA = a;
    }

    /**
     * For testing purposes only
     * @return Returns train's current force vector
     */
    public float getForce() {
        return force;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }
}
