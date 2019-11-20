package tcss.trackmodel;

public class Switch{
    
    private int dest1;
    private int dest2;
    private boolean straight;
    private boolean lights;

    public Switch(){
        dest1 = 0;
        dest2 = 0;
        straight = true; //true == straight, false == curved
        lights = false; //true == on, false == off
    }

    public int getDest1(){
        return dest1;
    }

    public int getDest2(){
        return dest2;
    }

    public boolean getStraight(){
        return orientation;
    }

    public boolean lightsOn(){
        return lights;
    }

    public void setOrientation(boolean orientation){
        this.orientation = orientation;
    }

    public void setLights(boolean lights){
        this.lights = lights;
    }

    public void setDest1(int d){
        dest1 = 1;
    }
}