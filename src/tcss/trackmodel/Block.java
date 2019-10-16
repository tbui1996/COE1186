package tcss.trackmodel;

import tcss.trainmodel.TrainModel;

public class Block{

    private int line;
    private int section;
    private int blockNum;
    private int length;

    private float grade;
    private float speedLimit;
    private float elevation;
    private float cumulativeElevation;

    private boolean underground;
    private boolean occupied;

    private Switch sw;
    private Station station;
    private RXR rxr;
    private TrainModel train;

    public Block(){
        setLine(-1);
        setSection(-1);
        setBlockNum(-1);
        setLength(-1);

        setGrade(-1);
        setSpeedLimit(-1);
        setElevation(-1);
        setCumulativeElevation(-1);

        setUnderground(false);
        setOccupied(false);
        
        setSwitch(null);
        setStation(null);
        setRXR(null);
        setTrain(null);
    }

    //****************** ACCESSOR METHODS **********************************

    public int getLine(){
        return line;
    }

    public int getSection(){
        return section;
    }

    public int getBlockNum(){
        return blockNum;
    }

    public int getLength(){
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

    public Switch getSwitch(){
        return sw;
    }

    public Station getStation(){
        return station;
    }

    public RXR getRXR(){
        return rxr;
    }

    public TrainModel getTrain(){
        return train;
    }

    //****************** MUTATOR METHODS **********************************

    public void setLine(int l){
        line = l;
    }

    public void setSection(int s){
        section = s;
    }

    public void setBlockNum(int bn){
        blockNum = bn;
    }

    public void setLength(int l){
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

    public void setSwitch(Switch s){
        sw = s;
    }

    public void setStation(Station s){
        station = s;
    }

    public void setTrain(TrainModel t){
        train = t;
    }

    public void setRXR(RXR r){
        rxr = r;
    }
}