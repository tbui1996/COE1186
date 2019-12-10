package tcss.ctc;

import java.util.*;

public class Schedule {

    private String name = "";
    private int line;   //Red == 1, Green == 2
    private ArrayList<String> stopList;
    private ArrayList<Float> dwellList;

    public Schedule(int line) {
        this.line = line;
        this.stopList = new ArrayList<String>();
        this.dwellList = new ArrayList<Float>();
    }

    public void addStop(String stopName, float seconds) {
        this.stopList.add(stopName);
        this.dwellList.add(seconds);
    }

    public String getLine() {
        if (line == 1)
            return "RED";
        else
            return "GREEN";
    }

    //Set line as int
    public void setLine(int line) {
        line = line;
    }

    public int getStopNums() {
        return this.stopList.size();
    }

    public String getStopName(int i) {
        return stopList.get(i);
    }

    public float getStopDwell(int i) {
        return dwellList.get(i);
    }

    public String toString() {
        StringBuilder temp = new StringBuilder();
        temp.append("line: " + this.getLine());
        for (int i = 0; i < this.stopList.size(); i++)
            temp.append("\nStop Name: " + this.stopList.get(i) + "\nDwell: " + this.dwellList.get(i));

        return temp.toString();
    }

    public void setName(String n) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
