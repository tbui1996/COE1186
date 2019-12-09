package tcss.trackmodel;

public class Station{

    private String name;
    private int passengers;

    public Station(){
        setName("");
        setPassengers(0);
    }

    public Station(String n){
        setName(n);
        setPassengers(0);
    }

    public String getName(){
        return name;
    }

    public int getPassengers(){
        return passengers;
    }

    public void setName(String n){
        name = n;
    }

    public void setPassengers(int p){
        passengers = p;
    }
}