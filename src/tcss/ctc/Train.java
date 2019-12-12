package tcss.ctc;

import java.util.ArrayList;

public class Train {

    public String name;
    private double SS;
    private int auth;
    private String nextStation;
    private int dHr;
    private int dMin;
    private ArrayList<String> stopList;
    protected int block = -1;

    public Train(String n) {
        this.name = n;
        this.stopList = new ArrayList<>();
    }

    public Train(String n, int b) {
        this.name = n;
        this.block = b;
        this.stopList = new ArrayList<>();
    }

    public void setName(String n) {
        this.name = n;
    }

    public String getName() {
        return this.name;
    }

    public void setBlock(int b) {
        this.block = b;
    }

    public void setStation(String s) {
        this.nextStation = s;
    }

    public void setDTime(int h, int m) {
        this.dHr = h;
        this.dMin = m;
    }

    private String dTimeString() {
        if (dHr == 0) {
            if (dMin < 10) {
                return "12:0" + this.dMin + " AM";
            } else {
                return "12:" + this.dMin + " AM";
            }
        } else if (dHr <= 11) {
            if (dMin < 10) {
                return this.dHr + ":0" + this.dMin + " AM";
            } else {
                return this.dHr + ":" + this.dMin + " AM";
            }
        } else if (dHr == 12) {
            if (dMin < 10) {
                return "12:0" + this.dMin + " PM";
            } else {
                return "12:" + this.dMin + " PM";
            }
        } else {
            if (dMin < 10) {
                return this.dHr%12 + ":0" + this.dMin + " PM";
            } else {
                return this.dHr%12 + ":" + this.dMin + " PM";
            }
        }
    }

    public String getBlock() {
        if (block == -1)
            return "N/A";
        else
            return Integer.toString(this.block);
    }

    public int getBlockInt() {
        return this.block;
    }

    public String toString() {
        return "Name: " + this.name + "\nDeparture Time: " + this.dTimeString() + "\nNextStop: " + this.nextStation;
    }



}
