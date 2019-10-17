package tcss.trackmodel;

public class Beacon {

    private char[] data;

    public Beacon(){
        setData("".toCharArray());
    }

    public char[] getData(){
        return data;
    }

    public void setData(char[] d){
        data = d;
    }
}
