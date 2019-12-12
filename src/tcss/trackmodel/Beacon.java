package tcss.trackmodel;

public class Beacon {

    private String data;
    private final int MAX_BEACON_CHARS = 128;

    public Beacon(String data){
        if(data.length() > MAX_BEACON_CHARS){
            data = data.substring(0, MAX_BEACON_CHARS);
        }
        setData(data);
    }

    public String getData(){
        return data;
    }

    public void setData(String data){
        this.data = data;
    }
}
