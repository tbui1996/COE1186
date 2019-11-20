package tcss.trackmodel;

import tcss.trainmodel.TrainModel;

enum Failure{
    BROKEN_RAIL, CIRCUIT_FAILURE, POWER_FAILURE, NONE;
}

public class Block{

    private int line;
    private char section;
    private int blockNum;
    private int authority;

    private float suggestedSpeed;
    private float length;
    private float grade;
    private float speedLimit;
    private float elevation;
    private float cumulativeElevation;

    private boolean underground;
    private boolean occupied;
    private boolean closed;

    private Failure failure;

    private Block head;
    private Block tail;
    private Block branch;

    private Switch sw;
    private Station station;
    private RXR rxr;
    private Beacon beacon;
    private TrainModel train;

    public Block(){
        setLine(-1);
        setSection('\u0000');
        setBlockNum(-1);
        setLength(-1);
        setAuthority(Integer.MIN_VALUE);

        setSuggestedSpeed(Float.MIN_VALUE);
        setGrade(-1);
        setSpeedLimit(-1);
        setElevation(-1);
        setCumulativeElevation(-1);

        setUnderground(false);
        setOccupied(false);

        setFailure(Failure.NONE);

        setHead(null);
        setTail(null);
        setBranch(null);
        
        setSwitch(null);
        setStation(null);
        setRXR(null);
        setBeacon(null);
        setTrain(null);
    }

    public Block getNextBlock(){
        return head;
    }

    public Block getPreviousBlock(){
        return tail;
    }

    public boolean setSuggSpeedAndAuth(float ss, int a){

        System.out.println("SS: " + ss + ", Auth:" + a);

        if(ss == -1.0 && a == -1){
            System.out.println("Initializing train on block " + getBlockNum());
            return initTrain(ss, a, 0);
        }else if(ss == -2.0 && a == 0){

            //close block for maintenance
            setClosed(true);
        }else if(ss == -2.0 && a == 1){

            //open block
            setClosed(false);
        }else if(ss == -3.0 && a == 0){

            //rxr set up
            if(getRXR() == null){
                return false;
            }else{
                getRXR().setDown(false);
            }
        }else if(ss == -3.0 && a == 1){

            //rxr set down
            if(getRXR() == null){
                return false;
            }else{
                getRXR().setDown(true);
            }
        }else if(ss == -4.0 && a == 0){

            //set switch curved
            if(getSwitch() == null){
                return false;
            }else{
                getSwitch().setStraight(false);
            }
        }else if(ss == -4.0 && a == 1){

            //set switch straight
            if(getSwitch() == null){
                return false;
            }else{
                getSwitch().setStraight(true);
            }
        }else{
            if(getTrain() == null){
                //no train to pass values to
                return false;
            }else{
                //pass values to train on block
                getTrain().passCommands(ss, a);
            }
        }

        return true;
    }

    public boolean initTrain(float suggSpeed, int auth, int id){
        if(isOccupied()){
            System.out.println("Failed to init, block is occupied");
            return false;
        }
        TrainModel train = new TrainModel(suggSpeed, auth, id, this);
        setTrain(train);
        setOccupied(true);
        return true;
    }

    //****************** ACCESSOR METHODS **********************************

    public int getLine(){
        return line;
    }

    public char getSection(){
        return section;
    }

    public int getBlockNum(){
        return blockNum;
    }

    public int getAuthority(){
        return authority;
    }

    public float getSuggestedSpeed(){
        return suggestedSpeed;
    }

    public float getLength(){
        return length;
    }

    public float getGrade(){
        return grade;
    }

    public float getSpeedLimit(){
        return speedLimit;
    }

    public float getElevation(){
        return elevation;
    }

    public float getCumulativeElevation(){
        return cumulativeElevation;
    }

    public boolean isUnderground(){
        return underground;
    }

    public boolean isOccupied(){
        return occupied;
    }

    public boolean isClosed(){
        return closed;
    }

    public Failure getFailure(){
        return failure;
    }

    public Block getHead() {
        return head;
    }

    public Block getTail() {
        return tail;
    }

    public Block getBranch() {
        return branch;
    }

    public Switch getSwitch(){
        return sw;
    }

    public Station getStation(){
        return station;
    }

    public RXR getRXR(){
        return rxr;
    }

    public Beacon getBeacon(){
        return beacon;
    }

    public TrainModel getTrain(){
        return train;
    }

    //****************** MUTATOR METHODS **********************************

    public void setLine(int line){
        this.line = line;
    }

    public void setSection(char section){
        this.section = section;
    }

    public void setBlockNum(int blockNum){
        this.blockNum = blockNum;
    }

    public void setAuthority(int authority){
        this.authority = authority;
    }

    public void setSuggestedSpeed(float suggestedSpeed){
        this.suggestedSpeed = suggestedSpeed;
    }

    public void setLength(float length){
        this.length = length;
    }

    public void setGrade(float grade){
        this.grade = grade;
    }

    public void setSpeedLimit(float speedLimit){
        this.speedLimit = speedLimit;
    }

    public void setElevation(float elevation){
        this.elevation = elevation;
    }

    public void setCumulativeElevation(float ce){
        cumulativeElevation = ce;
    }

    public void setUnderground(boolean underground){
        this.underground = underground;
    }

    public void setOccupied(boolean occupied){
        this.occupied = occupied;
    }

    public void setClosed(boolean closed){
        this.closed = closed;
    }

    public void setFailure(Failure failure){
        this.failure = failure;
    }

    public void setHead(Block head) {
        this.head = head;
    }

    public void setTail(Block tail) {
        this.tail = tail;
    }

    public void setBranch(Block branch) {
        this.branch = branch;
    }

    public void setSwitch(Switch sw) {
        this.sw = sw;
    }

    public void setStation(Station station){
        this.station = station;
    }

    public void setRXR(RXR rxr){
        this.rxr = rxr;
    }

    public void setBeacon(Beacon beacon){
        this.beacon = beacon;
    }

    public void setTrain(TrainModel train){
        this.train = train;
    }
}