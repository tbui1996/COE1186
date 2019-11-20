package tcss.trackmodel;

public class Switch{
    
    private int straightDest;
    private int branchDest;
    private boolean straight;
    private boolean lights;

    public Switch(){
        straightDest = -1;
        branchDest = -1;
        straight = true; //true == straight, false == curved
        lights = false; //true == on, false == off
    }

    //****************** GETTERS **********************************

    public int getStraightDest(){
        return straightDest;
    }

    public int getBranchDest(){
        return branchDest;
    }

    public boolean getStraight() {
        return straight;
    }

    public boolean lightsOn(){
        return lights;
    }

    //****************** SETTERS **********************************

    public void setStraightDest(int straightDest){
        this.straightDest = straightDest;
    }

    public void setBranchDest(int branchDest){ this.branchDest = branchDest;}

    public void setStraight(boolean straight){
        this.straight = straight;
    }

    public void setLights(boolean lights){
        this.lights = lights;
    }
}