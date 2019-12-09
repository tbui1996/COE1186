package tcss.trackmodel;

public class Switch{
    
    private int dest1;
    private int dest2;
    private boolean orientation;

    public Switch(){
        dest1 = 0;
        dest2 = 0;
        orientation = true;
    }

    public int getDest1(){
        return dest1;
    }

    public int getDest2(){
        return dest2;
    }

    public boolean getOrientation(){
        return orientation;
    }
}