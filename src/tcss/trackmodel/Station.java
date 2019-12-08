package tcss.trackmodel;

import java.util.Random;

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
        generatePassengers();
    }

    public int generatePassengers(){
        Random rng = new Random();
        int num = (int) (rng.nextGaussian() * 35) + 50;
        if(num < 0){
            num = 0;
        }
        setPassengers(num);
        return num;
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