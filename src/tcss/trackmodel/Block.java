package tcss.trackmodel;

import tcss.trainmodel.TrainModel;

public class Block{

    private int line;
    private int section;
    private int blockNum;
    private int length;

    private double grade;
    private double speedLimit;
    private double elevation;
    private double cumulativeElevation;

    private boolean underground;
    private boolean occupied;

    private Switch sw;
    private Station station;
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

    public double getGrade(){
        return grade;
    }

    public double getSpeedLimit(){
        return speedLimit;
    }

    public double getElevation(){
        return elevation;
    }

    public double getCumulativeElevation(){
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

    public void setGrade(double g){
        grade = g;
    }

    public void setSpeedLimit(double sl){
        speedLimit = sl;
    }

    public void setElevation(double e){
        elevation = e;
    }

    public void setCumulativeElevation(double ce){
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
}