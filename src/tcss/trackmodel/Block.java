package tcss.trackmodel;

import tcss.trainmodel.TrainModel;

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
        
        setSwitch(null);
        setStation(null);
        setRXR(null);
        setBeacon(null);
        setTrain(null);
    }

    public boolean setSuggSpeedAndAuth(float ss, int a){

        System.out.println("SS: " + ss + ", Auth:" + a);

        if(ss == -1.0 && a == -1){
            System.out.println("Initializing train on block " + getBlockNum());
            return initTrain(ss, a, 0);
        }else if(getSuggestedSpeed() == -2 && getAuthority() == -2){
            setClosed(true);
        }else{
            setSuggestedSpeed(ss);
            setAuthority(a);
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

    public void setLine(int l){
        line = l;
    }

    public void setSection(char s){
        section = s;
    }

    public void setBlockNum(int bn){
        blockNum = bn;
    }

    public void setAuthority(int a){
        authority = a;
    }

    public void setSuggestedSpeed(float ss){
        suggestedSpeed = ss;
    }

    public void setLength(float l){
        length = l;
    }

    public void setGrade(float g){
        grade = g;
    }

    public void setSpeedLimit(float sl){
        speedLimit = sl;
    }

    public void setElevation(float e){
        elevation = e;
    }

    public void setCumulativeElevation(float ce){
        cumulativeElevation = ce;
    }

    public void setUnderground(boolean u){
        underground = u;
    }

    public void setOccupied(boolean o){
        occupied = o;
    }

    public void setClosed(boolean c){
        closed = c;
    }

    public void setSwitch(Switch s){
        sw = s;
    }

    public void setStation(Station s){
        station = s;
    }

    public void setRXR(RXR r){
        rxr = r;
    }

    public void setBeacon(Beacon b){
        beacon = b;
    }

    public void setTrain(TrainModel t){
        train = t;
    }
}