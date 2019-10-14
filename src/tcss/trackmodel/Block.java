package tcss.trackmodel;

public class Block{

    private int line;
    private char section;
    private int blockNum;
    private int length;
    private int grade;
    private int speedLimit;
    private boolean underground;
    private int elevation;
    private int cumulativeElevation;
    private boolean occupied;

    private Switch sw;
    private Station station;

    public Block(){
        setLine(-1);
        setSection('\u0000');
        setBlockNum(-1);
        setGrade(-1);
        setSpeedLimit(-1);
        setUnderground(false);
        setElevation(-1);
        setCumulativeElevation(-1);
        setOccupied(false);
        
        setSwitch(null);
        setStation(null);
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

    public int getGrade(){
        return grade;
    }

    public int getSpeedLimit(){
        return speedLimit;
    }

    public boolean isUnderground(){
        return underground;
    }

    public int getElevation(){
        return elevation;
    }

    public int getCumulativeElevation(){
        return cumulativeElevation;
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

    public void setGrade(int g){
        grade = g;
    }

    public void setSpeedLimit(int sl){
        speedLimit = sl;
    }

    public void setUnderground(boolean u){
        underground = u;
    }

    public void setElevation(int e){
        elevation = e;
    }

    public void setCumulativeElevation(int ce){
        cumulativeElevation = ce;
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
}