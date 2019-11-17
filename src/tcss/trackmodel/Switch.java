package tcss.trackmodel;

public class Switch{
    
    private int dest1;
    private int dest2;
    private boolean orientation;
    private boolean lights;

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

    public void setDest1(int d){
        dest1 = 1;
    }
}